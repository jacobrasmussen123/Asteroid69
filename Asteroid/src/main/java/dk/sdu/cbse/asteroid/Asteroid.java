package dk.sdu.cbse.asteroid;

import dk.sdu.cbse.common.Entity;
import java.util.Arrays;

public class Asteroid extends Entity {
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
        dx = Math.cos(rad) * size.speed;
        dy = Math.sin(rad) * size.speed;
    }

    private double[] generateShape() {
        int points = 12;
        double[] shape = new double[points * 2];
        for(int i = 0; i < points; i++) {
            double angle = Math.toRadians(i * 360.0 / points);
            double radiusVariation = size.radius * (0.8 + Math.random() * 0.4);
            shape[i*2] = Math.cos(angle) * radiusVariation;
            shape[i*2+1] = Math.sin(angle) * radiusVariation;
        }
        return shape;
    }

    public Size getSize() { return size; }
    public double getDx() { return dx; }
    public double getDy() { return dy; }
}