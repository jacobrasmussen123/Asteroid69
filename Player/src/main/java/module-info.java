module Player {
    requires Common;
    requires javafx.graphics;

    uses dk.sdu.cbse.common.bullet.BulletSPI;
    exports dk.sdu.cbse.player;
    exports dk.sdu.cbse.player.systems;

    provides dk.sdu.cbse.common.services.IGamePluginService
            with dk.sdu.cbse.player.PlayerPlugin;
    provides dk.sdu.cbse.common.services.IEntityProcessingService
            with dk.sdu.cbse.player.systems.PlayerControlSystem;
}
