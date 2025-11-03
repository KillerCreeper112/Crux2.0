package killercreepr.cruxmenus.api.menu.module;

import killercreepr.crux.api.data.StringIdentifiable;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.cruxmenus.api.menu.Menu;
import net.kyori.adventure.key.Keyed;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ActiveMenuModule extends Keyed, StringIdentifiable {
    default void onOpen(@NotNull HumanEntity p, @NotNull Menu menu){}
    default void onClose(@NotNull HumanEntity p, @NotNull Menu menu){}
    default void onUpdate(@NotNull Menu menu){}

    default void load(@NotNull Menu menu){
        //refresh(menu);
    }
    default void onLoaded(Menu menu){
    }
    default void refresh(@NotNull Menu menu){}
    default @Nullable MergedTagContainer buildTags(@NotNull Menu menu, @NotNull TagParser tagParser){ return null; }
}
