package BHTree;

public final class Body {
    private double x;
    private double y;
    private double vX;
    private double vY;
    private final double mass;
    private final String name;

    public Body(double x, double y, double vX, double vY, double mass, String name) {
        this.x = x;
        this.y = y;
        this.vX = vX;
        this.vY = vY;
        this.mass = mass;
        this.name = name;
    }

    public static Body newInstance(Body b) {
        return new Body(b.x, b.y, b.vX, b.vY, b.mass, b.name);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getMass() {
        return mass;
    }

    /**
     * Euclidean distance between two Bodies
     *
     * @param b the other body
     * @return the distance
     */
    public double distanceFrom(Body b) {
        return Math.sqrt(Math.pow(x - b.x, 2) + Math.pow(y - b.y, 2));
    }

    public static Body centerOfMass(Body a, Body b) {
        final double m = a.mass + b.mass;
        final double x = ((a.x * a.mass) + (b.x * b.mass)) / m;
        final double y = ((a.y * a.mass) + (b.y * b.mass)) / m;

        return new Body(x, y, 0, 0, m, "");
    }

    Force forceFrom(Body b) {
        return forceFrom(b, distanceFrom(b));
    }

    Force forceFrom(Body b, double distance) {
        final double G = 6.67E-11;

        final double F = G * mass * b.mass / Math.pow(distance, 2);
        final double Fx = F * (x - b.x) / distance;
        final double Fy = F * (y - b.y) / distance;

        return new Force(Fx, Fy);
    }

    public void applyForce(Force f) {
        double ax = f.Fx() / mass;
        double ay = f.Fy() / mass;

        //update speed
        vX += ax;
        vY += ay;

        //update position
        x += vX;
        y += vY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Body body = (Body) o;

        if (Double.compare(body.x, x) != 0) return false;
        if (Double.compare(body.y, y) != 0) return false;
        if (Double.compare(body.vX, vX) != 0) return false;
        if (Double.compare(body.vY, vY) != 0) return false;
        if (Double.compare(body.mass, mass) != 0) return false;
        return name.equals(body.name);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(vX);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(vY);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(mass);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        String sb = "{" + x + ", " + y + ", " + vX + ", " + vY + ", " + mass + ", \"" + name + "\"}";
        return sb;
    }
}