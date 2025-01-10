package killercreepr.crux.api.text.hook;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.tags.TagsPrefixBuilder;
import killercreepr.crux.core.text.hook.prefix.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Deprecated(forRemoval = true)
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
