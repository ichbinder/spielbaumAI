package main;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "Client stuff")
public class ClientOptions
{
	@Parameter(names = {"-n", "--name"}, description = "Client Name")
	private String	name = "Client";
	
	@Parameter(names = {"-s", "--start"}, required = true, description = "Start Client")
	private boolean	start;
	
	@Parameter(names = {"-gip", "--gameServerIP"}, description = "Game Server IP Address")
	private String	gameServerIP = "localhost";
	
	@Parameter(names = {"-p", "--intercomPort"}, description = "Start Client")
	private String	intercomPort = "8080";
	
	@Parameter(names = {"-icip", "--intercomIP"}, description = "Start Client")
	private String	intercomIP = "localhost";
	
	@Parameter(names = {"-i", "--pathToImage"}, description = "Start Client")
	private String	pathToImage = "/META-INF/icon.png";
	
	@Parameter(names = {"-h", "--help"}, help = true, description = "Show this help.")
	private boolean help;
	

	public String getClientName()
	{
		return name;
	}
	
	public boolean getStart()
	{
		return start;
	}

	public String getGameServerIP()
	{
		return gameServerIP;
	}

	public String getIntercomPort()
	{
		return intercomPort;
	}

	public String getIntercomIP()
	{
		return intercomIP;
	}

	public String getPathToImage()
	{
		return pathToImage;
	}

	public boolean getHelp()
	{
		return help;
	}
	
	
}
