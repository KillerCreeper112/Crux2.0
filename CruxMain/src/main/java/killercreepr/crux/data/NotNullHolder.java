package killercreepr.crux.data;

import org.jetbrains.annotations.NotNull;

@Deprecated(forRemoval = true, since = "Just use killercreepr.crux.data.Holder<T>")
public interface NotNullHolder<T> extends Holder<T>{
    @Override
    @NotNull T value();
}
