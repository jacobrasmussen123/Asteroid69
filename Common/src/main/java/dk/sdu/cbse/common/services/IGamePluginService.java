package dk.sdu.cbse.common.services;

import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
/**
 * SPI for game plugins that initialize and tear down game features.
 * Plugins can add or remove entities, register systems, or modify game state.
 */
public interface IGamePluginService {
    /**
     * Handles the lifecycle of a game plugin, allowing initialization and cleanup of game features.
     * Preconditions for both operations:
     * @param gameData must not be null and provide the current game context (delta time, display size, input state).
     * @param world    must not be null and represent the current game state, allowing entity management.
     * Post conditions for start:
     * All entities or systems added by this plugin are initialized and ready for processing in the game loop.
     * Post conditions for stop:
     * All entities or systems added by this plugin have been removed from the world,
     */
    void start(GameData gameData, World world);
    void stop(GameData gameData, World world);
}