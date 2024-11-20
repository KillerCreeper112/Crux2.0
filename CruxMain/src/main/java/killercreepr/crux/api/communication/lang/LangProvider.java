package killercreepr.crux.api.communication.lang;

import killercreepr.crux.api.data.Reloadable;
import org.jetbrains.annotations.NotNull;

public interface LangProvider extends Reloadable {
    @NotNull CreateLang lang();
}
