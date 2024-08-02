package killercreepr.cruxadvancements.advancement.reward;

import killercreepr.cruxadvancements.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.manager.CruxAdvancementManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class CommandsReward implements CruxAdvanceReward{
    protected final @NotNull Collection<String> commands;
    public CommandsReward(@NotNull Collection<String> commands) {
        this.commands = commands;
    }

    public @NotNull Collection<String> getCommands() {
        return commands;
    }

    @Override
    public void onReward(@NotNull CruxAdvancementManager<?> manager, @NotNull CruxAdvancement advancement, @NotNull Player p) {
        for(String s : commands){
            s = s.replace("<player>", p.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s);
        }
    }
}
