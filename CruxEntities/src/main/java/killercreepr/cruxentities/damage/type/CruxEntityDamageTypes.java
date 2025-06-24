package killercreepr.cruxentities.damage.type;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import killercreepr.crux.core.Crux;
import killercreepr.cruxentities.registries.CruxEntityRegistries;
import org.bukkit.damage.DamageType;

public class CruxEntityDamageTypes {
    public static final DamageType ENTITY_ATTACK_CALCULATE_CUSTOM = RegistryAccess.registryAccess()
        .getRegistry(RegistryKey.DAMAGE_TYPE).get(Crux.key("entity_attack_calculate_custom"));
    public static final DamageType ENTITY_PROJECTILE_CALCULATE_CUSTOM = RegistryAccess.registryAccess()
        .getRegistry(RegistryKey.DAMAGE_TYPE).get(Crux.key("entity_projectile_calculate_custom"));

    public static void register(){
        CruxEntityRegistries.DAMAGE_TYPE_CALCULATE_CUSTOM.register(ENTITY_ATTACK_CALCULATE_CUSTOM);
        CruxEntityRegistries.DAMAGE_TYPE_CALCULATE_CUSTOM.register(ENTITY_PROJECTILE_CALCULATE_CUSTOM);
    }
}
