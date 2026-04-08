package killercreepr.crux.core.codec;

import killercreepr.crux.api.codec.Codec;
import killercreepr.crux.api.codec.DecodeException;
import killercreepr.crux.api.codec.node.DataNode;
import killercreepr.crux.api.codec.node.DataObject;
import killercreepr.crux.core.codec.node.ObjectDataNode;
import killercreepr.crux.core.codec.node.StringDataNode;

import java.util.Map;
import java.util.function.Supplier;

public final class MapCodec<K, V> implements Codec<Map<K, V>> {
    private final Supplier<Map<K, V>> mapSupplier;
    private final Codec<? extends K> keyCodec;
    private final Codec<? extends V> valueCodec;

  public MapCodec(Supplier<Map<K, V>> mapSupplier, Codec<? extends K> keyCodec, Codec<? extends V> valueCodec) {
    this.mapSupplier = mapSupplier;
    this.keyCodec = keyCodec;
    this.valueCodec = valueCodec;
  }

  @Override
    public Map<K, V> decode(DataNode node) {
        if (!node.isObjectData()) {
            throw new DecodeException("Expected object for map codec");
        }

        DataObject obj = node.asObjectData();
        Map<K, V> map = mapSupplier.get();
        obj.forEachDataPair((k, v) -> {
            var key = keyCodec.decode(new StringDataNode(k));
            if(key == null) return;
            var value = valueCodec.decode(v);
            if(value == null) return;
            map.put(key, value);
        });
        return map;
    }

    @Override
    public DataNode encode(Map<K, V> value) {
      var node = new ObjectDataNode();
      value.forEach((k, v) -> {
          var keyNode = keyCodec.encodeUnchecked(k);
          if(keyNode == null) return;
          var valueNode = valueCodec.encodeUnchecked(v);
          if(valueNode == null) return;
          node.put(
            keyNode.asString(),
            valueNode
          );
      });
      return node;
    }
}