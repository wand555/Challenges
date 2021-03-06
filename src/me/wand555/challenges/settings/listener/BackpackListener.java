package me.wand555.challenges.settings.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.wand555.challenges.api.events.settingschange.ChallengeStatusSwitchEvent;
import me.wand555.challenges.api.events.settingschange.CustomHealthChallengeStatusSwitchEvent;
import me.wand555.challenges.api.events.settingschange.MLGChallengeStatusSwitchEvent;
import me.wand555.challenges.api.events.settingschange.PunishableChallengeStatusSwitchEvent;
import me.wand555.challenges.settings.challengeprofile.Backpack;
import me.wand555.challenges.settings.challengeprofile.ChallengeProfile;
import me.wand555.challenges.settings.challengeprofile.types.PunishType;
import me.wand555.challenges.start.Challenges;

public class BackpackListener implements Listener {

	private Challenges plugin;
	
	public BackpackListener(Challenges plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	
	@EventHandler
	public void test(MLGChallengeStatusSwitchEvent event) {
		if(event.getPunishType() == PunishType.HEALTH_ALL_10) {
			//event.setPunishType(PunishType.ALL_ITEMS);
			//event.setCancelled(true);
			//event.setDeniedMessage("dsf");
		}
	}
	
	@EventHandler
	public void onBackpackClickEvent(InventoryClickEvent event) {
		if(event.getClickedInventory() != null) {
			if(event.getWhoClicked() instanceof Player) {	
				if(event.getView().getTitle().equalsIgnoreCase(ChatColor.GREEN + "Backpack")) {
					new BukkitRunnable() {
						
						@Override
						public void run() {
							//ChallengeProfile.getInstance().getBackpack().setContents(event.getWhoClicked().getOpenInventory().getTopInventory().getContents());
							//ChallengeProfile.getInstance().getBackpack().updateInventories();
						}
					}.runTaskLater(plugin, 1L);
				}
			}
		}
	}
	
	@EventHandler
	public void onBackpackDragEvent(InventoryDragEvent event) {
		if(event.getView().getTitle().equalsIgnoreCase(ChatColor.GREEN + "Backpack")) {
			//ChallengeProfile.getInstance().getBackpack().setContents(event.getView().getTopInventory().getContents());
			//ChallengeProfile.getInstance().getBackpack().updateInventories();
		}
	}
	
	@EventHandler
	public void onBackpackOpenEvent(InventoryOpenEvent event) {
		if(event.getView().getTitle().equalsIgnoreCase(ChatColor.GREEN + "Backpack")) {
			ChallengeProfile.getInstance().getBackpack().addToOpenPlayers(event.getPlayer().getUniqueId());
			event.getView().getTopInventory().setContents(ChallengeProfile.getInstance().getBackpack().getContents());
		}
	}
	
	@EventHandler
	public void onBackpackCloseEvent(InventoryCloseEvent event) {
		if(event.getView().getTitle().equalsIgnoreCase(ChatColor.GREEN + "Backpack")) {
			ChallengeProfile.getInstance().getBackpack().removeFromOpenPlayers(event.getPlayer().getUniqueId());
		}
	}
}
