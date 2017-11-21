package tk.nukeduck.TeleportationUtilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.fusesource.jansi.Ansi.Color;

public class TeleportationUtilities extends JavaPlugin {
	public List<String> rules = new ArrayList<String>();
	
	@Override
	public void onEnable() {
		if(this.getConfig().getStringList("rules") != null) {
			rules = this.getConfig().getStringList("rules");
		} else {
			this.getConfig().set("rules", rules);
		}
		
		int id = getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
		    public void run() {
		        for(Player player : getServer().getOnlinePlayers()) {
		        	Map<String, Double> vars = new HashMap<String, Double>();
		        	vars.put("x", player.getLocation().getX());
		        	vars.put("y", player.getLocation().getY());
		        	vars.put("z", player.getLocation().getZ());
		        	vars.put("rx", player.getLocation().getDirection().getX());
		        	vars.put("ry", player.getLocation().getDirection().getY());
		        	vars.put("rz", player.getLocation().getDirection().getZ());
		        	vars.put("mx", player.getVelocity().getX());
		        	vars.put("my", player.getVelocity().getY());
		        	vars.put("mz", player.getVelocity().getZ());
		        	vars.put("l", (double) player.getLevel());
		        	
		        	boolean flag = false;
		        	
		        	for(String rule : rules) {
		        		String[] sections = rule.split("/");
		        		String[] conditions = sections[1].split("&");
		        		for(String s : conditions) {
		        			if(s.contains(">=")) {
		        				if(vars.get(s.split(">=")[0]) >= Double.parseDouble(s.split(">=")[1])) {
			        					flag = true;
			        				}
			        				else {
				        				flag = false;
				        				break;
			        				}
			        			} else if(s.contains("<=")) {
			        				if(vars.get(s.split("<=")[0]) <= Double.parseDouble(s.split("<=")[1])) {
			        					flag = true;
			        				}
			        				else {
				        				flag = false;
				        				break;
			        				}
			        			} else if(s.contains("!=")) {
			        				if(vars.get(s.split("!=")[0]) != Double.parseDouble(s.split("!=")[1])) {
			        					flag = true;
			        				}
			        				else {
				        				flag = false;
				        				break;
			        				}
				        		} else if(s.contains(">")) {
			        				if(vars.get(s.split(">")[0]) > Double.parseDouble(s.split(">")[1])) {
			        					flag = true;
			        				}
			        				else {
				        				flag = false;
				        				break;
			        				}
			        			} else if(s.contains("<")) {
			        				if(vars.get(s.split("<")[0]) < Double.parseDouble(s.split("<")[1])) {
			        					flag = true;
			        				}
			        				else {
				        				flag = false;
				        				break;
			        				}
			        			} else if(s.contains("=")) {
			        				if(vars.get(s.split("=")[0]) == Double.parseDouble(s.split("=")[1])) {
			        					flag = true;
			        				}
			        				else {
				        				flag = false;
				        				break;
			        				}
			        			}
		        			}
		        		
		        		if(flag) player.teleport(new Location(player.getWorld(), Double.parseDouble(rule.split("/")[2]), Double.parseDouble(rule.split("/")[3]), Double.parseDouble(rule.split("/")[4])));
		        	}
		        }
		    }
		}, 0, 2);
	}
	
	public void onDisable() {
		this.getConfig().set("rules", rules);
	    this.saveConfig();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("tprules") || cmd.getName().equalsIgnoreCase("teleportrules")) {
			if(sender instanceof Player && args.length > 0 && sender.hasPermission("TeleportationUtilities.editrule")) {
				if(args[0].equalsIgnoreCase("list")) {
					sender.sendMessage(ChatColor.GREEN + "Teleportation Rules");
					for(String rule : rules) {
						sender.sendMessage("- " + rule);
					}
					return true;
				} else if(args[0].equalsIgnoreCase("add") && args.length == 6) {
					if(!isValid(args[2])) {
						sender.sendMessage(ChatColor.RED + "Invalid condition!");
						return false;
					}
					
					if(!isNumeric(args[3]) || !isNumeric(args[4]) || !isNumeric(args[5])) {
						sender.sendMessage(ChatColor.RED + "Invalid coordinates!");
						return false;
					}
					
					rules.add(args[1] + "/" + args[2] + "/" + args[3] + "/" + args[4] + "/" + args[5]);
					sender.sendMessage("Rule: \"" + args[1] + "\" successfully added.");
					return true;
				} else if(args[0].equalsIgnoreCase("remove") && args.length == 2) {
					for(String rule : rules) {
						if(rule.split("/")[0].equals(args[1])) {
							rules.remove(rule);
							sender.sendMessage("Rule:\"" + args[1] + "\" successfully removed.");
							return true;
						}
					}
					sender.sendMessage(ChatColor.RED + "That rule could not be found");
				}
			} else {
				if(args.length <= 0) sender.sendMessage("Incorrect amount of arguments.");
				if(!(sender instanceof Player)) sender.sendMessage("Only players can use this command.");
			}
		} else if(cmd.getName().equalsIgnoreCase("tpconditions")) {
			String s = "";
			for(int i = 0; i < playerProperties.length; i++) {
				if(i > 0) s += ", ";
				s += playerProperties[i] + " - " + propertyDescriptions[i];
			}
			sender.sendMessage(Color.CYAN + "Conditions syntax:");
			sender.sendMessage("  <property><operator><value>");
			sender.sendMessage("  " + Color.GREEN + "Properties:");
			sender.sendMessage("    " + s);
			sender.sendMessage("  " + Color.GREEN + "Operators:");
			sender.sendMessage("    =, <=, >=, <, >, !=");
		}
		return false;
	}
	
	public String[] playerProperties = {
		"x", "y", "z", "rx", "ry", "rz", "mx", "my", "mz", "l"
	};
	
	public String[] propertyDescriptions = {
		"Player X Position", "Player Y Position", "Player Z Position", "Player X Rotation", "Player Y Rotation", "Player Z Rotation", "Player X Motion", "Player Y Motion", "Player Z Motion", "Player Level"
	};
	
	public static boolean isNumeric(String str)
	{
		return str.matches("-?\\d+(\\.\\d+)?");
	}
	
	public static boolean contains(Object[] array, Object object) {
		for(Object o : array) {
			if(o.equals(object)) return true;
		}
		return false;
	}
	
	private boolean isValid(String string) {
		String[] conditions = string.split("&");
		for(String i : conditions) {
			 if(i.contains("<=")) {
				String[] numbers = i.split("<=");
				if(!(contains(playerProperties, numbers[0]) && isNumeric(numbers[1]))) {
					return false;
				}
			} else if(i.contains(">=")) {
				String[] numbers = i.split(">=");
				if(!(contains(playerProperties, numbers[0]) && isNumeric(numbers[1]))) {
					return false;
				}
			} else if(i.contains("!=")) {
				String[] numbers = i.split("!=");
				if(!(contains(playerProperties, numbers[0]) && isNumeric(numbers[1]))) {
					return false;
				}
			} else if(i.contains("<")) {
				String[] numbers = i.split("<");
				if(!(contains(playerProperties, numbers[0]) && isNumeric(numbers[1]))) {
					return false;
				}
			} else if(i.contains(">")) {
				String[] numbers = i.split(">");
				if(!(contains(playerProperties, numbers[0]) && isNumeric(numbers[1]))) {
					return false;
				}
			} else if(i.contains("=")) {
				String[] numbers = i.split("=");
				if(!(contains(playerProperties, numbers[0]) && isNumeric(numbers[1]))) {
					return false;
				}
			} else {
				return false;
			}
		}
		return true;
	}

	public void save(HashMap<String, Integer> map, String path) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
			oos.writeObject(map);
			oos.flush();
			oos.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public HashMap<String, Integer> load(String path) {
		try
		{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
			Object result = ois.readObject();
			ois.close();
			//you can feel free to cast result to HashMap<String, Integer> if you know there's that HashMap in the file
			return (HashMap<String, Integer>) result;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}