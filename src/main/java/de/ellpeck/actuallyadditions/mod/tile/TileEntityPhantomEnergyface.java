/*
 * This file ("TileEntityPhantomEnergyface.java") is part of the Actually Additions mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2015-2017 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod.tile;

import de.ellpeck.actuallyadditions.mod.blocks.BlockPhantom;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

public class TileEntityPhantomEnergyface extends TileEntityPhantomface implements ISharingEnergyProvider {

    public TileEntityPhantomEnergyface() {
        super("energyface");
        this.type = BlockPhantom.Type.ENERGYFACE;
    }

    @Override
    protected boolean isValidTarget(TileEntity tile) {
        if (tile == null || tile instanceof TileEntityLaserRelayEnergy)
            return false;

        for (EnumFacing facing : EnumFacing.VALUES) {
            if (tile.hasCapability(CapabilityEnergy.ENERGY, facing))
                return true;
        }

        return false;
    }

    @Override
    protected boolean isCapabilitySupported(Capability<?> capability) {
        return capability == CapabilityEnergy.ENERGY;
    }

    @Override
    public int getEnergyToSplitShare() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean doesShareEnergy() {
        return true;
    }

    @Override
    public EnumFacing[] getEnergyShareSides() {
        return EnumFacing.values();
    }

    @Override
    public boolean canShareTo(TileEntity tile) {
        return true;
    }
}
