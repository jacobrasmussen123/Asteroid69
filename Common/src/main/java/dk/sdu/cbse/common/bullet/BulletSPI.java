package dk.sdu.cbse.common.bullet;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;


public interface BulletSPI {
    Entity createBullet(Entity e, GameData gameData);
}