module Player {
    exports dk.sdu.cbse.player;
    requires Bullet;
    requires Common;
    requires javafx.graphics;
    provides dk.sdu.cbse.common.services.IGamePluginService with dk.sdu.cbse.player.PlayerPlugin;
}