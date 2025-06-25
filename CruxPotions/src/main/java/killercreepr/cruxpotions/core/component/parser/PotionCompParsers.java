package killercreepr.cruxpotions.core.component.parser;

import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import killercreepr.crux.core.component.parser.type.ComponentInputParsers;
import killercreepr.cruxpotions.api.potion.StoredPotion;
import killercreepr.cruxpotions.core.component.impl.ConsumableCruxPotions;
import killercreepr.cruxpotions.core.registries.CruxPotionRegistries;

import java.util.List;

public class PotionCompParsers {
    public static PersistTextParser<StoredPotion> STORED_CRUX_POTION = PersistTextParser.mapBuilder(StoredPotion.class)
        .field("potion", TextInputField.field(PersistTextParser.KEY, pot -> pot.getPotion().key()))
        .field("duration", TextInputField.field(PersistTextParser.INTEGER, StoredPotion::getDuration))
        .field("amplifier", TextInputField.field(PersistTextParser.INTEGER, StoredPotion::getAmplifier))
        .apply(ctx ->{
            return StoredPotion.storedPotion(
                CruxPotionRegistries.POTION.get(ctx.get("potion")),
                ctx.get("duration"), ctx.getOptional("amplifier", 0)
            );
        });

    public static PersistTextParser<List<StoredPotion>> LIST_STORED_CRUX_POTION = PersistTextParser.list(STORED_CRUX_POTION);



    public static final PersistTextParser<ConsumableCruxPotions> CONSUMABLE_CRUX_POTIONS = PersistTextParser.mapBuilder(ConsumableCruxPotions.class)
        .field("crux_potions", TextInputField.field(PotionCompParsers.LIST_STORED_CRUX_POTION, ConsumableCruxPotions::getCruxPotions))
        .apply(ctx -> new ConsumableCruxPotions(
            ctx.getOptional("crux_potions")
        ));
}
