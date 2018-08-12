package com.chromaclypse.enchantnerf;

import java.util.Locale;
import java.util.Map;
import java.util.Random;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

public class DamageListener implements Listener {
	private final DamageConfig config;
	private final Random random = new Random();
	
	public DamageListener(DamageConfig config) {
		this.config = config;
	}
	
	public double getBonusDamage(ItemStack item) {
		double modifier = 0;
		
		for(Map.Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet()) {
			Map<String, Double> costs = config.enchantmentDamage.get(entry.getKey().getKey().getKey());
			
			if(costs != null) {
				Double cost = costs.get(entry.getValue().toString());
				
				if(cost != null) {
					modifier += cost;
				}
				else {
					cost = costs.get("scale");
					
					if(cost != null) {
						modifier += cost * entry.getValue();
					}
				}
			}
		}
		
		Double scalar = config.materialScales.get(item.getType().name().toUpperCase(Locale.ENGLISH));
		
		if(scalar != null) {
			modifier *= scalar;
		}
		
		return modifier;
	}
	
	@EventHandler
	public void onDamage(PlayerItemDamageEvent event) {
		
		int current = event.getDamage();
		if(current == 0) {
			return;
		}
		
		double modifier = getBonusDamage(event.getItem());
		int additional = (int)Math.floor(modifier);
		
		if(random.nextDouble() < modifier - additional) {
			++additional;
		}
		int remaining = event.getItem().getType().getMaxDurability() - event.getItem().getDurability() - current;
		int targetDamage = current + Math.max(Math.min(additional, remaining), 0);
		
		event.setDamage(targetDamage);
	}
}
