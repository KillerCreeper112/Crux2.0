package killercreepr.crux.component;

public interface DataComponentType<T> {
    static <T> Builder<T> builder(){
        return new Builder.SimpleBuilder<>();
    }

    interface Builder<T>{
        DataComponentType<T> build();

        class SimpleBuilder<T> implements Builder<T>{

            @Override
            public DataComponentType<T> build() {
                return new DataComponentType<T>() {
                };
            }
        }
    }
}
