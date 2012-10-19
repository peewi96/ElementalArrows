package me.cybermaxke.ElementalArrows.Materials;

import me.cybermaxke.ElementalArrows.ArrowEntity;

import org.bukkit.Effect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class FireArrow extends CustomArrowItem {

	public FireArrow(Plugin plugin, String name, String texture) {
		super(plugin, name, texture);
		this.setFireTicks(70);
	}

	@Override
	public void onHit(Player shooter, LivingEntity entity, ArrowEntity arrow) {
		entity.getWorld().playEffect(entity.getLocation(), Effect.MOBSPAWNER_FLAMES, 10);
	}

	@Override
	public void onHit(Player shooter, ArrowEntity arrow) {

	}

	@Override
	public void onShoot(Player shooter, ArrowEntity arrow) {
		
	}
}