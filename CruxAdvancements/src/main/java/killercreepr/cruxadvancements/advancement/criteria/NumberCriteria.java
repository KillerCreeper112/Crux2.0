package killercreepr.cruxadvancements.advancement.criteria;

public class NumberCriteria implements CruxCriteria {
    protected final int maxProgress;
    public NumberCriteria(int maxProgress) {
        this.maxProgress = maxProgress;
    }
    public int getMaxProgress() {
        return maxProgress;
    }
}
