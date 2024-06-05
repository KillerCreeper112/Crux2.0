package killercreepr.crux.tags.format;

import killercreepr.crux.tags.container.ObjectHookContainer;
import killercreepr.crux.tags.tag.ObjectTag;
import org.jetbrains.annotations.NotNull;

public interface FormatPrefix {
    static @NotNull FormatPrefix empty(){
        return generic("");
    }

    static @NotNull FormatPrefix generic(@NotNull String prefix) {
        return new FormatPrefix() {
            @Override
            public @NotNull String prefix(@NotNull ObjectTag<?> tag, @NotNull Object object) {
                return prefix;
            }

            @Override
            public @NotNull String hookedPrefix(@NotNull ObjectTag<?> mainTag, @NotNull Object object, @NotNull ObjectHookContainer<?> hooked) {
                return hooked.getObjectTag().defaultPrefix().prefix(mainTag, object);
            }
        };
    }

    static @NotNull FormatPrefix addonPlusHook(@NotNull String prefix){
        return new FormatPrefix() {
            @Override
            public @NotNull String prefix(@NotNull ObjectTag<?> tag, @NotNull Object object) {
                return prefix + tag.defaultPrefix().prefix(tag, object);
            }

            @Override
            public @NotNull String hookedPrefix(@NotNull ObjectTag<?> mainTag, @NotNull Object object, @NotNull ObjectHookContainer<?> hooked) {
                Object value = hooked.getHolder().value();
                if(value == null) return prefix + mainTag.defaultPrefix().prefix(mainTag, object);
                return prefix + mainTag.defaultPrefix().prefix(mainTag, object) +
                        hooked.getObjectTag().defaultPrefix().prefix(hooked.getObjectTag(), value);
            }
        };
    }

    static @NotNull FormatPrefix addon(@NotNull String prefix) {
        return new FormatPrefix() {
            @Override
            public @NotNull String prefix(@NotNull ObjectTag<?> tag, @NotNull Object object) {
                return prefix + tag.defaultPrefix().prefix(tag, object);
            }

            @Override
            public @NotNull String hookedPrefix(@NotNull ObjectTag<?> mainTag, @NotNull Object object, @NotNull ObjectHookContainer<?> hooked) {
                Object value = hooked.getHolder().value();
                if(value == null) return prefix;
                return prefix + hooked.getObjectTag().defaultPrefix().prefix(hooked.getObjectTag(), value);
            }
        };
    }


    @NotNull String prefix(@NotNull ObjectTag<?> tag, @NotNull Object object);
    @NotNull String hookedPrefix(@NotNull ObjectTag<?> tag, @NotNull Object object, @NotNull ObjectHookContainer<?> hooked);
}
