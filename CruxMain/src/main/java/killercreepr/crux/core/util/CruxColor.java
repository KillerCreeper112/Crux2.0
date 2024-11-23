package killercreepr.crux.core.util;

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

        try {
            int red = Integer.parseInt(hex.substring(0, 2), 16);
            int green = Integer.parseInt(hex.substring(2, 4), 16);
            int blue = Integer.parseInt(hex.substring(4, 6), 16);

            // Return the Bukkit color
            return Color.fromRGB(red, green, blue);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex color string. Ensure the hex values are valid: " + hex, e);
        }

        // Parse the hex string into an integer
        /*int rgbValue = Integer.parseInt(hex, 16);

        // Extract individual RGB components
        int red = (rgbValue >> 16) & 0xFF;
        int green = (rgbValue >> 8) & 0xFF;
        int blue = rgbValue & 0xFF;

        // Create a Color object
        return Color.fromRGB(red, green, blue);*/
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
        }catch (IllegalStateException | IndexOutOfBoundsException | IllegalArgumentException ignored){}
        try{
            return DyeColor.valueOf(id.toUpperCase()).getColor();
        }catch (IllegalStateException ignored){ }
        return defaultValue;
    }

    public static Color hsbToBukkitColor(float hue, float saturation, float brightness){
        java.awt.Color cc = hsbToColor(hue, saturation, brightness);
        return Color.fromRGB(cc.getRed(), cc.getGreen(), cc.getBlue());
    }

    public static java.awt.Color hsbToColor(float hue, float saturation, float brightness){

        float h = Math.max( 0, Math.min( 360, hue ) );
        float s = Math.max( 0, Math.min( 100, saturation ) );
        float v = Math.max( 0, Math.min( 100, brightness ) );
        h /= 360;
        s /= 100;
        v /= 100;

        return java.awt.Color.getHSBColor( h, s, v );
    }
}
