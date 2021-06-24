package com.yakovliam.deluxechathex.converter.deluxeformat;

import com.yakovliam.deluxechathex.converter.Converter;
import com.yakovliam.deluxechathex.model.formatting.Extra;
import com.yakovliam.deluxechathex.model.formatting.FormatPart;
import com.yakovliam.deluxechathex.model.formatting.action.ClickAction;
import com.yakovliam.deluxechathex.model.formatting.action.ClickActionType;
import com.yakovliam.deluxechathex.model.formatting.action.HoverAction;
import com.yakovliam.deluxechathex.util.Triple;

import java.util.List;

public class SuffixConverter implements Converter<Triple<String, List<String>, String>, FormatPart> {

    /**
     * Converts a k into a v
     *
     * @param stringStringStringTriple k
     * @return v
     */
    @Override
    public FormatPart convert(Triple<String, List<String>, String> stringStringStringTriple) {
        String suffix = stringStringStringTriple.getLeft();
        List<String> suffixTooltip = stringStringStringTriple.getCenter();
        String suffixClickCmd = stringStringStringTriple.getRight();

        Extra extra = new Extra(suffixClickCmd != null
                ? new ClickAction(ClickActionType.SUGGEST_COMMAND, suffixClickCmd) : null,
                new HoverAction(suffixTooltip));

        return new FormatPart(suffix, extra);
    }
}
