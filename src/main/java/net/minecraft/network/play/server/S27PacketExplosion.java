package net.minecraft.network.play.server;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.List;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class S27PacketExplosion implements Packet<INetHandlerPlayClient> {
	private double posX;
	private double posY;
	private double posZ;
	private float strength;
	private List<BlockPos> affectedBlockPositions;
	private float motionX;
	private float motionY;
	private float motionZ;

	public S27PacketExplosion() {
	}

	public S27PacketExplosion(double p_i45193_1_, double y, double z, float strengthIn, List<BlockPos> affectedBlocksIn,
			Vec3 p_i45193_9_) {
		this.posX = p_i45193_1_;
		this.posY = y;
		this.posZ = z;
		this.strength = strengthIn;
		this.affectedBlockPositions = Lists.newArrayList(affectedBlocksIn);

		if (p_i45193_9_ != null) {
			this.motionX = (float) p_i45193_9_.xCoord;
			this.motionY = (float) p_i45193_9_.yCoord;
			this.motionZ = (float) p_i45193_9_.zCoord;
		}
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	public void readPacketData(PacketBuffer buf) throws IOException {
		this.posX = (double) buf.readFloat();
		this.posY = (double) buf.readFloat();
		this.posZ = (double) buf.readFloat();
		this.strength = buf.readFloat();
		int i = buf.readInt();
		this.affectedBlockPositions = Lists.<BlockPos>newArrayListWithCapacity(i);
		int j = (int) this.posX;
		int k = (int) this.posY;
		int l = (int) this.posZ;

		for (int i1 = 0; i1 < i; ++i1) {
			int j1 = buf.readByte() + j;
			int k1 = buf.readByte() + k;
			int l1 = buf.readByte() + l;
			this.affectedBlockPositions.add(new BlockPos(j1, k1, l1));
		}

		this.motionX = buf.readFloat();
		this.motionY = buf.readFloat();
		this.motionZ = buf.readFloat();
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	public void writePacketData(PacketBuffer buf) throws IOException {
		buf.writeFloat((float) this.posX);
		buf.writeFloat((float) this.posY);
		buf.writeFloat((float) this.posZ);
		buf.writeFloat(this.strength);
		buf.writeInt(this.affectedBlockPositions.size());
		int i = (int) this.posX;
		int j = (int) this.posY;
		int k = (int) this.posZ;

		for (BlockPos blockpos : this.affectedBlockPositions) {
			int l = blockpos.getX() - i;
			int i1 = blockpos.getY() - j;
			int j1 = blockpos.getZ() - k;
			buf.writeByte(l);
			buf.writeByte(i1);
			buf.writeByte(j1);
		}

		buf.writeFloat(this.motionX);
		buf.writeFloat(this.motionY);
		buf.writeFloat(this.motionZ);
	}

	/**
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(INetHandlerPlayClient handler) {
		handler.handleExplosion(this);
	}

	public float func_149149_c() {
		return this.motionX;
	}

	public float func_149144_d() {
		return this.motionY;
	}

	public float func_149147_e() {
		return this.motionZ;
	}

	public double getX() {
		return this.posX;
	}

	public double getY() {
		return this.posY;
	}

	public double getZ() {
		return this.posZ;
	}

	public float getStrength() {
		return this.strength;
	}

	public List<BlockPos> getAffectedBlockPositions() {
		return this.affectedBlockPositions;
	}

	/**
	 * @return the posX
	 */
	public double getPosX() {
		return posX;
	}

	/**
	 * @param posX the posX to set
	 */
	public void setPosX(double posX) {
		this.posX = posX;
	}

	/**
	 * @return the posY
	 */
	public double getPosY() {
		return posY;
	}

	/**
	 * @param posY the posY to set
	 */
	public void setPosY(double posY) {
		this.posY = posY;
	}

	/**
	 * @return the posZ
	 */
	public double getPosZ() {
		return posZ;
	}

	/**
	 * @param posZ the posZ to set
	 */
	public void setPosZ(double posZ) {
		this.posZ = posZ;
	}

	/**
	 * @return the motionX
	 */
	public float getMotionX() {
		return motionX;
	}

	/**
	 * @param motionX the motionX to set
	 */
	public void setMotionX(float motionX) {
		this.motionX = motionX;
	}

	/**
	 * @return the motionY
	 */
	public float getMotionY() {
		return motionY;
	}

	/**
	 * @param motionY the motionY to set
	 */
	public void setMotionY(float motionY) {
		this.motionY = motionY;
	}

	/**
	 * @return the motionZ
	 */
	public float getMotionZ() {
		return motionZ;
	}

	/**
	 * @param motionZ the motionZ to set
	 */
	public void setMotionZ(float motionZ) {
		this.motionZ = motionZ;
	}

	/**
	 * @param strength the strength to set
	 */
	public void setStrength(float strength) {
		this.strength = strength;
	}

	/**
	 * @param affectedBlockPositions the affectedBlockPositions to set
	 */
	public void setAffectedBlockPositions(List<BlockPos> affectedBlockPositions) {
		this.affectedBlockPositions = affectedBlockPositions;
	}

}
