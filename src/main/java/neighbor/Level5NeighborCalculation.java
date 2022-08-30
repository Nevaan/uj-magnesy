package neighbor;

import point.Point;

import java.util.ArrayList;
import java.util.List;

public class Level5NeighborCalculation extends NeighborCalculation {

    public Level5NeighborCalculation(int maxX, int maxY) {
        super(maxX, maxY, 5);
    }

    @Override
    List<Point> computeNeighbors(int x, int y) {

        List<Point> levelFiveCoordinates = new ArrayList<>();

        levelFiveCoordinates.add(new Point(checkX(x-2), checkY(y+2)));
        levelFiveCoordinates.add(new Point(checkX(x+2), checkY(y+2)));
        levelFiveCoordinates.add(new Point(checkX(x-2), checkY(y-2)));
        levelFiveCoordinates.add(new Point(checkX(x+2), checkY(y-2)));

        return levelFiveCoordinates;
    }
}