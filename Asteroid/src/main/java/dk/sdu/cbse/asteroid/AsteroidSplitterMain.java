package dk.sdu.cbse.asteroid;

import dk.sdu.cbse.common.asteroid.AsteroidSplitter;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.World;

import java.util.Random;

public class AsteroidSplitterMain implements AsteroidSplitter {
    private final Random random = new Random();

    @Override
    public void SplitAsteroid(Entity e, World world) {
        // Only split Asteroid instances
        if (!(e instanceof Asteroid)) {
            return;
        }
        Asteroid parent = (Asteroid) e;
        double radius = parent.getRadius();
        Asteroid.Size currentSize;
        if (radius > Asteroid.Size.MEDIUM.radius) {
            currentSize = Asteroid.Size.LARGE;
        } else if (radius > Asteroid.Size.SMALL.radius) {
            currentSize = Asteroid.Size.MEDIUM;
        } else {

            return;
        }

        int childrenCount = currentSize.children;
        Asteroid.Size newSize = (currentSize == Asteroid.Size.LARGE)
                ? Asteroid.Size.MEDIUM
                : Asteroid.Size.SMALL;

        double x = parent.getX();
        double y = parent.getY();

        double baseDir = Math.atan2(parent.getDy(), parent.getDx());

        for (int i = 0; i < childrenCount; i++) {
            double spreadRad = Math.toRadians(random.nextDouble() * 60.0 - 30.0);
            double childDirRad = baseDir + spreadRad;
            double childDirDeg = Math.toDegrees(childDirRad);

            Asteroid child = new Asteroid(newSize, x, y, childDirDeg);
            world.addEntity(child);
        }
    }
}
