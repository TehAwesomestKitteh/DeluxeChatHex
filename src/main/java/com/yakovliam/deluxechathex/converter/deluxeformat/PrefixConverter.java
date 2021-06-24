package com.yakovliam.deluxechathex.converter.deluxeformat;

import com.yakovliam.deluxechathex.converter.Converter;
import com.yakovliam.deluxechathex.model.formatting.Extra;
import com.yakovliam.deluxechathex.model.formatting.FormatPart;
import com.yakovliam.deluxechathex.model.formatting.action.ClickAction;
import com.yakovliam.deluxechathex.model.formatting.action.ClickActionType;
import com.yakovliam.deluxechathex.model.formatting.action.HoverAction;
import com.yakovliam.deluxechathex.util.Triple;

import java.util.List;

public class PrefixConverter implements Converter<Triple<String, List<String>, String>, FormatPart> {

    /**
     * Converts a k into a v
     *
     * @param stringStringStringTriple k
     * @return v
     */
    @Override
    public FormatPart convert(Triple<String, List<String>, String> stringStringStringTriple) {
        String prefix = stringStringStringTriple.getLeft();
        List<String> prefixTooltip = stringStringStringTriple.getCenter();
        String prefixClickCmd = stringStringStringTriple.getRight();

        Extra extra = new Extra(prefixClickCmd != null
                ? new ClickAction(ClickActionType.SUGGEST_COMMAND, prefixClickCmd) : null,
                new HoverAction(prefixTooltip));

        return new FormatPart(prefix, extra);
    }
}
