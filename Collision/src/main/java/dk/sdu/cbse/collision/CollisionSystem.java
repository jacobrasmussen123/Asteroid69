package dk.sdu.cbse.collision;

import dk.sdu.cbse.common.Entity;
import dk.sdu.cbse.common.GameData;
import dk.sdu.cbse.common.World;
import dk.sdu.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.cbse.asteroid.Asteroid;
import dk.sdu.cbse.bullet.Bullet;
import dk.sdu.cbse.enemy.Enemy;
import dk.sdu.cbse.player.Player;

import java.util.ArrayList;
import java.util.List;

public class CollisionSystem implements IPostEntityProcessingService {

    private static final int PLAYER_MAX_HEALTH = 3;
    private static final int ENEMY_MAX_HEALTH = 2;
    private static final int BULLET_DAMAGE = 1;

    @Override
    public void process(GameData gameData, World world) {
        checkShipAsteroidCollisions(world);
        checkBulletAsteroidCollisions(world);
        checkBulletShipCollisions(world);
    }

    private void checkShipAsteroidCollisions(World world) {
        List<Entity> ships = new ArrayList<>();
        ships.addAll(world.getEntities(Player.class));
        ships.addAll(world.getEntities(Enemy.class));

        List<Entity> asteroids = world.getEntities(Asteroid.class);

        for (Entity ship : ships) {
            for (Entity asteroid : asteroids) {
                if (collides(ship, asteroid)) {
                    handleShipAsteroidCollision(ship, asteroid, world);
                }
            }
        }
    }

    private void checkBulletAsteroidCollisions(World world) {
        for (Entity bullet : world.getEntities(Bullet.class)) {
            for (Entity asteroid : world.getEntities(Asteroid.class)) {
                if (collides(bullet, asteroid)) {
                    handleBulletAsteroidCollision(bullet, asteroid, world);
                }
            }
        }
    }

    private void checkBulletShipCollisions(World world) {
        List<Entity> bullets = world.getEntities(Bullet.class);
        List<Entity> ships = new ArrayList<>();
        ships.addAll(world.getEntities(Player.class));
        ships.addAll(world.getEntities(Enemy.class));

        for (Entity bullet : bullets) {
            for (Entity ship : ships) {
                if (!isFriendlyFire(bullet, ship) && collides(bullet, ship)) {
                    handleBulletShipCollision(bullet, ship, world);
                }
            }
        }
    } // Removed extra brace here

    private boolean collides(Entity a, Entity b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < (a.getRadius() + b.getRadius());
    }

    private boolean isFriendlyFire(Entity bullet, Entity ship) {
        String owner = ((Bullet) bullet).getOwner();
        return ship.getClass().getSimpleName().equalsIgnoreCase(owner);
    }

    private void handleShipAsteroidCollision(Entity ship, Entity asteroid, World world) {
        // Apply damage to the ship
        if (ship instanceof Player) {
            Player player = (Player) ship;
            player.takeDamage(1); // Damage value for asteroid collision
            if (player.getHealth() <= 0) {
                world.removeEntity(player);
            }
        } else if (ship instanceof Enemy) {
            Enemy enemy = (Enemy) ship;
            enemy.takeDamage(1);
            if (enemy.getHealth() <= 0) {
                world.removeEntity(enemy);
            }
        }
        // Remove the asteroid on collision
        world.removeEntity(asteroid);
    }
    private void handleBulletAsteroidCollision(Entity bullet, Entity asteroidEntity, World world) {
        Asteroid asteroid = (Asteroid) asteroidEntity;
        world.removeEntity(bullet);

        if (asteroid.getSize().children > 0) {
            splitAsteroid(asteroid, world);
        }
        world.removeEntity(asteroid);
    }

    private void splitAsteroid(Asteroid original, World world) {
        Asteroid.Size newSize = switch (original.getSize()) {
            case LARGE -> Asteroid.Size.MEDIUM;
            case MEDIUM -> Asteroid.Size.SMALL;
            default -> null;
        };

        if (newSize != null) {
            for (int i = 0; i < 2; i++) {
                Asteroid child = new Asteroid(
                        newSize,
                        original.getX(),
                        original.getY(),
                        original.getRotation() + (i == 0 ? 45 : -45)
                );
                world.addEntity(child);
            }
        }
    }

    private void handleBulletShipCollision(Entity bullet, Entity ship, World world) {
        world.removeEntity(bullet);

        if (ship instanceof Player) {
            Player player = (Player) ship;
            player.takeDamage(BULLET_DAMAGE); // Fixed typo: BULLET_DAMAGE
            if (player.getHealth() <= 0) {
                world.removeEntity(player);
            }
        } else if (ship instanceof Enemy) {
            Enemy enemy = (Enemy) ship;
            enemy.takeDamage(BULLET_DAMAGE);
            if (enemy.getHealth() <= 0) {
                world.removeEntity(enemy);
            }
        }
    }
}