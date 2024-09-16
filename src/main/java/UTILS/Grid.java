package UTILS;

class Grid {
    private final Point[][] grid;
    private final int MAX_X;
    private final int MAX_Y;

    private Point firstPoint;
    private Point lastPoint;

    public Grid(Point[][] grid) {
        this.grid = grid;
        this.MAX_X = grid[0].length - 1;
        this.MAX_Y = grid.length - 1;

    }

    public void printCords() {
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = 0; j < grid[i].length; j++) {
                getPointByCords(j, i).printCords();
            }
            System.out.println();
        }
    }

    public void printSigns() {
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = 0; j < grid[i].length; j++) {
                getPointByCords(j, i).printSign();
            }
            System.out.println();
        }
    }

    public Point getPointByCords(int x, int y) {
        return grid[y][x];
    }
}
