package killercreepr.cruxmenus.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import killercreepr.cruxmenus.CruxMenusModule;
import killercreepr.cruxmenus.menu.bukkit.holder.MenuHolder;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class CruxMenuHolderArgument implements CustomArgumentType.Converted<MenuHolder, Key> {
    protected final CruxMenusModule module;

    public CruxMenuHolderArgument(CruxMenusModule module) {
        this.module = module;
    }

    @Override
    public @NotNull MenuHolder convert(@NotNull Key nativeType) {
        return Objects.requireNonNull(module.menuRegistry().MENU_HOLDERS.get(nativeType));
    }

    @Override
    public @NotNull ArgumentType<Key> getNativeType() {
        return ArgumentTypes.key();
    }

    @Override
    public @NotNull <S> CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        for(MenuHolder p : module.menuRegistry().MENU_HOLDERS){
            builder.suggest(p.key().asMinimalString());
        }
        return builder.buildFuture();
    }

}
