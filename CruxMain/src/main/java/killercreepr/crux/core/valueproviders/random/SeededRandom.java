package killercreepr.crux.core.valueproviders.random;

import java.util.Random;

public class SeededRandom extends Random{
    protected long seed;
    public SeededRandom(long seed) {
        super(seed);
        this.seed = seed;
    }

    public SeededRandom(){
    }

    @Override
    public synchronized void setSeed(long seed) {
        super.setSeed(seed);
        this.seed = seed;
    }

    public long getSeed() {
        return seed;
    }
}
