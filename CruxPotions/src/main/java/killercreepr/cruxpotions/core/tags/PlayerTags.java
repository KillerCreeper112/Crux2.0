package killercreepr.cruxpotions.core.tags;

import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.text.resolver.Tag;
import killercreepr.crux.core.util.CruxColor;
import killercreepr.cruxpotions.api.entity.PotionHolder;
import killercreepr.cruxpotions.api.potion.ActivePotion;
import killercreepr.cruxpotions.core.entity.memory.SimplePotionHolder;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerTags implements ObjectTag<Player> {
    @Override
    public @NotNull Class<Player> getObjectType() {
        return Player.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("player_");
    }

    /**
     * @param object
     * @param tags
     * @return Requests these object's string hooks.
     */
    @Override
    public @Nullable TagContainer<StringResolver> requestStrings(@NotNull Player object, @NotNull TagParser tags) {
        return TagContainer.string(tags)
            .add(Tag.string("has_crux_potion", (args, ctx) ->{
                PotionHolder holder = EntityMemory.getDataHolder(object, SimplePotionHolder.class);
                if(holder == null) return "false";
                if(!args.has(0)){
                    return !holder.getActiveEffects().isEmpty() + "";
                }
                Key type = Crux.key(ctx.deserializeString(args.get(0)));
                return holder.hasPotion(type) + "";
            }))
            .add(Tag.string("crux_potions_tab", (args, ctx) ->{
                PotionHolder holder = EntityMemory.getDataHolder(object, SimplePotionHolder.class);
                if(holder == null || holder.getActiveEffects().isEmpty()) return "";
                int index = 0;
                StringBuilder builder = new StringBuilder();
                for(ActivePotion a : holder.getActiveEffects()){
                    index++;
                    String hex = (a.getPotion().getColor() == null ? "yellow" : CruxColor.colorToHex(a.getPotion().getColor()));
                    String pot = "<" + hex + ">" + ActivePotion.formatPotion(a.getPotion(), a.getAmplifier(), a.getDuration());
                    builder.append(pot);
                    if(index < holder.getActiveEffects().size()){
                        if(index % 3 == 0) builder.append("<newline>");
                        else builder.append(" <dark_gray>| ");
                    }
                }
                return builder.toString();
            }))
            ;
    }
    //public @NotNull Component build() {
    //        if(getActiveEffects().isEmpty()) return Component.empty();
    //        int index = 0;
    //        Component c = Component.empty();
    //        for(ActivePotion a : getActiveEffects()){
    //            index++;
    //            Component pot = a.format();
    //            if(pot == null) continue;
    //            c = c.append(pot);
    //            if(index < getActiveEffects().size()){
    //                if(index % 3 == 0) c = c.append(Component.newline());
    //                else c = c.append(Component.text(" | ", NamedTextColor.DARK_GRAY));
    //            }
    //        }
    //        return c;
    //    }
}
