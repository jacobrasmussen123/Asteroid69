package dk.sdu.cbse.collision;

import dk.sdu.cbse.common.GameData;
import dk.sdu.cbse.common.World;
import dk.sdu.cbse.common.services.IGamePluginService;

/**
 * Plugin to register the collision processing system in the game loop.
 */
public class CollisionPlugin implements IGamePluginService {

    private final CollisionSystem collisionSystem = new CollisionSystem();

    @Override
    public void start(GameData gameData, World world) {
        // No initialization required for collision system
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Nothing to clean up when stopping
    }

    @Override
    public void update(GameData gameData, World world) {
        // Process collisions each frame
        collisionSystem.process(gameData, world);
    }
}
