package DAY_16;

import java.util.Objects;

class Light {

    Point currentPoint;
    Direction direction;

    public Light(Point currentPoint, Direction direction) {
        this.currentPoint = currentPoint;
        this.direction = direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    int getX() {
        return currentPoint.getX();
    }

    int getY() {
        return currentPoint.getY();
    }

    public void changePoint(Point point) {
        currentPoint = point;
    }

    public Point getCurrentPoint() {
        return currentPoint;
    }

    public char getCurrentSign() {
        return currentPoint.getSign();
    }


    public Direction getDirection() {
        return direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Light light = (Light) o;
        return Objects.equals(currentPoint, ((Light) o).getCurrentPoint()) && direction == light.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY(), direction);
    }

    @Override
    public String toString() {
        return "Light{" +
                "currentPoint=" + currentPoint +
                ", direction=" + direction +
                '}';
    }
}
