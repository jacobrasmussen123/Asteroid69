package dk.sdu.cbse.common.services;

import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;

public interface IEntityProcessingService {

    /**
     *
     *
     *
     * @param gameData
     * @param world
     * @throws
     */
    void process(GameData gameData, World world);
}