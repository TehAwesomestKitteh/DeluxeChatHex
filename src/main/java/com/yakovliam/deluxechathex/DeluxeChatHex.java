package com.yakovliam.deluxechathex;

import com.yakovliam.deluxechathex.builder.live.NormalLiveChatFormatBuilder;
import com.yakovliam.deluxechathex.converter.deluxeformat.DeluxeFormatConverter;
import com.yakovliam.deluxechathex.model.formatting.ChatFormat;
import com.yakovliam.deluxechathex.util.Triple;
import me.clip.deluxechat.events.DeluxeChatEvent;
import me.clip.deluxechat.objects.DeluxeFormat;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import org.bstats.bukkit.Metrics;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;

public final class DeluxeChatHex extends JavaPlugin implements Listener {

    /**
     * Bukkit audiences for kyori adventure
     */
    private BukkitAudiences bukkitAudiences;

    @Override
    public void onEnable() {
        // create audiences
        bukkitAudiences = BukkitAudiences.create(this);

        // Plugin startup logic
        this.getServer().getPluginManager().registerEvents(this, this);

        // initialize metrics
        new Metrics(this, 11871);
    }

    @Override
    public void onDisable() {
        if (this.bukkitAudiences != null) {
            this.bukkitAudiences.close();
            this.bukkitAudiences = null;
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        event.getRecipients().clear();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onDeluxeChatEvent(DeluxeChatEvent event) {
        // get deluxe format
        DeluxeFormat format = event.getDeluxeFormat();
        // convert to 'ChatFormat'
        ChatFormat converted = new DeluxeFormatConverter().convert(format);

        // serialize and send
        Component component = new NormalLiveChatFormatBuilder(this).build(new Triple<>(event.getPlayer(), event.getChatMessage(), converted.getFormat()));

        // send to all players
        bukkitAudiences.filter(c -> c instanceof Player && event.getRecipients().stream()
                .anyMatch(p -> p.getUniqueId().equals(((Player) c).getUniqueId())))
                .sendMessage(component);

        // clear recipients list to prevent unwanted messages in chat that aren't ours
        event.setRecipients(Collections.emptySet());
    }
}
