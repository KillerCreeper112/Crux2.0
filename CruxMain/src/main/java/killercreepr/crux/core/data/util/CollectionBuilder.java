package killercreepr.crux.core.data.util;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.function.Consumer;

public class CollectionBuilder<V> {
    private final Collection<V> collection;
    public CollectionBuilder(Collection<V> collection) {
        this.collection = collection;
    }

    public CollectionBuilder() {
        this(new HashSet<>());
    }

    public CollectionBuilder<V> add(V value) {
        collection.add(value);
        return this;
    }

    public CollectionBuilder<V> addAll(Collection<V> value) {
        collection.addAll(value);
        return this;
    }

    public CollectionBuilder<V> apply(@NotNull Consumer<CollectionBuilder<V>> consumer){
        consumer.accept(this);
        return this;
    }

    public Collection<V> build() {
        return collection;
    }

    public Collection<V> buildUnmodifiable(){
        return Collections.unmodifiableCollection(collection);
    }
}
