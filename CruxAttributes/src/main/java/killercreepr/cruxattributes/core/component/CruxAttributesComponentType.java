package killercreepr.cruxattributes.core.component;

import killercreepr.crux.api.component.DataComponentAccessor;
import killercreepr.crux.api.component.DataComponentType;
import killercreepr.crux.api.component.parser.ComponentTextInputParser;
import killercreepr.crux.api.component.parser.hybrid.PersistParser;
import killercreepr.crux.api.component.serialization.ComponentSerializer;
import killercreepr.cruxattributes.api.attribute.CruxAttributeContainer;
import org.jetbrains.annotations.Nullable;

public class CruxAttributesComponentType extends DataComponentType.Simple<CruxAttributeContainer> implements DataComponentType.Notify<CruxAttributeContainer> {
    public CruxAttributesComponentType(@Nullable ComponentSerializer<?, CruxAttributeContainer> serializer,
                                       @Nullable ComponentTextInputParser<CruxAttributeContainer> textParser) {
        super(serializer, textParser);
    }

    public CruxAttributesComponentType(PersistParser<CruxAttributeContainer> parser) {
        super(parser, parser);
    }

    @Override
    public void onComponentApplied(DataComponentAccessor holder, CruxAttributeContainer value, CruxAttributeContainer previousValue) {

    }

    @Override
    public void onComponentRemoved(DataComponentAccessor holder, CruxAttributeContainer previousValue) {

    }

    /*@Override
    public void onComponentApplied(DataComponentAccessor holder,
                                   CruxAttributeContainer value, CruxAttributeContainer previousValue) {
        if(!(holder instanceof PersistHolderComponentHandler handler)) return;
        PersistentDataContainer componentsContainer = handler.getComponentsPersistentContainer();
        if(componentsContainer == null) return;
        value.getAttributeInstances().forEach(i ->{
            i.getModifiers().forEach(mod ->{
                CruxAttribute.addModifier(componentsContainer, i.getAttribute(), mod);
            });
        });
        handler.onComponentsPersistentContainerChanged(componentsContainer);
    }

    @Override
    public void onComponentRemoved(DataComponentAccessor holder,
                                   CruxAttributeContainer previousValue) {
        if(!(holder instanceof PersistHolderComponentHandler handler)) return;
        PersistentDataContainer componentsContainer = handler.getComponentsPersistentContainer();
        if(componentsContainer == null) return;
        CruxTag.remove(componentsContainer, "attributes");
        handler.onComponentsPersistentContainerChanged(componentsContainer);
    }*/
}
