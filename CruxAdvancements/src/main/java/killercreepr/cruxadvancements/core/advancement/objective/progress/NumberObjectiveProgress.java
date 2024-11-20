package killercreepr.cruxadvancements.core.advancement.objective.progress;

import killercreepr.cruxadvancements.api.advancement.objective.progress.ObjectiveProgress;

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

    @Override
    public String toString() {
        return "NumberObjectiveProgress{progress=" + progress + "}";
    }
}
