package killercreepr.crux.data.communication;

import killercreepr.crux.data.Reloadable;
import org.jetbrains.annotations.NotNull;

public interface LangProvider extends Reloadable {
    @NotNull CreateLang lang();
}
