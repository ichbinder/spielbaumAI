package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import server.GameServer;
import client.Client;
import client.IClient;
import client.agent.IAgent;
import client.agent.RandomAgent;
import client.agent.gametree.GameTreeAgent;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import evaluator.DummyEvaluator;

public class CmdTest
{

	public static void main(String[] args) throws IOException
	{
		String text;
		List<String> arge = null;
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		
		do
		{
			arge = new ArrayList<String>();
			System.out.println("Eingabe :");
			text = input.readLine();
			Collections.addAll(arge, text.split(" "));
			
			if (arge.get(0).equals("client")) {
				ClientOptions opt = new ClientOptions();
				JCommander cmd = CmdTest.cmdPaser(arge, opt);
				if (opt.getStart() == true) {
					IAgent gta = new GameTreeAgent(new DummyEvaluator(), 0);
					IClient sp = new Client(opt.getClientName(), gta, opt.getPathToImage());
					sp.Connect(opt.getGameServerIP());
					System.out.println("Client Starded!");
				} if (opt.getHelp() == true)
					cmd.usage();
			} else if (arge.get(0).equals("server")) {
				ServerOptions so = new ServerOptions();
				JCommander cmd = CmdTest.cmdPaser(arge, so);
				if (so.getStart() == true) {
					GameServer gs = new GameServer();
					gs.start();
					System.out.println("Server Starded!");
				}	if (so.getHelp() == true)
					cmd.usage();
			}
			else if(arge.get(0).equals("test"))
			{
				ClientOptions opt = new ClientOptions();
				JCommander cmd = CmdTest.cmdPaser(arge, opt);
				
				IClient sp = null;
				
				//sp = new Client("A-R", new RandomAgent(), opt.getPathToImage());
				//sp.ConnectToLocalhost();
				/*sp = new Client("B-R", new RandomAgent(), opt.getPathToImage());
				sp.ConnectToLocalhost();
				sp = new Client("C-R", new RandomAgent(), opt.getPathToImage());
				sp.ConnectToLocalhost();
*/
				
				sp = new Client("Tree 0", new GameTreeAgent(new DummyEvaluator(), 0), opt.getPathToImage());
				sp.ConnectToLocalhost();
				sp = new Client("Tree 1", new GameTreeAgent(new DummyEvaluator(), 1), opt.getPathToImage());
				sp.ConnectToLocalhost();
				sp = new Client("Tree 2", new GameTreeAgent(new DummyEvaluator(), 2), opt.getPathToImage());
				sp.ConnectToLocalhost();
				sp = new Client("Tree 2", new GameTreeAgent(new DummyEvaluator(), 2), opt.getPathToImage());
				sp.ConnectToLocalhost();
				
			}
		} while (!arge.get(0).equals("end"));
		
	}
	
	static JCommander cmdPaser(List<String> arg, Object opt) {
		JCommander cmd = new JCommander(opt);
		if (arg.size() != 1) {
			arg.remove(0);
			try
			{
				cmd.parse(arg.toArray(new String[arg.size()]));
			} catch (ParameterException ex)
			{
				cmd.usage();
			}
		} else 
			cmd.usage();
		return cmd;
	}
}
