package uj.wmii.pwj.collections;

public interface BattleshipGenerator {

    String generateMap();

    static BattleshipGenerator defaultInstance() {
        return new BattleshipGeneratorImpl();
    }
}
