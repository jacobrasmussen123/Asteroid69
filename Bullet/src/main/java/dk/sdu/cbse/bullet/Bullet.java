package dk.sdu.cbse.bullet;

import dk.sdu.cbse.common.Entity;

public class Bullet extends Entity {
    private double dx, dy;

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

    public void update(float dt, int w, int h) {
        setX(wrap(getX() + dx * dt, w));
        setY(wrap(getY() + dy * dt, h));
    }

    private double wrap(double val, double max) {
        if (val < 0) return val + max;
        if (val > max) return val - max;
        return val;
    }
}