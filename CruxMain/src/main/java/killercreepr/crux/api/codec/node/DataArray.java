package killercreepr.crux.api.codec.node;

import java.util.function.Consumer;

public interface DataArray extends DataNode {
    DataNode get(int index);
    void add(DataNode value);
    int size();

    void forEachDataNode(Consumer<DataNode> consumer);
}
