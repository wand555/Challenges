package me.wand555.Challenges.Timer;

import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import me.wand555.Challenges.Challenges;
import me.wand555.Challenges.ChallengeProfile.ChallengeProfile;
import me.wand555.Challenges.Config.LanguageMessages;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class SecondTimer extends BukkitRunnable {

	private TimerMessage messageType;
	private long time;
	
	public SecondTimer(Challenges plugin, TimerMessage messageType) {
		this.messageType = messageType;
		this.runTaskTimer(plugin, 0L, 20L);
	}
	
	public SecondTimer(Challenges plugin, long time) {
		this.time = time;
		this.messageType = time == 0 ? TimerMessage.START_TIMER : TimerMessage.TIMER_PAUSED;
		this.runTaskTimer(plugin, 0L, 20L);
	}
	
	@Override
	public void run() {
		ChallengeProfile cProfile = ChallengeProfile.getInstance();
		if(cProfile.getParticipants().isEmpty()) return;
		
		if(cProfile.hasStarted && !cProfile.isPaused) {
			this.time += 1;
			String displayTime = DateUtil.formatDuration(time);
			TextComponent component = new TextComponent(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + displayTime);
			cProfile.getParticipantsAsPlayers()
				.forEach(p -> p.spigot().sendMessage(ChatMessageType.ACTION_BAR, component));
		}
		else {
			TextComponent component;
			switch(messageType) {
			case START_TIMER:			
				component = new TextComponent(LanguageMessages.timerMessageStart);
				break;
			case TIMER_PAUSED:
				component = new TextComponent(LanguageMessages.timerMessagePause.replace("[TIME]", DateUtil.formatDuration(getTime())));
				break;
			case TIMER_FINISHED:
				component = new TextComponent(LanguageMessages.timerMessageFinished.replace("[TIME]", DateUtil.formatDuration(getTime())));
				break;
			default:
				component = new TextComponent(ChatColor.RED + "ERROR! PLEASE RESTART THE SERVER!");
				break;
			}
			//System.out.println(component);
			cProfile.getParticipantsAsPlayers()
				.forEach(p -> p.spigot().sendMessage(ChatMessageType.ACTION_BAR, component));
		}
	}
	
	public long getTime() {
		return this.time;
	}
	
	public void setTime(long time) {
		this.time = time;
	}

	public TimerMessage getMessageType() {
		return this.messageType;
	}
	
	public void setMessageType(TimerMessage messageType) {
		this.messageType = messageType;
	}
}