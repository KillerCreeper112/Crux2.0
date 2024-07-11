package killercreepr.cruxstructures.commands.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import killercreepr.cruxstructures.registries.StructureRegistries;
import killercreepr.cruxstructures.structure.Structure;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class StructureArgument implements CustomArgumentType.Converted<Structure, Key> {
    @Override
    public @NotNull Structure convert(@NotNull Key nativeType) {
        return Objects.requireNonNull(StructureRegistries.STRUCTURES.get(nativeType), "Structure '" + nativeType + "' not found!");
    }

    @Override
    public @NotNull ArgumentType<Key> getNativeType() {
        return ArgumentTypes.key();
    }
}
