package killercreepr.cruxconfig.config.common.yaml.element;

import java.util.Map;

public class YamlElement {
    protected final Map<String, Object> map;
    public YamlElement(Map<String, Object> map) {
        this.map = map;
    }

    public Map<String, Object> map() {
        return map;
    }
}
