package killercreepr.crux.tags.standard.minimessage;

import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConvertBoolTag implements TagResolver {
    @Override
    public @Nullable Tag resolve(@NotNull String name, @NotNull ArgumentQueue arguments, @NotNull Context ctx) throws ParsingException {
        if(!has(name)) return null;
        String[] args = arguments.pop().value().split(",");
        int amountTrue = 0;
        for(String s : args){
            if(Boolean.parseBoolean(s)) amountTrue++;
        }
        boolean value = amountTrue >= args.length;
        return Tag.preProcessParsed(value ? "1" : "0");
    }

    @Override
    public boolean has(@NotNull String name) {
        return name.equalsIgnoreCase("convert_bool");
    }
}
