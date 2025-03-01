package killercreepr.cruxcrafting.core.registries;

import killercreepr.crux.api.registry.MappedRegistry;
import killercreepr.crux.core.registry.SimpleMappedRegistry;
import killercreepr.cruxcrafting.api.crafting.ingredient.CruxRecipeIngredient;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;

public class CruxCraftingRegistries {
    public static final MappedRegistry<Key, CruxRecipeIngredient> RECIPE_INGREDIENT = new SimpleMappedRegistry<>(){
        @Override
        public <E extends CruxRecipeIngredient> @NotNull E register(@NotNull E object) {
            if(!(object instanceof Keyed k)) return super.register(object);
            return register(k.key(), object);
        }

        @Override
        public boolean unregister(@NotNull CruxRecipeIngredient object) {
            if(!(object instanceof Keyed k)) return super.unregister(object);
            return remove(k.key()) != null;
        }
    };
}
