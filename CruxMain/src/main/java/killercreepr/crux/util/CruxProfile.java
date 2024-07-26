package killercreepr.crux.util;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Base64;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CruxProfile {
    public static final Pattern BASE64_PATTERN = Pattern.compile("^[a-zA-Z0-9+/]*={0,2}$");
    public static @NotNull PlayerProfile createProfileFromBase64(@NotNull String base64String){
        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        profile.setProperty(new ProfileProperty("textures", base64String));
        return profile;
    }

    public static @NotNull ItemStack createSkullItemFromBase64(@NotNull String base64String){
        return editSkullItemFromBase64(base64String, new ItemStack(Material.PLAYER_HEAD));
    }

    public static @NotNull ItemStack editSkullItemFromBase64(@NotNull String base64String, @NotNull ItemStack item){
        item.editMeta(SkullMeta.class, meta ->{
            PlayerProfile profile = createProfileFromBase64(base64String);
            meta.setPlayerProfile(profile);
        });
        return item;
    }

    /**
     * Checks if a string is a valid Base64 encoded string.
     *
     * @param value The string to check.
     * @return true if the string is valid Base64; false otherwise.
     */
    public static boolean isBase64(@Nullable String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }

        // Check if the string matches the Base64 character set pattern
        Matcher matcher = BASE64_PATTERN.matcher(value);
        if (!matcher.matches()) {
            return false;
        }

        try {
            // Attempt to decode the string
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] decodedBytes = decoder.decode(value);

            // Check if decoded bytes can be properly re-encoded to match the original string
            String reEncodedString = Base64.getEncoder().encodeToString(decodedBytes);
            return reEncodedString.equals(value);
        } catch (IllegalArgumentException e) {
            // If decoding fails, it's not a valid Base64 encoded string
            return false;
        }
    }

}
