import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF uf;  // union, connected, count
    private WeightedQuickUnionUF uf_full;
    private int size;
    private boolean[] grid;       // boolean array
    private final int uf_size;
    private final int virtual_top;
    private final int virtual_bottom;
    private final int top_row;
    private final int bottom_row;
    private final int left_column;
    private final int right_column;

    public Percolation(int n) {   // create n-by-n grid, with all sites blocked
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        size = n;
        uf_size = size * size + 2;

        virtual_top = uf_size - 2;
        virtual_bottom = uf_size - 1;

        grid = new boolean[uf_size];

        top_row = 0;
        bottom_row = size - 1;
        left_column = 0;
        right_column = size - 1;

        for (int i = 0; i < size * size; i++) {
            grid[i] = false;
        }
        // open two virtual grid
        grid[virtual_top] = true;
        grid[virtual_bottom] = true;

        uf = new WeightedQuickUnionUF(uf_size);
        uf_full = new WeightedQuickUnionUF(uf_size - 1);
    }

    public void open(int row, int col) {   // open site (row, col) if it is not open already

        int i = row - 1;
        int j = col - 1;

        // validate args
        validateIndices(i, j);

        // open the grid
        int index = get_grid_index(i, j);
        if (!grid[index]) {
            grid[index] = true;

            // connect with surrounding grid
            if (i == top_row) {
                uf.union(index, virtual_top);
                uf_full.union(index, virtual_top);
                int bottomgrid = get_grid_index(i + 1, j);
                connectIfOpen(index, bottomgrid);
                if (size ==1) uf.union(index, virtual_bottom);
            }
            else if (i == bottom_row) {
                uf.union(index, virtual_bottom);
                int topgrid = get_grid_index(i - 1, j);
                connectIfOpen(index, topgrid);
            }
            else {
                int topgrid = get_grid_index(i - 1, j);
                int bottomgrid = get_grid_index(i + 1, j);
                connectIfOpen(index, topgrid);
                connectIfOpen(index, bottomgrid);
            }
            if (j == left_column) {
                int rightgrid = get_grid_index(i, j + 1);
                connectIfOpen(index, rightgrid);
            }
            else if (j == right_column) {
                int leftgrid = get_grid_index(i, j - 1);
                connectIfOpen(index, leftgrid);
            }
            else {
                int rightgrid = get_grid_index(i, j + 1);
                int leftgrid = get_grid_index(i, j - 1);
                connectIfOpen(index, rightgrid);
                connectIfOpen(index, leftgrid);
            }


        }
    }

    public boolean isOpen(int row, int col){ // is site (row, col) open?
        int i = row - 1;
        int j = col - 1;
        validateIndices(i, j);
        int index = get_grid_index(i, j);
        return grid[index];
    }
    public boolean isFull(int row, int col) { // is site (row, col) full?
        int i = row - 1;
        int j = col - 1;
        validateIndices(i, j);
        int index = get_grid_index(i, j);
        return uf_full.connected(virtual_top, index);

    }
    public int numberOfOpenSites() { // number of open sites
        int count = 0;
        for (int i = 0; i < grid.length-2; i++) {
            if (grid[i]) {
                count++;
            }
        }
        return count;
    }
    public boolean percolates() { // does the system percolate?
        return uf.connected(virtual_top, virtual_bottom);
    }

    public static void main(String[] args) { // test client (optional)
        Percolation p = new Percolation(6);
        openSiteAndLog(p, 1, 6);
        openSiteAndLog(p, 2, 1);

        System.out.println(p.isFull(4,4));
    }



    private void validateIndices(int i, int j) {
        if (i < 0 || i > size-1 || j < 0 || j > size-1) {
            throw new IllegalArgumentException();
        }
    }

    private int get_grid_index(int row, int col) {
        return row * size + col;
    }


    private void connectIfOpen(int ufIndex, int targetufIndex) {
        if (grid[targetufIndex]) {
            uf.union(ufIndex, targetufIndex);
            uf_full.union(ufIndex, targetufIndex);
        }
    }

    private static void openSiteAndLog(Percolation p, int i, int j) {
        p.open(i, j);
        //StdOut.println("p.isOpen(" + i + "," + j + "); => " + p.isOpen(i, j));
    }

}




