package com.yakovliam.deluxechathex.converter.deluxeformat;

import com.yakovliam.deluxechathex.converter.Converter;
import com.yakovliam.deluxechathex.model.formatting.ChatFormat;
import com.yakovliam.deluxechathex.model.formatting.Format;
import com.yakovliam.deluxechathex.model.formatting.FormatPart;
import com.yakovliam.deluxechathex.util.Triple;
import me.clip.deluxechat.objects.DeluxeFormat;

import java.util.Arrays;
import java.util.LinkedList;

public class DeluxeFormatConverter implements Converter<DeluxeFormat, ChatFormat> {

    /**
     * Converts a k into a v
     *
     * @param deluxeFormat k
     * @return v
     */
    @Override
    public ChatFormat convert(DeluxeFormat deluxeFormat) {
        // channel
        FormatPart channel = new ChannelConverter().convert(new Triple<>(deluxeFormat.getChannel(),
                deluxeFormat.getChannelTooltip(),
                deluxeFormat.getChannelClickCommand()));
        // prefix
        FormatPart prefix = new PrefixConverter().convert(new Triple<>(deluxeFormat.getPrefix(),
                deluxeFormat.getPrefixTooltip(),
                deluxeFormat.getPreClickCmd()));
        // name color
        FormatPart nameColor = new FormatPart(deluxeFormat.getNameColor(), null);
        // name
        FormatPart name = new PrefixConverter().convert(new Triple<>(deluxeFormat.getName(),
                deluxeFormat.getNameTooltip(),
                deluxeFormat.getNameClickCmd()));
        // suffix
        FormatPart suffix = new PrefixConverter().convert(new Triple<>(deluxeFormat.getSuffix(),
                deluxeFormat.getSuffixTooltip(),
                deluxeFormat.getSuffixClickCmd()));
        // chat color
        FormatPart chatColor = new FormatPart(deluxeFormat.getChatColor(), null);

        LinkedList<FormatPart> formatParts = new LinkedList<>();

        formatParts.add(channel);
        formatParts.add(prefix);
        formatParts.add(nameColor);
        formatParts.add(name);
        formatParts.add(suffix);
        formatParts.add(chatColor);

        // compile into one chat format
        return new ChatFormat(deluxeFormat.getIdentifier(),
                deluxeFormat.getIndex()
                , "G7bRYIbhUg",
                new Format(formatParts));
    }
}
