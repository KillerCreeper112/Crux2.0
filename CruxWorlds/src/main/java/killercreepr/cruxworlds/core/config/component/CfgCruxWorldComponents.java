package killercreepr.cruxworlds.core.config.component;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.component.TypedDataComponent;
import killercreepr.crux.api.entity.dynamic.DynamicEntityApplier;
import killercreepr.crux.api.loot.LootTable;
import killercreepr.crux.api.loot.item.ItemLootTable;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.item.dynamic.component.attribute.DynamicAttributeModifier;
import killercreepr.cruxconfig.config.bukkit.handler.impl.component.FileDataComponentType;
import killercreepr.cruxconfig.config.bukkit.registry.FileDataComponentRegistry;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxworlds.api.component.EntitySpawnComponent;
import killercreepr.cruxworlds.api.world.entity.NaturalEntitySpawn;
import killercreepr.cruxworlds.core.component.*;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.C;
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
                    ), ctx.getRegistry().deserializeFromFileOrDefault(Boolean.class, e.get("append"), false))
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

        registry.register("entity_spawn/base_attributes", new FileDataComponentType<EntitySpawnBaseAttributes>() {
            @Override
            public @Nullable TypedDataComponent<EntitySpawnBaseAttributes> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e) {
                if(!(e.get("attributes") instanceof FileObject o)) return null;
                FileRegistry registry = ctx.getRegistry();
                Map<Object, NumberProvider> attributes = new HashMap<>();
                o.forEach((id, obj) ->{
                    var got = registry.deserializeFromFile(NumberProvider.class, obj);
                    if(got == null) return;
                    attributes.put(id, got);
                });
                if(attributes.isEmpty()) return null;

                return TypedDataComponent.create(
                    CruxWorldsComponents.ENTITY_SPAWN_BASE_ATTRIBUTES,
                    new EntitySpawnBaseAttributes(attributes)
                );
            }
        });

        registry.register("entity_spawn/equipment", new FileDataComponentType<EntitySpawnEquipment>() {
            @Override
            public @Nullable TypedDataComponent<EntitySpawnEquipment> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e) {
                if(!(e.get("equipment") instanceof FileObject o)) return null;
                FileRegistry registry = ctx.getRegistry();
                Map<Object, LootTable<ItemStack>> map = registry.deserializeFromFile(
                    new TypeToken<Map<String, ItemLootTable>>(){}.getType(), o
                );
                if(map == null || map.isEmpty()) return null;

                return TypedDataComponent.create(
                    CruxWorldsComponents.ENTITY_SPAWN_EQUIPMENT,
                    new EntitySpawnEquipment(map)
                );
            }
        });

        registry.register("entity_spawn/applier", new FileDataComponentType<EntitySpawnApplier>() {
            @Override
            public @Nullable TypedDataComponent<EntitySpawnApplier> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e) {
                FileRegistry registry = ctx.getRegistry();
                DynamicEntityApplier map = registry.deserializeFromFile(DynamicEntityApplier.class, e.get("value"));
                if(map == null) return null;

                return TypedDataComponent.create(
                    CruxWorldsComponents.ENTITY_SPAWN_APPLIER,
                    new EntitySpawnApplier(map)
                );
            }
        });
    }
}
