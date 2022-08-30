package neighbor;

import point.Point;

import java.util.ArrayList;
import java.util.List;

public class Level2NeighborCalculation  extends NeighborCalculation {

    public Level2NeighborCalculation(int maxX, int maxY) {
        super(maxX, maxY, 2);
    }

    @Override
    List<Point> computeNeighbors(int x, int y) {

        List<Point> levelTwoCoordinates = new ArrayList<>();


        levelTwoCoordinates.add(new Point(checkX(x-1), checkY(y+1)));
        levelTwoCoordinates.add(new Point(checkX(x+1), checkY(y+1)));
        levelTwoCoordinates.add(new Point(checkX(x-1), checkY(y-1)));
        levelTwoCoordinates.add(new Point(checkX(x+1), checkY(y-1)));

        return levelTwoCoordinates;
    }

    @Override
    protected NeighborCalculation getNextHandler() {
        return new Level3NeighborCalculation(this.maxX, this.maxY);
    }
}