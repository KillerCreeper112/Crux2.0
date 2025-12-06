package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.api.item.dynamic.MergeOption;
import killercreepr.crux.core.Crux;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Experimental
@JsonSerializer(id = "merge_option")
public class FileMergeOption extends SimpleFileHandler<MergeOption> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull MergeOption object) {
        return context.getRegistry().serializeToFile(object.key());
    }

    @Override
    public @Nullable MergeOption deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        var key = context.getRegistry().deserializeFromFile(Key.class, e);
        return key == null ? null : MergeOption.mergeOption(key);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "merge_option";
    }
}
