module Core{

    uses dk.sdu.cbse.common.GamePlugin;
    uses dk.sdu.cbse.common.services.IGamePluginService;
    requires javafx.graphics;
    requires javafx.controls;
    requires Common;

    opens dk.sdu.cbse.core to javafx.graphics;
}
