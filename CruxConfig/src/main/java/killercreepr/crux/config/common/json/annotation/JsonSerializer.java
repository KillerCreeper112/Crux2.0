package killercreepr.crux.config.common.json.annotation;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface JsonSerializer {
    @NotNull String id();
}
