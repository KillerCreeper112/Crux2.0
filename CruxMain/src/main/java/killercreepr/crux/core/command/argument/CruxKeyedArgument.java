package killercreepr.crux.core.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public interface CruxKeyedArgument<T> extends CustomArgumentType<T, Key> {
    @Override
    default @NotNull T parse(@NotNull StringReader reader) throws CommandSyntaxException {
        return parse(CruxCmdArguments.CRUX_KEY.parse(reader));
    }

    @NotNull T parse(@NotNull Key key);


    @Override
    default @NotNull ArgumentType<Key> getNativeType() {
        return ArgumentTypes.key();
    }
}
