package me.wand555.Challenge.DeathRun.Conversations.Prompts;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

import me.wand555.Challenge.DeathRun.Conversations.ConversationsHandler;
import me.wand555.Challenge.DeathRun.Conversations.DeathRunSettingType;
import me.wand555.Challenges.Challenges;

public class DistancePrompt extends NumericPrompt {

	@Override
	protected boolean isNumberValid(ConversationContext context, Number number) {
		boolean isNumeric = super.isNumberValid(context, number);
		if(!isNumeric) return false;
		if(number.intValue() > 0 && number.intValue() <= 10e6) return true;
		return false;
	}
	
	@Override
	public String getPromptText(ConversationContext context) {
		return Challenges.PREFIX + ChatColor.GRAY + "What should the distance to reach be?";
	}

	@Override
	public Prompt acceptValidatedInput(ConversationContext context, Number number) {
		ConversationsHandler handler = ConversationsHandler.getConversationsHandler();
		Player player = (Player) context.getForWhom();
		handler.addAnswer(player, number.toString());
		context.getAllSessionData().put(DeathRunSettingType.DISTANCE, number.intValue());
		if(isNumberValid(context, number)) {
			String distanceOrTime = handler.getAnswers(player).get(1);
			return distanceOrTime.equalsIgnoreCase(DistanceOrTimePrompt.BOTH_GOAL) ? new TimePrompt() : new BorderPrompt();
		}
		else {
			context.getForWhom().sendRawMessage("Wrong entry");
			return this;
		}
		
	}
}