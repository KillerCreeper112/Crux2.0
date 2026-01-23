package killercreepr.cruxconfig.config.common.yaml.element;

public class YamlNull extends YamlElement {
    public static final YamlNull INSTANCE = new YamlNull();

    @Deprecated(since = "See YamlNull#INSTANCE")
    public YamlNull() {
    }

    @Override
    public boolean isNull() {
        return true;
    }
}
