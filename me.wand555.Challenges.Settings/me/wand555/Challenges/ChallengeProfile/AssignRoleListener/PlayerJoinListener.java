package me.wand555.Challenges.ChallengeProfile.AssignRoleListener;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.wand555.Challenges.Challenges;
import me.wand555.Challenges.ChallengeProfile.ChallengeProfile;
import me.wand555.Challenges.ChallengeProfile.ChallengeTypes.ChallengeType;
import me.wand555.Challenges.ChallengeProfile.ChallengeTypes.CustomHealthChallenge;
import me.wand555.Challenges.ChallengeProfile.ChallengeTypes.GenericChallenge;
import me.wand555.Challenges.WorldLinkingManager.WorldLinkManager;

public class PlayerJoinListener implements Listener {

	public PlayerJoinListener(Challenges plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	/**
	 * Deals with adding the player to the participants list if a challenge is running.
	 * Problem: A player has to manually reconnect because of health scale bug in bukkit/spigot.
	 * 
	 * @param event
	 */
	@EventHandler
	public void onPlayerJoinListener(PlayerJoinEvent event) {
		if(WorldLinkManager.worlds.contains(event.getPlayer().getWorld())) {
			ChallengeProfile.getInstance().addToParticipants(event.getPlayer().getUniqueId());
			CustomHealthChallenge cHealthChallenge = GenericChallenge.getChallenge(ChallengeType.CUSTOM_HEALTH);
			Player p = event.getPlayer();
			if(ChallengeProfile.getInstance().canTakeEffect()) {
				if(cHealthChallenge.isActive()) {			
					p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(cHealthChallenge.getAmount());
					p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
					p.setHealthScale(p.getHealth());
				}
				else {
					AttributeInstance maxHealth = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
					maxHealth.setBaseValue(maxHealth.getDefaultValue());
					p.setHealthScale(p.getHealth());
					maxHealth.setBaseValue(maxHealth.getDefaultValue());
					p.setHealthScale(p.getHealth());
					p.damage(0);
					//p.kickPlayer("Custom HP were changed. Please join back now.");
				}		
			}
		}
	}
}