package dk.sdu.cbse.common.asteroid;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.World;

/**
 * SPI for splitting asteroids into smaller fragments when destroyed.
 * Implementations determine the fragmentation logic and spawn new asteroid entities.
 */

public interface AsteroidSplitter {
    /**
     * Splits a destroyed asteroid into smaller fragments and adds them to the world.

     * Preconditions:
     * @param e must not be null and represent a valid asteroid entity.
     * @param world    must not be null and allow adding new entities.
     * Post conditions:
     * The original asteroid is removed, and zero or more new fragment entities are added to the world.
     */
    void SplitAsteroid(Entity e, World world);
}

