package killercreepr.cruxentities.modelengine.wrapper;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import killercreepr.cruxentities.modelengine.ModelEngineHook;
import killercreepr.cruxentities.wrapper.IEntityWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public interface IDesignEntity extends IEntityWrapper {
    default IDesignEntity addModel(@NotNull String id, boolean overrideHitbox){
        attemptAddModel(id, overrideHitbox);
        return this;
    }

    default @NotNull Optional<ActiveModel> attemptAddModel(@NotNull String id, boolean overrideHitbox){
        ModeledEntity modeled = getOrCreateModeledEntity();
        Optional<ActiveModel> gotted = getModel(id);
        if(gotted.isPresent()) return gotted;
        ActiveModel activeModel = createModel(id);
        modeled.addModel(activeModel, overrideHitbox);
        return Optional.of(activeModel);
    }

    /**
     * If ModelEngine is still importing the models, this will wait until
     * ModelEngine has completed its process.
     */
    default CompletableFuture<ActiveModel> getOrAddModelAsync(@NotNull String id){
        if(ModelEngineHook.phase().hasFinishedImport()){
            return CompletableFuture.completedFuture(getOrAddModel(id));
        }
        return CompletableFuture.supplyAsync(() ->{
            while (!ModelEngineHook.phase().hasFinishedImport()) {
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Thread interrupted while waiting for model import", e);
                }
            }

            return getModel(id).orElseGet(() -> {
                addModel(id);
                return getModelOrThrow(id);
            });
        });
    }

    default ActiveModel getOrAddModel(@NotNull String id){
        ActiveModel model = getModel(id).orElse(null);
        if(model == null){
            addModel(id);
            return getModelOrThrow(id);
        }
        return model;
    }

    default IDesignEntity addModel(@NotNull String id){
        return addModel(id, true);
    }

    default IDesignEntity removeModel(@NotNull String id){
        attemptRemoveModel(id);
        return this;
    }

    default @NotNull Optional<ActiveModel> attemptRemoveModel(@NotNull String id){
        ModeledEntity modeled = ModelEngineAPI.getModeledEntity(entity());
        if(modeled==null) return Optional.empty();
        return modeled.removeModel(id);
    }

    default @NotNull ActiveModel createModel(@NotNull String id){
        ActiveModel model = ModelEngineAPI.createActiveModel(id);
        Objects.requireNonNull(model);
        return model;
    }

    default boolean hasModel(@NotNull String id){
        return getOrCreateModeledEntity().getModels().containsKey(id);
    }

    default @NotNull Optional<ActiveModel> getModel(@NotNull String id){
        return getOrCreateModeledEntity().getModel(id);
    }

    default @NotNull ActiveModel getModelOrThrow(@NotNull String id){
        return getOrCreateModeledEntity().getModel(id).orElseThrow(() ->
            new RuntimeException(entity().getName() + " does not have an active model of '" + id + "'"));
    }

    default @NotNull ModeledEntity getOrCreateModeledEntity(){
        return ModelEngineAPI.getOrCreateModeledEntity(entity());
    }
}
