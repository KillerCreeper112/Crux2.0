package killercreepr.cruxpotions.command.argument.resolver;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.SelectorArgumentResolver;
import io.papermc.paper.math.BlockPosition;
import killercreepr.cruxpotions.potions.inflictor.BlockInflictor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class BlockInflictorResolver implements SelectorArgumentResolver<BlockInflictor> {
    protected final @NotNull BlockPositionResolver resolver;
    protected final @Nullable World world;
    public BlockInflictorResolver(@NotNull BlockPositionResolver resolver, @Nullable World world) {
        this.resolver = resolver;
        this.world = world;
    }

    @Override
    public @NotNull BlockInflictor resolve(@NotNull CommandSourceStack source) throws CommandSyntaxException {
        World w = world == null ? ((Entity) Objects.requireNonNullElse(source.getExecutor(), source.getSender())).getWorld() : world;
        BlockPosition pos = resolver.resolve(source);
        return new BlockInflictor(
            w.getBlockAt(pos.blockX(), pos.blockY(), pos.blockZ())
        );
    }
}
