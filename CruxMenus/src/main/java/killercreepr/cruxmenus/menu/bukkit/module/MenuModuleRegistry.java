package killercreepr.cruxmenus.menu.bukkit.module;

import killercreepr.crux.registry.Registry;
import killercreepr.crux.tags.TagParser;
import killercreepr.crux.tags.container.MultiTagContainer;
import killercreepr.crux.tags.format.Format;
import killercreepr.cruxmenus.menu.bukkit.CommonMenu;
import killercreepr.cruxmenus.menu.bukkit.Menu;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MenuModuleRegistry extends CommonMenu, Registry<ActiveMenuModule> {
    @NotNull
    Menu getMenu();
    @Nullable
    ActiveMenuModule getByID(@NotNull String id);
    @Nullable
    ActiveMenuModule get(@NotNull Key key);
    <T extends ActiveMenuModule> T register(@NotNull T module);
    @Nullable
    ActiveMenuModule unregister(@NotNull Key key);
    @Nullable
    ActiveMenuModule unregisterByID(@NotNull String id);
    default void refresh(){}
    default MultiTagContainer buildTags(@NotNull TagParser tagParser){
        MultiTagContainer tags = new MultiTagContainer(tagParser);
        for(ActiveMenuModule m : this){
            tags.addAll(m.buildTags(getMenu(), tagParser));
        }
        return tags;
    }
}
