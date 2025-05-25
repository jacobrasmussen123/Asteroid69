module Enemy {
    requires Common;
    requires javafx.graphics;

   exports dk.sdu.cbse.enemy; // renamed package
    exports dk.sdu.cbse.enemy.shared;        // renamed shared package

    provides dk.sdu.cbse.common.services.IGamePluginService
            with dk.sdu.cbse.enemy.shared.SharedPlugin;
    provides dk.sdu.cbse.common.services.IEntityProcessingService
            with dk.sdu.cbse.enemy.EnemyController;
    uses dk.sdu.cbse.common.bullet.BulletSPI;
}
