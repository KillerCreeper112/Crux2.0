package killercreepr.cruxconfig.config.common.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Config {
    /**
     * @return If true, the config will add any values that are not present within the config.
     * Normally not recommended since it does not provide full control over what is in the config for the user.
     */
    boolean autoUpdate() default false;
}
