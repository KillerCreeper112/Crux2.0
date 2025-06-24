package killercreepr.cruxentities.damage.type;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import killercreepr.crux.core.Crux;
import org.bukkit.damage.DamageType;

public class CruxEntityDamageTypes {
    public static final DamageType ENTITY_ATTACK_CALCULATE_CUSTOM = RegistryAccess.registryAccess()
        .getRegistry(RegistryKey.DAMAGE_TYPE).get(Crux.key("entity_attack_calculate_custom"));
}
