package killercreepr.crux.core.codec;

import killercreepr.crux.api.codec.Codec;
import killercreepr.crux.api.codec.DecodeException;
import killercreepr.crux.api.codec.node.DataArray;
import killercreepr.crux.api.codec.node.DataNode;
import killercreepr.crux.core.codec.node.ArrayDataNode;

import java.util.UUID;

public class UUIDCodec implements Codec<UUID> {
  @Override
  public UUID decode(DataNode node) throws DecodeException {
    var list = Codec.LIST.LONG.decode(node);
    return new UUID(list.get(0), list.get(1));
  }

  @Override
  public DataNode encode(UUID value) {
    DataArray a = new ArrayDataNode();
    a.add(Codec.LONG.encode(value.getMostSignificantBits()));
    a.add(Codec.LONG.encode(value.getLeastSignificantBits()));
    return a;
  }
}
