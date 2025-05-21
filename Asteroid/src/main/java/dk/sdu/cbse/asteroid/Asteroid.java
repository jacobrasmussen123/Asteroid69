package dk.sdu.cbse.asteroid;

import dk.sdu.cbse.common.data.Entity;
import javafx.scene.paint.Color;

public class Asteroid extends Entity {
    @Override
    public Color getBaseColor() {
        return Color.YELLOW;
    }
    public enum Size {
        LARGE(30, 2, 150),
        MEDIUM(20, 1, 250),
        SMALL(10, 0, 350);

        public final float radius;
        public final int children;
        public final float speed;

        Size(float radius, int children, float speed) {
            this.radius = radius;
            this.children = children;
            this.speed = speed;
        }
    }

    private final Size size;
    private double dx;
    private double dy;

    public Asteroid(Size size, double x, double y, double direction) {
        this.size = size;
        setPolygonCoordinates(generateShape());
        setRadius(size.radius);
        setX(x);
        setY(y);
        double rad = Math.toRadians(direction);
        this.dx = Math.cos(rad) * size.speed;
        this.dy = Math.sin(rad) * size.speed;
    }

    private double[] generateShape() {
        int points = 12;
        double[] shape = new double[points * 2];
        for (int i = 0; i < points; i++) {
            double angle = Math.toRadians(i * 360.0 / points);
            double variation = size.radius * (0.8 + Math.random() * 0.4);
            shape[i*2]   = Math.cos(angle) * variation;
            shape[i*2+1] = Math.sin(angle) * variation;
        }
        return shape;
    }

    public void update(float dt, int width, int height) {
        setX(getX() + dx * dt);
        setY(getY() + dy * dt);
        // screen wrapping
        if (getX() < -size.radius) setX(width + size.radius);
        else if (getX() > width + size.radius) setX(-size.radius);
        if (getY() < -size.radius) setY(height + size.radius);
        else if (getY() > height + size.radius) setY(-size.radius);
    }



    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }
}
