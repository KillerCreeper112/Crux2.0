package killercreepr.crux.valueproviders.random;

import java.util.Random;

public class SeededRandom extends Random{
    protected long seed;
    public SeededRandom(long seed) {
        super(seed);
        this.seed = seed;
    }

    public SeededRandom(){
    }

    public long getSeed() {
        return seed;
    }
}
