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
}
