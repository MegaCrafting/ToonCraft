package com.megacraft.tooncraft.commands;

import java.util.List;

import org.bukkit.entity.Player;

/**
 * Interface representation of a command executor.
 */
public interface SubCommand {
	/**
	 * Gets the name of the command.
	 * 
	 * @return The command's name
	 */
	public String getName();

	/**
	 * Gets the aliases for the command.
	 * 
	 * @return All aliases for the command
	 */
	public String[] getAliases();

	/**
	 * Gets the proper use of the command, in the format '/b
	 * {@link MBCommand#name name} arg1 arg2 ... '
	 * 
	 * @return the proper use of the command
	 */
	public String getProperUse();

	/**
	 * Gets the description of the command.
	 * 
	 * @return the description
	 */
	public String getDescription();

	/**
	 * Outputs the correct usage, and optionally the description, of a command
	 * to the given {@link Player}.
	 * 
	 * @param sender The Player to output the help to
	 * @param description Whether or not to output the description of the
	 *            command
	 */
	public void help(Player sender, boolean description);

	/**
	 * Executes the command.
	 * 
	 * @param sender The Player who issued the command
	 * @param args the command's arguments
	 */
	public void execute(Player sender, List<String> args);
}
