package dk.sdu.cbse.collision;

import dk.sdu.cbse.common.GameData;
import dk.sdu.cbse.common.World;
import dk.sdu.cbse.common.Entity;
import dk.sdu.cbse.common.services.IEntityProcessingService;
import dk.sdu.cbse.player.Player;
import dk.sdu.cbse.enemy.Enemy;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Renders health bars for Player and Enemy ships above their current position.
 */
public class HealthBarSystem implements IEntityProcessingService {
    private final GraphicsContext gc;
    private static final double BAR_WIDTH = 40;
    private static final double BAR_HEIGHT = 5;
    private static final double VERTICAL_OFFSET = 15;

    public HealthBarSystem(GraphicsContext gc) {
        this.gc = gc;
    }

    @Override
    public void process(GameData gameData, World world) {
        for (Entity e : world.getEntities()) {
            if (!(e instanceof Player) && !(e instanceof Enemy)) {
                continue;
            }
            double x = e.getX();
            double y = e.getY() - VERTICAL_OFFSET;

            int health;
            int maxHealth;
            if (e instanceof Player) {
                Player p = (Player) e;
                health = p.getHealth();
                maxHealth = Player.PLAYER_MAX_HEALTH;
            } else {
                Enemy en = (Enemy) e;
                health = en.getHealth();
                maxHealth = en.getMaxHealth();
            }

            double ratio = Math.max(0, Math.min(1.0, (double) health / maxHealth));
            double filled = BAR_WIDTH * ratio;

            // Draw empty bar background
            gc.setFill(Color.RED);
            gc.fillRect(x - BAR_WIDTH / 2, y, BAR_WIDTH, BAR_HEIGHT);

            // Draw filled portion
            gc.setFill(Color.LIMEGREEN);
            gc.fillRect(x - BAR_WIDTH / 2, y, filled, BAR_HEIGHT);
        }
    }
}
