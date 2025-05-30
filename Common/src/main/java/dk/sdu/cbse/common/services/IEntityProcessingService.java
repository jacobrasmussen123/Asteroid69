package dk.sdu.cbse.common.services;

import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
/**
 * SPI for processing game entities each frame.
 * Implementations update entity state (movement, AI, input handling, etc.) before rendering.
 */
public interface IEntityProcessingService {
    /**
     * Runs per-frame processing for all entities before rendering.
     * Preconditions:
     * @param gameData must not be null and provide current game context (delta time, display size, input state).
     * @param world    must not be null and contain all entities to be processed.
     * Postconditions:
     * All entities in the world have been updated based on their logic,
     */
    void process(GameData gameData, World world);
}
