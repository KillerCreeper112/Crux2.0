package killercreepr.cruxconfig.config.bukkit.handler.impl.entity;

import killercreepr.crux.api.entity.dynamic.DynamicEntityApplier;
import killercreepr.crux.api.entity.dynamic.DynamicEntityApplierComponent;
import killercreepr.crux.api.entity.dynamic.DynamicEntityApplierSingleComponent;
import killercreepr.crux.api.registry.MappedRegistry;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.entity.dynamic.component.DynamicEntityApplierGlowing;
import killercreepr.crux.core.entity.dynamic.component.DynamicEntityApplierInvisible;
import killercreepr.crux.core.entity.dynamic.component.DynamicEntityApplierPersistent;
import killercreepr.crux.core.entity.dynamic.component.DynamicEntityApplierRemoveWhenFarAway;
import killercreepr.crux.core.registry.SimpleMappedRegistry;
import killercreepr.cruxconfig.config.bukkit.handler.impl.entity.component.FileDynamicEntityApplierComponent;
import killercreepr.cruxconfig.config.bukkit.handler.impl.entity.component.FileSingleDynamicEntityApplierComponent;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileGeneric;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.logging.Level;

@JsonSerializer(id = "dynamic_entity_applier")
public class FileDynamicEntityApplier extends SimpleFileHandler<DynamicEntityApplier> {
    protected final MappedRegistry<String, FileDynamicEntityApplierComponent<?>> COMPONENT_REGISTRY = new SimpleMappedRegistry<>();

    public MappedRegistry<String, FileDynamicEntityApplierComponent<?>> getComponentRegistry() {
        return COMPONENT_REGISTRY;
    }

    public FileDynamicEntityApplier() {
        COMPONENT_REGISTRY.register("glowing", new FileSingleDynamicEntityApplierComponent<>(DynamicEntityApplierSingleComponent.class) {

            @Override
            public DynamicEntityApplierSingleComponent deserialize(Object value) {
                return new DynamicEntityApplierGlowing(value);
            }

            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_entity_applier_glowing";
            }
        });
        COMPONENT_REGISTRY.register("invisible", new FileSingleDynamicEntityApplierComponent<>(DynamicEntityApplierSingleComponent.class) {

            @Override
            public DynamicEntityApplierSingleComponent deserialize(Object value) {
                return new DynamicEntityApplierInvisible(value);
            }

            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_entity_applier_invisible";
            }
        });
        COMPONENT_REGISTRY.register("persistent", new FileSingleDynamicEntityApplierComponent<>(DynamicEntityApplierSingleComponent.class) {

            @Override
            public DynamicEntityApplierSingleComponent deserialize(Object value) {
                return new DynamicEntityApplierPersistent(value);
            }

            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_entity_applier_persistent";
            }
        });
        COMPONENT_REGISTRY.register("remove_when_far_away", new FileSingleDynamicEntityApplierComponent<>(DynamicEntityApplierSingleComponent.class) {

            @Override
            public DynamicEntityApplierSingleComponent deserialize(Object value) {
                return new DynamicEntityApplierRemoveWhenFarAway(value);
            }

            @Override
            public @NotNull String jsonSerializerID() {
                return "dynamic_entity_applier_remove_when_far_away";
            }
        });
    }

    public FileDynamicEntityApplier registerComponents(@NotNull FileRegistry registry){
        for(FileDynamicEntityApplierComponent<?> c : COMPONENT_REGISTRY){
            registry.registerFileHandler(c.getType(), c);
        }
        return this;
    }

    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull DynamicEntityApplier item) {
        FileRegistry registry = context.getRegistry();
        FileObject o = new FileObject();
        Map<String, DynamicEntityApplierComponent> components = item.components();
        if(components != null){
            components.forEach((key, value) ->{
                o.add(key, registry.serializeToFile(value));
            });
        }
        return o;
    }

    @Override
    public @Nullable DynamicEntityApplier deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        return deserializeFromYaml(context, e, null);
    }

    public @Nullable DynamicEntityApplier buildItem(@NotNull FileContext<?> context, @NotNull FileElement e){
        if(e instanceof FileGeneric s){
            return DynamicEntityApplier.builder().build();
        }
        if(!(e instanceof FileObject o)) return null;
        if(o.get("material") instanceof FileGeneric s){
            return DynamicEntityApplier.builder().build();
        }
        return o.isEmpty() ? null : DynamicEntityApplier.builder().build();
    }

    public @Nullable DynamicEntityApplier deserializeFromYaml(@NotNull FileContext<?> context, @NotNull FileElement e, @Nullable DynamicEntityApplier stack) {
        if(stack == null) stack = buildItem(context, e);
        if(stack == null) return null;

        if(e instanceof FileGeneric s){
            return stack;//.withType(s.getAsString());
        }
        if(!(e instanceof FileObject o)) return stack;

        for(Map.Entry<String, FileElement> entry : o){
            FileDynamicEntityApplierComponent<?> handler = COMPONENT_REGISTRY.get(entry.getKey());
            if(handler == null){
                switch (entry.getKey().toLowerCase()){
                    case "material", "amount" -> { continue; }
                }
                Crux.log(Level.WARNING, "Component " + entry.getKey() + " does not exist for DynamicEntityApplier! " + o.asMap());
                continue;
            }
            DynamicEntityApplierComponent component = handler.deserializeFromFile(context, entry.getValue());
            if(component == null){
                Crux.log(Level.WARNING, "No object set for DynamicEntityApplierComponent " + entry.getKey() + "! " + o.asMap());
                continue;
            }
            stack = stack.withComponent(component);
        }

        /*if(o.get("merge") instanceof FileObject oo){
            for(Map.Entry<String, FileElement> entry : oo){
                FileDynamicEntityApplierComponent<?> handler = COMPONENT_REGISTRY.get(entry.getKey());
                if(handler == null) continue;
                DynamicItemComponent component = handler.deserializeFromFile(context, entry.getValue());
                if(component == null) continue;
                stack = stack.mergeComponent(component);
            }
        }*/

        return stack;
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "dynamic_entity_applier";
    }
}
