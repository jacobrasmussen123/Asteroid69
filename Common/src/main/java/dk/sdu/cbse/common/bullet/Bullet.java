package dk.sdu.cbse.common.bullet;


import dk.sdu.cbse.common.data.Entity;
import javafx.scene.paint.Color;

public class Bullet extends Entity {
    @Override
    public Color getBaseColor() {
        return Color.MAGENTA.brighter();   // a pure, bright magenta
    }
}

