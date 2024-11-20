package killercreepr.cruxadvancements.core.advancement.progress;

import killercreepr.cruxadvancements.core.advancement.criteria.ListCriteria;
import killercreepr.cruxadvancements.api.advancement.progress.CriteriaResult;
import killercreepr.cruxadvancements.api.advancement.progress.CruxAdvancementProgress;
import killercreepr.cruxadvancements.api.advancement.progress.CruxCriterionProgress;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
//todo fix
public class ListAdvancementProgress extends SimpleCriterionProgress implements CruxAdvancementProgress {
    protected final @NotNull Map<String, CruxCriterionProgress> progressMap = new HashMap<>();
    protected final @NotNull ListCriteria criteria;
    public ListAdvancementProgress(@NotNull ListCriteria criteria) {
        this.criteria = criteria;
        for(String name : criteria.getActionNames()){
            progressMap.put(name, new SimpleCriterionProgress());
        }
    }

    @Override
    public @Nullable CruxCriterionProgress getCriterionProgress(@NotNull String name) {
        return progressMap.get(name);
    }

    @Override
    public @NotNull Collection<String> getAwardedCriteria() {
        return assemble(s -> {
            CruxCriterionProgress progress = getCriterionProgress(s);
            return progress != null && progress.isDone();
        });
    }

    @Override
    public @NotNull Collection<String> getRemainingCriteria() {
        return assemble(s -> {
            CruxCriterionProgress progress = getCriterionProgress(s);
            return progress != null && !progress.isDone();
        });
    }

    @Override
    public int getCriteriaMaxProgress() {
        return criteria.getActionNames().length;
    }

    @Override
    public int count(@NotNull Predicate<String> predicate) {
        int amount = 0;
        for(String s : criteria.getActionNames()){
            if(predicate.test(s)) amount++;
        }
        return amount;
    }

    @Override
    public @NotNull Collection<String> assemble(@NotNull Predicate<String> predicate) {
        Collection<String> set = new HashSet<>();
        for(String s : criteria.getActionNames()){
            if(predicate.test(s)) set.add(s);
        }
        return set;
    }

    @Override
    public boolean isEmpty() {
        return progressMap.isEmpty();
    }

    @Override
    public boolean isCriterionDone(@NotNull String name) {
        CruxCriterionProgress progress = getCriterionProgress(name);
        return progress != null && progress.isDone();
    }

    protected boolean checkAllGranted(){
        return criteria.test(this::isCriterionDone);
        /*for(CruxCriterionProgress progress : progressMap.values()){
            if(!progress.isDone()) return false;
        }
        return true;*/
    }
    protected boolean update(){
        if(checkAllGranted()){
            if(isDone()) return false;
            grant();
            return true;
        }
        if(!isDone()) return false;
        revoke();
        return true;
    }

    public @NotNull Map<String, CruxCriterionProgress> getProgressMap() {
        return progressMap;
    }

    public @NotNull ListCriteria getCriteria() {
        return criteria;
    }

    @Override
    public @NotNull CriteriaResult grantCriteria(@NotNull String... criteria) {
        if(isDone()) return CriteriaResult.UNCHANGED;
        Collection<String> changed = new HashSet<>();
        for (String criterion : criteria) {
            CruxCriterionProgress criterionProgress = this.getCriterionProgress(criterion);
            if(criterionProgress == null || criterionProgress.isDone()) continue;
            criterionProgress.grant();

            changed.add(criterion);
        }
        update();
        return new CriteriaResult(changed, isDone());
    }

    @Override
    public @NotNull CriteriaResult revokeCriteria(@NotNull String... criteria) {
        if(!isDone()) return CriteriaResult.UNCHANGED;
        Collection<String> changed = new HashSet<>();
        for (String criterion : criteria) {
            CruxCriterionProgress criterionProgress = this.getCriterionProgress(criterion);
            if(criterionProgress == null || !criterionProgress.isDone()) continue;
            criterionProgress.revoke();

            changed.add(criterion);
        }
        update();
        return new CriteriaResult(changed, false);
    }

    @Override
    public @NotNull CriteriaResult setCriteriaProgress(int amount) {
        List<String> criteria = new ArrayList<>();
        for(int i = 0; i <= amount; i++){
            criteria.add(i+"");
        }
        return grantCriteria(criteria.toArray(new String[0]));
    }

    @Override
    public int getCriteriaProgress() {
        return getAwardedCriteria().size();
    }
}
