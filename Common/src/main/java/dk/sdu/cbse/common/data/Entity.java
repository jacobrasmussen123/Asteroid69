package dk.sdu.cbse.common.data;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Entity implements Serializable {

    private final UUID ID = UUID.randomUUID();

    private double[] polygonCoordinates;
    private double x;
    private double y;
    private double rotation;
    private float radius;
    private double size;
    private double health;
    private Paint paint;
    private Boolean dead = false;
    private String type = getClass().getSimpleName();
    private final Map<Class<?>, Object> components = new HashMap<>();
    private Color color = Color.RED;

    public Color getBaseColor() {
        return color;
    }

    public String getID() {
        return ID.toString();
    }

    public void setPolygonCoordinates(double... coordinates ) {
        this.polygonCoordinates = coordinates;
    }

    public double[] getPolygonCoordinates() {
        return polygonCoordinates;
    }


    public void setX(double x) {
        this.x =x;
    }

    public double getX() {
        return x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getY() {
        return y;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return this.radius;
    }

    public Paint getPaint() { return paint; }

    public void setPaint(Paint paint) { this.paint = paint; }

    public Boolean getDead() { return dead; }

    public void setDead(Boolean dead) { this.dead = dead; }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getHealth() { return health; }

    public void setHealth(double health) { this.health = health; }

    public <T> void addComponent(T component) {
        components.put(component.getClass(), component);
    }

    public <T> T getComponent(Class<T> component) {
        return component.cast(components.get(component));
    }

    public <T> void removeComponent(Class<T> clazz) {
        components.remove(clazz);
    }

    public <T> boolean hasComponent(Class<T> clazz) {
        return components.containsKey(clazz);
    }

    public String getType() {return type;}

    public void setType(String type) {this.type = type; }

}