package neighbor;

import point.Point;

import java.util.ArrayList;
import java.util.List;

public class Level3NeighbourCalculation extends NeighbourCalculation {

    protected Level3NeighbourCalculation(int maxX, int maxY) {
        super(maxX, maxY, 3);
    }

    @Override
    protected List<Point> computeNeighbours(int x, int y) {

        List<Point> levelThreeCoordinates = new ArrayList<>();


        levelThreeCoordinates.add(new Point(checkX(x), checkY(y+2)));
        levelThreeCoordinates.add(new Point(checkX(x), checkY(y-2)));
        levelThreeCoordinates.add(new Point(checkX(x-2), checkY(y)));
        levelThreeCoordinates.add(new Point(checkX(x+2), checkY(y)));

        return levelThreeCoordinates;
    }

    @Override
    protected NeighbourCalculation getNextHandler() {
        return new Level4NeighbourCalculation(this.maxX, this.maxY);
    }
}