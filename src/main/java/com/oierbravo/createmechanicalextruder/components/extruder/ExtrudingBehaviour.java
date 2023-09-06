package com.oierbravo.createmechanicalextruder.components.extruder;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ExtrudingBehaviour extends BlockEntityBehaviour {

	public static final int CYCLE = 240;
	public static final BehaviourType<ExtrudingBehaviour> TYPE = new BehaviourType<>();
	public ExtrudingBehaviourSpecifics specifics;
	public int prevRunningTicks;
	public int runningTicks;
	public boolean running;
	public boolean finished;
	public float headOffset = 0.44f;

	public int bonks;
	private BiomeCondition biomeCondition = BiomeCondition.EMPTY;

	public interface ExtrudingBehaviourSpecifics {

		public void onExtrudingCompleted();
		public float getKineticSpeed();
		public boolean tryProcess(boolean simulate);
	}

	public <T extends SmartBlockEntity & ExtrudingBehaviourSpecifics> ExtrudingBehaviour(T te) {
		super(te);
		this.specifics = te;
	}

	@Override
	public void read(CompoundTag compound, boolean clientPacket) {
		running = compound.getBoolean("Running");
		finished = compound.getBoolean("Finished");
		prevRunningTicks = runningTicks = compound.getInt("Ticks");
		bonks = compound.getInt("Bonks");
		biomeCondition = BiomeCondition.fromString(compound.getString("Biome"));
		super.read(compound, clientPacket);

	}

	@Override
	public void write(CompoundTag compound, boolean clientPacket) {
		compound.putBoolean("Running", running);
		compound.putBoolean("Finished", finished);
		compound.putInt("Ticks", runningTicks);
		compound.putInt("Bonks", bonks);
		compound.putString("Biome", biomeCondition.toString());
		super.write(compound, clientPacket);

	}

	public float getRenderedPoleOffset(float partialTicks) {
		if (!running)
			return 0;
		int runningTicks = Math.abs(this.runningTicks);
		float ticks = Mth.lerp(partialTicks, prevRunningTicks, runningTicks);
		if (runningTicks < (CYCLE * 2) / 3)
			return (float) Mth.clamp(Math.pow(ticks / CYCLE * 2, 3), 0, 1);
		return Mth.clamp((CYCLE - ticks) / CYCLE * 3, 0, 1);
	}

	public void start() {
		running = true;
		prevRunningTicks = 0;
		runningTicks = 0;
		blockEntity.sendData();
	}
	public void resetBonks() {
		bonks = 0;
	}
	public int addBonk() {
		bonks += 1;
		return bonks;
	}
	public int getBonks(){
		return bonks;
	}

	public BiomeCondition getBiomeCondition(){
		return biomeCondition;

	}
	@Override
	public BehaviourType<?> getType() {
		return TYPE;
	}

	@Override
	public void tick() {
		super.tick();

		Level level = getWorld();
		BlockPos worldPosition = getPos();
		if (!running || level == null) {
			if (level != null && !level.isClientSide) {

				if (specifics.getKineticSpeed() == 0)
					return;

				if (specifics.tryProcess( true))
					start();


			}
			return;
		}


		if (level.isClientSide && runningTicks == -CYCLE / 2) {
			prevRunningTicks = CYCLE / 2;
			return;
		}

		if (runningTicks == CYCLE / 2 && specifics.getKineticSpeed() != 0) {
			apply();
			AllSoundEvents.MECHANICAL_PRESS_ACTIVATION_ON_BELT.playOnServer(level, worldPosition);
			if (!level.isClientSide)
				blockEntity.sendData();
		}

		if (!level.isClientSide && runningTicks > CYCLE) {
			finished = true;
			running = false;
			blockEntity.sendData();
			specifics.onExtrudingCompleted();
			return;
		}

		prevRunningTicks = runningTicks;
		runningTicks += getRunningTickSpeed();
		if (prevRunningTicks < CYCLE / 2 && runningTicks >= CYCLE / 2) {
			runningTicks = CYCLE / 2;
			// Pause the ticks until a packet is received
			if (level.isClientSide && !blockEntity.isVirtual())
				runningTicks = -(CYCLE / 2);
		}
	}



	protected void apply() {
		Level level = getWorld();
		//BlockPos worldPosition = getPos();
		//AABB bb = new AABB(worldPosition.below(1));

		//particleItems.clear();

		if (level.isClientSide)
			return;

		if (specifics.tryProcess(false))
			blockEntity.sendData();
	}

	public int getRunningTickSpeed() {
		float speed = specifics.getKineticSpeed();
		if (speed == 0)
			return 0;
		return (int) Mth.lerp(Mth.clamp(Math.abs(speed) / 512f, 0, 1), 1, 60);
	}



	public void makeCompactingParticleEffect(Vec3 pos, ItemStack stack) {
		Level level = getWorld();
		if (level == null || !level.isClientSide)
			return;
		for (int i = 0; i < 20; i++) {
			Vec3 motion = VecHelper.offsetRandomly(Vec3.ZERO, level.random, .175f)
				.multiply(1, 0, 1);
			level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, stack), pos.x, pos.y, pos.z, motion.x,
				motion.y + .25f, motion.z);
		}
	}


}
