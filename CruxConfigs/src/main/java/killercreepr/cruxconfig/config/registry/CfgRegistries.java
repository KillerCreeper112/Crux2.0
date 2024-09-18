package killercreepr.cruxconfig.config.registry;

import killercreepr.crux.registry.KeyedRegistry;
import killercreepr.crux.registry.SimpleKeyedRegistry;
import killercreepr.crux.registry.SimpleRegistry;
import killercreepr.cruxconfig.config.bukkit.handler.impl.item.component.persistence.FileDynamicPersistTagParser;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.json.registry.JsonRegistry;
import killercreepr.cruxconfig.config.common.json.registry.SimpleJsonRegistry;
import killercreepr.cruxconfig.config.common.json.registry.TaggedJsonRegistry;
import killercreepr.cruxconfig.config.common.yaml.registry.SimpleYamlRegistry;
import killercreepr.cruxconfig.config.common.yaml.registry.YamlRegistry;

public class CfgRegistries {
    public static final SimpleRegistry<FileRegistry> FILE = SimpleRegistry.fromSet();
    public static final SimpleRegistry<JsonRegistry> JSON_REGISTRY = SimpleRegistry.fromSet();
    public static final SimpleRegistry<YamlRegistry> YAML_REGISTRY = SimpleRegistry.fromSet();

    /**
     * These file registries are for registries that require the specific
     * object type to be inputted when deserializing.
     */
    public static final SimpleRegistry<FileRegistry> SIMPLE_REGISTRY = SimpleRegistry.fromSet();

    public static final JsonRegistry JSON = JSON_REGISTRY.register(SIMPLE_REGISTRY.register(FILE.register(new SimpleJsonRegistry())));
    public static final YamlRegistry YAML = YAML_REGISTRY.register(SIMPLE_REGISTRY.register(FILE.register(new SimpleYamlRegistry())));

    public static final TaggedJsonRegistry JSON_TAGGED = JSON_REGISTRY.register(FILE.register(new TaggedJsonRegistry()));

    public static final KeyedRegistry<FileDynamicPersistTagParser<?>> DYNAMIC_PERSIST_TAG_PARSER = new SimpleKeyedRegistry<>();
}
