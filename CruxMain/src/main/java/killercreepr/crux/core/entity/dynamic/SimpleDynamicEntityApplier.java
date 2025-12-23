package killercreepr.crux.core.entity.dynamic;

import killercreepr.crux.api.entity.CruxEntity;
import killercreepr.crux.api.entity.dynamic.DynamicEntityApplier;
import killercreepr.crux.api.entity.dynamic.DynamicEntityApplierComponent;
import killercreepr.crux.api.text.context.TextParserContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SimpleDynamicEntityApplier implements DynamicEntityApplier {
    protected final Map<String, DynamicEntityApplierComponent> components;

    public SimpleDynamicEntityApplier(Map<String, DynamicEntityApplierComponent> components) {
        this.components = components;
    }

    @Override
    public @Nullable Map<String, DynamicEntityApplierComponent> components() {
        return components;
    }

    @Override
    public void apply(@NotNull CruxEntity entity, @NotNull TextParserContext context) {
        if(components != null){
            for (DynamicEntityApplierComponent value : components.values()) {
                value.apply(entity, context);
            }
        }
    }

    @Override
    public DynamicEntityApplier withComponent(DynamicEntityApplierComponent component) {
        Map<String, DynamicEntityApplierComponent> components = new HashMap<>();
        if(this.components != null) components.putAll(this.components);
        components.put(component.name(), component);
        return new SimpleDynamicEntityApplier(components);
    }

    public static class Builder implements DynamicEntityApplier.Builder{
        protected final Map<String, DynamicEntityApplierComponent> components = new HashMap<>();

        public Builder addComponent(@NotNull DynamicEntityApplierComponent c){
            components.put(c.name(), c);
            return this;
        }

        public Builder addComponents(@Nullable Map<String, DynamicEntityApplierComponent> c){
            if(c==null) return this;
            components.putAll(c);
            return this;
        }

        public Builder addComponents(@Nullable Collection<DynamicEntityApplierComponent> components){
            if(components==null) return this;
            components.forEach(this::addComponent);
            return this;
        }

        @Override
        public Builder addComponents(@Nullable DynamicEntityApplierComponent... components) {
            if(components==null) return this;
            return addComponents(Arrays.asList(components));
        }

        public Builder components(@Nullable Map<String, DynamicEntityApplierComponent> components){
            this.components.clear();
            return addComponents(components);
        }

        public Builder components(@Nullable Collection<DynamicEntityApplierComponent> components){
            this.components.clear();
            return addComponents(components);
        }

        @Override
        public Builder components(@Nullable DynamicEntityApplierComponent... components) {
            return components(components == null ? null : Arrays.asList(components));
        }

        public @NotNull DynamicEntityApplier build(){
            return new SimpleDynamicEntityApplier(components.isEmpty() ? null : components);
        }
    }
}
