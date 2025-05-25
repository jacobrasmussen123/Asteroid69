package dk.sdu.cbse.player;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.Health;
import dk.sdu.cbse.player.systems.ShipDesign;
import javafx.scene.paint.Color;

public class Player extends Entity {
    public static final double ACCEL     = 300;  // px/sec²
    public static final double MAX_SPEED = 400;  // px/sec
    public static final double ROT_SPEED = 250;  // deg/sec
    private int maxHealth = 100;
    private int health = maxHealth;
    private double dx = 0, dy = 0;

    public Player() {
        ShipDesign design = ShipDesign.random();
        setPolygonCoordinates(design.getShape());
        setRadius(10f);
        setRotation(0);
        addComponent(new Health(1));
    }

    public double getDx() { return dx; }
    public double getDy() { return dy; }
    public void setDx(double dx) { this.dx = dx; }
    public void setDy(double dy) { this.dy = dy; }

    @Override
    public Color getBaseColor() {
        return Color.CRIMSON;
    }
}