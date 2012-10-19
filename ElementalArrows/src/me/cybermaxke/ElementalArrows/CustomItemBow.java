package me.cybermaxke.ElementalArrows;

import me.cybermaxke.ElementalArrows.Materials.CustomArrowItem;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.Enchantment;
import net.minecraft.server.EnchantmentManager;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumAnimation;
import net.minecraft.server.ItemBow;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;

import org.bukkit.Material;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.material.MaterialData;
import org.getspout.spoutapi.player.SpoutPlayer;

public class CustomItemBow extends ItemBow {

	public CustomItemBow() {
		super(MaterialData.bow.getRawId());
	    this.maxStackSize = 1;
	    this.setMaxDurability(384);
	    this.a(CreativeModeTab.j);
	    this.b(5, 1);
	    this.b("bow");
	}

	private int getFirstArrow(SpoutPlayer p) {
		for (int i = 0; i < p.getInventory().getSize(); i++) {
			if (p.getInventory().getItem(i) != null) {
				SpoutItemStack is = new SpoutItemStack(p.getInventory().getItem(i));
			
				if (is.getType().equals(Material.ARROW))
					return i;
			
				if (is.isCustomItem() && is.getMaterial() instanceof CustomArrowItem)
					return i;
			}
		}
		
		return -1;	
	}

	@Override
	public void a(ItemStack itemstack, World world, EntityHuman entityhuman, int i) {
		SpoutPlayer p = SpoutManager.getPlayer((Player) entityhuman.getBukkitEntity());
		
		int slot = this.getFirstArrow(p);
		if (slot == -1)
			return;
		
		int j = this.a(itemstack) - i;
        float f = (float) j / 20.0F;

        f = (f * f + f * 2.0F) / 3.0F;
        if ((double) f < 0.1D) {
            return;
        }

        if (f > 1.0F) {
            f = 1.0F;
        }

        ArrowEntity enw = new ArrowEntity(world, entityhuman, f * 2.0F);

        if (f == 1.0F) {
            enw.d(true);
        }		
			
		SpoutItemStack is = new SpoutItemStack(p.getInventory().getItem(slot));
		if (is.getType().equals(Material.ARROW)) {
			if (is.getAmount() == 1)
				is = null;
			else 
				is.setAmount(is.getAmount() - 1);
		}
			
		if (is != null) {
			if (is.isCustomItem() && is.getMaterial() instanceof CustomArrowItem) {	
				CustomArrowItem ai = (CustomArrowItem) is.getMaterial();
				
				enw.setArrow(ai);
				enw.setDamage(ai.getDamage());
				enw.setKnockback(ai.getKnockback());
				enw.setFireTicks(ai.getFireTicks());
			
				if (is.getAmount() == 1)
					is = null;
				else 
					is.setAmount(is.getAmount() - 1);
				
				ai.onShoot(p, enw);
			}
		}
		
		int damage = EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_DAMAGE.id, itemstack) + enw.getDamage();
		enw.setDamage(damage);
		
		int knockback = EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_KNOCKBACK.id, itemstack) + enw.getKnockback();
		enw.setKnockback(knockback);
		
		if (EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_FIRE.id, itemstack) > 0)
			enw.setFireTicks(100 + enw.getFireTicks());
		
		EntityShootBowEvent event = CraftEventFactory.callEntityShootBowEvent(entityhuman, itemstack, enw, f);
	    if (event.isCancelled()) {
	      	event.getProjectile().remove();
	      	return;
	    }
	    
	    if (event.getProjectile() == enw.getBukkitEntity()) {
	        world.addEntity(enw);      
	    }
			
	    itemstack.damage(1, entityhuman);
		p.getInventory().setItem(slot, is);
	    world.makeSound(entityhuman, "random.bow", 1.0F, 1.0F / (d.nextFloat() * 0.4F + 1.2F) + f * 0.5F);	
	}
	
	@Override
	public ItemStack b(ItemStack itemstack, World world, EntityHuman entityhuman) {
		return itemstack;
	}
	
	@Override
	public int a(ItemStack itemstack) {
		return 72000;
	}

	@Override
	public EnumAnimation b(ItemStack itemstack) {	
		return EnumAnimation.e;
	}
	
	@Override
	public ItemStack a(ItemStack itemstack, World world, EntityHuman entityhuman) {	
		SpoutPlayer p = SpoutManager.getPlayer((Player) entityhuman.getBukkitEntity());
		
		int slot = this.getFirstArrow(p);
		if (slot != -1)
			entityhuman.a(itemstack, a(itemstack));
		
		return itemstack;
	}

	@Override
	public int b() {
		return 1;
	}
}