module Player {
    requires Bullet;
    requires Common;
    provides dk.sdu.cbse.common.services.IGamePluginService with dk.sdu.cbse.player.PlayerPlugin;
}