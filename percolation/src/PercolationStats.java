import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final int numberOfTrials;
    private final int gridSize;
    private double[] threshholds;
    private final double mean;
    private final double stddev;

    public PercolationStats(int n, int t) { // perform trials independent experiments on an n-by-n grid
        if (n <= 0 || t <= 0) {
            throw new IllegalArgumentException();
        }
        gridSize = n;
        numberOfTrials = t;

        threshholds = new double[numberOfTrials];

        for (int k = 0; k < numberOfTrials; k++) {
            int openedGrids = 0;
            Percolation p = new Percolation(gridSize);
            while (!p.percolates()) {
                int i = StdRandom.uniform(1, gridSize + 1);
                int j = StdRandom.uniform(1, gridSize + 1);
                if (!p.isOpen(i, j)) {
                    p.open(i, j);
                    openedGrids++;
                }
            }
            threshholds[k] = (double) openedGrids / (gridSize * gridSize);
        }
        mean = StdStats.mean(threshholds);
        stddev = StdStats.stddev(threshholds);
    }
    public double mean() { // sample mean of percolation threshold
        return mean;
    }
    public double stddev() {  // sample standard deviation of percolation threshold
        return stddev;
    }
    public double confidenceLo() { // low  endpoint of 95% confidence interval
        return mean - (1.96 * stddev) / Math.sqrt(numberOfTrials);
    }
    public double confidenceHi() {  // high endpoint of 95% confidence interval
        return mean + (1.96 * stddev) / Math.sqrt(numberOfTrials);
    }

    public static void main(String[] args) {  // test client (described below)
        if (args == null || args.length != 2){
            throw new IllegalArgumentException();
        }

        int size = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(size, trials);

        System.out.println("mean                              = " + stats.mean());
        System.out.println("stddev                            = " + stats.stddev());
        System.out.println("95% confidence interval           = [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }
}
