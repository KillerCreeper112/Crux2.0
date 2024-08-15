package killercreepr.cruxconfig.config.registry;

import killercreepr.cruxconfig.config.common.base.BaseFileRegistry;
import killercreepr.cruxconfig.config.common.json.JsonRegistry;
import killercreepr.cruxconfig.config.common.json.SimpleJsonRegistry;
import killercreepr.cruxconfig.config.common.yaml.registry.SimpleYamlRegistry;
import killercreepr.cruxconfig.config.common.yaml.registry.YamlRegistry;

public class CfgRegistries {
    public static final JsonRegistry JSON = new SimpleJsonRegistry();
    public static final YamlRegistry YAML = new SimpleYamlRegistry();
    public static final BaseFileRegistry FILE = new BaseFileRegistry();
}
