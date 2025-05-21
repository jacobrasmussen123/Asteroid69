// dk/sdu/cbse/asteroid/split/AsteroidSplitterMain.java
package dk.sdu.cbse.asteroid;

import dk.sdu.cbse.common.asteroid.AsteroidSplitter;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.asteroid.Asteroid;

import java.util.Random;

public class AsteroidSplitterMain implements AsteroidSplitter {
    private final Random random = new Random();

    @Override
    public void SplitAsteroid(Entity e, World world) {
        float radius = e.getRadius();
        if (radius <= Asteroid.Size.SMALL.radius) {
            return;  // can’t split any smaller
        }

        // Determine next size
        Asteroid.Size newSize = switch (Asteroid.Size.values()[0]) {
            case LARGE  -> Asteroid.Size.MEDIUM;
            case MEDIUM -> Asteroid.Size.SMALL;
            default      -> null;
        };

        double x = e.getX();
        double y = e.getY();
        float baseDir = (float) e.getRotation();

        // spawn two children with ± random spread
        for (int i = 0; i < 2; i++) {
            float spread = (random.nextFloat() * 60f) - 30f;
            float dir = baseDir + spread;
            Asteroid child = new Asteroid(newSize, x, y, dir);
            world.addEntity(child);
        }
    }
}
