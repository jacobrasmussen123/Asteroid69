package dk.sdu.cbse.player.systems;

import dk.sdu.cbse.common.bullet.BulletSPI;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.GameKeys;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.data.WallCollisionMode;
import dk.sdu.cbse.common.services.IEntityProcessingService;
import dk.sdu.cbse.player.Player;

import java.util.ServiceLoader;

public class PlayerControlSystem implements IEntityProcessingService {
    private final BulletSPI bulletSPI;
    private float shootCooldown = 0f;
    private static final float FIRE_RATE = 0.05f;
    private static final double DECELERATION_FACTOR = 0.97;

    public PlayerControlSystem() {
        // load any BulletSPI provider (null if Bullet module absent)
        this.bulletSPI = ServiceLoader
                .load(BulletSPI.class)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void process(GameData gameData, World world) {
        float dt = gameData.getDeltaTime();

        for (Entity e : world.getEntities(Player.class)) {
            Player p = (Player) e;
            GameKeys keys = gameData.getKeys();

            // —— ROTATION ——
            if (keys.isDown(GameKeys.LEFT)) {
                p.setRotation(p.getRotation() - Player.ROT_SPEED * dt);
            }
            if (keys.isDown(GameKeys.RIGHT)) {
                p.setRotation(p.getRotation() + Player.ROT_SPEED * dt);
            }

            // —— THRUST vs DECELERATION ——
            if (keys.isDown(GameKeys.UP)) {
                double rad = Math.toRadians(p.getRotation());
                p.setDx(p.getDx() + Math.cos(rad) * Player.ACCEL * dt);
                p.setDy(p.getDy() + Math.sin(rad) * Player.ACCEL * dt);
                // clamp to MAX_SPEED
                double speed = Math.hypot(p.getDx(), p.getDy());
                if (speed > Player.MAX_SPEED) {
                    double scale = Player.MAX_SPEED / speed;
                    p.setDx(p.getDx() * scale);
                    p.setDy(p.getDy() * scale);
                }
            } else {
                p.setDx(p.getDx() * Math.pow(DECELERATION_FACTOR, dt * 60));
                p.setDy(p.getDy() * Math.pow(DECELERATION_FACTOR, dt * 60));
            }

            // —— MOVE & HANDLE WALLS ——
            double newX = p.getX() + p.getDx() * dt;
            double newY = p.getY() + p.getDy() * dt;
            double dx   = p.getDx();
            double dy   = p.getDy();
            WallCollisionMode mode = gameData.getWallMode();

            switch (mode) {
                case WRAP:
                    newX = wrap(newX, gameData.getDisplayWidth());
                    newY = wrap(newY, gameData.getDisplayHeight());
                    break;

                case BOUNCE:
                    if (newX < 0 || newX > gameData.getDisplayWidth()) {
                        dx *= -1;
                        newX = Math.max(0, Math.min(newX, gameData.getDisplayWidth()));
                    }
                    if (newY < 0 || newY > gameData.getDisplayHeight()) {
                        dy *= -1;
                        newY = Math.max(0, Math.min(newY, gameData.getDisplayHeight()));
                    }
                    break;

                case STOP:
                    if ((newX < 0 && dx < 0) || (newX > gameData.getDisplayWidth() && dx > 0)) dx = 0;
                    if ((newY < 0 && dy < 0) || (newY > gameData.getDisplayHeight() && dy > 0)) dy = 0;
                    newX = Math.max(0, Math.min(newX, gameData.getDisplayWidth()));
                    newY = Math.max(0, Math.min(newY, gameData.getDisplayHeight()));
                    break;
            }

            p.setX(newX);
            p.setY(newY);
            p.setDx(dx);
            p.setDy(dy);

            // —— SHOOTING ——
            shootCooldown -= dt;
            if (bulletSPI != null && keys.isDown(GameKeys.SPACE) && shootCooldown <= 0f) {
                world.addEntity(bulletSPI.createBullet(p, gameData));
                shootCooldown = FIRE_RATE;
            }

            // —— UPDATE KEY STATES ——
            keys.update();
        }
    }

    private double wrap(double value, double max) {
        if (value < 0) return value + max;
        if (value > max) return value - max;
        return value;
    }
}
