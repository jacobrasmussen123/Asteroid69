module Player {
    requires Common;
    requires javafx.graphics;


    exports dk.sdu.cbse.player;
    exports dk.sdu.cbse.player.systems;

    provides dk.sdu.cbse.common.services.IGamePluginService with dk.sdu.cbse.player.PlayerPlugin;

}