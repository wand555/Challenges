package me.wand555.Challenges.API.Events.SettingsChange;

import org.bukkit.entity.Player;

import me.wand555.Challenges.API.Events.Overridable;
import me.wand555.Challenges.ChallengeProfile.ChallengeTypes.ItemCollectionLimitChallenge.ItemCollectionLimitGlobalChallenge;

public class ItemCollectionLimitGlobalChallengeStatusSwitchEvent extends ChallengeStatusSwitchEvent<ItemCollectionLimitGlobalChallenge> implements Overridable {

	private int limit;
	
	public ItemCollectionLimitGlobalChallengeStatusSwitchEvent(ItemCollectionLimitGlobalChallenge challenge, int limit, Player player) {
		super(challenge, player);
		this.setLimit(limit);
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	@Override
	public String getOverrideMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOverrideMessage(String overrideMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasOverrideMessage() {
		// TODO Auto-generated method stub
		return false;
	}

}
