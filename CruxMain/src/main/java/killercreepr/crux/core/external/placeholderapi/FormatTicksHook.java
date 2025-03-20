package killercreepr.crux.core.external.placeholderapi;

import killercreepr.crux.core.text.tags.standard.DateFormatResolver;
import killercreepr.crux.core.text.tags.standard.DurationFormatResolver;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FormatTicksHook extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "cruxtimeticks";
    }

    @Override
    public @NotNull String getAuthor() {
        return "killercreepr";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        try{
            return formatTimestamp(Long.parseLong(params));
        }catch (IllegalArgumentException ignored){}

        long ticks;
        String text = PlaceholderAPI.setPlaceholders(player, "%" + params + "%").replace(",", "");
        try{
            ticks = Long.parseLong(text);
        }catch (IllegalArgumentException ignored){
            return text;
        }
        return formatTimestamp(ticks * 50L);
    }

    public static String formatTimestamp(long timestamp) {
        // Calculate time units
        long seconds = timestamp / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long weeks = days / 7;

        // Remainders for smaller units
        days %= 7;
        hours %= 24;
        minutes %= 60;
        seconds %= 60;

        // Format the string
        StringBuilder sb = new StringBuilder();
        if (weeks > 0) sb.append(weeks).append("w, ");
        if (days > 0) sb.append(days).append("d, ");
        if (hours > 0) sb.append(hours).append("h, ");
        if (minutes > 0) sb.append(minutes).append("m, ");
        if (seconds > 0) sb.append(seconds).append("s");

        // Remove trailing comma if necessary
        String result = sb.toString().trim();
        if (result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
        }

        return result;
    }
}
