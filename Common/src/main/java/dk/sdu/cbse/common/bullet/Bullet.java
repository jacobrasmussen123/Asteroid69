package dk.sdu.cbse.common.bullet;


import dk.sdu.cbse.common.data.Entity;
import javafx.scene.paint.Color;

public class Bullet extends Entity {
    private String ownerType; // "Player" or "Enemy"

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }
    @Override
    public Color getBaseColor() {
        return Color.MAGENTA.brighter();
    }
}

