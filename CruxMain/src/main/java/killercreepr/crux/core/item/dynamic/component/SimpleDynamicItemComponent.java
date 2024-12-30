package killercreepr.crux.core.item.dynamic.component;

import killercreepr.crux.api.item.dynamic.DynamicItemComponent;

public abstract class SimpleDynamicItemComponent implements DynamicItemComponent {
    @Override
    public DynamicItemComponent clone() {
        try{
            return (DynamicItemComponent) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
