package com.github.mrstop.stdemo.tileentity;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import com.github.mrstop.stdemo.core.IGUIFluid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class TileEntityMachineElectrolyticMachine extends TileEntity implements ISidedInventory, IFluidHandler, IEnergyReceiver, IGUIFluid {
    private static final int fluidTankCapacity = 10_000;
    private static final int totalProcessTime = 100;
    private static final int energyCapacity = 10_000;
    private static final int energyMaxInput = 30;

    @SideOnly(Side.CLIENT)
    public int GUIFluidAmount = 0;
    @SideOnly(Side.CLIENT)
    public int GUIEnergyAmount = 0;
    @SideOnly(Side.CLIENT)
    public int GUIProcessTime = 0;
    @SideOnly(Side.CLIENT)
    public int GUIFluidID = 0;

    private FluidTank fluidTank;
    private EnergyStorage energyStorage = new EnergyStorage(this.energyCapacity, this.energyMaxInput);
    private int processTime = 0;

    private String electrolyticMachineCustomName = null;

    public TileEntityMachineElectrolyticMachine() {
        this.fluidTank = new FluidTank(this.fluidTankCapacity);
    }

    public int getProcessTime() {
        return processTime;
    }

    @Override
    public int getFluidAmount(int tankIndex) {
        return this.fluidTank.getFluidAmount();
    }

    @Override
    public int getFluidID(int tankIndex) {
        if (this.fluidTank.getFluid() != null){
            return this.fluidTank.getFluid().getFluidID();
        }
        return 0;
    }

    public int getEnergyAmount(){
        return this.energyStorage.getEnergyStored();
    }

    @SideOnly(Side.CLIENT)
    public int getEnergyScale(int scale){
        return (int) (((double) this.GUIEnergyAmount / this.energyCapacity) * scale);
    }

    @SideOnly(Side.CLIENT)
    public int getFluidScale(int scale){
        return (int) (((double) this.GUIFluidAmount / this.fluidTankCapacity) * scale);
    }

    public void setCustomInventoryName(String electrolyticMachineCustomName) {
        this.electrolyticMachineCustomName = electrolyticMachineCustomName;
    }

    @Override
    public void updateEntity() {
        if (!this.worldObj.isRemote){
        }
//        worldObj.markBlockRangeForRenderUpdate();
//        worldObj.notifyBlockChange();
//        worldObj.scheduleBlockUpdate();
        this.markDirty();
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        this.writeToNBT(nbtTagCompound);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1

















                , nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.fluidTank.readFromNBT(compound);
        this.energyStorage.readFromNBT(compound);
        this.processTime = compound.getInteger("ProcessTime");
        if (compound.hasKey("CustomName", 8)){
            this.electrolyticMachineCustomName = compound.getString("CustomName");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        this.fluidTank.writeToNBT(compound);
        this.energyStorage.writeToNBT(compound);
        compound.setInteger("ProcessTime", this.processTime);
        if (this.isCustomInventoryName()){
            compound.setString("CustomName", this.electrolyticMachineCustomName);
        }
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return this.fluidTank.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if (resource == null){
            //����ջ�ǿյ�
            return null;
        }
        if (!resource.isFluidEqual(this.fluidTank.getFluid())){
            //���������ջ�봢���е�����ջ��ͬ
            return null;
        }
        return this.drain(from, resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return this.fluidTank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        Fluid tankFluid = this.fluidTank.getFluid().getFluid();
        return tankFluid == null || tankFluid == fluid;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        Fluid tankFluid = this.fluidTank.getFluid().getFluid();
        return tankFluid != null && tankFluid == fluid;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[]{this.fluidTank.getInfo()};
    }

    public boolean isUsedByPlayer(EntityPlayer player) {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && player.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public int receiveEnergy(ForgeDirection forgeDirection, int eneygyCount, boolean isSimulate) {
        return this.energyStorage.receiveEnergy(eneygyCount, isSimulate);
    }

    @Override
    public int getEnergyStored(ForgeDirection forgeDirection) {
        return this.energyStorage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection forgeDirection) {
        return this.energyStorage.getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection forgeDirection) {
        return true;
    }

    @Override
    public int[] getSlotsForFace(int p_94128_1_) {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_) {
        return false;
    }

    @Override
    public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_) {
        return false;
    }

    @Override
    public int getSizeInventory() {
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int slotIn) {
        return null;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {

    }

    @Override
    public String getInventoryName() {
        return this.isCustomInventoryName() ? this.electrolyticMachineCustomName : "stdemo.container.electrolyticMachine";
    }

    @Override
    public boolean isCustomInventoryName() {
        return this.electrolyticMachineCustomName != null && this.electrolyticMachineCustomName.length() > 0;
    }

    @Override
    public int getInventoryStackLimit() {
        return 0;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return false;
    }

    @Override
    public void openChest() {

    }

    @Override
    public void closeChest() {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return false;
    }
}