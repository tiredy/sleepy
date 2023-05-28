package me.tiredy.mini.chat;

import me.tiredy.mini.MiniLib;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatUtil {
    private static final MiniLib instance = MiniLib.getInstance();

    public static Component component(String message) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(message).asComponent();
    }

    public static void sendMessage(CommandSender sender, String message) {
        Audience audience = instance.getAdventure().sender(sender);
        audience.sendMessage(component(message));
    }
    public static void sendMessage(Player p, String message) {
        Audience audience = instance.getAdventure().player(p);
        audience.sendMessage(component(message));
    }

    private record Placeholder(String name, String replacement) {
        private Placeholder(String name, String replacement) {
                this.name = "{" + name + "}";
                this.replacement = replacement;
        }
    }
}
