import BHTree.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static BHTree simulateTime(BHTree old, int generations, int threads) throws InterruptedException {
        BHTree nextGen;
        try (ExecutorService exec = Executors.newFixedThreadPool(threads)) {
            while (generations-- != 0) {
                nextGen = new BHTree(BoundingBox.newInstance(old.box));

                List<BHTree> list = old.getAllNodesWithBodies();

                int from = 0;
                int to = 0;
                int step = list.size() / threads;
                List<Callable<Object>> toCall = new ArrayList<>(2);

                for (int i = 0; i < threads; ++i) {
                    to = (i < threads - 1) ? (from + step) : list.size();
                    List<BHTree> all = list.subList(from, to);
                    from += step;

                    toCall.add(new SimulateTime(old, all, nextGen));
                }
                exec.invokeAll(toCall);
                old = nextGen;
            }

            exec.shutdown();
        }

        return old;
    }

    public static BHTree simulateTimeSerial(BHTree root, int generations) {
        while (generations-- != 0) {
            BHTree nextGen = new BHTree(BoundingBox.newInstance(root.box));
            for (BHTree x : root.getAllNodesWithBodies()) {
                Body body = Body.newInstance(x.getBody());
                Force force = root.calculateForce(body, x.getLength());
                body.applyForce(force);
                nextGen.insert(body);
            }
            root = nextGen;
        }
        return root;
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 4) {
            System.err.println("USAGE: Main [input-file] [#generations] [#threads] [out-file]");
            System.exit(1);
        }

        BHTree root = BHTree.read(args[0]);

        int generations = Integer.parseInt(args[1]);
        int threads = Integer.parseInt(args[2]);

        long startTime = System.nanoTime();
        root = simulateTime(root, generations, threads);
        double secondsPassed = (System.nanoTime() - startTime) * 1e-9;

        System.out.println("secondsPassed = " + secondsPassed);

        if (args.length == 4) {
            try (PrintWriter pw = new PrintWriter(args[3])) {
                pw.print(root);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
