package BHTree;

public record Force(double Fx, double Fy) {
    public static Force add(Force a, Force b) {
        return new Force(a.Fx + b.Fx, a.Fy + b.Fy);
    }
}
