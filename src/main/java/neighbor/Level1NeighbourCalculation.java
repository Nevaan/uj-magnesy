package neighbor;

import point.Point;

import java.util.ArrayList;
import java.util.List;

public class Level1NeighbourCalculation extends NeighbourCalculation {

    public Level1NeighbourCalculation(int maxX, int maxY) {
        super(maxX, maxY, 1);
    }

    @Override
    protected List<Point> computeNeighbours(int x, int y) {

        List<Point> levelOneCoordinates = new ArrayList<>();
        levelOneCoordinates.add(new Point(checkX(x), checkY(y-1)));
        levelOneCoordinates.add(new Point(checkX(x), checkY(y+1)));
        levelOneCoordinates.add(new Point(checkX(x+1), checkY(y)));
        levelOneCoordinates.add(new Point(checkX(x-1), checkY(y)));

        return levelOneCoordinates;
    }

    @Override
    protected NeighbourCalculation getNextHandler() {
        return new Level2NeighbourCalculation(this.maxX, this.maxY);
    }
}
