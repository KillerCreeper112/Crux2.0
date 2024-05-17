package killercreepr.cruxconfig.config.common.yaml.element;

public class YamlPrimitive extends YamlGeneric{
    public YamlPrimitive(String value){
        super(value);
    }
    public YamlPrimitive(Boolean value){
        super(value);
    }
    public YamlPrimitive(Number value){
        super(value);
    }

}
