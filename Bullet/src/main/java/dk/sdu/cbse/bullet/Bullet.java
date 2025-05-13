package dk.sdu.cbse.bullet;

import dk.sdu.cbse.common.Entity;

public class Bullet extends Entity {
    private final double dx;
    private final double dy;
    private float timeAlive = 0f;
    private static final float MAX_LIFETIME = 3.0f;
    private String owner;

    public Bullet(double x, double y, double rotDeg, BulletConfig config) {
        // square sprite
        setPolygonCoordinates(-2, -2, 2, -2, 2, 2, -2, 2);
        setRadius(config.getRadius());
        setX(x);
        setY(y);
        setRotation(rotDeg);
        double rad = Math.toRadians(rotDeg);
        this.dx = Math.cos(rad) * config.getSpeed();
        this.dy = Math.sin(rad) * config.getSpeed();
    }

    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }

    public boolean isExpired() {
        return timeAlive >= MAX_LIFETIME;
    }

    public void update(float dt, int width, int height) {
        timeAlive += dt;
        setX(getX() + dx * dt);
        setY(getY() + dy * dt);

        // expire when leaving screen bounds
        if (getX() < 0 || getX() > width || getY() < 0 || getY() > height) {
            timeAlive = MAX_LIFETIME;
        }
    }
}