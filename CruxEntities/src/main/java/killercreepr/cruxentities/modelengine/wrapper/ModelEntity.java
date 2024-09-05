package killercreepr.cruxentities.modelengine.wrapper;

import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import killercreepr.cruxentities.wrapper.IEntityWrapper;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ModelEntity extends DesignEntity implements IModelEntity{
    protected @NotNull ActiveModel model;
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

    @Override
    public ModelEntity setLockPitch(boolean value) {
        getModel().setLockPitch(value);
        return this;
    }

    @Override
    public ModelEntity setLockYaw(boolean value) {
        getModel().setLockYaw(value);
        return this;
    }

    @Override
    public boolean isLockPitch() {
        return getModel().isLockPitch();
    }

    @Override
    public boolean isLockYaw() {
        return getModel().isLockYaw();
    }
}
