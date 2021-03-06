package client;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lenz.htw.kimpl.net.NetworkClient;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;


@Path("client") 
public class SpielClient extends Thread
{
	private String			gameServerIP;
	private String			intercomPort;
	private String			intercomIP;
	private String			clientName;
	private String			pathToImage;
	private HttpServer		restServer;
	private NetworkClient	netc;

	public SpielClient(String clientName, String gmaeServerIP, String pathToImage, String intercomPort, String intercomIP)
	{
		this.clientName = clientName;
		this.gameServerIP = gmaeServerIP;
		this.pathToImage = pathToImage;
		this.intercomPort = intercomPort;
		this.intercomIP = intercomIP;
		this.restServer = null;
		this.netc = null;
		
	}

	public void run()
	{
//		this.createRestServer();
		try
		{
			this.connectToGame();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void connectToGame() throws IOException
	{
		netc = new NetworkClient(this.gameServerIP, this.clientName,
				ImageIO.read(new File(this.pathToImage)));
		System.out.println(netc.getMyPlayerNumber());
	}

	private void createRestServer()
	{
		String HttpUrl = "http://" + this.intercomIP + ":" + this.intercomPort
				+ "/rest";
		System.out.println(HttpUrl);
		try
		{
			restServer = HttpServerFactory.create(HttpUrl);
		} catch ( IOException e)
		{
			e.printStackTrace();
		}
		restServer.start();
	}

	@GET @Path("stop") 
	@Produces(MediaType.TEXT_PLAIN) 
	public String stopClient()
	{
		System.out.println("Client " + this.clientName + "Stoped!");
		restServer.stop(0);
		System.exit(0);
		return "Stoped!";
	}
}
