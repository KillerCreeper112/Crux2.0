package killercreepr.crux.core.codec.node;

import killercreepr.crux.api.codec.node.DataArray;
import killercreepr.crux.api.codec.node.DataNode;
import killercreepr.crux.api.codec.node.DataObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ArrayDataNode implements DataArray {
  protected final List<DataNode> value;

  public ArrayDataNode(List<DataNode> value) {
    this.value = value;
  }
  public ArrayDataNode() {
    this.value = new ArrayList<>();
  }


  @Override
  public boolean isObjectData() {
    return false;
  }

  @Override
  public boolean isArrayData() {
    return true;
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
    throw new IllegalStateException("Cannot convert to DataObject");
  }

  @Override
  public DataArray asArrayData() {
    return this;
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
  public DataNode get(int index) {
    return value.get(index);
  }

  @Override
  public void add(DataNode value) {
    this.value.add(value);
  }

  @Override
  public int size() {
    return value.size();
  }

  @Override
  public void forEachDataNode(Consumer<DataNode> consumer) {
    value.forEach(consumer);
  }
}
