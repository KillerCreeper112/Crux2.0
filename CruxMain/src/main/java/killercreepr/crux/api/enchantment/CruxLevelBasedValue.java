package killercreepr.crux.api.enchantment;

public interface CruxLevelBasedValue {
    float calculate(int level);

    record Linear(float base, float perLevelAboveFirst) implements CruxLevelBasedValue {
        public Linear(float base, float perLevelAboveFirst) {
            this.base = base;
            this.perLevelAboveFirst = perLevelAboveFirst;
        }

        public float calculate(int level) {
            return this.base + (this.perLevelAboveFirst * (float)(level - 1));
        }

        public float base() {
            return this.base;
        }

        public float perLevelAboveFirst() {
            return this.perLevelAboveFirst;
        }
    }
}
