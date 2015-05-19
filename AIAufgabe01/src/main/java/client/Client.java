package client;

import java.awt.image.BufferedImage;

import client.agent.IAgent;
import client.game.Board;
import client.game.IBoard;
import lenz.htw.kimpl.Move;
import lenz.htw.kimpl.net.NetworkClient;

public class Client implements IClient, Runnable {
	
	private String Name;
	private String Server = null;
	private NetworkClient GameSocket = null;
	private Thread BotThread = null;
	private byte PlayerID = 0;
	private boolean Ingame = true;
	
	private IBoard GameBoard = null;
	private IAgent Bot = null;
	
	public Client(String Name, IAgent Bot)
	{
		this.Name = Name;
		this.Bot = Bot;
	}

	public void ConnectToLocalhost() {
		Connect(null);	
	}

	public void Connect(String Server) {
		this.Server = Server;
		BotThread = new Thread(this);
		BotThread.start();
	}

	public void run() {
		Setup();
		Loop();
		CleanUp();
	}
	
	private void Setup()
	{
		BufferedImage Img = new BufferedImage(256, 256, BufferedImage.TYPE_3BYTE_BGR);
		GameSocket = new NetworkClient(Server, Name, Img);
		
		PlayerID = (byte) GameSocket.getMyPlayerNumber();		
		GameBoard = new Board(PlayerID);
	}
	
	private void Loop()
	{
		Move Buffer;
		
		while(Ingame)
		{
			//Process Others Move
			while((Buffer = GameSocket.receiveMove()) != null)
			{		
				GameBoard.ProcessMove(Buffer);
			}

			System.out.println(PlayerID + ": My Turn");
			
			//Calculate Own Move
			Buffer = Bot.CalculateMove(GameBoard);
			
			if(Buffer == null)
			{
				System.out.println("GG");
				GameSocket.sendMove(new Move(0,0,0,0));
				Ingame = false;
			}
			else
			{
				System.out.println("Move: " + Buffer);
				GameBoard.RotateToGameSpace(Buffer);
				System.out.println("Rotated Move: " + Buffer);	
				
				//Send Out Move	
				GameSocket.sendMove(Buffer);		
			}			
		}
	}

	private void CleanUp()
	{
		
	}
}
