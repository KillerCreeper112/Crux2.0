package killercreepr.cruxstats.core.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import killercreepr.cruxstats.api.bukkit.BukkitStatHolder;
import org.bukkit.entity.Player;

public class StatHolderArgument implements CustomArgumentType.Converted<StatHolderResolver, PlayerSelectorArgumentResolver> {
    @Override
    public StatHolderResolver convert(PlayerSelectorArgumentResolver nativeType) throws CommandSyntaxException {
        return source ->{
            try{
                Player p = nativeType.resolve(source).getFirst();
                return BukkitStatHolder.holder(p);
            }catch (Exception ignored){}
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().create("");
        };
    }

    @Override
    public ArgumentType<PlayerSelectorArgumentResolver> getNativeType() {
        return ArgumentTypes.player();
    }
}
