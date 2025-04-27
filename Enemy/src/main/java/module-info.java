module Enemy {
    requires Bullet;
    requires Common;
    provides dk.sdu.cbse.common.GamePlugin with dk.sdu.cbse.enemy.EnemyPlugin;
}