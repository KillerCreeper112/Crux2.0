package killercreepr.cruxadvancements.advancement.objective.progress;

public class NumberObjectiveProgress implements ObjectiveProgress {
    protected int progress;
    public NumberObjectiveProgress(int progress) {
        this.progress = progress;
    }

    public NumberObjectiveProgress() {
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
