package killercreepr.cruxpotions.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import killercreepr.cruxpotions.command.argument.resolver.EntityInflictorResolver;
import org.jetbrains.annotations.NotNull;

public class EntityInflictorArgument implements CustomArgumentType.Converted<EntityInflictorResolver, EntitySelectorArgumentResolver> {
    @Override
    public @NotNull EntityInflictorResolver convert(@NotNull EntitySelectorArgumentResolver nativeType) {
        return new EntityInflictorResolver(nativeType);
    }

    @Override
    public @NotNull ArgumentType<EntitySelectorArgumentResolver> getNativeType() {
        return ArgumentTypes.entity();
    }
}
