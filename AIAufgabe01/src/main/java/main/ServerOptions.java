package main;

import com.beust.jcommander.Parameter;

public class ServerOptions
{
	@Parameter(names = {"-s", "--start"}, description = "Start Server.")
	private boolean start;
	
	@Parameter(names = {"-h", "--help"}, help = true, description = "Show this help.")
	private boolean help;

	public boolean getStart()
	{
		return start;
	}

	public boolean getHelp()
	{
		return help;
	}
	
	
}
