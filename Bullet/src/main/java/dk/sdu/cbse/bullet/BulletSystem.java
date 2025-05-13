package dk.sdu.cbse.bullet;

import dk.sdu.cbse.common.World;
import dk.sdu.cbse.common.Entity;

import java.util.ArrayList;
import java.util.List;

public class BulletSystem {
    private final BulletConfig config = new BulletConfig();

    public void spawnBullet(Entity shooter, World world) {
        Bullet b = new Bullet(shooter.getX(), shooter.getY(), shooter.getRotation(), config);
        b.setOwner(shooter.getClass().getSimpleName());
        world.addEntity(b);
    }

    public void updateBullets(World world, float dt, int width, int height) {
        List<Entity> toRemove = new ArrayList<>();
        for (Entity e : world.getEntities(Bullet.class)) {
            Bullet b = (Bullet) e;
            b.update(dt, width, height);
            if (b.isExpired()) {
                toRemove.add(b);
            }
        }
        toRemove.forEach(world::removeEntity);
    }
}