package neighbor;

import point.Point;

import java.util.ArrayList;
import java.util.List;

public class Level4NeighborCalculation extends NeighborCalculation {

    public Level4NeighborCalculation(int maxX, int maxY) {
        super(maxX, maxY, 4);
    }

    @Override
    List<Point> computeNeighbors(int x, int y, int level) {

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
    protected NeighborCalculation getNextHandler() {
        return new Level5NeighborCalculation(this.maxX, this.maxY);
    }
}
