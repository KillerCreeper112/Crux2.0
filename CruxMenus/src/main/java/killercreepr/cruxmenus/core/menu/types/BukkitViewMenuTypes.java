package killercreepr.cruxmenus.core.menu.types;

import killercreepr.crux.core.Crux;
import killercreepr.cruxmenus.core.registries.MenuRegistries;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.MenuType;

public class BukkitViewMenuTypes {
    public static final ViewMenuType ANVIL = register(Crux.key("anvil"), MenuType.ANVIL);
    public static final ViewMenuType FURNACE = register(Crux.key("furnace"), MenuType.FURNACE);
    public static final ViewMenuType HOPPER = register(Crux.key("furnace"), MenuType.HOPPER);
    public static final ViewMenuType GENERIC_3X3 = register(Crux.key("generic_3x3"), MenuType.GENERIC_3X3);
    public static final ViewMenuType GRINDSTONE = register(Crux.key("grindstone"), MenuType.GRINDSTONE);
    public static final ViewMenuType BLAST_FURNACE = register(Crux.key("blast_furnace"), MenuType.BLAST_FURNACE);
    public static final ViewMenuType SMOKER = register(Crux.key("smoker"), MenuType.SMOKER);
    public static final ViewMenuType BREWING_STAND = register(Crux.key("brewing_stand"), MenuType.BREWING_STAND);

    private static ViewMenuType register(Key key, MenuType type){
        return register(new SimpleViewMenuType(key, type));
    }

    private static ViewMenuType register(ViewMenuType type){
        return MenuRegistries.VIEW_MENU_TYPE.register(type);
    }
}
