package killercreepr.cruxconfig.config.common.element;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.element.YamlNull;
import org.jetbrains.annotations.NotNull;

public class FileNull extends FileElement{
    public static final FileNull INSTANCE = new FileNull();

    @Deprecated(since = "See FileNull#INSTANCE")
    public FileNull() {
    }

    @Override
    public @NotNull JsonElement toJson() {
        return JsonNull.INSTANCE;
    }

    @Override
    public @NotNull YamlElement toYaml() {
        return YamlNull.INSTANCE;
    }
}
