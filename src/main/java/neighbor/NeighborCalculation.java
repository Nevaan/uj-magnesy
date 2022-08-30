package neighbor;

import point.Point;

import java.util.List;
import java.util.Map;

public abstract class NeighborCalculation {

    protected final int level;
    protected int maxX;
    protected int maxY;

    public NeighborCalculation(int maxX, int maxY, int level) {
        this.maxX = maxX;
        this.maxY = maxY;
        this.level = level;
    }

    abstract List<Point> computeNeighbors(int x, int y, int level);

    protected NeighborCalculation getNextHandler() {
        return null;
    }

    public Map<Integer, List<Point>> addNeighbors(Map<Integer, List<Point>> neighborsMap, int x, int y) {
        neighborsMap.put(level, computeNeighbors(x, y, level));

        NeighborCalculation nextHandler = getNextHandler();

        if (nextHandler != null) {
            return getNextHandler().addNeighbors(neighborsMap, x, y);
        }

        return neighborsMap;

    }

    protected int checkX(int x) {
        if(x<0) {
            return maxX + x;
        }

        if(x>=maxX) {
            return x - maxX;
        }

        return x;
    }

    protected int checkY(int y) {

        if(y<0) {
            return maxY + y;
        }

        if(y>=maxY) {
            return y - maxY;
        }

        return y;
    }

}
