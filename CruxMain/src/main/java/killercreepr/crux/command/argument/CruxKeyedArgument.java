package killercreepr.crux.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;

public interface CruxKeyedArgument<T extends Keyed> extends CustomArgumentType<T, Key> {
    @NotNull T parse(@NotNull Key key);

    @Override
    default @NotNull ArgumentType<Key> getNativeType() {
        return CruxCmdArguments.CRUX_KEY;
    }
}
