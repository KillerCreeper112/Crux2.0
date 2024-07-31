package killercreepr.cruxadvancements.advancement.progression;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public class ListAdvancementProgress extends SimpleCriterionProgress implements CruxAdvancementProgress{
    protected final @NotNull Map<String, CruxCriterionProgress> progressMap = new HashMap<>();
    protected final @NotNull String[][] requirements;
    public ListAdvancementProgress(@NotNull String[][] requirements) {
        this.requirements = requirements;
        for(String[] list : requirements){
            for(String s : list){
                progressMap.put(s, new SimpleCriterionProgress());
            }
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
        return requirements.length;
    }

    @Override
    public int count(@NotNull Predicate<String> predicate) {
        int amount = 0;
        for(String[] list : requirements){
            for(String s : list){
                if(predicate.test(s)) amount++;
            }
        }
        return amount;
    }

    @Override
    public @NotNull Collection<String> assemble(@NotNull Predicate<String> predicate) {
        Collection<String> set = new HashSet<>();
        for(String[] list : requirements){
            for(String s : list){
                if(predicate.test(s)) set.add(s);
            }
        }
        return set;
    }

    protected boolean checkAllGranted(){
        for(CruxCriterionProgress progress : progressMap.values()){
            if(!progress.isDone()) return false;
        }
        return true;
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

    @Override
    public @NotNull GrantCriteriaResult grantCriteria(@NotNull String... criteria) {
        if(isDone()) return GrantCriteriaResult.UNCHANGED;
        GrantCriteriaResult result = GrantCriteriaResult.UNCHANGED;
        for (String criterion : criteria) {
            CruxCriterionProgress criterionProgress = this.getCriterionProgress(criterion);
            if(criterionProgress == null || criterionProgress.isDone()) continue;
            criterionProgress.grant();
            result = GrantCriteriaResult.CHANGED;
        }
        update();
        return isDone() ? GrantCriteriaResult.COMPLETED : result;
    }

    @Override
    public @NotNull GenericResult revokeCriteria(@NotNull String... criteria) {
        if(!isDone()) return GenericResult.UNCHANGED;
        GenericResult result = GenericResult.UNCHANGED;
        for (String criterion : criteria) {
            CruxCriterionProgress criterionProgress = this.getCriterionProgress(criterion);
            if(criterionProgress == null || !criterionProgress.isDone()) continue;
            criterionProgress.revoke();
            result = GenericResult.CHANGED;
        }
        update();
        return result;
    }

    @Override
    public @NotNull GrantCriteriaResult setCriteriaProgress(int amount) {
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
