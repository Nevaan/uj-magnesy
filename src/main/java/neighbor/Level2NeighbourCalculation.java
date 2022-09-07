package neighbor;

import point.Point;

import java.util.ArrayList;
import java.util.List;

public class Level2NeighbourCalculation extends NeighbourCalculation {

    protected Level2NeighbourCalculation(int maxX, int maxY) {
        super(maxX, maxY, 2);
    }

    @Override
    protected List<Point> computeNeighbours(int x, int y) {

        List<Point> levelTwoCoordinates = new ArrayList<>();


        levelTwoCoordinates.add(new Point(checkX(x-1), checkY(y+1)));
        levelTwoCoordinates.add(new Point(checkX(x+1), checkY(y+1)));
        levelTwoCoordinates.add(new Point(checkX(x-1), checkY(y-1)));
        levelTwoCoordinates.add(new Point(checkX(x+1), checkY(y-1)));

        return levelTwoCoordinates;
    }

    @Override
    protected NeighbourCalculation getNextHandler() {
        return new Level3NeighbourCalculation(this.maxX, this.maxY);
    }
}