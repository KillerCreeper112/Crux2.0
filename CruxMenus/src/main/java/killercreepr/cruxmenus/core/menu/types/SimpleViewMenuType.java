package killercreepr.cruxmenus.core.menu.types;

import killercreepr.crux.core.data.SimpleKeyed;
import killercreepr.cruxmenus.api.menu.ViewedMenu;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;

public class SimpleViewMenuType extends SimpleKeyed implements ViewMenuType{
    protected final MenuType type;
    public SimpleViewMenuType(Key key, MenuType type) {
        super(key);
        this.type = type;
    }

    @Override
    public @NotNull ViewedMenu create(@NotNull Entity viewer, @NotNull Component title) {
        return new SimpleViewMenu(type).reconstruct(viewer, title);
    }
}
