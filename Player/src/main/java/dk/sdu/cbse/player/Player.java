package dk.sdu.cbse.player;

import dk.sdu.cbse.common.Entity;
import dk.sdu.cbse.player.systems.ShipDesign;

public class Player extends Entity {
    public static final double ACCEL     = 200;  // px/sec²
    public static final double MAX_SPEED = 300;  // px/sec
    public static final double ROT_SPEED = 180;  // deg/sec

    private double dx = 0, dy = 0;

    public Player() {
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