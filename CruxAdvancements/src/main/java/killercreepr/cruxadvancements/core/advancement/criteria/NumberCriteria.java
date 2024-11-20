package killercreepr.cruxadvancements.core.advancement.criteria;

import killercreepr.cruxadvancements.api.advancement.criteria.CruxCriteria;

public class NumberCriteria implements CruxCriteria {
    protected final int maxProgress;
    public NumberCriteria(int maxProgress) {
        this.maxProgress = maxProgress;
    }
    public int getMaxProgress() {
        return maxProgress;
    }
}
