package killercreepr.cruxconfig.config.registry;

import killercreepr.cruxconfig.config.common.base.BaseFileRegistry;
import killercreepr.cruxconfig.config.common.json.SimpleJsonRegistry;
import killercreepr.cruxconfig.config.common.yaml.registry.SimpleYamlRegistry;

public class CfgRegistries {
    public static final SimpleJsonRegistry JSON = new SimpleJsonRegistry();
    public static final SimpleYamlRegistry YAML = new SimpleYamlRegistry();
    public static final BaseFileRegistry FILE = new BaseFileRegistry();
}
