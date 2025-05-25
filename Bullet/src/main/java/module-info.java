module Bullet {
    requires Common;
    requires javafx.graphics;

    exports dk.sdu.cbse.bullet;

    provides dk.sdu.cbse.common.services.IGamePluginService
            with dk.sdu.cbse.bullet.BulletPlugin;
    provides dk.sdu.cbse.common.services.IEntityProcessingService
            with dk.sdu.cbse.bullet.BulletSystem;
    provides dk.sdu.cbse.common.bullet.BulletSPI
            with dk.sdu.cbse.bullet.BulletSystem;
}
