package killercreepr.cruxconfig.config.common.yaml.annotation;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface YamlSerializer {
    @NotNull String id();
}
