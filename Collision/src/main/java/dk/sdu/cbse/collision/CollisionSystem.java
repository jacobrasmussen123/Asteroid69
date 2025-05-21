package dk.sdu.cbse.collision;

import dk.sdu.cbse.common.asteroid.AsteroidSplitter;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.Health;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IPostEntityProcessingService;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class CollisionSystem implements IPostEntityProcessingService {

    private final AsteroidSplitter asteroidSplitter;


    public CollisionSystem() {
        AsteroidSplitter found = ServiceLoader
                .load(AsteroidSplitter.class)
                .findFirst()
                .orElse(null);

        if (found != null) {
            this.asteroidSplitter = found;
        } else {
            // Fallback: no splitting
            this.asteroidSplitter = (original, world) -> { };
        }
    }


    @Override
    public void process(GameData gameData, World world) {
        List<Entity> entityList = new ArrayList<>(world.getEntities());

        for (int i = 0; i < entityList.size(); i++) {
            Entity entity1 = entityList.get(i);
            for (int j = i + 1; j < entityList.size(); j++) {
                Entity entity2 = entityList.get(j);
                if (collides(entity1, entity2)) {
                    handleCollisions(entity1, entity2, world);
                }
            }
        }
    }

    public void handleCollisions(Entity entity1, Entity entity2, World world) {
        String entity1Type = entity1.getType();
        String entity2Type = entity2.getType();

        if (entity1Type.equals("Player") && entity2Type.equals("Asteroid")){
            //Player hit by asteroid, removes player
            world.removeEntity(entity1);
            return;
        }
        if (entity1Type.equals("Asteroid") && entity2Type.equals("Player")){
            //Player hit by asteroid, removes player
            world.removeEntity(entity2);
            return;
        }

        if (entity1Type.equals("Enemy") && entity2Type.equals("Asteroid")){
            //Enemy hit by asteroid, removes player
            world.removeEntity(entity1);
            return;
        }
        if (entity1Type.equals("Asteroid") && entity2Type.equals("Enemy")){
            //Enemy hit by asteroid, removes player
            world.removeEntity(entity2);
            return;
        }
        if (entity1Type.equals("Bullet") && entity2Type.equals("Asteroid")) {
            //Asteroid hit by bullet, removes bullet and splits asteroid into two.
            world.removeEntity(entity1);
            splitAndRemoveAsteroid(entity2, world);
            return;
        }
        if (entity1Type.equals("Asteroid") && entity2Type.equals("Bullet")) {
            //Asteroid hit by bullet, removes bullet and splits asteroid into two.
            world.removeEntity(entity2);
            splitAndRemoveAsteroid(entity1, world);
            return;
        }
        if (entity1Type.equals("Bullet") && entity2Type.equals("Player")) {
            //Player hit by bullet, and loses 1 hp per hit, health starts at 3
            world.removeEntity(entity1);
            damage(entity2, world);
            return;
        }
        if (entity1Type.equals("Player") && entity2Type.equals("Bullet")) {
            //Player hit by bullet, and loses 1 hp per hit, health starts at 3
            world.removeEntity(entity2);
            damage(entity1, world);
            return;
        }
        if (entity1Type.equals("Bullet") && entity2Type.equals("Enemy")) {
            //Enemy hit by bullet, and loses 1 hp per hit, health starts at 3
            world.removeEntity(entity1);
            damage(entity2, world);
            return;
        }
        if (entity1Type.equals("Enemy") && entity2Type.equals("Bullet")) {
            //Enemy hit by bullet, and loses 1 hp per hit, health starts at 3
            world.removeEntity(entity2);
            damage(entity1, world);
            return;
        }
        if (entity1Type.equals("Enemy") && entity2Type.equals("Player")) {
            //Enemy and Player Crashes into each other
            world.removeEntity(entity1);
            world.removeEntity(entity2);
            return;
        }
        if (entity1Type.equals("Player") && entity2Type.equals("Enemy")) {
            //Enemy and Player Crashes into each other
            world.removeEntity(entity1);
            world.removeEntity(entity2);
        }

    }

    public Boolean collides(Entity entity1, Entity entity2) {
        float dx = (float) entity1.getX() - (float) entity2.getX();
        float dy = (float) entity1.getY() - (float) entity2.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance < (entity1.getRadius() + entity2.getRadius());
    }

    private void splitAndRemoveAsteroid(Entity asteroid, World world) {
        asteroidSplitter.SplitAsteroid(asteroid, world);
        world.removeEntity(asteroid);
    }

    private void damage(Entity target, World world) {
        Health health = target.getComponent(Health.class);
        if (health != null) {
            health.setHealth(health.getHealth() - 1);
            if (health.getHealth() <= 0) {
                world.removeEntity(target);
            }
        }
    }
}
