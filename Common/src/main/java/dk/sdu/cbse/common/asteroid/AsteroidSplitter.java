package dk.sdu.cbse.common.asteroid;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.World;

public interface AsteroidSplitter {
    void SplitAsteroid(Entity e, World world);
}