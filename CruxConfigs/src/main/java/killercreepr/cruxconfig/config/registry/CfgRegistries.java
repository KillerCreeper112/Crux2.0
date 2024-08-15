package killercreepr.cruxconfig.config.registry;

import killercreepr.cruxconfig.config.common.json.registry.JsonRegistry;
import killercreepr.cruxconfig.config.common.json.registry.SimpleJsonRegistry;
import killercreepr.cruxconfig.config.common.json.registry.TaggedJsonRegistry;
import killercreepr.cruxconfig.config.common.yaml.registry.SimpleYamlRegistry;
import killercreepr.cruxconfig.config.common.yaml.registry.YamlRegistry;

public class CfgRegistries {
    public static final TaggedJsonRegistry JSON_TAGGED = new TaggedJsonRegistry();
    public static final JsonRegistry JSON = new SimpleJsonRegistry();
    public static final YamlRegistry YAML = new SimpleYamlRegistry();
}
