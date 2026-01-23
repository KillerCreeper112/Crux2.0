package killercreepr.crux.core.codec;

import killercreepr.crux.api.codec.Codec;
import killercreepr.crux.api.codec.DecodeException;
import killercreepr.crux.api.codec.node.DataArray;
import killercreepr.crux.api.codec.node.DataNode;
import killercreepr.crux.core.codec.node.ArrayDataNode;

import java.util.ArrayList;
import java.util.List;

public final class ListCodec<T> implements Codec<List<T>> {
    private final Codec<T> element;

    public ListCodec(Codec<T> element) {
        this.element = element;
    }

    @Override
    public List<T> decode(DataNode node) {
        if (!node.isArrayData()) throw new DecodeException("Expected array");
        DataArray arr = node.asArrayData();
        List<T> list = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            list.add(element.decode(arr.get(i)));
        }
        return list;
    }

    @Override
    public DataNode encode(List<T> value) {
        DataArray arr = new ArrayDataNode();
        for (T v : value) arr.add(element.encode(v));
        return arr;
    }
}