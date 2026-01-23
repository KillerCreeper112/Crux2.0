package killercreepr.crux.core.codec.node;

import killercreepr.crux.api.codec.node.DataArray;
import killercreepr.crux.api.codec.node.DataNode;
import killercreepr.crux.api.codec.node.DataObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class ObjectDataNode implements DataObject {
  protected final Map<String, DataNode> value;

  public ObjectDataNode() {
    value = new HashMap<>();
  }

  public ObjectDataNode(Map<String, DataNode> value) {
    this.value = value;
  }

  @Override
  public boolean isObjectData() {
    return true;
  }

  @Override
  public boolean isArrayData() {
    return false;
  }

  @Override
  public boolean isString() {
    return false;
  }

  @Override
  public boolean isNumber() {
    return false;
  }

  @Override
  public boolean isBoolean() {
    return false;
  }

  @Override
  public boolean isNull() {
    return false;
  }

  @Override
  public DataObject asObjectData() {
    return this;
  }

  @Override
  public DataArray asArrayData() {
    throw new IllegalStateException("Cannot convert to DataArray");
  }

  @Override
  public String asString() {
    throw new IllegalStateException("Cannot convert to String");
  }

  @Override
  public Number asNumber() {
    throw new IllegalStateException("Cannot convert to Number");
  }

  @Override
  public boolean asBoolean() {
    throw new IllegalStateException("Cannot convert to Boolean");
  }

  @Override
  public DataNode get(String key) {
    return value.get(key);
  }

  @Override
  public void put(String key, DataNode value) {
    this.value.put(key, value == null ? NullDataNode.INSTANCE : value);
  }

  @Override
  public void forEachDataPair(BiConsumer<String, DataNode> consumer) {
    value.forEach(consumer);
  }
}
