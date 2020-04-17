package me.wand555.Challenges;

import java.util.Random;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.World.Environment;
import org.bukkit.plugin.java.JavaPlugin;

import me.wand555.Challenges.ChallengeProfile.ChallengeProfile;
import me.wand555.Challenges.ChallengeProfile.AssignRoleListener.PlayerJoinListener;
import me.wand555.Challenges.ChallengeProfile.AssignRoleListener.PlayerQuitListener;
import me.wand555.Challenges.ChallengeProfile.ChallengeTypes.ChallengeType;
import me.wand555.Challenges.ChallengeProfile.ChallengeTypes.GenericChallenge;
import me.wand555.Challenges.ChallengeProfile.ChallengeTypes.NoBlockBreakingChallenge;
import me.wand555.Challenges.ChallengeProfile.ChallengeTypes.NoBlockPlacingChallenge;
import me.wand555.Challenges.ChallengeProfile.ChallengeTypes.Punishable;
import me.wand555.Challenges.ChallengeProfile.ChallengeTypes.ReasonNotifiable;
import me.wand555.Challenges.Config.ConfigHandler;
import me.wand555.Challenges.Config.Language;
import me.wand555.Challenges.Config.LanguageMessages;
import me.wand555.Challenges.Config.UserConfig;
import me.wand555.Challenges.Listener.BackpackListener;
import me.wand555.Challenges.Listener.MLGListener;
import me.wand555.Challenges.Listener.NoBlockBreakingListener;
import me.wand555.Challenges.Listener.NoBlockPlacingListener;
import me.wand555.Challenges.Listener.NoCraftingListener;
import me.wand555.Challenges.Listener.NoDamageListener;
import me.wand555.Challenges.Listener.NoRegListener;
import me.wand555.Challenges.Listener.NoSneakingListener;
import me.wand555.Challenges.Listener.PlayerDeathListener;
import me.wand555.Challenges.Listener.RandomizeListener;
import me.wand555.Challenges.Listener.SharedHealthPlayerChangeLifeListener;
import me.wand555.Challenges.Timer.SecondTimer;
import me.wand555.Challenges.Timer.TimerMessage;
import me.wand555.Challenges.WorldLinkingManager.WorldLinkManager;
import me.wand555.GUI.GUI;
import me.wand555.GUI.GUIClickListener;
import me.wand555.GUI.SignMenuFactory;
import me.wand555.NetherLinking.PortalListener;

public class Challenges extends JavaPlugin {

	public static final String PREFIX = ChatColor.GRAY + "[" + ChatColor.GREEN + "Challenge" + ChatColor.GRAY + "] ";
	public static final Random random = new Random();
	
	private CE myCE;
	private GUI gui;
	private SignMenuFactory signMenuFactory;
	
	public void onEnable() {
		UserConfig.setUpDefaultConfig();
		UserConfig.placeLanguages();
		String lang = this.getConfig().getString("Language");
		if(lang.equalsIgnoreCase("en")) LanguageMessages.loadLang(Language.ENGLISH);
		else if(lang.equalsIgnoreCase("de")) LanguageMessages.loadLang(Language.GERMAN);
		else LanguageMessages.loadLang(Language.ENGLISH);
		
		
		WorldLinkManager.worlds.add(Bukkit.createWorld(new WorldCreator("ChallengeOverworld").hardcore(true).environment(Environment.NORMAL)));
		WorldLinkManager.worlds.add(Bukkit.createWorld(new WorldCreator("ChallengeNether").hardcore(true).environment(Environment.NETHER)));
		WorldLinkManager.worlds.add(Bukkit.createWorld(new WorldCreator("ChallengeEnd").hardcore(true).environment(Environment.THE_END)));
		World mlgWorld = new WorldCreator("MLGWorld")
				.environment(Environment.NORMAL)
				.type(WorldType.FLAT)
				.generateStructures(false)
				.createWorld();
		mlgWorld.setDifficulty(Difficulty.PEACEFUL);
		mlgWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
		WorldLinkManager.worlds.add(mlgWorld);
		
		ConfigHandler.loadFromConfig();
		
		
		
		/**
		 * 
		 * ON LOAD EVERY CHALLENGE INSTANCE HAS TO BE CREATED ONCE SO IT IS NEVER NULL, DONT DO THAT HERE, DO IT
		 * WHEN LOADING FROM CONFIG WITH THE STORED VALUES (boolean)!!!!!!!!!!!
		 * 
		 * 
		 */
		
		gui = new GUI(this);
		signMenuFactory = new SignMenuFactory(this);
		
		myCE = new CE(gui);
		this.getCommand("challenge").setExecutor(myCE);
		this.getCommand("timer").setExecutor(myCE);
		this.getCommand("pos").setExecutor(myCE);
		this.getCommand("bp").setExecutor(myCE);
		this.getCommand("hp").setExecutor(myCE);
		this.getCommand("settings").setExecutor(myCE);
		
		registerListeners();
	}
	
	public void onDisable() {
		ChallengeProfile.getInstance().pauseTimer();
		ConfigHandler.storeToConfig();
	}
	
	private void registerListeners() {
		new PlayerJoinListener(this);
		new PlayerQuitListener(this);
		new GUIClickListener(this, gui, signMenuFactory);
		new PortalListener(this);
		new PlayerDeathListener(this);
		new NoDamageListener(this);
		new NoRegListener(this);
		new SharedHealthPlayerChangeLifeListener(this);
		new NoBlockPlacingListener(this);
		new NoBlockBreakingListener(this);
		new NoCraftingListener(this);
		new NoSneakingListener(this);
		new RandomizeListener(this);
		new MLGListener(this);
		
		new BackpackListener(this);
	}
}