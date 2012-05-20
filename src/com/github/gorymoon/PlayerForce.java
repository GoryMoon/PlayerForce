package com.github.gorymoon;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerForce extends JavaPlugin implements Listener{
	
	public Logger log;
	public FileConfiguration config;
	private boolean enable;
	private boolean forceAdmin;
	private String adminCom;
	private boolean adminUserCom;
	private String userCom;
	private String[] args;
	private String userCom2;
	private boolean userComUse2;
	private boolean forceAdmin2;
	private String adminCom2;
	private boolean adminUserCom2;

	public void onEnable(){
		log = getLogger();
    	
    	// Save default config.yml
		if (!new File(getDataFolder(), "config.yml").exists()){
			saveDefaultConfig();
		}
		
		// Initialize
		getServer().getPluginManager().registerEvents(this, this);
    	Config();
    	if(enable == false){
    		log.log(Level.ALL, " Config disables plugin.");
    		getPluginLoader().disablePlugin(this);
    	}
	
    	if(enable==true){
    		log.info("version " + getDescription().getVersion() + " enabled");
    	}
	}
	
	public void onDisable(){
		
		log.info("version " + getDescription().getVersion() + " disabled");
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		final String name = command.getName();
		if (name.equalsIgnoreCase("PF")){
			return PFReload(sender,args);
		}else{
			return false;
		}
	}
	
	public boolean PFReload(CommandSender sender, String[] args){
		if (sender.hasPermission("force.admin")){
			if (args.length != 0){
				if (args[0].equalsIgnoreCase("reload")){
				Config();
				if (sender instanceof CraftPlayer){
					sender.sendMessage(ChatColor.GOLD + "[Box]" + ChatColor.GREEN + " Config reloaded");
				}
				log.info("version " + getDescription().getVersion() + " reloaded");
				if(enable == false){
					log.log(Level.ALL, " Config disables plugin.");
					if (sender instanceof CraftPlayer){
						sender.sendMessage(ChatColor.GOLD + "[Box]" + ChatColor.RED + " Plugin disabled");
					}
					getPluginLoader().disablePlugin(this);
				}
				return true;
				}else{
					sender.sendMessage(ChatColor.GOLD + "[Box]" + ChatColor.RED + " Try to use /pf reload");
				}
			}else{
				sender.sendMessage(ChatColor.GOLD + "[Box]" + ChatColor.RED + " Try to use /pf reload");
			}
		}else{
			sender.sendMessage(ChatColor.DARK_RED + " You don't have permissions for this command!");
			return false;
		}
		return true;
	}
	
	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent event){
		if(event.getPlayer().hasPermission("force.user") && !event.getPlayer().hasPermission("force.admin")){
			try {
				executeCom(event.getPlayer(), userCom);
				if(userComUse2){
					executeCom(event.getPlayer(), userCom2);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(event.getPlayer().hasPermission("force.admin") && forceAdmin == true && adminUserCom == true){
			try {
				executeCom(event.getPlayer(), userCom);
				if(adminUserCom2 && forceAdmin2){
					executeCom(event.getPlayer(), userCom2);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(event.getPlayer().hasPermission("force.admin") && forceAdmin == true){
			try {
				executeCom(event.getPlayer(), adminCom);
				if(forceAdmin2){
					executeCom(event.getPlayer(), adminCom2);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void executeCom(Player user, String com) throws Exception{
		final String command;
		String[] arguments = null;
		if (com.contains(" ")){
			args = com.split(" ");
			command = args[0];
			
			arguments = new String[args.length - 1];
			if (arguments.length > 0)
			{
				System.arraycopy(args, 1, arguments, 0, args.length - 1);
			}
		}else{
			command = com;
		}

		final PluginCommand execCommand = getServer().getPluginCommand(command);
		if (execCommand != null)
		{
			execCommand.execute(user, command, arguments);
		}
	}
	
	public void Config() {
    	config = getConfig();
        enable = config.getBoolean("Enable");
        userCom = config.getString("UserCommand");
        userCom2 = config.getString("UserCommand2");
        userComUse2 = config.getBoolean("UserCom2");
        forceAdmin = config.getBoolean("ForceAdmin");
        forceAdmin2 = config.getBoolean("ForceAdmin2");
        adminCom = config.getString("AdminCommand");
        adminCom2 = config.getString("AdminCommand2");
        adminUserCom = config.getBoolean("AdminUserCommand");
        adminUserCom2 = config.getBoolean("AdminUserCommand2");
    }
}
