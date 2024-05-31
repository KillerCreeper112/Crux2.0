package killercreepr.cruxpotion.command;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PotionCmd implements BasicCommand {
    @Override
    public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {
        Commands.literal("new-command")
                .executes(ctx -> {
                    Player test =  ctx.getArgument("", Player.class);
                    ctx.getSource().getSender().sendPlainMessage("some message");
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }
}
