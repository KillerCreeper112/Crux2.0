package killercreepr.crux.core.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class CruxNumberFormat {
    protected final @NotNull NumberFormat pattern;
    public CruxNumberFormat() {
        this(0, ",");
    }

    public CruxNumberFormat(int decimalPlaces) {
        this(decimalPlaces, ",");
    }

    public CruxNumberFormat(int decimalPlaces, @Nullable String comma) {
        this(new DecimalFormat((comma == null ? "####" : "#" + comma + "###") + (decimalPlaces < 1 ? "" : ("." + "#".repeat(decimalPlaces)))));
    }

    public CruxNumberFormat(@NotNull DecimalFormat pattern) {
        this.pattern = pattern;
    }

    public @NotNull NumberFormat getPattern() {
        return pattern;
    }

    public @NotNull String format(double value){
        return pattern.format(value);
    }

    public @NotNull String format(float value){
        return pattern.format(value);
    }

    public @NotNull String format(long value){
        return pattern.format(value);
    }

    public @NotNull String format(int value){
        return pattern.format(value);
    }
    public @NotNull String format(@NotNull Number value){
        return pattern.format(value);
    }
}
