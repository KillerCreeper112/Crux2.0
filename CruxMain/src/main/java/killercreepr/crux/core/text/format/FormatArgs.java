package killercreepr.crux.core.text.format;

import killercreepr.crux.core.util.CruxMath;
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

    public int size(){
        return args.length;
    }

    public @Nullable String getSafely(int index){
        return index < 0 || index >= args.length ? null : args[index];
    }

    public boolean has(int atIndex){
        return atIndex >= 0 && atIndex < args.length;
    }
    public boolean isEmpty(){
        return args.length == 0;
    }

    public @NotNull String get(int index){
        return args[index];
    }

    public @NotNull Optional<String> getOptional(int index){
        return Optional.ofNullable(getSafely(index));
    }

    public double parseDouble(int index){
        return CruxMath.evaluate(get(index));
    }

    public int parseInt(int index){
        return (int) parseDouble(index);
    }

    public float parseFloat(int index){
        return (float) parseDouble(index);
    }

    public double parseDoubleOr(int index, double defaultValue){
        try{
            return Double.parseDouble(get(index));
        }catch (IllegalArgumentException | IndexOutOfBoundsException ignored){ return defaultValue; }
    }

    public int parseIntOr(int index, int defaultValue){
        return (int) parseDoubleOr(index, defaultValue);
    }

    public float parseFloatOr(int index, float defaultValue){
        return (float) parseDoubleOr(index, defaultValue);
    }
}
