package killercreepr.cruxentities.modelengine.wrapper;

import com.ticxo.modelengine.api.model.ActiveModel;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class ModelEntity extends DesignEntity implements IModelEntity{
    protected @NotNull ActiveModel model;
    public ModelEntity(@NotNull Entity entity, @NotNull String modelID) {
        super(entity);
        model = attemptAddModel(modelID, true).orElseThrow(()->
            new RuntimeException("Model of " + modelID + " could not be added to " + entity.getName() + "!"));
    }
    public ModelEntity(@NotNull Entity entity, @NotNull ActiveModel model) {
        super(entity);
        this.model = model;
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
}
