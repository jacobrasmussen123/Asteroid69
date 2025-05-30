package dk.sdu.cbse.common.bullet;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;


/**
 * SPI for creating bullets in the game world.
 * Implementations define how a bullet entity is instantiated and initialized.
 */
public interface BulletSPI {
    /**
     * Defines how bullets are created in the game world based on a shooter(e) entity and context.
     * Preconditions:
     * @param e  must not be null and represent a valid firing entity.
     * @param gameData must not be null and provide the current game context (delta time, display size, input state).
     * Post conditions:
     * Returns a new Entity instance configured as a bullet and ready for addition to the world.
     */
    Entity createBullet(Entity e, GameData gameData);
}

