package br.ufsc.ine5633;

public class Cell {
    private Seed content;
    private Coordinate position;

    public Cell(int x, int y) {
        this.content = Seed.EMPTY;
        this.position = new Coordinate(x, y);
    }

    public Seed getContent() {
        return content;
    }

    public Cell setContent(Seed content) {
        this.content = content;
        return this;
    }

    public Coordinate getPosition() {
        return this.position;
    }

}
