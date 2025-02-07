package killercreepr.cruxitems.core.item;

import killercreepr.crux.api.component.DataComponentAccessor;
import killercreepr.crux.api.component.DataComponentType;
import killercreepr.crux.api.item.CruxItemType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class CfgItemType implements CruxItemType {
    protected final DataComponentAccessor data;
    public CfgItemType(DataComponentAccessor data) {
        this.data = data;
    }

    @Override
    public <T> @Nullable T getDefaultData(@NotNull DataComponentType<T> type) {
        return data == null ? null : data.get(type);
    }

    @Override
    public boolean hasDefaultData(@NotNull DataComponentType<?> type) {
        return data != null && data.has(type);
    }

    @Override
    public @NotNull Collection<DataComponentType<?>> getDefaultData() {
        return data.keySet();
    }

    @Override
    public String toString() {
        return "CfgItemType{" +
            "components=" + data +
            '}';
    }
}
