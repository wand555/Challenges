package me.wand555.challenges.settings.challengeprofile.types;

public enum ChallengeType {
END_ON_DEATH(false, false),
NETHER_FORTRESS_SPAWN(false, false),
NO_DAMAGE(true, false),
NO_REG(false, false),
NO_REG_HARD(false, false),
CUSTOM_HEALTH(false, true),
SHARED_HEALTH(false, false),
NO_BLOCK_PLACING(true, false),
NO_BLOCK_BREAKING(true, false),
NO_CRAFTING(true, false),
NO_SNEAKING(true, false),
RANDOMIZE_BLOCK_DROPS(false, false),
RANDOMIZE_MOB_DROPS(false, false),
RANDOMIZE_CRAFTING(false, false),
MLG(true, false),
ON_BLOCK(true, false),
ITEM_LIMIT_GLOBAL(false, true),
NO_SAME_ITEM(true, false),
GROUND_IS_LAVA(false, false),
BE_AT_HEIGHT(true, false)
;
	/**
	 * If the challenge is punishable
	 */
	private final boolean punishable;
	/**
	 * If the challenge holds any numbers/values to be displayed in the GUI.
	 * Hence that's why MLG/ForceBlock is false, despite having numbers interally.
	 */
	private final boolean amountable;
	
	ChallengeType(boolean punishable, boolean amountable) {
		this.punishable = punishable;
		this.amountable = amountable;
	}

	public boolean isPunishable() {
		return this.punishable;
	}
	
	public boolean isAmountable() {
		return this.amountable;
	}
}
