package killercreepr.cruxcrafting.core;

import killercreepr.crux.api.plugin.module.CruxModule;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.crux.core.plugin.module.StandardModules;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxcrafting.api.crafting.RecipeCategory;
import killercreepr.cruxcrafting.core.commands.CruxCraftingCmds;
import killercreepr.cruxcrafting.core.config.CruxCraftingCfg;
import killercreepr.cruxcrafting.core.menu.CruxMenusHook;
import org.jetbrains.annotations.NotNull;

public class CruxCraftingModule implements CruxModule {
    public static final String NAMESPACE = StandardModules.CRUX_CRAFTING;
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    @Override
    public void onLoad(@NotNull CruxPlugin plugin) {
        if(CruxRegistries.MODULES.containsKey(StandardModules.CRUX_CONFIGS)){
            CruxCraftingCfg.onLoad();
        }
        if(CruxRegistries.MODULES.containsKey(StandardModules.CRUX_MENUS)){
            CruxMenusHook.onLoad();
        }
        RecipeCategory.register();

        new CruxCraftingCmds(plugin).register();
    }
}
