package br.ufsc.ine5633;
public class Group {
    private Seed seed;
    private int countSeeds;
    private int countEmptys;
    private int groupScore;

    public Group(Seed seed, int countSeeds, int countEmptys) {
        this.seed = seed;
        this.countSeeds = countSeeds;
        this.countEmptys = countEmptys;
        this.groupScore = 0;
    }

    public Seed getSeed() {
        return seed;
    }

    public Group setSeed(Seed seed) {
        this.seed = seed;
        return this;
    }

    public int getCountSeeds() {
        return countSeeds;
    }

    public Group setCountSeeds(int countSeeds) {
        this.countSeeds = countSeeds;
        return this;
    }

    public int getCountEmptys() {
        return countEmptys;
    }

    public Group setCountEmptys(int countEmptys) {
        this.countEmptys = countEmptys;
        return this;
    }

    public Group setGroupScore(int groupScore) {
        this.groupScore = groupScore;
        return this;
    }

    public int getGroupScore() {
        return groupScore;
    }
}
