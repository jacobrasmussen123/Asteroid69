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
     * @param gameData must not be null and contain valid frame context (delta time, input state).
     * @param world    must not be null and reflect current state of all entities.
     * Postconditions:
     * All entities in the world have had their state updated according to their specific processing logic.
     */
    void process(GameData gameData, World world);
}