package fr.heavencraft.heavenproxy.jit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.ServerSocket;

import com.sun.management.OperatingSystemMXBean;

@SuppressWarnings("restriction")
public class SystemHelper
{
	private static final OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory
			.getOperatingSystemMXBean();

	public static long getFreeMemoryMb()
	{
		try (BufferedReader reader = new BufferedReader(new FileReader("/proc/meminfo")))
		{
			String line;
			while ((line = reader.readLine()) != null)
			{
				if (line.startsWith("MemAvailable:"))
				{
					line = line.substring("MemAvailable:".length());
					line = line.substring(0, line.length() - "kB".length());
					line = line.trim();

					return Long.parseLong(line) >> 10;
				}
			}
		}
		catch (final IOException ex)
		{
			ex.printStackTrace();
		}
		return operatingSystemMXBean.getFreePhysicalMemorySize() >> 20;
	}

	public static boolean isPortAvailable(int port)
	{
		try (ServerSocket ss = new ServerSocket(port))
		{
			ss.setReuseAddress(true);
			return true;
		}
		catch (final IOException e)
		{
			return false;
		}
	}
}