package killercreepr.crux.core.component.parser.hybrid;

import killercreepr.crux.api.component.parser.InputDecodeContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class SimpleInputDecodeContext implements InputDecodeContext {
    protected final @NotNull Object value;

    public SimpleInputDecodeContext(@NotNull Object value) {
        this.value = value;
    }

    @Override
    public List<?> toList() {
        return (List<?>) value;
    }

    @Override
    public Map<?, ?> toMap() {
        return (Map<?, ?>) value;
    }

    @Override
    public <T> T get() {
        return (T) value;
    }

    @Override
    public Object getUnchecked() {
        return value;
    }

    @Override
    public <T> T get(String id) {
        return (T) toMap().get(id);
    }
}
