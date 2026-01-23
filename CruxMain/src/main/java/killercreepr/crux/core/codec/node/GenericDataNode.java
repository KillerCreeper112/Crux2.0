package killercreepr.crux.core.codec.node;

import killercreepr.crux.api.codec.node.DataArray;
import killercreepr.crux.api.codec.node.DataNode;
import killercreepr.crux.api.codec.node.DataObject;

public class GenericDataNode implements DataNode {
  protected final Object value;
  public GenericDataNode(Object value) {
    this.value = value;
  }

  public Object getValue() {
    return value;
  }

  @Override
  public boolean isObjectData() {
    return false;
  }

  @Override
  public boolean isArrayData() {
    return false;
  }

  @Override
  public boolean isString() {
    return value instanceof String;
  }

  @Override
  public boolean isNumber() {
    return value instanceof Number;
  }

  @Override
  public boolean isBoolean() {
    return value instanceof Boolean;
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
    throw new IllegalStateException("Cannot convert to DataArray");
  }

  @Override
  public String asString() {
    return (String) value;
  }

  @Override
  public Number asNumber() {
    return (Number) value;
  }

  @Override
  public boolean asBoolean() {
    return (boolean) value;
  }
}
