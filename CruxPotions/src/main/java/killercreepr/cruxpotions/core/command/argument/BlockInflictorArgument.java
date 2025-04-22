package killercreepr.cruxpotions.core.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import killercreepr.cruxpotions.core.command.argument.resolver.BlockInflictorResolver;
import org.jetbrains.annotations.NotNull;

public class BlockInflictorArgument implements CustomArgumentType.Converted<BlockInflictorResolver, BlockPositionResolver> {
    @Override
    public @NotNull BlockInflictorResolver convert(@NotNull BlockPositionResolver nativeType) {
        return new BlockInflictorResolver(nativeType, null);
    }

    @Override
    public @NotNull ArgumentType<BlockPositionResolver> getNativeType() {
        return ArgumentTypes.blockPosition();
    }
}
