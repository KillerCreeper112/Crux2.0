package killercreepr.cruxmenus.menu.bukkit.requirements.impl;

import killercreepr.cruxmenus.menu.bukkit.requirements.RequirementContext;
import killercreepr.cruxmenus.menu.bukkit.requirements.SimpleMenuRequirement;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
//todo
public class PermissionRequirement extends SimpleMenuRequirement {
    public PermissionRequirement(@NotNull Key key) {
        super(key);
    }

    @Override
    public boolean test(@NotNull Player p, @NotNull RequirementContext info, @NotNull String[] args) {
        return false;
    }
}
