package me.tiredy.sleepy.chat;

import me.tiredy.sleepy.SleepyAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class ChatUtil {
    private final BukkitAudiences audiences;
    public ChatUtil(SleepyAPI instance) {
        this.audiences = instance.getAudiences();
    }

    public Component component(String message) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(message).asComponent();
    }

    public void sendMessage(CommandSender sender, String message) {
        Audience audience = audiences.sender(sender);
        audience.sendMessage(component(message));
    }
    public void sendMessage(Player p, String message) {
        Audience audience = audiences.player(p);
        audience.sendMessage(component(message));
    }

    public void sendAll(String message) {
        Audience audience = audiences.all();
        audience.sendMessage(component(message));
    }
}
