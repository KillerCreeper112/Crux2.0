package killercreepr.crux.tags.standard.minimessage;

import killercreepr.crux.util.CruxString;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CheckBoolTag implements TagResolver {
    /*protected final @NotNull TextParserContext context;
    public CheckBoolTag(@NotNull TextParserContext context) {
        this.context = context;
    }*/

    @Override
    public @Nullable Tag resolve(@NotNull String name, @NotNull ArgumentQueue arguments, @NotNull Context ctx) throws ParsingException {
        if(!has(name)) return null;
        String[] args = arguments.pop().value().split(",");
        int amountTrue = 0;
        for(String s : args){
            //s = context.parseString(s);
            if(CruxString.parseBoolean(s)) amountTrue++;
        }
        boolean value = amountTrue >= args.length;
        if(!value) arguments.pop();
        String text = arguments.pop().value();
        return Tag.preProcessParsed(text);
    }

    @Override
    public boolean has(@NotNull String name) {
        return name.equalsIgnoreCase("check_bool");
    }
}
