package killercreepr.cruxmenus.menu.bukkit.module;

import killercreepr.crux.data.StringIdentifiable;
import killercreepr.crux.tags.TagParser;
import killercreepr.crux.tags.container.MergedTagContainer;
import killercreepr.cruxmenus.menu.bukkit.Menu;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ActiveMenuModule extends Keyed, StringIdentifiable {
    default void onOpen(@NotNull Player p, @NotNull Menu menu){}
    default void onClose(@NotNull Player p, @NotNull Menu menu){}
    default void onUpdate(@NotNull Menu menu){}

    default void load(@NotNull Menu menu){
        refresh(menu);
    }
    default void refresh(@NotNull Menu menu){}
    default @Nullable MergedTagContainer buildTags(@NotNull Menu menu, @NotNull TagParser tagParser){ return null; }
    @NotNull MenuModule getModule();
    @Override
    default @NotNull Key key(){
        return getModule().key();
    }
}
