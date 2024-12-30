package killercreepr.cruxmenus.api.menu.module;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public interface ActiveMenuModuled extends ActiveMenuModule {
    @NotNull MenuModule getModule();
    @Override
    default @NotNull Key key(){
        return getModule().key();
    }
}
