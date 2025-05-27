package dk.sdu.cbse.player.systems;

import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.GameKeys;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerControlSystemTest {

    private PlayerControlSystem system;
    private GameData gameData;
    private World world;
    private Player player;

    @BeforeEach
    void setUp() {
        system   = new PlayerControlSystem();
        gameData = new GameData();
        world    = new World();

        // create a fresh Player and put it in the world
        player = new Player();
        player.setX(100);
        player.setY(100);
        player.setRotation(0);
        player.setDx(0);
        player.setDy(0);
        world.addEntity(player);
    }

    @Test
    void whenPressUp_thenPlayerAcceleratesForward() {

        gameData.setDeltaTime(1.0f);
        gameData.getKeys().setKey(GameKeys.UP, true);

        system.process(gameData, world);


        double expectedDx = Math.cos(Math.toRadians(0)) * Player.ACCEL * 1.0;
        assertEquals(expectedDx, player.getDx(), 1e-6,
                "Player should accelerate in the +X direction");

        assertEquals(0.0, player.getDy(), 1e-6,
                "Player should not accelerate in the Y direction at 0° rotation");
    }

    @Test
    void whenNoInput_thenPlayerDecelerates() {

        player.setDx(200);
        player.setDy(0);
        gameData.setDeltaTime(1.0f);


        system.process(gameData, world);


        assertTrue(player.getDx() < 200,
                "With no UP pressed, player should slow down on X axis");
    }
}
