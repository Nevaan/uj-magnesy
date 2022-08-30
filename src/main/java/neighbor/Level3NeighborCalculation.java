package neighbor;

import point.Point;

import java.util.ArrayList;
import java.util.List;

public class Level3NeighborCalculation   extends NeighborCalculation {

    public Level3NeighborCalculation(int maxX, int maxY) {
        super(maxX, maxY, 3);
    }

    @Override
    List<Point> computeNeighbors(int x, int y) {

        List<Point> levelThreeCoordinates = new ArrayList<>();


        levelThreeCoordinates.add(new Point(checkX(x), checkY(y+2)));
        levelThreeCoordinates.add(new Point(checkX(x), checkY(y-2)));
        levelThreeCoordinates.add(new Point(checkX(x-2), checkY(y)));
        levelThreeCoordinates.add(new Point(checkX(x+2), checkY(y)));

        return levelThreeCoordinates;
    }

    @Override
    protected NeighborCalculation getNextHandler() {
        return new Level4NeighborCalculation(this.maxX, this.maxY);
    }
}