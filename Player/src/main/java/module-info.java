module Player {
    requires Common;
    requires javafx.graphics;

    exports dk.sdu.cbse.player;          // renamed from dk.sdu.cbse.player.main
    exports dk.sdu.cbse.player.systems;
    exports dk.sdu.cbse.player.shared;       // renamed shared package

    provides dk.sdu.cbse.common.services.IGamePluginService
            with dk.sdu.cbse.player.shared.SharedPlugin;
    provides dk.sdu.cbse.common.services.IEntityProcessingService
            with dk.sdu.cbse.player.systems.PlayerControlSystem;
    uses dk.sdu.cbse.common.bullet.BulletSPI;
}
