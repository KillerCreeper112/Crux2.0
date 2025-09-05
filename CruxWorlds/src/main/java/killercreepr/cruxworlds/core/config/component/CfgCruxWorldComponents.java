package killercreepr.cruxworlds.core.config.component;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.component.TypedDataComponent;
import killercreepr.cruxconfig.config.bukkit.handler.impl.component.FileDataComponentType;
import killercreepr.cruxconfig.config.bukkit.registry.FileDataComponentRegistry;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxworlds.api.world.entity.NaturalEntitySpawn;
import killercreepr.cruxworlds.core.component.CruxWorldsComponents;
import killercreepr.cruxworlds.core.component.EntitySpawnPassengers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
    }
}
