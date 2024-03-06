package BHTree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class BHTree {
    private final BoundingBox box;
    private Body body;
    private Body centerOfMass;
    private BHTree quadI;
    private BHTree quadII;
    private BHTree quadIII;
    private BHTree quadIV;

    public BHTree(BoundingBox box) {
        this.box = box;
    }

    public static BHTree fromFile(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            int bodiesCount = Integer.parseInt(br.readLine());
            final double worldSize = Double.parseDouble(br.readLine());

            BHTree root = new BHTree(new BoundingBox(0, 0, worldSize));

            while (bodiesCount-- != 0) {
                String[] t = br.readLine().split(" ");
                double x = Double.parseDouble(t[0]);
                double y = Double.parseDouble(t[1]);
                double vX = Double.parseDouble(t[2]);
                double vY = Double.parseDouble(t[3]);
                double m = Double.parseDouble(t[4]);
                root.insert(new Body(x, y, vX, vY, m, t[5]));
            }

            return root;
        } catch (IOException e) {
            System.err.println("file" + path + " could not be read");
            System.exit(1);
        } catch (NumberFormatException e) {
            System.err.println("Error while parsing numbers from input file: " + e);
            System.exit(1);
        }
        return null;
    }

    public synchronized void insert(Body b) {

        if (body == null && quadI == null) {
            body = b;
            centerOfMass = Body.newInstance(b);
            return;
        }

        if (quadI == null) divideIntoFour();

        centerOfMass = Body.centerOfMass(centerOfMass, b);

        insertInAppropriateQuad(b);
    }

    private synchronized void divideIntoFour() {
        quadI = new BHTree(box.calcQuadI());
        quadII = new BHTree(box.calcQuadII());
        quadIII = new BHTree(box.calcQuadIII());
        quadIV = new BHTree(box.calcQuadIV());

        // move existing body in new quad (since we divided the tree)
        insertInAppropriateQuad(body);
        body = null;
    }

    private synchronized void insertInAppropriateQuad(Body body) {
        if (quadI.box.contains(body)) {
            quadI.insert(body);
        } else if (quadII.box.contains(body)) {
            quadII.insert(body);
        } else if (quadIII.box.contains(body)) {
            quadIII.insert(body);
        } else if (quadIV.box.contains(body)) {
            quadIV.insert(body);
        }
    }

    public List<Body> getAllBodies() {
        List<Body> all = new ArrayList<>();

        if (body != null) all.add(body);

        if (quadI == null) return all;

        all.addAll(quadI.getAllBodies());
        all.addAll(quadII.getAllBodies());
        all.addAll(quadIII.getAllBodies());
        all.addAll(quadIV.getAllBodies());

        return all;
    }

    public List<BHTree> getAllNodesWithBodies() {
        List<BHTree> all = new ArrayList<>();

        if (body != null) all.add(this);

        if (quadI == null) return all;

        all.addAll(quadI.getAllNodesWithBodies());
        all.addAll(quadII.getAllNodesWithBodies());
        all.addAll(quadIII.getAllNodesWithBodies());
        all.addAll(quadIV.getAllNodesWithBodies());

        return all;
    }

    public Body getBody() {
        return body;
    }

    public double getLength() {
        return box.size() * 2;
    }

    public Force calculateForce(Body body, final double boxDimension) {
        final boolean containedHere = box.contains(body);
        final double distance = centerOfMass.distanceFrom(body);

        if (distance > boxDimension && !containedHere) {
            return body.forceFrom(centerOfMass, distance);
        } else {
            Force totalForce = new Force(0, 0);

            List<Body> allBodies = getAllBodies();
            for (Body b : allBodies) {
                if (Objects.equals(b, body)) continue;
                totalForce = Force.add(totalForce, body.forceFrom(b));
            }
            return totalForce;
        }
    }

    public BoundingBox getBox() {
        return box;
    }

    private static int levelsDeep = -1;
    public String toStringTreeFormat() {
        ++levelsDeep;
        StringBuilder sb = new StringBuilder();

        sb.append("\t".repeat(levelsDeep));
        // for (int i = 0; i < levelsDeep; ++i) sb.append('\t');

        sb.append(box);
        if (centerOfMass != null)
            sb.append(", centerOfMass{").append(centerOfMass.getX()).append(", ").append(centerOfMass.getY())
              .append("}, totalMass: ").append(centerOfMass.getMass());

        if (body != null) sb.append(" -> ").append(body);
        sb.append('\n');

        if (quadI != null) {
            sb.append(quadI.toStringTreeFormat());
            sb.append(quadII.toStringTreeFormat());
            sb.append(quadIII.toStringTreeFormat());
            sb.append(quadIV.toStringTreeFormat());
        }

        --levelsDeep;
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        if (body != null) {
            sb.append(body).append('\n');
            return sb.toString();
        }

        if (quadI == null) return "";

        sb.append(quadI);
        sb.append(quadII);
        sb.append(quadIII);
        sb.append(quadIV);
        return sb.toString();
    }
}
