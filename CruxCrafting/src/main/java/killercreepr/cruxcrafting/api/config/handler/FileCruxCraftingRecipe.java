package killercreepr.cruxcrafting.api.config.handler;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxcrafting.api.crafting.recipe.CruxCraftingRecipe;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FileCruxCraftingRecipe {
    @Nullable CruxCraftingRecipe deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e,
                                                     @NotNull Key key);
}
