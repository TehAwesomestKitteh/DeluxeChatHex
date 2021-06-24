package com.yakovliam.deluxechathex.builder.live;

import com.yakovliam.deluxechathex.DeluxeChatHex;
import com.yakovliam.deluxechathex.builder.Builder;
import com.yakovliam.deluxechathex.model.formatting.Extra;
import com.yakovliam.deluxechathex.model.formatting.Format;
import com.yakovliam.deluxechathex.replacer.AmpersandReplacer;
import com.yakovliam.deluxechathex.replacer.SectionReplacer;
import com.yakovliam.deluxechathex.util.Triple;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.examination.string.MultiLineStringExaminer;
import org.bukkit.entity.Player;

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
        final Component[] componentBuilder = {Component.text("")};

        // loop through format parts
        format.getFormatParts().forEach(formatPart -> {
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

            componentBuilder[0] = componentBuilder[0].append(parsedText);
        });

        // replace hex #123456 with &#123456
        // this adds the chat message on the end of the component builder
        messageString = messageString.replaceAll(OLD_HEX_PATTERN.pattern(), NEW_HEX_PATTERN);
        componentBuilder[0] = componentBuilder[0].append(LegacyComponentSerializer.legacyAmpersand().deserialize(messageString));

        // TODO not working, this was a test made by kashike of kyori, and it's being worked on
        componentBuilder[0].examine(MultiLineStringExaminer.simpleEscaping()).forEach(System.out::println);

        // return built component builder
        return componentBuilder[0];
    }
}
