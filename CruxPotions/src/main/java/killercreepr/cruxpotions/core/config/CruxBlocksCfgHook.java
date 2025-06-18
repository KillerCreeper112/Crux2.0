package killercreepr.cruxpotions.core.config;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.component.TypedDataComponent;
import killercreepr.crux.api.entity.predicate.EntityPredicate;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxblocks.api.block.component.CruxEntityMoveInsideBlockComponent;
import killercreepr.cruxconfig.config.bukkit.handler.impl.component.FileDataComponentType;
import killercreepr.cruxconfig.config.bukkit.registry.FileDataComponentRegistry;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxpotions.api.potion.StoredPotion;
import killercreepr.cruxpotions.core.component.CruxBlocksPotionComponents;
import killercreepr.cruxpotions.core.cruxblocks.ApplyCruxPotionEffectsEntityMoveInsideBlockComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class CruxBlocksCfgHook {
    public static void register(@NotNull FileDataComponentRegistry registry){
        registry.register("entity_move_inside_crux_potion_effects", new FileDataComponentType<CruxEntityMoveInsideBlockComponent>() {
            @Override
            public @Nullable TypedDataComponent<CruxEntityMoveInsideBlockComponent> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e) {
                Collection<StoredPotion> potions = ctx.getRegistry().deserializeFromFile(
                    new TypeToken<Collection<StoredPotion>>(){}.getType(),
                    e.get("potion_effects")
                );
                if(potions == null || potions.isEmpty()) return null;
                EntityPredicate filter = ctx.getRegistry().deserializeFromFile(EntityPredicate.class, e.get("filter"));
                return TypedDataComponent.create(
                    CruxBlocksPotionComponents.GENERIC_CRUX_POTIONS_ENTITY_MOVE_INSIDE,
                    new ApplyCruxPotionEffectsEntityMoveInsideBlockComponent(potions, filter)
                );
            }
        });
    }

    private static NumberProvider num(FileRegistry registry, FileObject o, String x, NumberProvider fallback){
        NumberProvider v = registry.deserializeFromFile(NumberProvider.class, o.get(x));
        if(v == null) return fallback;
        return v;
    }
}
