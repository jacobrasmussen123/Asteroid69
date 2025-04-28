package dk.sdu.cbse.common.bullet;



import dk.sdu.cbse.common.Entity;
import dk.sdu.cbse.common.GameData;

/**
 *
 * @author corfixen
 */
public interface BulletSPI {
    Entity createBullet(Entity e, GameData gameData);
}