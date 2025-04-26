module Core{

    uses dk.sdu.cbse.common.GamePlugin;
    requires javafx.graphics;
    requires javafx.controls;
    requires Common;

    opens dk.sdu.cbse.core to javafx.graphics;
}
