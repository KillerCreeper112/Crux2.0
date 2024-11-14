package killercreepr.cruxstats.core.command.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import killercreepr.cruxstats.api.stat.CruxStatHolder;
import org.jetbrains.annotations.NotNull;

public interface StatHolderResolver {
    @NotNull
    CruxStatHolder resolve(CommandSourceStack source) throws CommandSyntaxException;
}
