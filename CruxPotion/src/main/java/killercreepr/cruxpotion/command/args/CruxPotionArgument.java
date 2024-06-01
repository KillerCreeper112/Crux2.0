package killercreepr.cruxpotion.command.args;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import killercreepr.cruxpotion.potions.CruxPotion;
import killercreepr.cruxpotion.registries.CruxPotionRegistries;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class CruxPotionArgument implements CustomArgumentType<CruxPotion, Key> {
    private static final CruxPotionArgument CRUX_POTION = new CruxPotionArgument();
    public static CruxPotionArgument cruxPotion(){
        return CRUX_POTION;
    }

    @Override
    public @NotNull CruxPotion parse(@NotNull StringReader reader) {
        Key key = Key.key(reader.getString());
        return CruxPotionRegistries.POTIONS.get(key);
    }

    @Override
    public @NotNull ArgumentType<Key> getNativeType() {
        return ArgumentTypes.key();
    }
}
