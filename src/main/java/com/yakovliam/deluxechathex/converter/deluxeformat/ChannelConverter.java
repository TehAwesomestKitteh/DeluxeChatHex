package com.yakovliam.deluxechathex.converter.deluxeformat;

import com.yakovliam.deluxechathex.converter.Converter;
import com.yakovliam.deluxechathex.model.formatting.FormatPart;
import com.yakovliam.deluxechathex.model.formatting.action.ClickAction;
import com.yakovliam.deluxechathex.model.formatting.action.ClickActionType;
import com.yakovliam.deluxechathex.model.formatting.action.HoverAction;
import com.yakovliam.deluxechathex.util.Triple;

import java.util.List;

public class ChannelConverter implements Converter<Triple<String, List<String>, String>, FormatPart> {

    /**
     * Converts a k into a v
     *
     * @param stringStringStringTriple k
     * @return v
     */
    @Override
    public FormatPart convert(Triple<String, List<String>, String> stringStringStringTriple) {
        String channel = stringStringStringTriple.getLeft();
        List<String> channelTooltip = stringStringStringTriple.getCenter();
        String channelClickCmd = stringStringStringTriple.getRight();

        com.yakovliam.deluxechathex.model.formatting.Extra extra = new com.yakovliam.deluxechathex.model.formatting.Extra(channelClickCmd != null
                ? new ClickAction(ClickActionType.SUGGEST_COMMAND, channelClickCmd) : null,
                new HoverAction(channelTooltip));

        return new FormatPart(channel, extra);
    }
}
