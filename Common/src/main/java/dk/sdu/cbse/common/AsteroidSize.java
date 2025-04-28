package dk.sdu.cbse.common;

public enum AsteroidSize {
    LARGE(30, true),
    MEDIUM(20, true),
    SMALL(10, false);

    private final float radius;
    private final boolean canSplit;

    AsteroidSize(float radius, boolean canSplit) {
        this.radius = radius;
        this.canSplit = canSplit;
    }

    public float getRadius() {
        return radius;
    }

    public boolean canSplit() {
        return canSplit;
    }

    public AsteroidSize nextSize() {
        return switch (this) {
            case LARGE -> MEDIUM;
            case MEDIUM -> SMALL;
            default -> null;
        };
    }
}