package killercreepr.cruxentities.modelengine;

import com.ticxo.modelengine.api.events.ModelRegistrationEvent;
import killercreepr.crux.plugin.CruxPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ModelEngineHook implements Listener {
    private static ModelGenPhase PHASE = ModelGenPhase.PRE_IMPORT;
    public static ModelGenPhase phase(){
        return PHASE;
    }

    public static void register(CruxPlugin plugin){
        plugin.registerListeners(new ModelEngineHook());
    }

    @EventHandler(ignoreCancelled = true)
    public void onModelRegistration(ModelRegistrationEvent event) {
        try{
            ModelGenPhase phase = ModelGenPhase.valueOf(event.getPhase().toString().toUpperCase());
            PHASE = phase;
        }catch (IllegalArgumentException ignored){}
    }

}
