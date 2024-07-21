package killercreepr.crux.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import killercreepr.crux.Crux;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class CruxKeyArgument implements CustomArgumentType<Key, Key> {
    @Override
    public @NotNull Key parse(@NotNull StringReader reader) throws CommandSyntaxException {


        String string = readString(reader);
        return Crux.key(string);
    }

    public String readString(@NotNull StringReader reader) {
        final int start = reader.getCursor();
        while (reader.canRead() && reader.peek() != ' ') {
            reader.skip();
        }
        return reader.getString().substring(start, reader.getCursor());
    }

    @Override
    public @NotNull ArgumentType<Key> getNativeType() {
        return ArgumentTypes.key();
    }
}
