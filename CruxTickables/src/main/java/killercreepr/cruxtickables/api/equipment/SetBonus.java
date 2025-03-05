package killercreepr.cruxtickables.api.equipment;

import killercreepr.crux.api.data.CruxKeyed;

public interface SetBonus extends CruxKeyed {
    int getEquipmentAmount();
    default boolean isMain(){
        return getEquipmentAmount() > 0;
    }
}
