/*
 * This file ("TileEntityPhantomface.java") is part of the Actually Additions mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2015-2017 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod.tile;

import de.ellpeck.actuallyadditions.api.tile.IPhantomTile;
import de.ellpeck.actuallyadditions.mod.blocks.BlockPhantom;
import de.ellpeck.actuallyadditions.mod.blocks.InitBlocks;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class TileEntityPhantomface extends TileEntityInventoryBase implements IPhantomTile {

    public static final int RANGE = 16;
    public BlockPos boundPosition;
    public BlockPhantom.Type type;
    public int range;
    private int rangeBefore;
    private BlockPos boundPosBefore;
    private Block boundBlockBefore;
    private boolean isBoundThingInRange;

    public TileEntityPhantomface(String name) {
        super(0, name);
    }

    public static int upgradeRange(int defaultRange, World world, BlockPos pos) {
        int newRange = defaultRange;
        for (int i = 0; i < 3; i++) {
            Block block = world.getBlockState(pos.up(1 + i)).getBlock();
            if (block == InitBlocks.blockPhantomBooster) {
                newRange = newRange * 2;
            } else {
                break;
            }
        }
        return newRange;
    }

    @Override
    public void writeSyncableNBT(NBTTagCompound compound, NBTType type) {
        super.writeSyncableNBT(compound, type);
        if (type != NBTType.SAVE_BLOCK) {
            compound.setInteger("Range", this.range);
            if (this.boundPosition != null) {
                compound.setInteger("xOfTileStored", this.boundPosition.getX());
                compound.setInteger("yOfTileStored", this.boundPosition.getY());
                compound.setInteger("zOfTileStored", this.boundPosition.getZ());
            }
        }
    }

    @Override
    public void readSyncableNBT(NBTTagCompound compound, NBTType type) {
        super.readSyncableNBT(compound, type);
        if (type != NBTType.SAVE_BLOCK) {
            int x = compound.getInteger("xOfTileStored");
            int y = compound.getInteger("yOfTileStored");
            int z = compound.getInteger("zOfTileStored");
            this.range = compound.getInteger("Range");
            if (!(x == 0 && y == 0 && z == 0)) {
                this.boundPosition = new BlockPos(x, y, z);
                this.markDirty();
            }
        }
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (!this.world.isRemote) {
            if (this.ticksElapsed % 20 == 0)
                this.range = upgradeRange(RANGE, this.world, this.getPos());

            updateBoundPosition();

            if (this.doesNeedUpdateSend()) {
                this.onUpdateSent();
            }
        } else {
            updateBoundPosition();
            if (this.boundPosition != null) {
                this.renderParticles();
            }
        }
    }

    private void updateBoundPosition() {
        if (!hasBoundPosition())
            return;

        TileEntity target = this.world.getTileEntity(this.boundPosition);
        if (target instanceof IPhantomTile) {
            this.boundPosition = null;
            this.isBoundThingInRange = false;
        } else if (!isValidTarget(target)) {
            this.isBoundThingInRange = false;
        } else {
            this.isBoundThingInRange = this.boundPosition.distanceSq(this.getPos()) <= this.range * this.range;
        }
    }

    protected boolean doesNeedUpdateSend() {
        return this.boundPosition != this.boundPosBefore || this.boundPosition != null && this.world.getBlockState(this.boundPosition).getBlock() != this.boundBlockBefore || this.rangeBefore != this.range;
    }

    protected void onUpdateSent() {
        this.rangeBefore = this.range;
        this.boundPosBefore = this.boundPosition;
        this.boundBlockBefore = this.boundPosition == null ? null : this.world.getBlockState(this.boundPosition).getBlock();

        if (this.boundPosition != null) {
            this.world.notifyNeighborsOfStateChange(this.pos, this.world.getBlockState(this.boundPosition).getBlock(), false);
        }

        this.sendUpdate();
        this.markDirty();
    }

    @Override
    public boolean hasBoundPosition() {
        return this.boundPosition != null;
    }

    @SideOnly(Side.CLIENT)
    public void renderParticles() {
        if (this.world.rand.nextInt(2) == 0) {
            double d1 = this.boundPosition.getY() + this.world.rand.nextFloat();
            int i1 = this.world.rand.nextInt(2) * 2 - 1;
            int j1 = this.world.rand.nextInt(2) * 2 - 1;
            double d4 = (this.world.rand.nextFloat() - 0.5D) * 0.125D;
            double d2 = this.boundPosition.getZ() + 0.5D + 0.25D * j1;
            double d5 = this.world.rand.nextFloat() * 1.0F * j1;
            double d0 = this.boundPosition.getX() + 0.5D + 0.25D * i1;
            double d3 = this.world.rand.nextFloat() * 1.0F * i1;
            this.world.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
        }
    }

    @Override
    public boolean isBoundThingInRange() {
        return this.isBoundThingInRange;
    }

    @Override
    public BlockPos getBoundPosition() {
        return this.boundPosition;
    }

    @Override
    public void setBoundPosition(BlockPos pos) {
        this.boundPosition = pos;
    }

    @Override
    public int getGuiID() {
        return -1;
    }

    @Override
    public int getRange() {
        return this.range;
    }

    protected abstract boolean isCapabilitySupported(Capability<?> capability);

    protected abstract boolean isValidTarget(TileEntity tile);

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (this.isBoundThingInRange() && this.isCapabilitySupported(capability)) {
            TileEntity tile = this.world.getTileEntity(this.getBoundPosition());
            if (tile != null) {return tile.hasCapability(capability, facing);}
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (this.isBoundThingInRange() && this.isCapabilitySupported(capability)) {
            TileEntity tile = this.world.getTileEntity(this.getBoundPosition());
            if (tile != null) {return tile.getCapability(capability, facing);}
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public int getComparatorStrength() {
        return 0;
    }
}
