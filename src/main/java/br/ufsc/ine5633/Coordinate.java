package br.ufsc.ine5633;

public class Coordinate {
    private Integer coordX;
    private Integer coordY;

    public Coordinate(Integer coordX, Integer coordY) {
        this.coordX = coordX;
        this.coordY = coordY;
    }

    public Integer getCoordX() {
        return coordX;
    }

    public Coordinate setCoordX(Integer coordX) {
        this.coordX = coordX;
        return this;
    }

    public Integer getCoordY() {
        return coordY;
    }

    public Coordinate setCoordY(Integer coordY) {
        this.coordY = coordY;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinate that = (Coordinate) o;
        return (coordX != null ? coordX.equals(that.coordX) : that.coordX == null) &&
                (coordY != null ? coordY.equals(that.coordY) : that.coordY == null);
    }
}
