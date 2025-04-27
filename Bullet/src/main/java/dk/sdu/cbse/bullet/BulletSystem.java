package dk.sdu.cbse.bullet;

import dk.sdu.cbse.common.Entity;
import dk.sdu.cbse.common.World;
import dk.sdu.cbse.bullet.Bullet;
import dk.sdu.cbse.bullet.BulletConfig;

import java.util.ArrayList;
import java.util.List;

public class BulletSystem {
    private final BulletConfig defaultConfig = new BulletConfig();

    public void spawnBullet(Entity shooter, World world) {
        spawnBullet(shooter, defaultConfig, world);
    }

    public void spawnBullet(Entity shooter, BulletConfig config, World world) {
        Bullet b = new Bullet(shooter.getX(), shooter.getY(), shooter.getRotation(), config);
        world.addEntity(b);
    }

    public void updateBullets(World world, float dt, int w, int h) {
        List<Entity> toRemove = new ArrayList<>();

        for (Entity e : world.getEntities(Bullet.class)) {
            Bullet b = (Bullet) e;
            b.update(dt, w, h);

            if (b.isExpired()
                    || b.getX() < 0 || b.getX() > w
                    || b.getY() < 0 || b.getY() > h) {
                toRemove.add(b);
            }
        }

        toRemove.forEach(world::removeEntity);
    }

}