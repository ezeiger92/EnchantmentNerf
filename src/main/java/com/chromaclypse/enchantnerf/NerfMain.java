package com.chromaclypse.enchantnerf;

import java.util.Locale;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.chromaclypse.api.messages.Formatter;
import com.chromaclypse.api.messages.Text;

public class NerfMain extends JavaPlugin {
	private DamageConfig config = new DamageConfig();
	private DamageListener listener;

	@Override
	public void onEnable() {
		config.init(this);
		listener = new DamageListener(config);
		
		getServer().getPluginManager().registerEvents(listener, this);
		
		getCommand("enchantnerf").setExecutor(this);
	}
	
	@Override
	public void onDisable() {
		HandlerList.unregisterAll(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
		boolean reloadPerm = sender.hasPermission("enchantnerf.reload");
		
		Formatter format = Text.format();
		
		sender.sendMessage(format.colorize("&8&m(&3&m|&3 "+command.getName() + " v" + getDescription().getVersion()));
		if(args.length > 0) {
			String arg0 = args[0].toLowerCase(Locale.ENGLISH);
			
			if(arg0.equals("reload") && reloadPerm) {
				config.init(this);
				sender.sendMessage(format.colorize("  &7Config reloaded"));
			}
			else if(arg0.equals("query")) {
				if(sender instanceof Player) {
					Player player = (Player) sender;
					
					double damage = listener.getBonusDamage(player.getInventory().getItemInMainHand());
					sender.sendMessage(format.colorize("  &f" + damage + " &7extra damage per use"));
					
				}
				else {
					sender.sendMessage(format.colorize("  &cMust be a player to use this command!"));
				}
			}
			else {
				sender.sendMessage(format.colorize("  &cUnrecognised argument: " + arg0));
			}
		}
		else {
			if(reloadPerm) {
				sender.sendMessage(format.colorize("  &7Use &f/"+alias+" reload &7to reload"));
			}
			sender.sendMessage(format.colorize("  &7Use &f/"+alias+" query &7to check bonus damage"));
		}
		
		return true;
	}
}
