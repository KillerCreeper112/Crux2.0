package killercreepr.crux.component;

import killercreepr.crux.data.Holder;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public interface DataComponentHandler extends DataComponentAccessor, DataComponentEditor{
    static DataComponentHandler simple(){
        return new Simple();
    }

    static DataComponentHandler empty(){
        return new Empty();
    }

    class Empty implements DataComponentHandler {
        @Override
        public <T> @Nullable T get(DataComponentType<? extends T> type) {
            return null;
        }

        @Override
        public Set<DataComponentType<?>> keySet() {
            return Set.of();
        }

        @Override
        public <T> @Nullable T set(DataComponentType<? super T> type, @Nullable T value) {
            return null;
        }
    }

    class Simple implements DataComponentHandler {
        protected final Map<DataComponentType<?>, Holder<?>> map;
        public Simple(){
            this(new HashMap<>());
        }
        public Simple(Map<DataComponentType<?>, Holder<?>> map) {
            this.map = map;
        }

        @Override
        public <T> @Nullable T get(DataComponentType<? extends T> type) {
            Holder<?> holder = map.get(type);
            return holder == null ? null : (T) holder.value();
        }

        @Override
        public Set<DataComponentType<?>> keySet() {
            return map.keySet();
        }

        @Override
        public <T> @Nullable T set(DataComponentType<? super T> type, @Nullable T value) {
            Holder<?> holder;
            if(value == null) holder = map.remove(type);
            else holder = map.put(type, Holder.direct(value));
            return holder == null ? null : (T) holder.value();
        }
    }
}
