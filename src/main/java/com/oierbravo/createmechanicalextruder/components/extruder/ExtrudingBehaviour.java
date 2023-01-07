package com.oierbravo.createmechanicalextruder.components.extruder;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.behaviour.belt.BeltProcessingBehaviour;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ExtrudingBehaviour extends BeltProcessingBehaviour {

	public static final int CYCLE = 240;

	public ExtrudingBehaviourSpecifics specifics;
	public int prevRunningTicks;
	public int runningTicks;
	public boolean running;
	public boolean finished;
	public float headOffset = 0.44f;


	public interface ExtrudingBehaviourSpecifics {

		public void onExtrudingCompleted();


		public float getKineticSpeed();
		public boolean tryProcess(boolean simulate);
	}

	public <T extends SmartTileEntity & ExtrudingBehaviourSpecifics> ExtrudingBehaviour(T te) {
		super(te);
		this.specifics = te;
	}

	@Override
	public void read(CompoundTag compound, boolean clientPacket) {
		running = compound.getBoolean("Running");
		finished = compound.getBoolean("Finished");
		prevRunningTicks = runningTicks = compound.getInt("Ticks");
		super.read(compound, clientPacket);

	}

	@Override
	public void write(CompoundTag compound, boolean clientPacket) {
		compound.putBoolean("Running", running);
		compound.putBoolean("Finished", finished);
		compound.putInt("Ticks", runningTicks);
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
		tileEntity.sendData();
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
				tileEntity.sendData();
		}

		if (!level.isClientSide && runningTicks > CYCLE) {
			finished = true;
			running = false;
			tileEntity.sendData();
			specifics.onExtrudingCompleted();
			return;
		}

		prevRunningTicks = runningTicks;
		runningTicks += getRunningTickSpeed();
		if (prevRunningTicks < CYCLE / 2 && runningTicks >= CYCLE / 2) {
			runningTicks = CYCLE / 2;
			// Pause the ticks until a packet is received
			if (level.isClientSide && !tileEntity.isVirtual())
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
			tileEntity.sendData();
	}

	public int getRunningTickSpeed() {
		float speed = specifics.getKineticSpeed();
		if (speed == 0)
			return 0;
		return (int) Mth.lerp(Mth.clamp(Math.abs(speed) / 512f, 0, 1), 1, 60);
	}

	/*protected void spawnParticles() {
		if (particleItems.isEmpty())
			return;

		BlockPos worldPosition = getPos();

		if (mode == Mode.BASIN)
			particleItems
				.forEach(stack -> makeCompactingParticleEffect(VecHelper.getCenterOf(worldPosition.below(2)), stack));
		if (mode == Mode.BELT)
			particleItems.forEach(stack -> makePressingParticleEffect(VecHelper.getCenterOf(worldPosition.below(2))
				.add(0, 8 / 16f, 0), stack));
		if (mode == Mode.WORLD)
			particleItems.forEach(stack -> makePressingParticleEffect(VecHelper.getCenterOf(worldPosition.below(1))
				.add(0, -1 / 4f, 0), stack));

		particleItems.clear();
	}*/





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
