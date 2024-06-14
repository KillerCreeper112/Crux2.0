package killercreepr.crux.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import killercreepr.crux.util.CruxColor;
import org.bukkit.Color;
import org.jetbrains.annotations.NotNull;

public class ColorArgument implements CustomArgumentType.Converted<Color, String> {


    @Override
    public @NotNull ArgumentType<String> getNativeType() {
        return StringArgumentType.string();
    }

    @Override
    public @NotNull Color convert(@NotNull String nativeType) throws CommandSyntaxException {
        return CruxColor.hexToColor(nativeType);
    }
}
