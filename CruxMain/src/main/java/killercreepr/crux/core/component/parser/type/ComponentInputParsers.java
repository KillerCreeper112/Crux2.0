package killercreepr.crux.core.component.parser.type;

import killercreepr.crux.api.component.parser.InputDecodeContext;
import killercreepr.crux.api.component.parser.hybrid.PersistInputParser;
import killercreepr.crux.api.component.parser.hybrid.PersistTextInputParser;
import killercreepr.crux.api.component.parser.hybrid.TextInputResultParser;
import killercreepr.crux.api.item.component.ToolComponent;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.component.parser.hybrid.text.MapPersistTextInputParser;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ComponentInputParsers {
    public static PersistTextInputParser<ToolComponent> TOOL = new MapPersistTextInputParser<ToolComponent>(
        Map.of(""),
        ctx -> new ToolComponent.Simple(ctx.getOptional("default_mining_speed", 1f), ctx.getOptional("rules")),
        ToolComponent.class
    );
}
