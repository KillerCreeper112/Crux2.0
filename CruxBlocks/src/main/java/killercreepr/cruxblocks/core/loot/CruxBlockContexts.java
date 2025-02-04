package killercreepr.cruxblocks.core.loot;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.cruxblocks.api.block.context.BlockContext;
import killercreepr.cruxblocks.api.event.CruxBlockBreakEvent;
import killercreepr.cruxblocks.core.mining.user.EntityMiner;
import org.bukkit.entity.Entity;

public class CruxBlockContexts {
    public static LootContext.Builder builder(CruxBlockBreakEvent event){
        BlockContext ctx = event.getContext();
        Entity entityMiner = ctx.getMiner() instanceof EntityMiner m ? m.getEntity() : null;
        return LootContext.builder()
            .info(DataExchange.builder()
                .putAll(ctx.getBlock(), "block", "block_broken")
                .putAll(event.getBlock(), "crux_block")
                .putAll(entityMiner, "miner", "entity")
                .build());
    }
}
