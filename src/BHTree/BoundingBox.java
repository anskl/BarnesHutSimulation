package BHTree;

public final class BoundingBox {
    private final double x;
    private final double y;
    private final double size;

    public BoundingBox(double x, double y, double size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public static BoundingBox newInstance(BoundingBox b) {
        return new BoundingBox(b.x, b.y, b.size);
    }


    boolean contains(Body b) {
        return x - size <= b.getX() && b.getX() <= x + size && y - size <= b.getY() && b.getY() <= y + size;
    }

    public BoundingBox calcQuadI() {
        return new BoundingBox(x + (size / 2), y + (size / 2), size / 2);
    }

    public BoundingBox calcQuadII() {
        return new BoundingBox(x - (size / 2), y + (size / 2), size / 2);
    }

    public BoundingBox calcQuadIII() {
        return new BoundingBox(x - (size / 2), y - (size / 2), size / 2);
    }

    public BoundingBox calcQuadIV() {
        return new BoundingBox(x + (size / 2), y - (size / 2), size / 2);
    }

    public double getSize() {
        return size;
    }

    @Override
    public String toString() {
        String sb = "center{" + x + ", " + y + "} size: " + size;
        return sb;
    }
}
