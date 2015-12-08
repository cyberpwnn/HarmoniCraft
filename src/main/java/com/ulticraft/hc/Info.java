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
	
	public static final String UI_TITLE_ULTICRAFT = ChatColor.DARK_GRAY + NAME;
	public static final String UI_TITLE_PERKS_GET = ChatColor.DARK_GRAY + "Get Perks";
	public static final String UI_TITLE_PERKS_MY = ChatColor.DARK_GRAY + "My Perks";
	
	public static final String UI_ACTION_BACK = ChatColor.RED + "" + DINGBAT_BACK + " Back";
	
	public static final String PERM_GOD = "harmonicraft.god";
		
	public static final String TAG = ChatColor.LIGHT_PURPLE + "[" + ChatColor.DARK_GRAY + "%s" + ChatColor.LIGHT_PURPLE + "]" + ChatColor.AQUA + ": ";
	public static final String TAG_NOTES = String.format(TAG, "Notes");
	public static final String TAG_HARMONICRAFT = String.format(TAG, "HarmoniCraft");
	
	public static final String CMD_GEM = "gem";
	public static final String CMD_ULTICRAFT = "ulticraft";
	public static final String CMD_MANA = "mana";
	public static final String CMD_PERK = "perk";
	public static final String CMD_SPELL = "spell";
	
	public static final String[] CMD_HELP_NOTES = new String[]{TAG_NOTES + ChatColor.LIGHT_PURPLE + "/notes " + ChatColor.DARK_GRAY + "- Get current Notes"};
	public static final String[] CMD_HELP_ADMIN_NOTES = new String[]{TAG_NOTES + ChatColor.LIGHT_PURPLE + "/notes " + ChatColor.DARK_GRAY + "- Get current notes", Info.TAG_NOTES + ChatColor.LIGHT_PURPLE + "/notes get " + ChatColor.AQUA + "<player> " + ChatColor.DARK_GRAY + "- Get a player's notes", TAG_NOTES + ChatColor.LIGHT_PURPLE + "/notes give " + ChatColor.AQUA + "[player] [notes] " + ChatColor.DARK_GRAY + "- Give a player notes", TAG_NOTES + ChatColor.LIGHT_PURPLE + "/notes take " + ChatColor.AQUA + "[player] [notes] " + ChatColor.DARK_GRAY + "- Take player notes"};
	
	public static final String MSG_NO_PERMISSION = TAG_HARMONICRAFT + ChatColor.RED + "Insufficient Permissions";
	public static final String MSG_CANT_FIND_PLAYER = TAG_HARMONICRAFT + ChatColor.RED + "Cannot find player '%s'";
	public static final String MSG_NOT_POSITIVE = TAG_HARMONICRAFT + ChatColor.RED + "%s is clearly not positive.";
	public static final String MSG_NOT_NEGATIVE = TAG_HARMONICRAFT + ChatColor.RED + "%s is clearly not negative.";
	public static final String MSG_NOT_ZERO = TAG_HARMONICRAFT + ChatColor.RED + "%s is zero. Fix that.";
	public static final String MSG_NOT_NUMBER = TAG_HARMONICRAFT + ChatColor.RED + "'%s' is not a number. It's Trash.";
	public static final String MSG_PLAYER_GEM_HAS = TAG_HARMONICRAFT + ChatColor.LIGHT_PURPLE + "%s" + ChatColor.DARK_GRAY + " has " + ChatColor.LIGHT_PURPLE + "%s Notes";
	public static final String MSG_PLAYER_ONLY = TAG_HARMONICRAFT + ChatColor.RED + "Player Only Command";
	public static final String MSG_CONSOLE_ONLY = TAG_HARMONICRAFT + ChatColor.RED + "Console Only Command";
	public static final String MSG_UNKNOWN_SUB_COMMAND = TAG_HARMONICRAFT + ChatColor.RED + "Unknown Sub Command '%s'";
	public static final String MSG_NOTES_GAVE = TAG_NOTES + ChatColor.DARK_GRAY + "Gave " + ChatColor.LIGHT_PURPLE + "%s Notes" + ChatColor.DARK_GRAY + " to " + ChatColor.LIGHT_PURPLE + "%s";
	public static final String MSG_NOTES_TOOK = TAG_NOTES + ChatColor.DARK_GRAY + "Took " + ChatColor.LIGHT_PURPLE + "%s Notes" + ChatColor.DARK_GRAY + " from " + ChatColor.LIGHT_PURPLE + "%s";
	public static final String MSG_NOTES_NOT_ENOUGH = TAG_NOTES + ChatColor.DARK_GRAY + "Nope. " + ChatColor.LIGHT_PURPLE + "%s" + ChatColor.DARK_GRAY + " only has " + ChatColor.LIGHT_PURPLE + "%s Notes";
	
	public static final String MSG_NOTES_HAVE = TAG_NOTES + ChatColor.DARK_GRAY + "You have " + ChatColor.LIGHT_PURPLE + "%s Notes";
	public static final String MSG_NOTES_SPENT = ChatColor.DARK_GRAY + "Spent " + ChatColor.LIGHT_PURPLE + "%s Notes";
	public static final String MSG_NOTES_EARNED = ChatColor.DARK_GRAY + "Earned " + ChatColor.LIGHT_PURPLE + "%s Notes";
	public static final String MSG_PERK_UNLOCK = ChatColor.DARK_GRAY + "Unlocked " + ChatColor.LIGHT_PURPLE + "%s";
	public static final String MSG_ACHIEVEMENT = ChatColor.DARK_GRAY + "Achieved " + ChatColor.LIGHT_PURPLE + "%s %s";
	public static final String MSG_UPGRADED = ChatColor.DARK_GRAY + "Updated Ulticraft " + ChatColor.LIGHT_PURPLE + "v" + VERSION;
	public static final String MSG_DEVELOPED_BY = ChatColor.LIGHT_PURPLE + "Developed by";
	
	public static final String BROAD_ACHIEVEMENT = ChatColor.LIGHT_PURPLE + "%s " + ChatColor.DARK_GRAY + "Achieved " + ChatColor.LIGHT_PURPLE + "%s %s";
	public static final String BROAD_PERK_UNLOCK = ChatColor.LIGHT_PURPLE + "%s " + ChatColor.DARK_GRAY + "Unlocked " + ChatColor.LIGHT_PURPLE + "%s";
	
	public static final int MANA_BAR_SPLIT = 20;	
}
