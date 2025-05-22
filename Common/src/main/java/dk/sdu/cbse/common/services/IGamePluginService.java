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
     * @param gameData must not be null and provide display and input configuration.
     * @param world    must not be null and allow adding or removing entities and systems.
     * Post conditions for start:
     * One or more entities or systems have been registered in the world.
     * Post conditions for stop:
     * All entities and systems registered by this plugin have been removed from the world.
     */
    void start(GameData gameData, World world);


    void stop(GameData gameData, World world);


}