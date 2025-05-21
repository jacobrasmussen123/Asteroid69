package dk.sdu.cbse.common.services;



import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;

/**
 *
 * @author jcs
 */
public interface IPostEntityProcessingService {

    void process(GameData gameData, World world);
}