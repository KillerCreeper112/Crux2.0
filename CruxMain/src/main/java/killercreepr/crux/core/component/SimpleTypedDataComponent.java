package killercreepr.crux.core.component;

import killercreepr.crux.api.component.DataComponentType;
import killercreepr.crux.api.component.TypedDataComponent;
import killercreepr.crux.core.data.util.Pair;
import org.jetbrains.annotations.NotNull;

public class SimpleTypedDataComponent<T> extends Pair<DataComponentType<T>, T> implements TypedDataComponent<T> {
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
