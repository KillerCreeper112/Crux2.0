package killercreepr.crux.data;

import org.jetbrains.annotations.NotNull;

public interface NotNullHolder<T> extends Holder<T>{
    @Override
    @NotNull T value();
}
