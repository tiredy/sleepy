package me.tiredy.sleepy.blueprint.region;

import me.tiredy.sleepy.blueprint.vector.BlockVec3;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class CuboidRegion {
    private BlockVec3 min;
    private BlockVec3 max;
    private World world;

    public CuboidRegion(BlockVec3 min, BlockVec3 max, World world) {
        this.min = min;
        this.max = max;
        this.world = world;
    }

    public BlockVec3 getMin() {
        return min;
    }

    public void setMin(BlockVec3 min) {
        this.min = min;
    }

    public BlockVec3 getMax() {
        return max;
    }

    public void setMax(BlockVec3 max) {
        this.max = max;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Collection<? extends Player> getPlayersInRegion() {
        return Bukkit.getOnlinePlayers().stream().filter(this::isPlayerInRegion).collect(Collectors.toSet());
    }

    public boolean isPlayerInRegion(Player player) {
        // Assuming BlockVec3 has getX(), getY(), and getZ() methods to retrieve the coordinates.
        double playerX = player.getLocation().getX();
        double playerY = player.getLocation().getY();
        double playerZ = player.getLocation().getZ();
        String playerWorld = player.getWorld().getName();

        double minX = min.getX();
        double minY = min.getY();
        double minZ = min.getZ();

        double maxX = max.getX();
        double maxY = max.getY();
        double maxZ = max.getZ();
        String world = getWorld().getName();

        return playerX >= minX && playerX <= maxX
                && playerY >= minY && playerY <= maxY
                && playerZ >= minZ && playerZ <= maxZ
                && playerWorld.equals(world);
    }
}
