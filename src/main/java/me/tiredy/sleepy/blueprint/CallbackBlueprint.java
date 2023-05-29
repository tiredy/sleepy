package me.tiredy.sleepy.blueprint;

import me.tiredy.sleepy.blueprint.region.CuboidRegion;
import me.tiredy.sleepy.blueprint.vector.BlockVec3;
import me.tiredy.sleepy.callback.ResultCallback;
import me.tiredy.sleepy.callback.VoidCallback;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class CallbackBlueprint extends Blueprint {
    protected CallbackBlueprint(ArrayList<BlueprintBlock> blocks, BlockVec3 origin) {
        super(blocks, origin);
    }

    public void to(BlockVec3 location, World world, VoidCallback callback) {
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
    }

    public static void from(World world, CuboidRegion region, ResultCallback<CallbackBlueprint> callback) {
        ArrayList<BlueprintBlock> blocks = new ArrayList<>();

        BlockVec3 min = region.getMin();
        BlockVec3 max = region.getMax();

        for (int x = min.getX(); x <= max.getX(); x++) {
            for (int y = min.getY(); y <= max.getY(); y++) {
                for (int z = min.getZ(); z <= max.getZ(); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    blocks.add(new BlueprintBlock(block.getType(), block.getBlockData(), new BlockVec3(x,y,z)));
                }
            }
        }

        callback.onSuccess(new CallbackBlueprint(blocks,new BlockVec3(0,0,0)));
    }
}
