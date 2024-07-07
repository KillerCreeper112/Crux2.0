package killercreepr.crux.registry;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class SimpleRegistry<T> implements Registry<T> {
    protected final Collection<T> collection;
    public SimpleRegistry(@NotNull Collection<T> collection){
        this.collection = collection;
    }

    public static <T> SimpleRegistry<T> fromSet(){
        return new SimpleRegistry<>(new HashSet<>());
    }

    public static <T> SimpleRegistry<T> fromList(){
        return new SimpleRegistry<>(new ArrayList<>());
    }

    @Override
    public @NotNull Stream<T> stream() {
        return StreamSupport.stream(this.spliterator(), false);
    }

    @Override
    public <E extends T> @NotNull E register(@NotNull E object) {
        collection.add(object);
        return object;
    }

    @Override
    public boolean unregister(@NotNull T object) {
        return collection.remove(object);
    }

    @Override
    public void clear() {
        collection.clear();
    }

    @Override
    public @NotNull Collection<T> values() {
        return collection;
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return this.collection.iterator();
    }
}
