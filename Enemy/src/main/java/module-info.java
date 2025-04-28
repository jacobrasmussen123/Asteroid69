module Enemy {
    exports dk.sdu.cbse.enemy;
    requires Bullet;
    requires Common;
    provides dk.sdu.cbse.common.services.IGamePluginService with dk.sdu.cbse.enemy.EnemyPlugin;
}