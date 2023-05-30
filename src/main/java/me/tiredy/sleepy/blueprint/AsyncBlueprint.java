package me.tiredy.sleepy.blueprint;

import me.tiredy.sleepy.SleepyAPI;
import me.tiredy.sleepy.blueprint.region.CuboidRegion;
import me.tiredy.sleepy.blueprint.vector.BlockVec3;
import me.tiredy.sleepy.callback.ResultCallback;
import me.tiredy.sleepy.callback.VoidCallback;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("unused")
public class AsyncBlueprint extends Blueprint {
    protected AsyncBlueprint(CopyOnWriteArrayList<BlueprintBlock> blocks, BlockVec3 originVector, BlockVec3 sizeVector) {
        super(blocks, originVector, sizeVector);
    }

    /**
     * @deprecated Unsafe, changing chunks or blocks asynchronously is <span style="color:red;font-weight:800;">NOT</span> recommended
     */
    @Deprecated
    public void asyncTo(SleepyAPI instance, BlockVec3 location, World world, VoidCallback callback) {
        Bukkit.getScheduler().runTaskAsynchronously(instance.getPlugin(),() -> {
            for (BlueprintBlock block : blocks) {
                Block targetBlock = world.getBlockAt(
                        location.getX() + block.getPos().getX(),
                        location.getY() + block.getPos().getY(),
                        location.getZ() + block.getPos().getZ()
                );
                targetBlock.setType(block.getMaterial(), false);
                targetBlock.setBlockData(block.getData());
            }

            callback.onSuccess();
        });
    }

    public static void from(SleepyAPI instance, CuboidRegion region, ResultCallback<AsyncBlueprint> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(instance.getPlugin(), () -> {
            CopyOnWriteArrayList<BlueprintBlock> blocks = new CopyOnWriteArrayList<>();

            BlockVec3 min = region.getMin();
            BlockVec3 max = region.getMax();

            BlockVec3 size = max.subtract(min);

            World world = region.getWorld();

            for (int x = min.getX(); x <= max.getX(); x++) {
                for (int y = min.getY(); y <= max.getY(); y++) {
                    for (int z = min.getZ(); z <= max.getZ(); z++) {
                        Block block = world.getBlockAt(x, y, z);
                        blocks.add(new BlueprintBlock(block.getType(), block.getBlockData(), new BlockVec3(x,y,z)));
                    }
                }
            }

            callback.onSuccess(new AsyncBlueprint(blocks, new BlockVec3(0,0,0), size));
        });
    }
}
