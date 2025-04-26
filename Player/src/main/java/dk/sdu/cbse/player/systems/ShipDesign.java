package dk.sdu.cbse.player.systems;


public enum ShipDesign {
    CLASSIC(new double[]{10, 0, -10, -7, -10, 7}),
    ARROWHEAD(new double[]{15, 0, -10, -10, -5, 0, -10, 10}),
    STEALTH(new double[]{12, 0, -8, -8, -4, 0, -8, 8});

    private final double[] shape;

    ShipDesign(double[] shape) {
        this.shape = shape;
    }

    public double[] getShape() {
        return shape;
    }

    public static ShipDesign random() {
        ShipDesign[] all = values();
        return all[(int) (Math.random() * all.length)];
    }
}
