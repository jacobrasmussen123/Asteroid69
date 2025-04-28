package dk.sdu.cbse.player;

import dk.sdu.cbse.common.*;
import dk.sdu.cbse.common.services.IGamePluginService;
import dk.sdu.cbse.bullet.*;
import dk.sdu.cbse.player.systems.InputSystem;
import dk.sdu.cbse.player.systems.MovementSystem;

public class PlayerPlugin implements IGamePluginService {
    private String playerId;
    private final InputSystem inputSystem = new InputSystem();
    private final MovementSystem movementSystem = new MovementSystem();
    private final BulletSystem bulletSystem = new BulletSystem();

    @Override
    public void start(GameData data, World world) {
        Player p = new Player();
        p.setX(data.getDisplayWidth() / 2.0);
        p.setY(data.getDisplayHeight() / 2.0);
        playerId = world.addEntity(p);
    }

    @Override
    public void stop(GameData gameData, World world) {

    }

    @Override
    public void update(GameData data, World world) {
        float dt = data.getDeltaTime();
        Player p = (Player) world.getEntity(playerId);
        GameKeys keys = data.getKeys();

        if (inputSystem.isRotatingLeft(keys))
            p.setRotation(p.getRotation() - Player.ROT_SPEED * dt);
        if (inputSystem.isRotatingRight(keys))
            p.setRotation(p.getRotation() + Player.ROT_SPEED * dt);
        if (inputSystem.isAccelerating(keys))
            movementSystem.applyThrust(p, dt);

        movementSystem.moveAndHandleWalls(p, dt, data.getDisplayWidth(), data.getDisplayHeight(), data.getWallMode());


        if (inputSystem.isShooting(keys)) {
            bulletSystem.spawnBullet(p, world);
        }

        bulletSystem.updateBullets(world, dt, data.getDisplayWidth(), data.getDisplayHeight());

        keys.update();
    }
}