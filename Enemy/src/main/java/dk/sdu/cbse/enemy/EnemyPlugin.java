package dk.sdu.cbse.enemy;

import dk.sdu.cbse.common.*;
import dk.sdu.cbse.common.services.IGamePluginService;
import dk.sdu.cbse.enemy.systems.AIController;

import java.util.Random;
import dk.sdu.cbse.bullet.*;
import dk.sdu.cbse.enemy.systems.MovementSystem;

public class EnemyPlugin implements IGamePluginService {
    // Add these new fields
    private final AIController aiController = new AIController();
    private final Random random = new Random();
    private String enemyId;
    private final MovementSystem movementSystem = new MovementSystem();
    private final BulletSystem bulletSystem = new BulletSystem();

    @Override
    public void start(GameData data, World world) {
        Enemy e = new Enemy();
        e.setX(data.getDisplayWidth() / 2.0);
        e.setY(data.getDisplayHeight() / 2.0);
        enemyId = world.addEntity(e);
    }


    public void stop(GameData gameData, World world) {

    }

    @Override
    public void update(GameData data, World world) {
        float dt = data.getDeltaTime();
        Enemy e = (Enemy) world.getEntity(enemyId);

        // Find player entity (you'll need to implement this)
        Entity player = findPlayerEntity(world);

        // AI-controlled movement
        aiController.updateAI(e, player, dt);

        // Auto-shooting
        if (aiController.shouldShoot(random)) {
            bulletSystem.spawnBullet(e, world);
        }

        movementSystem.moveAndHandleWalls(e, dt,
                data.getDisplayWidth(), data.getDisplayHeight(),
                data.getWallMode());

        bulletSystem.updateBullets(world, dt,
                data.getDisplayWidth(), data.getDisplayHeight());


    }

    private Entity findPlayerEntity(World world) {
        // Implement logic to find player entity
        for (Entity entity : world.getEntities()) {
            if (entity.getClass().getSimpleName().equals("Player")) {
                return entity;
            }
        }
        return null;
    }


}