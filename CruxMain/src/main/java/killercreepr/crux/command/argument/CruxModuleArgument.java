package killercreepr.crux.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import killercreepr.crux.module.CruxModule;
import killercreepr.crux.registries.CruxRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CruxModuleArgument implements CustomArgumentType.Converted<CruxModule, String> {
    /**
     * Converts the value from the native type to the custom argument type.
     *
     * @param nativeType native argument provided value
     * @return converted value
     * @throws CommandSyntaxException if an exception occurs while parsing
     */
    @Override
    public @NotNull CruxModule convert(@NotNull String nativeType) throws CommandSyntaxException {
        return Objects.requireNonNull(CruxRegistries.MODULES.get(nativeType), "CruxModule " + nativeType + ", not found!");
    }

    /**
     * Gets the native type that this argument uses,
     * the type that is sent to the client.
     *
     * @return native argument type
     */
    @Override
    public @NotNull ArgumentType<String> getNativeType() {
        return StringArgumentType.string();
    }
}
