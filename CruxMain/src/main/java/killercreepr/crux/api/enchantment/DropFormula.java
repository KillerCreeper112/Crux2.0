package killercreepr.crux.api.enchantment;

import java.util.Random;

public interface DropFormula {
    int calculateNewCount(Random random, int originalCount, int enchantLevel);

    class OreDrops implements DropFormula{
        public int calculateNewCount(Random random, int originalCount, int enchantmentLevel) {
            if (enchantmentLevel > 0) {
                int i = random.nextInt(enchantmentLevel + 2) - 1;
                if (i < 0) {
                    i = 0;
                }

                return originalCount * (i + 1);
            } else {
                return originalCount;
            }
        }
    }

    class BinomialWithBonusCount implements DropFormula {
        protected final int extraRounds;
        protected final float probability;

        public BinomialWithBonusCount(int extraRounds, float probability) {
            this.extraRounds = extraRounds;
            this.probability = probability;
        }

        public int calculateNewCount(Random random, int originalCount, int enchantmentLevel) {
            for(int i = 0; i < enchantmentLevel + this.extraRounds; ++i) {
                if (random.nextFloat() < this.probability) {
                    ++originalCount;
                }
            }

            return originalCount;
        }
    }

    class Uniform implements DropFormula{
        protected final int bonusMultiplier;

        public Uniform(int bonusMultiplier) {
            this.bonusMultiplier = bonusMultiplier;
        }

        public int calculateNewCount(Random random, int originalCount, int enchantmentLevel) {
            return originalCount + random.nextInt(this.bonusMultiplier * enchantmentLevel + 1);
        }
    }
}
