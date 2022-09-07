package neighbor;

import point.Point;

import java.util.ArrayList;
import java.util.List;

public class Level5NeighbourCalculation extends NeighbourCalculation {

    protected Level5NeighbourCalculation(int maxX, int maxY) {
        super(maxX, maxY, 5);
    }

    @Override
    protected List<Point> computeNeighbours(int x, int y) {

        List<Point> levelFiveCoordinates = new ArrayList<>();

        levelFiveCoordinates.add(new Point(checkX(x-2), checkY(y+2)));
        levelFiveCoordinates.add(new Point(checkX(x+2), checkY(y+2)));
        levelFiveCoordinates.add(new Point(checkX(x-2), checkY(y-2)));
        levelFiveCoordinates.add(new Point(checkX(x+2), checkY(y-2)));

        return levelFiveCoordinates;
    }
}