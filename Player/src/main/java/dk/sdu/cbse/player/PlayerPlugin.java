package dk.sdu.cbse.player;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.data.Health;
import dk.sdu.cbse.common.services.IGamePluginService;
import javafx.scene.paint.Color;

public class PlayerPlugin implements IGamePluginService {

    @Override
    public void start(GameData data, World world) {
        // Create the player ship and add it to the world
        Entity player = createPlayerShip(data);
        world.addEntity(player);
        player.setPaint(Color.BLACK);
    }

    /**
     * Factory for the player ship: sets shape, position, size, health, and color.
     */
    private Entity createPlayerShip(GameData data) {
        Player player = new Player();

        // Shape (hexagon)
        player.setPolygonCoordinates(
                -4, -12,
                12,   0,
                -4,  12,
                -24,  12,
                -12,   0,
                -24, -12
        );

        // Center on screen
        player.setX(data.getDisplayWidth() / 2.0);
        player.setY(data.getDisplayHeight() / 2.0);

        // Collision radius and size
        player.setSize(12);
        player.setRadius(12);

        // Health component
        player.addComponent(new Health(150));

        // Visual color
        player.setPaint(Color.BLACK);

        return player;
    }

    @Override
    public void stop(GameData data, World world) {
        // Mark all player entities as dead on shutdown
        world.getEntities(Player.class)
                .forEach(e -> e.setDead(true));
    }
}
