/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Nov 3, 2014, 12:15:09 AM (GMT)]
 */
package vazkii.botania.common.brew.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.common.lib.LibPotionNames;

public class PotionAllure extends PotionMod {

	public PotionAllure() {
		super(LibPotionNames.ALLURE, false, 0x0034E4, 5);
		MinecraftForge.EVENT_BUS.register(this);
		setShowOnFirstRow();
	}

	@SubscribeEvent
	public void onEntityUpdate(LivingUpdateEvent event) {
		EntityLivingBase e = event.getEntityLiving();
		if(e instanceof EntityPlayer && hasEffect(e)) {
			EntityFishHook hook = ((EntityPlayer) e).fishEntity;
			if(hook != null)
				hook.onUpdate();
		}
	}

}
