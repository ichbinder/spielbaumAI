package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import client.SpielClient;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

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
				CmdTest.cmdPaser(arge, opt);
				System.out.println(opt.getStart());
				if (opt.getStart() == true) {
					SpielClient sp = new SpielClient();
					sp.start();
					System.out.println("Client Starded!");
				}
			} else if (arge.get(0).equals("server")) {
				
			} 
			
		} while (!arge.get(0).equals("end"));
		
		
//		if (!opt.getClientName().isEmpty())
//		{
//			SpielClient sp = new SpielClient("localhost", "8080", "localhost",
//					"Client1", "META-INF/icon.png");
//			SpielClient sp = new SpielClient();
//			sp.start();
//			System.out.println("Client Starded!");
//		}
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
