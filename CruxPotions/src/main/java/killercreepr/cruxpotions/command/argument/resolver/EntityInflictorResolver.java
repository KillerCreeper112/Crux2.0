package killercreepr.cruxpotions.command.argument.resolver;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.SelectorArgumentResolver;
import killercreepr.cruxpotions.potions.inflictor.EntityInflictor;
import org.jetbrains.annotations.NotNull;

public class EntityInflictorResolver implements SelectorArgumentResolver<EntityInflictor> {
    protected final @NotNull EntitySelectorArgumentResolver resolver;
    public EntityInflictorResolver(@NotNull EntitySelectorArgumentResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public @NotNull EntityInflictor resolve(@NotNull CommandSourceStack source) throws CommandSyntaxException {
        return new EntityInflictor(resolver.resolve(source).getFirst());
    }
}
