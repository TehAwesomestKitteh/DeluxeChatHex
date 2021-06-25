package com.yakovliam.deluxechathex.model.formatting.action;

import com.google.common.base.Joiner;
import com.yakovliam.deluxechathex.replacer.AmpersandReplacer;
import com.yakovliam.deluxechathex.replacer.SectionReplacer;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.regex.Pattern;

public class HoverAction {

    /**
     * Old hex pattern
     */
    private static final Pattern OLD_HEX_PATTERN = Pattern.compile("#[0-9A-Fa-f]{6}");

    /**
     * New hex pattern
     */
    private static final String NEW_HEX_PATTERN = "&$0";

    /**
     * Ampersand replacer
     */
    private static final AmpersandReplacer AMPERSAND_REPLACER = new AmpersandReplacer();

    /**
     * Section replacer
     */
    private static final SectionReplacer SECTION_REPLACER = new SectionReplacer();

    /**
     * The list of lines in the
     * hover action
     */
    private List<String> lines;

    /**
     * Construct hover action
     *
     * @param lines lines
     */
    public HoverAction(List<String> lines) {
        this.lines = lines;
    }

    /**
     * Converts the hover action to a BungeeCord / Spigot hover event
     *
     * @param player The player
     * @return The hover event
     */
    public HoverEvent<Component> toHoverEvent(Player player) {
        // parse action
        HoverEvent.Action<Component> action = HoverEvent.Action.SHOW_TEXT;

        String line = SECTION_REPLACER.apply(PlaceholderAPI.setPlaceholders(player, AMPERSAND_REPLACER.apply(Joiner.on("\n").join(lines), player)), player);

        // replace hex #123456 with &#123456
        line = line.replaceAll(OLD_HEX_PATTERN.pattern(), NEW_HEX_PATTERN);

        // build & return
        return HoverEvent.hoverEvent(action, LegacyComponentSerializer.legacyAmpersand().deserialize(line));
    }

    /**
     * Returns lines
     *
     * @return lines
     */
    public List<String> getLines() {
        return lines;
    }

    /**
     * Sets lines
     *
     * @param lines lines
     */
    public void setLines(List<String> lines) {
        this.lines = lines;
    }
}
