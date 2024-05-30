package killercreepr.cruxmenu.menu.bukkit.requirements.impl;

import killercreepr.cruxmenu.menu.bukkit.requirements.RequirementContext;
import killercreepr.cruxmenu.menu.bukkit.requirements.SimpleMenuRequirement;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
//todo
public class PermissionRequirement extends SimpleMenuRequirement {
    public PermissionRequirement(@NotNull NamespacedKey key) {
        super(key);
    }

    @Override
    public boolean test(@NotNull Player p, @NotNull RequirementContext info, @NotNull String[] args) {
        return false;
    }
}
