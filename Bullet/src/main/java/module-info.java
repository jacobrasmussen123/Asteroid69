module Bullet {
    requires Common;
    requires CommonBullet;
    exports dk.sdu.cbse.bullet;
    provides dk.sdu.cbse.common.services.IGamePluginService with dk.sdu.cbse.bullet.BulletPlugin;
}
