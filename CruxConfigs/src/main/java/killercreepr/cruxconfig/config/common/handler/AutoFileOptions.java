package killercreepr.cruxconfig.config.common.handler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class AutoFileOptions {
    public static Builder builder(){
        return new Builder();
    }

    protected final @Nullable BiPredicate<String, Object> isValid;
    protected final @Nullable Predicate<Field> disabledFields;
    protected final @Nullable Map<String, FileConvertHandler<?, ?>> manualHandlers;

    public AutoFileOptions(@Nullable BiPredicate<String, Object> isValid, @Nullable Predicate<Field> disabledFields, @Nullable Map<String, FileConvertHandler<?, ?>> manualHandlers) {
        this.isValid = isValid;
        this.disabledFields = disabledFields;
        this.manualHandlers = manualHandlers;
    }

    public boolean testDisabled(@NotNull Field field){
        return disabledFields != null && disabledFields.test(field);
    }

    public boolean testIsValid(String key, Object value){
        return isValid == null || isValid.test(key, value);
    }

    public FileConvertHandler<?, ?> getManualHandler(@NotNull String id){
        if(manualHandlers==null) return null;
        return manualHandlers.get(id);
    }

    public boolean hasManualHandler(@NotNull String id){
        return manualHandlers != null && manualHandlers.containsKey(id);
    }

    /**
     * @return A predicate that determines whether a Field's object
     * is valid when deserializing. If any of the fields are not valid,
     * the deserializeFromYaml method will return null.
     */
    public @Nullable BiPredicate<String, Object> getIsValid() {
        return isValid;
    }

    /**
     * @return A predicate that determines whether a Field should be
     * deserialized and serialized. If the predicate returns
     * true on a Field, that Field will not be saved or loaded.
     */
    public @Nullable Predicate<Field> getDisabledFields() {
        return disabledFields;
    }

    public @Nullable Map<String, FileConvertHandler<?, ?>> getManualHandlers() {
        return manualHandlers;
    }


    public static final class Builder {
        private @Nullable BiPredicate<String, Object> isValid;
        private @Nullable Predicate<Field> disabledFields;
        private @Nullable Map<String, FileConvertHandler<?, ?>> manualHandlers;

        public Builder isValid(BiPredicate<String, Object> isValid) {
            this.isValid = isValid;
            return this;
        }

        public Builder disabledFields(Predicate<Field> disabledFields) {
            this.disabledFields = disabledFields;
            return this;
        }

        public Builder manualHandlers(Map<String, FileConvertHandler<?, ?>> manualHandlers) {
            this.manualHandlers = manualHandlers;
            return this;
        }

        public AutoFileOptions build() {
            return new AutoFileOptions(isValid, disabledFields, manualHandlers);
        }
    }
}
