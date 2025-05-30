package dk.sdu.cbse.common.services;

import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;

/**
 * SPI for post-processing game entities after core logic.
 * Typically used for collision resolution, cleanup, and spawning new entities.
 * Implementations run after all {@link IEntityProcessingService} instances.
 *
 */
public interface IPostEntityProcessingService {
    /**
     * Runs post-processing logic once per game loop iteration, typically handling collisions and cleanup.
     *
     * Preconditions:
     * @param gameData must not be null and contain valid frame context (delta time, display size).
     * @param world    must not be null and contain all entities to be processed.
     * Post conditions:
     * All necessary cleanup, collision resolution, and spawned actions have been applied to the world,
     * ensuring consistency of entity lifecycle.
     */
    void process(GameData gameData, World world);
}
