package dk.sdu.cbse.enemy;


import dk.sdu.cbse.common.Entity;
import dk.sdu.cbse.enemy.systems.ShipDesign;

public class Enemy extends Entity {
    public static final double ACCEL     = 200;  // px/sec²
    public static final double MAX_SPEED = 300;  // px/sec
    public static final double ROT_SPEED = 180;  // deg/sec
    private int maxHealth = 100;
    private int health = maxHealth;
    private double dx = 0, dy = 0;

    public Enemy() {
        ShipDesign design = ShipDesign.random();
        setPolygonCoordinates(design.getShape());
        setRadius(10f);
        setRotation(0);
    }

    public void takeDamage(int damage) {
        health = Math.max(0, health - damage);
    }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }

    public double getDx() { return dx; }
    public double getDy() { return dy; }
    public void setDx(double dx) { this.dx = dx; }
    public void setDy(double dy) { this.dy = dy; }
}