package dk.sdu.cbse.enemy;

import dk.sdu.cbse.common.bullet.BulletSPI;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.WallCollisionMode;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IEntityProcessingService;
import dk.sdu.cbse.common.util.ServiceLocator;

import java.util.*;

public class EnemyController implements IEntityProcessingService {
    private final BulletSPI bulletSPI;
    private final Random random = new Random();

    // Stores per-enemy timers & directions
    private final Map<String, Float> dirTimers    = new HashMap<>();
    private final Map<String, Double> dirAngles   = new HashMap<>();
    private final Map<String, Float>   shootCd    = new HashMap<>();

    private static final float CHANGE_INTERVAL   = 1.0f;   // sec between direction changes
    private static final float MOVE_SPEED        = 100.0f; // px/sec
    private static final float FIRE_RATE         = 0.5f;   // sec between possible shots
    private static final double SHOOT_CHANCE     = 0.02;   // chance to shoot each eligible frame

    public EnemyController() {
        this.bulletSPI = ServiceLoader
                .load(BulletSPI.class)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void process(GameData gameData, World world) {
        float dt     = gameData.getDeltaTime();
        int   w      = gameData.getDisplayWidth();
        int   h      = gameData.getDisplayHeight();
        WallCollisionMode mode = gameData.getWallMode();

        for (Entity e : world.getEntities(Enemy.class)) {
            Enemy enemy = (Enemy) e;
            String id   = enemy.getID();

            // — Update direction timer —
            float t = dirTimers.getOrDefault(id, 0f) - dt;
            if (t <= 0) {
                // pick new random angle
                double angle = random.nextDouble() * 360;
                dirAngles.put(id, angle);
                t = CHANGE_INTERVAL;
            }
            dirTimers.put(id, t);

            // — Move in that direction —
            double angleRad = Math.toRadians(dirAngles.get(id));
            double dx = Math.cos(angleRad) * MOVE_SPEED * dt;
            double dy = Math.sin(angleRad) * MOVE_SPEED * dt;

            double nx = enemy.getX() + dx;
            double ny = enemy.getY() + dy;

            // — Wall handling —
            switch (mode) {
                case WRAP:
                    if (nx < 0) nx += w;
                    else if (nx > w) nx -= w;
                    if (ny < 0) ny += h;
                    else if (ny > h) ny -= h;
                    break;
                case BOUNCE:
                    if (nx < 0 || nx > w) {
                        dx = -dx;
                        nx = Math.max(0, Math.min(nx, w));
                    }
                    if (ny < 0 || ny > h) {
                        dy = -dy;
                        ny = Math.max(0, Math.min(ny, h));
                    }
                    break;
                case STOP:
                    if (nx < 0) nx = 0;
                    if (nx > w) nx = w;
                    if (ny < 0) ny = 0;
                    if (ny > h) ny = h;
                    break;
            }

            enemy.setX(nx);
            enemy.setY(ny);
            enemy.setRotation(dirAngles.get(id));

            // — Shooting cooldown —
            float sc = shootCd.getOrDefault(id, 0f) - dt;
            if (sc < 0) sc = 0;
            // random shoot chance
            if (bulletSPI != null && sc <= 0 && random.nextDouble() < SHOOT_CHANCE) {
                world.addEntity(bulletSPI.createBullet(enemy, gameData));
                sc = FIRE_RATE;
            }
            shootCd.put(id, sc);
        }
    }
}