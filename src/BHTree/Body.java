package BHTree;

import java.util.Objects;

public final class Body {
    private final String name;
    private final double mass;
    private double x;
    private double y;
    private double vX;
    private double vY;

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

    public static Body centerOfMass(Body a, Body b) {
        final double m = a.mass + b.mass;
        final double x = ((a.x * a.mass) + (b.x * b.mass)) / m;
        final double y = ((a.y * a.mass) + (b.y * b.mass)) / m;

        return new Body(x, y, 0, 0, m, "");
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

        // update speed
        vX += ax;
        vY += ay;

        // update position
        x += vX;
        y += vY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Body body = (Body) o;
        return Double.compare(mass, body.mass) == 0 &&
               Double.compare(x, body.x) == 0 &&
               Double.compare(y, body.y) == 0 &&
               Double.compare(vX, body.vX) == 0 &&
               Double.compare(vY, body.vY) == 0 &&
               Objects.equals(name, body.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, mass, x, y, vX, vY);
    }

    @Override
    public String toString() {
        return x + " " + y + " " + vX + " " + vY + " " + mass + " " + name;
    }
}