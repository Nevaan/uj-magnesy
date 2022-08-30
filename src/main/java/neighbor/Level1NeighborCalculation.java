package neighbor;

import point.Point;

import java.util.ArrayList;
import java.util.List;

public class Level1NeighborCalculation extends NeighborCalculation {

    public Level1NeighborCalculation(int maxX, int maxY) {
        super(maxX, maxY, 1);
    }

    @Override
    List<Point> computeNeighbors(int x, int y) {

        List<Point> levelOneCoordinates = new ArrayList<>();
        levelOneCoordinates.add(new Point(checkX(x), checkY(y-1)));
        levelOneCoordinates.add(new Point(checkX(x), checkY(y+1)));
        levelOneCoordinates.add(new Point(checkX(x+1), checkY(y)));
        levelOneCoordinates.add(new Point(checkX(x-1), checkY(y)));

        return levelOneCoordinates;
    }

    @Override
    protected NeighborCalculation getNextHandler() {
        return new Level2NeighborCalculation(this.maxX, this.maxY);
    }
}