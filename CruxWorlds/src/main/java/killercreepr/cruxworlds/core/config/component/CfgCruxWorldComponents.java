package killercreepr.cruxworlds.core.config.component;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.component.TypedDataComponent;
import killercreepr.crux.core.item.dynamic.component.attribute.DynamicAttributeModifier;
import killercreepr.cruxconfig.config.bukkit.handler.impl.component.FileDataComponentType;
import killercreepr.cruxconfig.config.bukkit.registry.FileDataComponentRegistry;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxworlds.api.world.entity.NaturalEntitySpawn;
import killercreepr.cruxworlds.core.component.CruxWorldsComponents;
import killercreepr.cruxworlds.core.component.EntitySpawnAttributes;
import killercreepr.cruxworlds.core.component.EntitySpawnPassengers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CfgCruxWorldComponents {
    public static void register(@NotNull FileDataComponentRegistry registry){
        registry.register("entity_spawn/passengers", new FileDataComponentType<EntitySpawnPassengers>() {
            @Override
            public @Nullable TypedDataComponent<EntitySpawnPassengers> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e) {
                return TypedDataComponent.create(
                    CruxWorldsComponents.ENTITY_SPAWN_PASSENGERS,
                    new EntitySpawnPassengers(ctx.getRegistry().deserializeFromFile(
                        new TypeToken<List<NaturalEntitySpawn>>(){}.getType(), e.get("passengers")
                    ))
                );
            }
        });

        registry.register("entity_spawn/attributes", new FileDataComponentType<EntitySpawnAttributes>() {
            @Override
            public @Nullable TypedDataComponent<EntitySpawnAttributes> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e) {
                if(!(e.get("attributes") instanceof FileArray o)) return null;
                FileRegistry registry = ctx.getRegistry();
                Map<Object, Collection<DynamicAttributeModifier>> attributes = new HashMap<>();
                o.forEach(element ->{
                    if(!(element instanceof FileObject oo)) return;
                    String key = oo.getObject(String.class, "attribute");
                    if(key == null) return;
                    Collection<DynamicAttributeModifier> level = registry.deserializeFromFile(
                        new TypeToken<Set<DynamicAttributeModifier>>(){}.getType(), oo.get("modifiers"));
                    if(level==null) return;
                    attributes.put(key, level);
                });
                if(attributes.isEmpty()) return null;

                return TypedDataComponent.create(
                    CruxWorldsComponents.ENTITY_SPAWN_ATTRIBUTES,
                    new EntitySpawnAttributes(attributes)
                );
            }
        });
    }
}
