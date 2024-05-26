package killercreepr.crux.tags;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class FormatArgs {
    public static @NotNull FormatArgs empty(){
        return new FormatArgs(new String[0]);
    }

    protected final @NotNull String[] args;
    public FormatArgs(@NotNull String[] args) {
        this.args = args;
    }

    public @NotNull String[] getArgs() {
        return args;
    }

    public String getOrDefault(int index, @Nullable String defaultValue){
        String v = getSafely(index);
        return v==null?defaultValue:v;
    }

    public @Nullable String getSafely(int index){
        return index < 0 || index >= args.length ? null : args[index];
    }

    public boolean has(int atIndex){
        return atIndex >= 0 && atIndex < args.length;
    }

    public @NotNull String get(int index){
        return args[index];
    }

    public @NotNull Optional<String> getOptional(int index){
        return Optional.ofNullable(getSafely(index));
    }
}
