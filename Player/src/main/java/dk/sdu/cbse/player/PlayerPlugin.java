package dk.sdu.cbse.player;

import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IGamePluginService;
import dk.sdu.cbse.player.Player;

public class PlayerPlugin implements IGamePluginService {
    private String playerId;

    @Override
    public void start(GameData gameData, World world) {
        Player player = new Player();
        player.setX(gameData.getDisplayWidth()  / 2.0);
        player.setY(gameData.getDisplayHeight() / 2.0);
        playerId = world.addEntity(player);
    }

    @Override
    public void stop(GameData gameData, World world) {

    }


}