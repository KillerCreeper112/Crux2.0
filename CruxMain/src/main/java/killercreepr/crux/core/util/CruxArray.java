package killercreepr.crux.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CruxArray<T> {
    protected T[] array;

    public CruxArray(T... array) {
        this.array = array;
    }

    public CruxArray<T> merge(T... values){
        if(values==null || values.length == 0) return this;
        return merge(List.of(values));
    }

    public CruxArray<T> merge(Collection<T> values){
        if (values == null || values.isEmpty()) return this;
        Collection<T> list = toMutableList();
        list.addAll(values);
        setArray(createArray(list));
        return this;
    }

    @SuppressWarnings("unchecked")
    public T[] createArray(Collection<T> values){
        return (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), values.size());
    }

    public Collection<T> toMutableList(){
        return new ArrayList<>(toList());
    }

    public Collection<T> toList(){
        return Arrays.asList(array);
    }

    public T[] getArray(){
        return array;
    }

    public CruxArray<T> setArray(T[] array) {
        this.array = array;
        return this;
    }
}
