package dk.sdu.cbse.bullet;

import dk.sdu.cbse.common.Entity;

public class Bullet extends Entity {
    private double dx, dy;
    private float timeAlive = 1;
    private final float maxLifetime = 3.0f; // in seconds

    public Bullet(double x, double y, double rotDeg, BulletConfig config) {
        setPolygonCoordinates(-2, -2, 2, -2, 2, 2, -2, 2);
        setRadius(config.getRadius());
        setX(x);
        setY(y);
        setRotation(rotDeg);

        double rad = Math.toRadians(rotDeg);
        dx = Math.cos(rad) * config.getSpeed();
        dy = Math.sin(rad) * config.getSpeed();
    }

    public boolean isExpired() {
        return timeAlive >= maxLifetime;
    }

    public void update(float dt, int w, int h) {
        timeAlive += dt;
        setX(getX() + dx * dt);
        setY(getY() + dy * dt);
    }
}