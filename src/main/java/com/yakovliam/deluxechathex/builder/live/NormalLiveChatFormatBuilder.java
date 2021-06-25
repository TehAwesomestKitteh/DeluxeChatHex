package com.yakovliam.deluxechathex.builder.live;

import com.yakovliam.deluxechathex.DeluxeChatHex;
import com.yakovliam.deluxechathex.builder.Builder;
import com.yakovliam.deluxechathex.model.formatting.Extra;
import com.yakovliam.deluxechathex.model.formatting.Format;
import com.yakovliam.deluxechathex.model.formatting.FormatPart;
import com.yakovliam.deluxechathex.replacer.AmpersandReplacer;
import com.yakovliam.deluxechathex.replacer.SectionReplacer;
import com.yakovliam.deluxechathex.util.Triple;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.LinearComponents;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.regex.Pattern;

public class NormalLiveChatFormatBuilder extends LiveChatFormatBuilder implements Builder<Triple<Player, String, Format>, Component> {

    /**
     * Ampersand replacer
     */
    private static final AmpersandReplacer AMPERSAND_REPLACER = new AmpersandReplacer();

    /**
     * Section replacer
     */
    private static final SectionReplacer SECTION_REPLACER = new SectionReplacer();

    /**
     * Old hex pattern
     */
    private static final Pattern OLD_HEX_PATTERN = Pattern.compile("#[0-9A-Fa-f]{6}");

    /**
     * New hex pattern
     */
    private static final String NEW_HEX_PATTERN = "&$0";

    public NormalLiveChatFormatBuilder(DeluxeChatHex plugin) {
        super(plugin);
    }

    /**
     * Builds an array of baseComponents from a message, player, and format
     *
     * @param input The trio of inputs
     * @return The array of baseComponents
     */
    @Override
    public Component build(Triple<Player, String, Format> input) {
        // get input parameters
        Player player = input.getLeft();
        String messageString = input.getCenter();
        Format format = input.getRight();

        // create component builder for message
        List<Component> componentBuilder = new ArrayList<>();

        // loop through format parts
        for (FormatPart formatPart : format.getFormatParts()) {
            String text = formatPart.getText();

            // basically what I am doing here is converting & -> section, then replacing placeholders, then section -> &
            // this just bypasses PAPI's hacky way of coloring text which shouldn't even be implemented...
            text = SECTION_REPLACER.apply(PlaceholderAPI.setPlaceholders(player, AMPERSAND_REPLACER.apply(text, player)), player);

            // replace hex #123456 with &#123456
            text = text.replaceAll(OLD_HEX_PATTERN.pattern(), NEW_HEX_PATTERN);

            // build text from legacy (and replace <chat_message> with the actual message)
            // and check permissions for chat colors
            Component parsedText = LegacyComponentSerializer.legacyAmpersand().deserialize(text);

            /* Retaining events for MULTIPLE components */

            // parse extra (if applicable)
            if (formatPart.getExtra() != null) {
                Extra extra = formatPart.getExtra();

                // if contains click action
                if (extra.getClickAction() != null) {
                    // apply
                    parsedText = parsedText.clickEvent(extra.getClickAction().toClickEvent(player));
                }

                // if contains hover action
                if (extra.getHoverAction() != null) {
                    // apply
                    parsedText = parsedText.hoverEvent(extra.getHoverAction().toHoverEvent(player));
                }
            }

            componentBuilder.add(parsedText);
        }

        // replace hex #123456 with &#123456
        // this adds the chat message on the end of the component builder
        messageString = messageString.replaceAll(OLD_HEX_PATTERN.pattern(), NEW_HEX_PATTERN);

        LegacyComponentSerializer.Builder legacyComponentSerializerBuilder = LegacyComponentSerializer.builder();

        if (player.hasPermission("deluxechat.url")) {
            legacyComponentSerializerBuilder.extractUrls();
        }
        if (player.hasPermission("deluxechat.color")) {
            legacyComponentSerializerBuilder.character('&');
            legacyComponentSerializerBuilder.hexColors();
        }
        if (!player.hasPermission("deluxechat.formatting")) {
            legacyComponentSerializerBuilder.flattener(ComponentFlattener.basic());
        }

        componentBuilder.add(legacyComponentSerializerBuilder.build()
                .deserialize(messageString));

        List<Component> finalComponentBuilder = mergeStylingLegacy(componentBuilder);

        // return built component builder
        return LinearComponents.linear(finalComponentBuilder.toArray(Component[]::new));
    }

    /**
     * Apply merge styling, legacy
     *
     * @param componentBuilder component builder
     * @return final component builder
     */
    private List<Component> mergeStylingLegacy(List<Component> componentBuilder) {
        List<Component> finalComponentBuilder = new ArrayList<>();
        // for each component in the component builder list, apply the style of the previous to the next (if absent)
        Component previous = null;
        for (Component current : componentBuilder) {
            if (previous == null) {
                finalComponentBuilder.add(current);
                previous = current;
                continue;
            }

            if (!current.hasStyling() || current.color() == null) {
                // current = current.style(previous.style());
                current = current.mergeStyle(previous, Style.Merge.COLOR, Style.Merge.DECORATIONS);
            }

            // if (current.color() == null) {
            //     current = current.colorIfAbsent(previous.color());
            //     for (Map.Entry<TextDecoration, TextDecoration.State> textDecorationStateEntry : previous.decorations().entrySet()) {
            //         if (!current.hasDecoration(textDecorationStateEntry.getKey())) {
            //             current = current.decoration(textDecorationStateEntry.getKey(), textDecorationStateEntry.getValue());
            //         }
            //     }
            // }

            finalComponentBuilder.add(current);

            previous = current;
        }

        return finalComponentBuilder;
    }
}
