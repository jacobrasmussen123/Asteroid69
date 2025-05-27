module Core {
    requires javafx.graphics;
    requires javafx.controls;
    requires Common;
    requires spring.context;
    requires spring.core;
    requires spring.beans;

    opens dk.sdu.cbse.core to spring.core, spring.beans,javafx.graphics,spring.context;

    uses dk.sdu.cbse.common.services.IEntityProcessingService;
    uses dk.sdu.cbse.common.services.IGamePluginService;
    uses dk.sdu.cbse.common.services.IPostEntityProcessingService;
}