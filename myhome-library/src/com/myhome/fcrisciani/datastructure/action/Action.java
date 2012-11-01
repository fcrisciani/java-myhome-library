/**
 * The Action is defined as a list of command that have to be execute on the MyHome plant
 * They can have a list of identifier of sensors that can inhibit the action execution.
 */
package com.myhome.fcrisciani.datastructure.action;

import java.util.ArrayList;

import com.myhome.fcrisciani.datastructure.command.CommandOPEN;
import com.myhome.fcrisciani.datastructure.command.DelayInterval;

/**
 * @author Flavio Crisciani
 *
 */
public class Action {
	// ----- TYPES ----- //

	// ---- MEMBERS ---- //

	private ArrayList<CommandOPEN> commandList = null;				// List of OpenWebNet Commands to execute
	private boolean commandWithDelay = false;						// Indicates if this action contains a delay as a command
	private Integer[] sensorIdInhibitionList = null; 				// List of sensors id that can inhibit the Action execution
	private String description = null;								// Description associated to this action
	private int commandPriority = 3;								// Priority associated to the commands of this Action

	// ---- METHODS ---- //
	/**
	 * Create an Action specifying its description and the list of sensors to check before its execution
	 * @param description string to describe the action
	 * @param sensorIdInibitionList list of sensors ids
	 */
	public Action(String description, Integer[] sensorIdInibitionList) {
		super();
		this.commandList = new ArrayList<CommandOPEN>();
		this.description = description;
		this.sensorIdInhibitionList = sensorIdInibitionList;
	}
	/**
	 * Create an Action specifying its description, the list of sensors to check before its execution and the command priority
	 * @param description string to describe the action 
	 * @param sensorIdInibitionList list of sensors ids
	 * @param priority queue priority {1 = HIGH, 2 = MEDIUM, 3 = LOW} 
	 */
	public Action(String description, Integer[] sensorIdInibitionList, int priority) {
		this(description,sensorIdInibitionList);
		this.commandPriority= priority;
	}
	/**
	 * Add a command on top of the list, i.e. to reset the actuator state
	 * @param resetCommandList command to put on front of the command list
	 */
	public void addResetCommandProcedure(ArrayList<CommandOPEN> resetCommandList){
		commandWithDelay = true;
		commandList.addAll(0, resetCommandList);
	}
	/**
	 * Add a command to this action
	 * @param command command to put on front of the command list
	 */
	public void addCommand(CommandOPEN command){
		if (command instanceof DelayInterval) {
			commandWithDelay = true;
		}
		commandList.add(command);
	}

	/**
	 * Get the list of command of this Action
	 * @return list of command
	 */
	public ArrayList<CommandOPEN> getCommandList() {
		return commandList;
	}

	/**
	 * Get the list of Sensor id to be checked	
	 * @return list of sensor id
	 */
	public Integer[] getSensorIdInibitionList() {
		return sensorIdInhibitionList;
	}
	/**
	 * Get Action description
	 * @return Action description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * Get Action command priority
	 * @return command priority set
	 */
	public int getCommandPriority() {
		return commandPriority;
	}

	/**
	 * Check if this Action has a delay command in the list of commands
	 * @return true if the Action has a delay in the command list
	 */
	public boolean isCommandWithDelay() {
		return commandWithDelay;
	}

	@Override
	public String toString() {
		return "Action: " + description + " [commandList=" + commandList + "]";
	}

}
