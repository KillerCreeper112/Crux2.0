package killercreepr.cruxcrafting.api.config.handler;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxcrafting.api.crafting.ingredient.CruxRecipeIngredient;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FileCruxRecipeIngredient {
    @Nullable CruxRecipeIngredient deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e,
                                                       @Nullable Key key);
}
