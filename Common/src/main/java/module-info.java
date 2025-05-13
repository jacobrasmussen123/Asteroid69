module Common {
    requires javafx.graphics;
    exports dk.sdu.cbse.common;
    exports dk.sdu.cbse.common.services;

    uses dk.sdu.cbse.common.services.IGamePluginService;
}
