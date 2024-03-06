package BHTree;

import java.util.List;
import java.util.concurrent.Callable;

public class SimulateTime implements Callable<Object> {

    private final BHTree root;
    private final List<BHTree> nodesWithBodies;

    private final BHTree nextGen;

    public SimulateTime(BHTree root, List<BHTree> nodesWithBodies, BHTree nextGen) {
        this.root = root;
        this.nodesWithBodies = nodesWithBodies;
        this.nextGen = nextGen;
    }

    @Override
    public Object call() {
        for (BHTree x : nodesWithBodies) {
            Body body = Body.newInstance(x.getBody());
            Force force = root.calculateForce(body, x.getLength());
            body.applyForce(force);
            synchronized (this) {
                nextGen.insert(body);
            }
        }
        return null;
    }
}