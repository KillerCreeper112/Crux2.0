package killercreepr.cruxmenus.menu.bukkit.module;

import killercreepr.cruxmenus.menu.bukkit.Menu;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * MenuModule tags start with: module_(module_id)_
 */
public interface MenuModule extends Keyed {
    static @NotNull String buildTagPrefix(@NotNull String moduleID){
        return "module_" + moduleID + "_";
    }
    static @NotNull String buildTag(@NotNull String moduleID, @NotNull String tagID){
        return buildTagPrefix(moduleID) + tagID;
    }
    @Nullable ActiveMenuModule build(@NotNull Menu menu);
}
