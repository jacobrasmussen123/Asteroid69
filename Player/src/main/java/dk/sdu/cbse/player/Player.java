package dk.sdu.cbse.player;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.player.systems.ShipDesign;

public class Player extends Entity {
    public static final double ACCEL = 200.0;         // px/sec²
    public static final double MAX_SPEED = 300.0;     // px/sec
    public static final double ROT_SPEED = 180.0;     // deg/sec
    public static final double DECEL_FACTOR = 0.9;    // velocity retention per frame
    public static final int PLAYER_MAX_HEALTH = 100;

    private double dx = 0.0;
    private double dy = 0.0;
    private int health;

    public Player() {
        this.health = PLAYER_MAX_HEALTH;
        ShipDesign design = ShipDesign.random();
        setPolygonCoordinates(design.getShape());
        setRadius(10f);
        setRotation(0);
    }

    // Velocity getters/setters
    public double getDx() { return dx; }
    public void setDx(double dx) { this.dx = dx; }
    public double getDy() { return dy; }
    public void setDy(double dy) { this.dy = dy; }

    // Movement constants accessors
    public double getMaxSpeed() { return MAX_SPEED; }
    public double getAcceleration() { return ACCEL; }
    public double getRotationSpeed() { return ROT_SPEED; }
    public double getDecelFactor() { return DECEL_FACTOR; }

    // Health management
    public double getHealth() { return health; }
    public void takeDamage(int damage) {
        this.health = Math.max(0, this.health - damage);
    }
}
