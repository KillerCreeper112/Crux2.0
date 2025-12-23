package killercreepr.crux.api.entity.dynamic;

import killercreepr.crux.api.entity.CruxEntity;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.core.entity.dynamic.SimpleDynamicEntityApplier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

public interface DynamicEntityApplier {
    static Builder builder(){
        return new SimpleDynamicEntityApplier.Builder();
    }

    @Nullable Map<String, DynamicEntityApplierComponent> components();
    void apply(@NotNull CruxEntity entity, @NotNull TextParserContext context);

    @Contract(pure = true)
    DynamicEntityApplier withComponent(DynamicEntityApplierComponent component);

    interface Builder{
        DynamicEntityApplier build();
        Builder components(Map<String, DynamicEntityApplierComponent> components);
        Builder addComponent(DynamicEntityApplierComponent component);
        Builder components(@Nullable Collection<DynamicEntityApplierComponent> components);
        Builder components(@Nullable DynamicEntityApplierComponent... components);

        Builder addComponents(@Nullable Map<String, DynamicEntityApplierComponent> components);
        Builder addComponents(@Nullable Collection<DynamicEntityApplierComponent> components);
        Builder addComponents(@Nullable DynamicEntityApplierComponent... components);
    }
}
