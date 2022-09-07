package neighbor;

import point.Point;

import java.util.List;
import java.util.Map;

public abstract class NeighbourCalculation {

    protected final int level;
    protected final int maxX;
    protected final int maxY;

    protected NeighbourCalculation(int maxX, int maxY, int level) {
        this.maxX = maxX;
        this.maxY = maxY;
        this.level = level;
    }

    protected abstract List<Point> computeNeighbours(int x, int y);

    public Map<Integer, List<Point>> addNeighbours(Map<Integer, List<Point>> neighborsMap, int x, int y) {
        neighborsMap.put(level, computeNeighbours(x, y));

        NeighbourCalculation nextHandler = getNextHandler();

        if (nextHandler != null) {
            return getNextHandler().addNeighbours(neighborsMap, x, y);
        }

        return neighborsMap;

    }


    protected NeighbourCalculation getNextHandler() {
        return null;
    }

    protected int checkX(int x) {
        if (x<0) {
            return maxX + x;
        }

        if (x>=maxX) {
            return x - maxX;
        }

        return x;
    }

    protected int checkY(int y) {

        if (y<0) {
            return maxY + y;
        }

        if (y>=maxY) {
            return y - maxY;
        }

        return y;
    }

}