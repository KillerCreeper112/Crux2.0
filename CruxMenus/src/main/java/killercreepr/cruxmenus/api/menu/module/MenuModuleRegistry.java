package killercreepr.cruxmenus.api.menu.module;

import killercreepr.crux.tags.TagParser;
import killercreepr.crux.tags.container.MergedTagContainer;
import killercreepr.crux.tags.container.TagContainer;
import killercreepr.cruxmenus.api.menu.CommonMenu;
import killercreepr.cruxmenus.api.menu.Menu;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface MenuModuleRegistry extends CommonMenu, Iterable<ActiveMenuModule> {
    @NotNull
    Menu getMenu();
    @Nullable
    ActiveMenuModule getByID(@NotNull String id);
    @Nullable
    Collection<ActiveMenuModule> get(@NotNull Key key);
    <T extends ActiveMenuModule> T register(@NotNull T module);
    @Nullable
    ActiveMenuModule unregister(@NotNull Key key);
    @Nullable
    ActiveMenuModule unregisterByID(@NotNull String id);
    default void refresh(){}
    default MergedTagContainer buildTags(@NotNull TagParser tagParser){
        MergedTagContainer tags = TagContainer.merged(tagParser);
        for(ActiveMenuModule m : this){
            tags.addAll(m.buildTags(getMenu(), tagParser));
        }
        return tags;
    }
}
