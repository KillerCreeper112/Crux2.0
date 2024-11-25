package killercreepr.cruxentities.modelengine.wrapper;

import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import killercreepr.crux.core.Crux;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.logging.Level;

public class ModelEntity extends DesignEntity implements IModelEntity{
    protected ActiveModel model;
    protected CompletableFuture<ActiveModel> cache;

    public ModelEntity(@NotNull Entity entity) {
        super(entity);
    }

    public ModelEntity(@NotNull Entity entity, @NotNull String modelID) {
        super(entity);
        model = attemptAddModel(modelID, true).orElseThrow(()->
            new RuntimeException("Model of " + modelID + " could not be added to " + entity.getName() + "!"));
        setBaseEntityVisible(false);
    }
    public ModelEntity(@NotNull Entity entity, @NotNull ActiveModel model) {
        super(entity);
        this.model = model;
        setBaseEntityVisible(false);
    }

    public ModelEntity applyToModel(@NotNull Consumer<ActiveModel> consumer){
        consumer.accept(getModel());
        return this;
    }

    public ModelEntity applyToModeledEntity(@NotNull Consumer<ModeledEntity> consumer){
        consumer.accept(getModeledEntity());
        return this;
    }

    @Override
    public IModelEntity model(CompletableFuture<ActiveModel> cache) {
        this.cache = cache;
        cache.whenComplete((model, throwable) ->{
            if(throwable != null) Crux.log(Level.WARNING, throwable.getMessage());
            setModel(model);
        });
        return this;
    }

    @Override
    public CompletableFuture<ActiveModel> model() {
        return cache;
    }

    @Override
    public ModeledEntity getModeledEntity() {
        return model.getModeledEntity();
    }

    @Override
    public @NotNull ActiveModel getModel() {
        return model;
    }

    @Override
    public ModelEntity setModel(@NotNull ActiveModel model) {
        this.model = model;
        return this;
    }

    @Override
    public ModelEntity setBaseEntityVisible(boolean value) {
        getModeledEntity().setBaseEntityVisible(value);
        return this;
    }

    @Override
    public ModelEntity setModelRotationLocked(boolean value) {
        getModeledEntity().setModelRotationLocked(value);
        return this;
    }

    @Override
    public boolean isModelRotationLocked() {
        return getModeledEntity().isModelRotationLocked();
    }

    @Override
    public boolean isBaseEntityVisible() {
        return getModeledEntity().isBaseEntityVisible();
    }
}
