package killercreepr.crux.core.codec.node;

import killercreepr.crux.api.codec.node.DataArray;
import killercreepr.crux.api.codec.node.DataNode;
import killercreepr.crux.api.codec.node.DataObject;

public class NullDataNode implements DataNode {
  public static final NullDataNode INSTANCE = new NullDataNode();

  private NullDataNode() {}

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
    return true;
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
}
