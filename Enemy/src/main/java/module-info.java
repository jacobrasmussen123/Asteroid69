module Enemy {
    requires Common;
    requires javafx.graphics;

    exports dk.sdu.cbse.enemy;
    uses dk.sdu.cbse.common.bullet.BulletSPI;


    provides dk.sdu.cbse.common.services.IGamePluginService with dk.sdu.cbse.enemy.EnemyPlugin;
    provides dk.sdu.cbse.common.services.IEntityProcessingService with dk.sdu.cbse.enemy.EnemyController;

}
