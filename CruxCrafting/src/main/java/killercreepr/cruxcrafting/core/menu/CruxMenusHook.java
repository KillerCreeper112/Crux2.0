package killercreepr.cruxcrafting.core.menu;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxmenus.CruxMenusModule;
import killercreepr.cruxmenus.api.menu.holder.MenuItems;

import java.util.Set;
import java.util.TreeMap;

public class CruxMenusHook {
    public static void onLoad(){
        CruxMenusModule menus = CruxRegistries.MODULES.getModule(CruxMenusModule.class);
        if(menus==null) return;
        menus.menuRegistry().menuHolders().register(new GenericRecipeViewMenuHolder(
            Crux.key("crafting/recipe/view"),
            "<white><crux_space:-8><font:\"crux:crafting\">0<reset><crux_space:-145>Recipe",
            NumberProvider.constant(45),
            MenuItems.items(new TreeMap<>()), DataExchange.empty(), Set.of()
        ));
    }
}
