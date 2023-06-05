package me.tiredy.sleepy.blueprint;

import me.tiredy.sleepy.blueprint.region.CuboidRegion;
import me.tiredy.sleepy.blueprint.vector.BlockVec3;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.structure.StructureRotation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Blueprint implements Serializable {
    protected List<BlueprintBlock> blocks;
    protected BlockVec3 originVector;
    protected BlockVec3 sizeVector;

    protected Blueprint(List<BlueprintBlock> blocks, BlockVec3 origin, BlockVec3 sizeVector) {
        this.blocks = blocks;
        this.originVector = origin;
        this.sizeVector = sizeVector;
    }

    public void to(BlockVec3 location, World world) {
        for (BlueprintBlock block : blocks) {
            Block targetBlock = world.getBlockAt(
                    location.getX() + block.getPos().getX(),
                    location.getY() + block.getPos().getY(),
                    location.getZ() + block.getPos().getZ()
            );
            targetBlock.setType(block.getMaterial(), false);
            targetBlock.setBlockData(block.getData(), false);
        }
    }

    public static Blueprint from(CuboidRegion region) {
        ArrayList<BlueprintBlock> blocks = new ArrayList<>();

        BlockVec3 min = region.getMin();
        BlockVec3 max = region.getMax();

        BlockVec3 size = max.subtract(min);

        World world = region.getWorld();

        for (int x = min.getX(); x <= max.getX(); x++) {
            for (int y = min.getY(); y <= max.getY(); y++) {
                for (int z = min.getZ(); z <= max.getZ(); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    BlockVec3 relativePos = new BlockVec3(x, y, z).subtract(min);
                    blocks.add(new BlueprintBlock(block.getType(), block.getBlockData(), relativePos));
                }
            }
        }

        return new Blueprint(blocks, new BlockVec3(0, 0, 0), size);
    }

    public Blueprint rotateY(int degrees) {
        int quarterTurns = (degrees / 90) & 3;

        List<BlueprintBlock> rotatedBlocks = new ArrayList<>();

        for (BlueprintBlock block : blocks) {
            BlockVec3 pos = block.getPos();
            int x = pos.getX();
            int z = pos.getZ();
            BlueprintBlock newBlock = switch (quarterTurns) {
                case 1 -> block.setPos(new BlockVec3(z, pos.getY(), -x));
                case 2 -> block.setPos(new BlockVec3(-x, pos.getY(), -z));
                case 3 -> block.setPos(new BlockVec3(-z, pos.getY(), x));
                default -> block;
            };
            rotatedBlocks.add(newBlock);
        }

        BlockVec3 newOriginVector, newSizeVector;
        switch (quarterTurns) {
            case 1 -> {
                newOriginVector = originVector.multiply(new BlockVec3(1, 1, -1));
                newSizeVector = sizeVector.multiply(new BlockVec3(1, 1, -1));
            }
            case 2 -> {
                newOriginVector = originVector.multiply(new BlockVec3(-1, 1, -1));
                newSizeVector = sizeVector.multiply(new BlockVec3(-1, 1, -1));
            }
            case 3 -> {
                newOriginVector = originVector.multiply(new BlockVec3(-1, 1, 1));
                newSizeVector = sizeVector.multiply(new BlockVec3(-1, 1, 1));
            }
            default -> {
                newOriginVector = originVector;
                newSizeVector = sizeVector;
            }
        }

        return new Blueprint(rotatedBlocks, newOriginVector, newSizeVector);
    }

    public Blueprint rotate(BlockVec3 rotationVector, int angleX, int angleY, int angleZ) {
        double radX = Math.toRadians(angleX % 360);
        double radY = Math.toRadians(angleY % 360);
        double radZ = Math.toRadians(angleZ % 360);

        double cosX = Math.cos(radX);
        double sinX = Math.sin(radX);
        double cosY = Math.cos(radY);
        double sinY = Math.sin(radY);
        double cosZ = Math.cos(radZ);
        double sinZ = Math.sin(radZ);

        // Copy the orignal blocks for the new Blueprint
        ArrayList<BlueprintBlock> newBlocks = new ArrayList<>(blocks);
        BlockVec3 newOrigin = originVector.copy();
        BlockVec3 newSize = sizeVector.copy();

        for (BlueprintBlock block : newBlocks) {
            // Translate the block coordinate to the rotation point
            int translatedX = block.getPos().getX() - rotationVector.getX();
            int translatedY = block.getPos().getY() - rotationVector.getY();
            int translatedZ = block.getPos().getZ() - rotationVector.getZ();

            // Apply rotation matrix
            int rotatedX = (int) (cosY * (sinZ * translatedY + cosZ * translatedX) - sinY * translatedZ);

            final var v = cosY * translatedZ + sinY * (sinZ * translatedY + cosZ * translatedX);
            int rotatedY = (int) (sinX * v + cosX * (cosZ * translatedY - sinZ * translatedX));
            int rotatedZ = (int) (cosX * v - sinX * (cosZ * translatedY - sinZ * translatedX));

            // Translate the rotated coordinate back
            block.getPos().setX(rotatedX + rotationVector.getX());
            block.getPos().setY(rotatedY + rotationVector.getY());
            block.getPos().setZ(rotatedZ + rotationVector.getZ());

            StructureRotation rotation = switch (angleY % 4) {
                case 1, -3 -> StructureRotation.CLOCKWISE_90;
                case 2, -2 -> StructureRotation.CLOCKWISE_180;
                case 3, -1 -> StructureRotation.COUNTERCLOCKWISE_90;
                default -> StructureRotation.NONE; // Default case when the angle does not match any of the above cases
            };

            block.getData().rotate(rotation);
        }

        return new Blueprint(newBlocks, newOrigin, newSize);
    }



    public List<BlueprintBlock> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<BlueprintBlock> blocks) {
        this.blocks = blocks;
    }

    public BlockVec3 getOrigin() {
        return originVector;
    }

    public void setOrigin(BlockVec3 origin) {
        this.originVector = origin;
    }

    public BlockVec3 getSize() {
        return sizeVector;
    }

    public void setSize(BlockVec3 sizeVector) {
        this.sizeVector = sizeVector;
    }
}
