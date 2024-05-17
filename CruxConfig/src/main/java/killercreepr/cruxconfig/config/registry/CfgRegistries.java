package killercreepr.cruxconfig.config.registry;

import killercreepr.cruxconfig.config.common.json.JsonRegistry;
import killercreepr.cruxconfig.config.common.yaml.registry.YamlRegistry;

public class CfgRegistries {
    public static final JsonRegistry JSON = new JsonRegistry();
    public static final YamlRegistry YAML = new YamlRegistry();
}
