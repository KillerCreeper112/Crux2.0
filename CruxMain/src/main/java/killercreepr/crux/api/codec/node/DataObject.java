package killercreepr.crux.api.codec.node;

import java.util.function.BiConsumer;

public interface DataObject extends DataNode {
    DataNode get(String key);
    void put(String key, DataNode value);
    void forEachDataPair(BiConsumer<String, DataNode> consumer);
}

