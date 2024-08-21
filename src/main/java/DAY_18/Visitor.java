package DAY_18;

public class Visitor {
    Point currentPoint;

    public Visitor(Point currentPoint) {
        this.currentPoint = currentPoint;
    }

    public void changeCurrentPoint(Point newPoint) {
        this.currentPoint = newPoint;
    }

    public void setCurrentPointAsVisited() {
        currentPoint.setIsEdge();
    }

    public int getX() {
        return currentPoint.getX();
    }

    public int getY() {
        return currentPoint.getY();
    }
}
