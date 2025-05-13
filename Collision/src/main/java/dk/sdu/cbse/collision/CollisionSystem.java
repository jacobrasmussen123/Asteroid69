package dk.sdu.cbse.collision;

import dk.sdu.cbse.common.GameData;
import dk.sdu.cbse.common.World;
import dk.sdu.cbse.common.Entity;
import dk.sdu.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.cbse.asteroid.Asteroid;
import dk.sdu.cbse.bullet.Bullet;
import dk.sdu.cbse.player.Player;
import dk.sdu.cbse.enemy.Enemy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CollisionSystem implements IPostEntityProcessingService {

    private final Random rng = new Random();

    @Override
    public void process(GameData gameData, World world) {
        // Make a stable snapshot so removals during collision handling don't interfere
        List<Entity> entities = new ArrayList<>(world.getEntities());
        for (int i = 0; i < entities.size(); i++) {
            Entity e1 = entities.get(i);
            for (int j = i + 1; j < entities.size(); j++) {
                Entity e2 = entities.get(j);

                if (collides(e1, e2)) {
                    handleCollision(e1, e2, world);
                }
            }
        }
    }

    private boolean collides(Entity e1, Entity e2) {
        double dx = e1.getX() - e2.getX();
        double dy = e1.getY() - e2.getY();
        double distance = Math.hypot(dx, dy);
        return distance < (e1.getRadius() + e2.getRadius());
    }

    private void handleCollision(Entity e1, Entity e2, World world) {
        // --- Player or Enemy vs Asteroid ---
        if (e1 instanceof Asteroid && (e2 instanceof Player || e2 instanceof Enemy)) {
            world.removeEntity(e2);
            return;
        }
        if (e2 instanceof Asteroid && (e1 instanceof Player || e1 instanceof Enemy)) {
            world.removeEntity(e1);
            return;
        }

        // --- Bullet vs Asteroid ---
        if (e1 instanceof Bullet && e2 instanceof Asteroid) {
            world.removeEntity(e1);
            splitAsteroid((Asteroid) e2, world);
            return;
        }
        if (e2 instanceof Bullet && e1 instanceof Asteroid) {
            world.removeEntity(e2);
            splitAsteroid((Asteroid) e1, world);
            return;
        }

        // --- Bullet vs Ship (no friendly fire) ---
        if (e1 instanceof Bullet && isShip(e2)) {
            Bullet b = (Bullet) e1;
            if (b.getOwner() != null && !b.getOwner().equals(e2.getClass().getSimpleName())) {
                world.removeEntity(b);
                damageShip(e2, world);
            }
            return;
        }
        if (e2 instanceof Bullet && isShip(e1)) {
            Bullet b = (Bullet) e2;
            if (b.getOwner() != null && !b.getOwner().equals(e1.getClass().getSimpleName())) {
                world.removeEntity(b);
                damageShip(e1, world);
            }
            return;
        }

        // --- Player vs Enemy crash ---
        if ((e1 instanceof Player && e2 instanceof Enemy) ||
                (e1 instanceof Enemy  && e2 instanceof Player)) {
            world.removeEntity(e1);
            world.removeEntity(e2);
        }
    }

    private boolean isShip(Entity e) {
        return e instanceof Player || e instanceof Enemy;
    }

    private void splitAsteroid(Asteroid ast, World world) {
        int children = ast.getSize().children;
        if (children <= 0) {
            world.removeEntity(ast);
            return;
        }

        Asteroid.Size nextSize = (ast.getSize() == Asteroid.Size.LARGE)
                ? Asteroid.Size.MEDIUM
                : Asteroid.Size.SMALL;
        double x = ast.getX(), y = ast.getY();

        for (int i = 0; i < children; i++) {
            double dir = rng.nextDouble() * 360;
            world.addEntity(new Asteroid(nextSize, x, y, dir));
        }
        world.removeEntity(ast);
    }

    private void damageShip(Entity ship, World world) {
        if (ship instanceof Player) {
            Player p = (Player) ship;
            p.takeDamage(1);
            if (p.getHealth() <= 0) {
                world.removeEntity(p);
            }
        } else if (ship instanceof Enemy) {
            Enemy e = (Enemy) ship;
            e.takeDamage(1);
            if (e.getHealth() <= 0) {
                world.removeEntity(e);
            }
        }
    }
}
