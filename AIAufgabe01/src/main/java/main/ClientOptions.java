package main;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "Client stuff")
public class ClientOptions
{
	@Parameter(names = "-n", description = "Client Name")
	private String	name = null;
	
	@Parameter(names = "--start", description = "Start Client")
	private boolean	start;
	
	@Parameter(names = "--help", help = true, description = "Show this help.")
	private boolean help;
	

	public String getClientName()
	{
		return name;
	}
	
	public boolean getStart()
	{
		return start;
	}
}
