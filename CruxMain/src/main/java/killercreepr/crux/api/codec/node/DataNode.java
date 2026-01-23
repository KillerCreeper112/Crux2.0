package killercreepr.crux.api.codec.node;

public interface DataNode {
    boolean isObjectData();
    boolean isArrayData();
    boolean isString();
    boolean isNumber();
    boolean isBoolean();
    boolean isNull();

    DataObject asObjectData();
    DataArray asArrayData();
    String asString();
    Number asNumber();
    boolean asBoolean();
}
