package com.ulticraft.hc;

import net.md_5.bungee.api.ChatColor;

public class Info
{
	public static final String CMD_NOTES = "notes";
	public static final String NAME = "HarmoniCraft";
	public static final String VERSION = "3.1.27.1337";
	public static final String[] AUTHORS = new String[]{"Daniel Mills (cyberpwn)"};
	
	public static final char DINGBAT_PENTAGON = '\u2B1F';
	public static final char DINGBAT_HEXAGON = '\u2B22';
	public static final char DINGBAT_CHECK = '\u2B22';
	public static final char DINGBAT_NOTCHECK = '\u2B22';
	public static final char DINGBAT_BACK = '\u2B22';
		
	public static final String UI_ACTION_BACK = ChatColor.RED + "" + DINGBAT_BACK + " Back";
	
	public static final String PERM_GOD = "harmonicraft.god";
		
	public static final String TAG = ChatColor.YELLOW + "[" + ChatColor.WHITE + "%s" + ChatColor.YELLOW + "]" + ChatColor.GOLD + ": ";
	public static final String TAG_NOTES = String.format(TAG, "Notes");
	public static final String TAG_HARMONICRAFT = String.format(TAG, "HarmoniCraft");
		
	public static final String[] CMD_HELP_NOTES = new String[]{TAG_NOTES + ChatColor.YELLOW + "/notes " + ChatColor.WHITE + "- Get current Notes", TAG_NOTES + ChatColor.YELLOW + "/notes buy" + ChatColor.WHITE + "- Open the note gui menu"};
	public static final String[] CMD_HELP_ADMIN_NOTES = new String[]{TAG_NOTES + ChatColor.YELLOW + "/notes " + ChatColor.WHITE + "- Get current notes", Info.TAG_NOTES + ChatColor.YELLOW + "/notes get " + ChatColor.GOLD + "<player> " + ChatColor.WHITE + "- Get a player's notes", TAG_NOTES + ChatColor.YELLOW + "/notes give " + ChatColor.GOLD + "[player] [notes] " + ChatColor.WHITE + "- Give a player notes", TAG_NOTES + ChatColor.YELLOW + "/notes take " + ChatColor.GOLD + "[player] [notes] " + ChatColor.WHITE + "- Take player notes", TAG_NOTES + ChatColor.YELLOW + "/notes buy " + ChatColor.WHITE + "- Open the note gui menu", TAG_NOTES + ChatColor.RED + "/notes import " + ChatColor.WHITE + "- Import old data. OVERWRITES CURRENT DATA!"};
	
	public static final String MSG_NO_PERMISSION = TAG_HARMONICRAFT + ChatColor.RED + "Insufficient Permissions";
	public static final String MSG_CANT_FIND_PLAYER = TAG_HARMONICRAFT + ChatColor.RED + "Cannot find player '%s'";
	public static final String MSG_NOT_POSITIVE = TAG_HARMONICRAFT + ChatColor.RED + "%s is clearly not positive.";
	public static final String MSG_NOT_NEGATIVE = TAG_HARMONICRAFT + ChatColor.RED + "%s is clearly not negative.";
	public static final String MSG_NOT_ZERO = TAG_HARMONICRAFT + ChatColor.RED + "%s is zero. Fix that.";
	public static final String MSG_NOT_NUMBER = TAG_HARMONICRAFT + ChatColor.RED + "'%s' is not a number. It's Trash.";
	public static final String MSG_PLAYER_GEM_HAS = TAG_HARMONICRAFT + ChatColor.YELLOW + "%s" + ChatColor.WHITE + " has " + ChatColor.YELLOW + "%s Notes";
	public static final String MSG_PLAYER_ONLY = TAG_HARMONICRAFT + ChatColor.RED + "Player Only Command";
	public static final String MSG_CONSOLE_ONLY = TAG_HARMONICRAFT + ChatColor.RED + "Console Only Command";
	public static final String MSG_UNKNOWN_SUB_COMMAND = TAG_HARMONICRAFT + ChatColor.RED + "Unknown Sub Command '%s'";
	public static final String MSG_NOTES_GAVE = TAG_NOTES + ChatColor.WHITE + "Gave " + ChatColor.YELLOW + "%s Notes" + ChatColor.WHITE + " to " + ChatColor.YELLOW + "%s";
	public static final String MSG_NOTES_TOOK = TAG_NOTES + ChatColor.WHITE + "Took " + ChatColor.YELLOW + "%s Notes" + ChatColor.WHITE + " from " + ChatColor.YELLOW + "%s";
	public static final String MSG_NOTES_NOT_ENOUGH = TAG_NOTES + ChatColor.WHITE + "Nope. " + ChatColor.YELLOW + "%s" + ChatColor.WHITE + " only has " + ChatColor.YELLOW + "%s Notes";
	
	public static final String MSG_NOTES_HAVE = TAG_NOTES + ChatColor.WHITE + "You have " + ChatColor.YELLOW + "%s Notes";
	public static final String MSG_NOTES_SPENT = ChatColor.WHITE + "Spent " + ChatColor.YELLOW + "%s Notes";
	public static final String MSG_NOTES_EARNED = ChatColor.WHITE + "Earned " + ChatColor.YELLOW + "%s Notes";
	public static final String MSG_PERK_UNLOCK = ChatColor.WHITE + "Unlocked " + ChatColor.YELLOW + "%s";
	public static final String MSG_ACHIEVEMENT = ChatColor.WHITE + "Achieved " + ChatColor.YELLOW + "%s %s";
	public static final String MSG_UPGRADED = ChatColor.WHITE + "Updated Ulticraft " + ChatColor.YELLOW + "v" + VERSION;
	public static final String MSG_DEVELOPED_BY = ChatColor.YELLOW + "Developed by";
	
	public static final String BROAD_ACHIEVEMENT = ChatColor.YELLOW + "%s " + ChatColor.WHITE + "Achieved " + ChatColor.YELLOW + "%s %s";
	public static final String BROAD_PERK_UNLOCK = ChatColor.YELLOW + "%s " + ChatColor.WHITE + "Unlocked " + ChatColor.YELLOW + "%s";
	
	public static final int MANA_BAR_SPLIT = 20;	
}
