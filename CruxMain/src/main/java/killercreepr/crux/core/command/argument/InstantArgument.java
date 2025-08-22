package killercreepr.crux.core.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import killercreepr.crux.core.util.CruxTimeUtil;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public class InstantArgument implements CustomArgumentType.Converted<Instant, String> {

    @Override
    public @NotNull ArgumentType<String> getNativeType() {
        return StringArgumentType.string();
    }
    @Override
    public Instant convert(String nativeType) throws CommandSyntaxException {
        return CruxTimeUtil.parseInstant(nativeType);
    }
}
