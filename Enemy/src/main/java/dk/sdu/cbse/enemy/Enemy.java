package dk.sdu.cbse.enemy;


import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.enemy.systems.ShipDesign;
import javafx.scene.paint.Color;

public class Enemy extends Entity {
    public static final double ACCEL     = 200;  // px/sec²
    public static final double MAX_SPEED = 300;  // px/sec
    public static final double ROT_SPEED = 180;  // deg/sec
    private int maxHealth = 100;
    private int health = maxHealth;
    private double dx = 0, dy = 0;

    @Override
    public Color getBaseColor() {
        return Color.GREEN;
    }
    public Enemy() {
        ShipDesign design = ShipDesign.random();
        setPolygonCoordinates(design.getShape());
        setRadius(10f);
        setRotation(0);
    }


    public double getDx() { return dx; }
    public double getDy() { return dy; }
    public void setDx(double dx) { this.dx = dx; }
    public void setDy(double dy) { this.dy = dy; }
}