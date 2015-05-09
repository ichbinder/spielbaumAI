package main;

import com.beust.jcommander.Parameter;

public class Options
{
	@Parameter(names = "-g", description = "Comma-separated list of group names to be run") 
	private String	groups;

	/**
	 * @return the groups
	 */
	public String getGroups()
	{
		return groups;
	}
	
	
}
