package killercreepr.crux.core.component.parser.persistent;

import killercreepr.crux.api.component.parser.persistent.PersistentTextParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public abstract class BasePersistentTextParser<T> implements PersistentTextParser<T> {
    protected final @Nullable Predicate<Object> canDecode;
    protected final @Nullable Predicate<Object> canEncode;

    public BasePersistentTextParser(@Nullable Predicate<Object> canDecode, @Nullable Predicate<Object> canEncode) {
        this.canDecode = canDecode;
        this.canEncode = canEncode;
    }

    @Override
    public boolean canDecode(@NotNull Object object) {
        return canDecode != null ? canDecode.test(object) : PersistentTextParser.super.canDecode(object);
    }

    @Override
    public boolean canEncode(@NotNull Object object) {
        return canEncode != null ? canEncode.test(object) : PersistentTextParser.super.canEncode(object);
    }
}
