package killercreepr.cruxstructures.core.structure.component;

import killercreepr.cruxstructures.api.component.BlockManipulatorComponent;

public class SimpleBlockManipulatorComponent implements BlockManipulatorComponent {
    protected final boolean disableBlockBreak;
    protected final boolean disableBlockPlace;

    public SimpleBlockManipulatorComponent(boolean disableBlockBreak, boolean disableBlockPlace) {
        this.disableBlockBreak = disableBlockBreak;
        this.disableBlockPlace = disableBlockPlace;
    }

    @Override
    public boolean disableBlockBreak() {
        return disableBlockBreak;
    }

    @Override
    public boolean disableBlockPlace() {
        return disableBlockPlace;
    }
}
