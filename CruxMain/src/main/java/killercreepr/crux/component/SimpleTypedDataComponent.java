package killercreepr.crux.component;

import killercreepr.crux.data.util.Pair;
import org.jetbrains.annotations.NotNull;

public class SimpleTypedDataComponent<T> extends Pair<DataComponentType<T>, T> implements TypedDataComponent<T>{
    public SimpleTypedDataComponent(DataComponentType<T> first, T second) {
        super(first, second);
    }

    @Override
    public @NotNull DataComponentType<T> getType() {
        return first;
    }

    @Override
    public T getValue() {
        return second;
    }
}
