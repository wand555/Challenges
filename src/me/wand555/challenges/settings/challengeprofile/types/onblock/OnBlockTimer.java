package me.wand555.challenges.settings.challengeprofile.types.onblock;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.wand555.challenges.api.events.violation.CallViolationEvent;
import me.wand555.challenges.settings.challengeprofile.ChallengeEndReason;
import me.wand555.challenges.settings.challengeprofile.ChallengeProfile;
import me.wand555.challenges.settings.challengeprofile.types.BossBarStatus;
import me.wand555.challenges.settings.challengeprofile.types.PunishType;
import me.wand555.challenges.settings.config.DisplayUtil;
import me.wand555.challenges.settings.config.LanguageMessages;
import me.wand555.challenges.start.Challenges;

public class OnBlockTimer extends BukkitRunnable implements CallViolationEvent {
	
	private long totalTimeTo;
	private long timeTo;
	
	private OnBlockChallenge onBlockChallenge;
	
	/**
	 * Called when the player enabled the onBlockChallenge. It will start fresh from the beginning (once the timer is running).
	 * @param plugin
	 * @param onBlockChallenge
	 */
	public OnBlockTimer(Challenges plugin, OnBlockChallenge onBlockChallenge) {
		this.onBlockChallenge = onBlockChallenge;
		timeTo = ThreadLocalRandom.current()
				.nextLong(onBlockChallenge.getEarliestToShow(), (onBlockChallenge.getLatestToShow()+1));
		totalTimeTo = timeTo;
		this.runTaskTimer(plugin, 0L, 20L);
		onBlockChallenge.setDefaults();
	}
	
	public OnBlockTimer(Challenges plugin, OnBlockChallenge onBlockChallenge, long totalTimeTo, long timeTo, boolean firstCreation) {
		this.totalTimeTo = totalTimeTo;
		this.timeTo = timeTo;
		this.onBlockChallenge = onBlockChallenge;
		if(firstCreation) {
			onBlockChallenge.setDefaults();
		}
		else {
			if(onBlockChallenge.getStatus() == BossBarStatus.SHOWN) {
				onBlockChallenge.setBossBarMessageShown(LanguageMessages.onBlockShown
						.replace("[BLOCK]", WordUtils.capitalize(onBlockChallenge.getToStayOn().toString().toLowerCase().replace('_', ' '))));
			}
		}
		
		this.runTaskTimer(plugin, 0L, 20L);
	}
	
	@Override
	public void run() {
		if(ChallengeProfile.getInstance().getParticipants().size() == 0) return;
		if(!ChallengeProfile.getInstance().canTakeEffect()) {
			//ChallengeProfile.getInstance().getParticipantsAsPlayers().stream().forEach(p -> onBlockChallenge.removePlayerFromBossBar(p));
			//this.cancel();
			return;
		}
		System.out.println(timeTo);
		if(timeTo <= 0) {
			//System.out.println(onBlockChallenge.getStatus());
			if(onBlockChallenge.getStatus() == BossBarStatus.HIDDEN) {
				timeTo = ThreadLocalRandom.current()
						.nextLong(onBlockChallenge.getEarliestOnBlock(), (onBlockChallenge.getLatestOnBlock()+1));
				totalTimeTo = timeTo;
				onBlockChallenge.fromHiddenToShownChange(totalTimeTo);
				
			}
			else {
				
				Set<Player> notStandingOnBlock = ChallengeProfile.getInstance().getParticipants().stream()
					.filter(p -> !playersOnBlock(p)).collect(Collectors.toSet());
				if(notStandingOnBlock.size() != 0) {
					if(onBlockChallenge.getPunishType() == PunishType.CHALLENGE_OVER) {
						ChallengeProfile.getInstance()
							.endChallenge(onBlockChallenge, 
									ChallengeEndReason.NOT_ON_BLOCK, 
									new Object[] {DisplayUtil.displayMaterial(onBlockChallenge.getToStayOn())},
									notStandingOnBlock.toArray(new Player[notStandingOnBlock.size()]));
						timeTo = ThreadLocalRandom.current()
								.nextLong(onBlockChallenge.getEarliestToShow(), (onBlockChallenge.getLatestToShow()+1));
						totalTimeTo = timeTo;
						onBlockChallenge.fromShownToHiddenChange();
					}
					else {
						Player[] causers = notStandingOnBlock.toArray(new Player[notStandingOnBlock.size()]);
						String message = onBlockChallenge.createReasonMessage(onBlockChallenge.getPunishCause(),
								onBlockChallenge.getPunishType(), causers);
						callForceBlockEventAndActUpon(onBlockChallenge, message, onBlockChallenge.getToStayOn(), causers);
						
						timeTo = ThreadLocalRandom.current()
								.nextLong(onBlockChallenge.getEarliestToShow(), (onBlockChallenge.getLatestToShow()+1));
						totalTimeTo = timeTo;
						onBlockChallenge.fromShownToHiddenChange();
					}
					
				}
				else {
					String message = onBlockChallenge.createLogMessage(onBlockChallenge.getPunishCause());
					ChallengeProfile.getInstance().sendMessageToAllParticipants(message);
					timeTo = ThreadLocalRandom.current()
							.nextLong(onBlockChallenge.getEarliestToShow(), (onBlockChallenge.getLatestToShow()+1));
					totalTimeTo = timeTo;
					onBlockChallenge.fromShownToHiddenChange();
				}	
				
			}
		}
		else {	
			timeTo -= 1;		
			if(onBlockChallenge.getStatus() == BossBarStatus.SHOWN) {
				onBlockChallenge.adjustProgress(timeTo < 0 ? 0 : timeTo);
				onBlockChallenge.adjustColorIfCase();
			}
			
		}
	}
	
	private boolean playersOnBlock(Player player) {
		Material toStandOn = onBlockChallenge.getToStayOn();
		World world = player.getWorld();
		for(int x=-1; x<=1; x++) {
			for(int y=-1; y<=1; y++) {
				for(int z=-1; z<=1; z++) {
					Block block = world.getBlockAt(player.getLocation().add(x, y, z));
					if(block.getType() == toStandOn) return true;
				}
			}
		}
		return false;
	}

	/**
	 * @return the timeTo
	 */
	public long getTimeTo() {
		return timeTo;
	}

	/**
	 * @param timeTo the timeTo to set
	 */
	public void setTimeTo(long timeTo) {
		this.timeTo = timeTo;
	}

	/**
	 * @return the totalTimeTo
	 */
	public long getTotalTimeTo() {
		return totalTimeTo;
	}

	/**
	 * @param totalTimeTo the totalTimeTo to set
	 */
	public void setTotalTimeTo(long totalTimeTo) {
		this.totalTimeTo = totalTimeTo;
	}

}
