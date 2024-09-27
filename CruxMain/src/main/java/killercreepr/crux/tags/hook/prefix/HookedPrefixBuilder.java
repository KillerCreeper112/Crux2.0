package killercreepr.crux.tags.hook.prefix;

import killercreepr.crux.tags.TagsPrefixBuilder;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.hook.HookedObjectTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface HookedPrefixBuilder {
    HookedPrefixBuilder EMPTY = (hooked, built, builder) -> built;
    static HookedPrefixBuilder empty(){
        return EMPTY;
    }
    HookedPrefixBuilder NOTHING = (hooked, built, builder) ->{
        if(builder == null) return null;
        return built;
    };
    static HookedPrefixBuilder nothing(){
        return NOTHING;
    }

    static HookedPrefixBuilder prefix(@Nullable FormatPrefix prefix){
        return (hooked, builtPrefix, builder) -> FormatPrefix.add(prefix, builtPrefix);
    }

    static HookedPrefixBuilder suffix(@Nullable FormatPrefix suffix){
        return (hooked, builtPrefix, builder) -> FormatPrefix.add(builtPrefix, suffix);
    }

    static HookedPrefixBuilder overwrite(@Nullable FormatPrefix value){
        return (hooked, builtPrefix, builder) ->{
            if(builder == null) return value;
            return FormatPrefix.add(value, builtPrefix);
        };
    }

    //complete
    //These disregard any prefix builders.
    HookedPrefixBuilder COMPLETE_NOTHING = (hooked, built, builder) -> null;
    static HookedPrefixBuilder completeNothing(){
        return COMPLETE_NOTHING;
    }

    static HookedPrefixBuilder completeOverwrite(@Nullable FormatPrefix value){
        return (hooked, builtPrefix, builder) -> value;
    }

    @Nullable
    FormatPrefix buildPrefix(@NotNull HookedObjectTag<?, ?> hookedObjectTag, @Nullable FormatPrefix builtPrefix, @Nullable TagsPrefixBuilder prefixBuilder);
}
