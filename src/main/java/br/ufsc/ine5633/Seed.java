package br.ufsc.ine5633;

public enum Seed {
    EMPTY(" "),
    PLAYER_1("O"),
    PLAYER_2("X");

    private String value;

    Seed(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
