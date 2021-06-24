package com.yakovliam.deluxechathex;

import com.yakovliam.deluxechathex.builder.live.NormalLiveChatFormatBuilder;
import com.yakovliam.deluxechathex.converter.deluxeformat.DeluxeFormatConverter;
import com.yakovliam.deluxechathex.model.formatting.ChatFormat;
import com.yakovliam.deluxechathex.util.Triple;
import me.clip.deluxechat.events.DeluxeChatEvent;
import me.clip.deluxechat.objects.DeluxeFormat;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

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
    }

    @Override
    public void onDisable() {
        if (this.bukkitAudiences != null) {
            this.bukkitAudiences.close();
            this.bukkitAudiences = null;
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onDeluxeChatEvent(DeluxeChatEvent event) {
        event.setCancelled(true);

        // get deluxe format
        DeluxeFormat format = event.getDeluxeFormat();
        // convert to 'ChatFormat'
        ChatFormat converted = new DeluxeFormatConverter().convert(format);

        // serialize and send
        TextComponent component = new NormalLiveChatFormatBuilder(this).build(new Triple<>(event.getPlayer(), event.getChatMessage(), converted.getFormat()));

        // send to all players
        bukkitAudiences.filter(c -> c instanceof Player && event.getRecipients().stream()
                .anyMatch(p -> p.getUniqueId().equals(((Player) c).getUniqueId())))
                .sendMessage(component);

        event.setChatMessage(null);
    }
}