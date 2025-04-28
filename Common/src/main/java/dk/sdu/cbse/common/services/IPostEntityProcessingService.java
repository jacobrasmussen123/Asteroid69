package dk.sdu.cbse.common.services;



import dk.sdu.cbse.common.GameData;
import dk.sdu.cbse.common.World;

/**
 *
 * @author jcs
 */
public interface IPostEntityProcessingService {

    void process(GameData gameData, World world);
}