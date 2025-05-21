package dk.sdu.cbse.player;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.data.Health;
import dk.sdu.cbse.common.services.IGamePluginService;
import javafx.scene.paint.Color;

public class PlayerPlugin implements IGamePluginService {
    private String playerId;

    @Override
    public void start(GameData gameData, World world) {
        // Create and configure the player entity
        Player player = new Player();
        player.setX(gameData.getDisplayWidth()  / 2.0);
        player.setY(gameData.getDisplayHeight() / 2.0);

        // Add to world and remember its ID
        playerId = world.addEntity(player);
    }

    @Override
    public void stop(GameData gameData, World world) {

    }


}