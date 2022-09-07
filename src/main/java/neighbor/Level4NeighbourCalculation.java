package neighbor;

import point.Point;

import java.util.ArrayList;
import java.util.List;

public class Level4NeighbourCalculation extends NeighbourCalculation {

    protected Level4NeighbourCalculation(int maxX, int maxY) {
        super(maxX, maxY, 4);
    }

    @Override
    protected List<Point> computeNeighbours(int x, int y) {

        List<Point> levelFourCoordinates = new ArrayList<>();


        levelFourCoordinates.add(new Point(checkX(x-1), checkY(y+2)));
        levelFourCoordinates.add(new Point(checkX(x+1), checkY(y+2)));
        levelFourCoordinates.add(new Point(checkX(x-2), checkY(y+1)));
        levelFourCoordinates.add(new Point(checkX(x+2), checkY(y+1)));
        levelFourCoordinates.add(new Point(checkX(x-2), checkY(y-1)));
        levelFourCoordinates.add(new Point(checkX(x+2), checkY(y-1)));
        levelFourCoordinates.add(new Point(checkX(x-1), checkY(y-2)));
        levelFourCoordinates.add(new Point(checkX(x+1), checkY(y-2)));

        return levelFourCoordinates;
    }

    @Override
    protected NeighbourCalculation getNextHandler() {
        return new Level5NeighbourCalculation(this.maxX, this.maxY);
    }
}