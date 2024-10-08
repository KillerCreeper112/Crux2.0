package killercreepr.crux.tags.hook.prefix;

import killercreepr.crux.tags.TagsPrefixBuilder;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.hook.HookedObjectTag;
import killercreepr.crux.tags.hook.prefix.impl.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface HookedPrefixBuilder {
    HookedPrefixBuilder EMPTY = new DummyHookedPrefixBuilder();
    static HookedPrefixBuilder empty(){
        return EMPTY;
    }
    HookedPrefixBuilder NOTHING = new NothingHookedPrefixBuilder();
    static HookedPrefixBuilder nothing(){
        return NOTHING;
    }

    static HookedPrefixBuilder prefix(@Nullable FormatPrefix prefix){
        return new PrefixAddHookedPrefixBuilder(prefix);
    }

    static HookedPrefixBuilder suffix(@Nullable FormatPrefix suffix){
        return new SuffixAddHookedPrefixBuilder(suffix);
    }

    static HookedPrefixBuilder overwrite(@Nullable FormatPrefix value){
        return new OverwriteHookedPrefixBuilder(value);
    }

    static HookedPrefixBuilder add(@NotNull HookedPrefixBuilder first, @NotNull HookedPrefixBuilder second){
        if(first.equals(second)) return first;
        return new AddonHookedPrefixBuilder(first, second);
    }

    //complete
    //These disregard any prefix builders. Probably should never to be used tbh...
    static HookedPrefixBuilder completeNothing(){
        return (hooked, built, builder) -> null;
    }

    static HookedPrefixBuilder completeOverwrite(@Nullable FormatPrefix value){
        return (hooked, builtPrefix, builder) -> value;
    }

    @Nullable
    FormatPrefix buildPrefix(@NotNull HookedObjectTag<?, ?> hookedObjectTag, @Nullable FormatPrefix builtPrefix, @Nullable TagsPrefixBuilder prefixBuilder);
}
