package dk.sdu.cbse.bullet;

import dk.sdu.cbse.common.bullet.Bullet;
import dk.sdu.cbse.common.bullet.BulletSPI;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.services.IEntityProcessingService;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class BulletSystem implements IEntityProcessingService, BulletSPI {
    private final BulletConfig config = new BulletConfig();



    @Override
    public Entity createBullet(Entity e, GameData gameData) {
        Bullet bullet = new Bullet();

        //  Make safe spawn distance so bullets don't collide with their shooter
        double safeDistance = e.getRadius() + bullet.getRadius() + 10f; // adjust the buffer as needed

        double rotation = e.getRotation();
        double spawnX = e.getX() + (double) Math.cos(Math.toRadians(rotation)) * safeDistance;
        double spawnY = e.getY() + (double) Math.sin(Math.toRadians(rotation)) * safeDistance;

        bullet.setX(spawnX);
        bullet.setY(spawnY);
        bullet.setRotation(rotation);

        return bullet;
    }

    @Override
    public void process(GameData gameData, World world) {
        for (Entity ent : world.getEntities(Bullet.class)) {
            double dx = Math.cos(Math.toRadians(ent.getRotation())) * config.getSpeed() * gameData.getDeltaTime();
            double dy = Math.sin(Math.toRadians(ent.getRotation())) * config.getSpeed() * gameData.getDeltaTime();
            ent.setX(ent.getX() + dx);
            ent.setY(ent.getY() + dy);
            setShape(ent);
        }
    }

    private void setShape(Entity e) {
        float r = config.getRadius();
        //The shape
        e.setPolygonCoordinates(
                -r, -r,
                r, -r,
                r,  r,
                -r,  r
        );
    }
}