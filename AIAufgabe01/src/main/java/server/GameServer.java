package server;

import java.io.IOException;

public class GameServer extends Thread
{
	public void run()
	{
		try
		{
			System.out.println(getClass().getResource("/META-INF/kimpl.jar"));
			Runtime.getRuntime().exec("java -jar " + getClass().getResource("/META-INF/kimpl.jar"));
//			ProcessBuilder p = new ProcessBuilder("java -jar /home/jakob/git/spielbaumAI/AIAufgabe01/META-INF/kimpl.jar");
//			p.start();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
