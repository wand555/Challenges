package me.wand555.Challenges.ChallengeProfile.ChallengeTypes.MLGChallenge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import me.wand555.Challenges.Challenges;
import me.wand555.Challenges.ChallengeProfile.ChallengeEndReason;
import me.wand555.Challenges.ChallengeProfile.ChallengeProfile;
import me.wand555.Challenges.ChallengeProfile.ChallengeTypes.ChallengeType;
import me.wand555.Challenges.ChallengeProfile.ChallengeTypes.GenericChallenge;
import me.wand555.Challenges.Config.LanguageMessages;
import me.wand555.Challenges.Config.WorldUtil;

public class MLGChallenge extends GenericChallenge {

	/**
	 * This map only holds entries of players who are currently in the MLG world.
	 * The boolean is whether they failed or beaten the MLG.
	 * not done = null
	 * beaten = true
	 * failed = false
	 */
	private HashMap<UUID, Boolean> inMLGWorld = new HashMap<UUID, Boolean>();
	private int earliest;
	private int latest;
	private int height;
	private MLGTimer timer;
	private final World world;
	
	
	public MLGChallenge() {
		super(ChallengeType.MLG);
		this.world = Bukkit.getWorld("MLGWorld");
		activeChallenges.put(type, this);
	}

	@Override
	public ItemStack getDisplayItem() {
		return createItem(Material.WATER_BUCKET, 
				LanguageMessages.guiRandomMLGName, 
				new ArrayList<String>(LanguageMessages.guiMLGLore),
				super.active);
	}
	
	public void onMLGPrepare(Set<Player> players) {
		if(players.size() == 0) return;
		ChallengeProfile.getInstance().setInMLGRightNow();
		double factor = 0.5;
		for(Player p : players) {
			WorldUtil.storePlayerInformationInChallenge(p);
			p.getInventory().clear();
			p.getInventory().setItemInMainHand(new ItemStack(Material.WATER_BUCKET));
			p.setGameMode(GameMode.SURVIVAL);
			p.teleport(new Location(world, factor, height+4, 0.5));
			inMLGWorld.put(p.getUniqueId(), null);
			factor += 20;
		}
		factor = 0.5;
	}
	
	public void onMLGDone(Player p, boolean beaten) {		
		if(beaten) {
			WorldUtil.loadPlayerInformationInChallengeAndApply(p);
			inMLGWorld.remove(p.getUniqueId());
			if(inMLGWorld.isEmpty()) {
				System.out.println("was empty");
				this.setTimer(new MLGTimer(Challenges.getPlugin(Challenges.class), this));
				ChallengeProfile.getInstance().setInMLGRightNow();
			}
		}
		else {
			inMLGWorld.keySet().stream()
				.map(Bukkit::getPlayer)
				.filter(p1 -> p1 != null)
				.forEach(WorldUtil::loadPlayerInformationInChallengeAndApply);
			ChallengeProfile.getInstance().endChallenge(p, ChallengeEndReason.FAILED_MLG);
			ChallengeProfile.getInstance().setInMLGRightNow();
			inMLGWorld.clear();
		}
	}
	
	public void addPlayerToMLGWorld(UUID uuid) {
		inMLGWorld.put(uuid, false);
	}
	
	public void removePlayerFromMLGWorld(UUID uuid) {
		inMLGWorld.remove(uuid);
	}
	
	public HashMap<UUID, Boolean> getInMLGWorld() {
		return inMLGWorld;
	}

	/**
	 * @return the earliest
	 */
	public int getEarliest() {
		return earliest;
	}

	/**
	 * @param earliest the earliest to set
	 */
	public void setEarliest(int earliest) {
		this.earliest = earliest;
	}

	/**
	 * @return the latest
	 */
	public int getLatest() {
		return latest;
	}

	/**
	 * @param latest the latest to set
	 */
	public void setLatest(int latest) {
		this.latest = latest;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return the world
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * @param inMLGWorld the inMLGWorld to set
	 */
	public void setInMLGWorld(HashMap<UUID, Boolean> inMLGWorld) {
		this.inMLGWorld = inMLGWorld;
	}

	/**
	 * @return the timer
	 */
	public MLGTimer getTimer() {
		return timer;
	}

	/**
	 * @param timer the timer to set
	 */
	public void setTimer(MLGTimer timer) {
		this.timer = timer;
	}
}