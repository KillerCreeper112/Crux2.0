package killercreepr.crux.util;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxColor {
    public static @NotNull Color hexToColor(@NotNull String hex) {
        // Check if the hex string starts with '#' and remove it
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }

        // Parse the hex string into an integer
        int rgbValue = Integer.parseInt(hex, 16);

        // Extract individual RGB components
        int red = (rgbValue >> 16) & 0xFF;
        int green = (rgbValue >> 8) & 0xFF;
        int blue = rgbValue & 0xFF;

        // Create a Color object
        return Color.fromRGB(red, green, blue);
    }

    public static @NotNull String colorToHex(@NotNull Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

        return String.format("#%02X%02X%02X", r, g, b);
    }

    public static @Nullable Color parseColor(@NotNull String id){
        return parseColor(id, null);
    }

    public static @Nullable Color parseColor(@NotNull String id, @Nullable Color defaultValue){
        try{
            return hexToColor(id);
        }catch (IllegalStateException ignored){}
        try{
            return DyeColor.valueOf(id.toUpperCase()).getColor();
        }catch (IllegalStateException ignored){ }
        return defaultValue;
    }
}
