package me.wand555.Challenges;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import me.wand555.Challenges.ChallengeProfile.Backpack;
import me.wand555.Challenges.ChallengeProfile.ChallengeProfile;
import me.wand555.Challenges.ChallengeProfile.Positions.Position;
import me.wand555.Challenges.ChallengeProfile.Positions.PositionManager;
import me.wand555.Challenges.Config.LanguageMessages;
import me.wand555.Challenges.Config.WorldUtil;
import me.wand555.GUI.GUI;
import me.wand555.GUI.GUIType;

public class CE implements CommandExecutor {
	
	private GUI gui;
	
	public CE(GUI gui) {
		this.gui = gui;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("Only for players");
			return false;
		}
		
		ChallengeProfile cProfile = ChallengeProfile.getInstance();
		Player player = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("challenge")) {
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("join")) {
					if(player.hasPermission("challenge.join")) {
						if(!ChallengeProfile.getInstance().isDone) {
							if(!ChallengeProfile.getInstance().isInChallenge(player.getUniqueId())) {
								//load player in challenge information
								//store normal world information
								WorldUtil.storePlayerInformationBeforeChallenge(player);
								WorldUtil.loadPlayerInformationInChallengeAndApply(player);
								
								ChallengeProfile.getInstance().addToParticipants(player.getUniqueId());
								player.sendMessage(LanguageMessages.teleportMsg);
							}
							else {
								player.sendMessage(LanguageMessages.alreadyInChallenge);
							}
						}
						else {
							player.sendMessage(LanguageMessages.noChallengeToJoin);
						}
					}
				}
				else if(args[0].equalsIgnoreCase("leave")) {
					if(player.hasPermission("challenge.leave")) {
						if(ChallengeProfile.getInstance().isInChallenge(player.getUniqueId())) {
							//store player in challenge information
							//load normal world information
							WorldUtil.storePlayerInformationInChallenge(player);
							WorldUtil.loadPlayerInformationBeforeChallengeAndApply(player);
							
							ChallengeProfile.getInstance().removeFromParticipants(player.getUniqueId());
							player.sendMessage(LanguageMessages.teleportMsg);
						}
						else {
							player.sendMessage(LanguageMessages.notInChallenge);
						}
					}
				}
				else if(args[0].equalsIgnoreCase("restore")) {
					if(player.hasPermission("challenge.restore")) {
						if(ChallengeProfile.getInstance().isDone) {
							ChallengeProfile.getInstance().restoreChallenge();
						}
						else {
							player.sendMessage(LanguageMessages.noChallengeToRestore);
						}
						
					}
				}
				else if(args[0].equalsIgnoreCase("reset")) {
					if(player.hasPermission("challenge.reset")) {
						ChallengeProfile.getInstance().resetChallenge();
						player.sendMessage(LanguageMessages.deletedChallengeWorlds);
						player.sendMessage(LanguageMessages.resetWarning);
					}
				}
				else {
					player.sendMessage(LanguageMessages.challengeOptionSyntax);
				}
			}
			else {
				player.sendMessage(LanguageMessages.challengeOptionSyntax);
			}
		}
		else if(cmd.getName().equalsIgnoreCase("timer")) {
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("start")) {
					if(player.hasPermission("timer.start")) {
						if(ChallengeProfile.getInstance().isInChallenge(player.getUniqueId())) {
							if(!ChallengeProfile.getInstance().hasStarted) {
								ChallengeProfile.getInstance().startTimer();
							}
							else {
								player.sendMessage(LanguageMessages.timerAlreadyStarted);
							}
						}
						else {
							player.sendMessage(LanguageMessages.notInChallenge);
						}
					}
				}
				else if(args[0].equalsIgnoreCase("pause")) {
					if(player.hasPermission("timer.pause")) {
						if(ChallengeProfile.getInstance().isInChallenge(player.getUniqueId())) {
							if(ChallengeProfile.getInstance().canTakeEffect()) {
								ChallengeProfile.getInstance().pauseTimer();
							}
							else if(ChallengeProfile.getInstance().isPaused && !ChallengeProfile.getInstance().isDone) {
								ChallengeProfile.getInstance().resumeTimer();
							}
							else {
								player.sendMessage(LanguageMessages.noPauseBecauseNotRunning);
							}
						}
						else {
							player.sendMessage(LanguageMessages.notInChallenge);
						}
					}
				}
				else {
					player.sendMessage(LanguageMessages.timerOptionSyntax);
				}
			}
			else {
				player.sendMessage(LanguageMessages.timerOptionSyntax);
			}
		}
		else if(cmd.getName().equalsIgnoreCase("pos")) {
			PositionManager posManager = ChallengeProfile.getInstance().getPosManager();
			if(args.length == 0) {
				if(player.hasPermission("challenge.pos.list")) {
					if(posManager.getPositions().isEmpty()) player.sendMessage("NO POS EXIST ADD TRANSLATION");
					posManager.getPositions().forEach(pos -> {
						player.sendMessage(posManager.displayPosition(pos));
					});
				}
			}
			else if(args.length == 1) {
				if(player.hasPermission("challenge.pos.add")) {
					if(posManager.positionWithNameExists(args[0])) {
						player.sendMessage(posManager.displayPosition(posManager.getPositionFromName(args[0])));
					}
					else {
						posManager.addToPositions(new Position(args[0], player.getLocation(), player.getUniqueId(), new Date()));
						player.sendMessage(LanguageMessages.registeredPosition.replace("[POS]", args[0]));
					}
				}
			}
			else {
				player.sendMessage(LanguageMessages.positionSyntax);
			}
		}
		else if(cmd.getName().equalsIgnoreCase("bp")) {
			if(args.length == 0) {
				if(player.hasPermission("challenge.bp")) {
					if(ChallengeProfile.getInstance().isInChallenge(player.getUniqueId())) {
						gui.createGUI(player, GUIType.BACKPACK);
					}
					else {
						player.sendMessage(LanguageMessages.notInChallenge);
					}
				}
			}
			else {
				player.sendMessage(LanguageMessages.bpSyntax);
			}
		}
		else if(cmd.getName().equalsIgnoreCase("hp")) {
			if(args.length == 2) {
				if(player.hasPermission("challenge.hp")) {
					if(ChallengeProfile.getInstance().isInChallenge(player.getUniqueId())) {
						@SuppressWarnings("deprecation")
						OfflinePlayer offlinetarget = Bukkit.getOfflinePlayer(args[1]);
						if(offlinetarget.isOnline()) {
							Player target = (Player) offlinetarget;
							if(ChallengeProfile.getInstance().isInChallenge(player.getUniqueId())) {
								if(StringUtils.isNumeric(args[0])) {
									double number = Double.valueOf(args[0]);
									target.setHealth(number < 0 ? 
											0 : 
										number > target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() ? 
												target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() 
												: number);
									player.sendMessage(LanguageMessages.setHP);
								}
								else {
									player.sendMessage(LanguageMessages.notANumber.replace("[NUMBER]", args[0]));
								}
							}
							else {
								//target not in challenge
							}
						}
						else {
							player.sendMessage(LanguageMessages.playerNotOnline.replace("[PLAYER]", offlinetarget.getName()));
						}
					}
					else {
						player.sendMessage(LanguageMessages.notInChallenge);
					}
				}
			}
			else {
				player.sendMessage(LanguageMessages.hpOptionSyntax);
			}
		}
		else if(cmd.getName().equalsIgnoreCase("settings")) {
			if(args.length == 0) {
				if(player.hasPermission("challenge.settings.view")) {
					if(cProfile.isInChallenge(player.getUniqueId())) {
						if(cProfile.isPaused || !cProfile.hasStarted) {
							//create settings gui
							gui.createGUI(player, GUIType.OVERVIEW);
						}
						else {
							player.sendMessage(LanguageMessages.noSettingsHasToBePaused);
						}
					}
					else {
						player.sendMessage(LanguageMessages.notInChallenge);
					}
				}
			}
			else {
				player.sendMessage(LanguageMessages.settingSyntax);
			}
		}
		return true;
	}
}