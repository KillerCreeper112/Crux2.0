package killercreepr.crux.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import killercreepr.crux.Crux;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class CruxKeyArgument implements CustomArgumentType<Key, String> {
    @Override
    public @NotNull Key parse(@NotNull StringReader reader) throws CommandSyntaxException {
        return Crux.key(getNativeType().parse(reader));
    }

    @Override
    public @NotNull ArgumentType<String> getNativeType() {
        return StringArgumentType.string();
    }
}
