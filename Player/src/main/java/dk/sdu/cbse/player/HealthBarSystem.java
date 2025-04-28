package dk.sdu.cbse.player;

import dk.sdu.cbse.common.GameData;
import dk.sdu.cbse.common.World;
import dk.sdu.cbse.common.services.IPostEntityProcessingService;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class HealthBarSystem implements IPostEntityProcessingService {
    private GraphicsContext gc;

    public void setGraphicsContext(GraphicsContext gc) {
        this.gc = gc;
    }

    @Override
    public void process(GameData gameData, World world) {
        if (gc == null) return;

        // Find the player and draw their health
        world.getEntities(Player.class).forEach(entity -> {
            Player player = (Player) entity;
            drawHealthBar(player, gameData);
        });
    }

    private void drawHealthBar(Player player, GameData gameData) {
        double x = player.getX() - 20;
        double y = player.getY() + 30; // Below the player
        double width = 40;
        double height = 5;
        double healthPercent = (double) player.getHealth() / Player.PLAYER_MAX_HEALTH;

        // Background
        gc.setFill(Color.GRAY);
        gc.fillRect(x, y, width, height);

        // Health
        gc.setFill(Color.GREEN);
        gc.fillRect(x, y, width * healthPercent, height);
    }
}