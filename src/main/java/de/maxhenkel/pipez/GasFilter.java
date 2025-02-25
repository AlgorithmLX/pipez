package de.maxhenkel.pipez;

import de.maxhenkel.corelib.tag.SingleElementTag;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.ChemicalTags;
import mekanism.api.chemical.gas.Gas;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.Constants;

import java.util.UUID;

public class GasFilter extends Filter<Gas> {

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compound = new CompoundTag();
        if (tag != null) {
            if (tag instanceof SingleElementTag) {
                ResourceLocation key = MekanismAPI.gasRegistry().getKey(((SingleElementTag<Gas>) tag).getElement());
                if (key != null) {
                    compound.putString("Gas", key.toString());
                }
            } else {
                compound.putString("Tag", tag.getName().toString());
            }
        }
        if (destination != null) {
            compound.put("Destination", destination.serializeNBT());
        }
        if (invert) {
            compound.putBoolean("Invert", true);
        }
        compound.putUUID("ID", id);

        return compound;
    }

    @Override
    public void deserializeNBT(CompoundTag compound) {
        tag = null;
        if (compound.contains("Gas", Constants.NBT.TAG_STRING)) {
            Gas gas = MekanismAPI.gasRegistry().getValue(new ResourceLocation(compound.getString("Gas")));
            if (gas != null) {
                tag = new SingleElementTag<>(gas);
            }
        }
        if (compound.contains("Tag", Constants.NBT.TAG_STRING)) {
            tag = ChemicalTags.GAS.tag(new ResourceLocation(compound.getString("Tag")));
        }

        metadata = null;
        exactMetadata = false;

        if (compound.contains("Destination", Constants.NBT.TAG_COMPOUND)) {
            destination = new DirectionalPosition();
            destination.deserializeNBT(compound.getCompound("Destination"));
        } else {
            destination = null;
        }

        if (compound.contains("Invert", Constants.NBT.TAG_BYTE)) {
            invert = compound.getBoolean("Invert");
        } else {
            invert = false;
        }

        if (compound.contains("ID", Constants.NBT.TAG_INT_ARRAY)) {
            id = compound.getUUID("ID");
        } else {
            id = UUID.randomUUID();
        }
    }

}
