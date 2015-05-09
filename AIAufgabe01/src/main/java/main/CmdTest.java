package main;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

public class CmdTest
{

	public static void main(String[] args)
	{
		Options opt = new Options();
		JCommander cmd = new JCommander(opt);
		try
		{
			cmd.parse(args);
		} catch (ParameterException ex)
		{
			System.out.println(ex.getMessage());
			cmd.usage();
		}
	}
}
