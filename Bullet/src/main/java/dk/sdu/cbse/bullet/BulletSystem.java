package dk.sdu.cbse.bullet;

import dk.sdu.cbse.common.bullet.Bullet;
import dk.sdu.cbse.common.bullet.BulletSPI;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.services.IEntityProcessingService;

import java.util.ArrayList;
import java.util.List;

public class BulletSystem implements IEntityProcessingService, BulletSPI {
    private final BulletConfig config = new BulletConfig();


    public Entity createBullet(Entity e, GameData gameData) {
        Bullet bullet = new Bullet();

        bullet.setX(e.getX());
        bullet.setY(e.getY());

        bullet.setRadius(3f);

        bullet.setSize(10);
        bullet.setPolygonCoordinates(-2,2, 2,2, 2,-2, -2,-2);
        bullet.setRotation(e.getRotation());
        bullet.setHealth(1);
        bullet.setPaint(e.getPaint());
        return bullet;
    }

    @Override
    public void process(GameData gameData, World world) {

        for (Entity bullet : world.getEntities(Bullet.class)) {

            if (bullet.getHealth() <= 0) {
                bullet.setDead(true);
            }
            double changeX = Math.cos(Math.toRadians(bullet.getRotation()));
            double changeY = Math.sin(Math.toRadians(bullet.getRotation()));
            bullet.setX(bullet.getX() + changeX * 3);
            bullet.setY(bullet.getY() + changeY * 3);
        }
    }

    private void setShape(Entity entity) {
    }
}