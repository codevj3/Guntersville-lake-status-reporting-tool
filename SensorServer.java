import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import SensorApp.*;
/**
 * This class represents a server for the sensor. (Servant Server)
 */
public class SensorServer {
	public static void main(String[] args) {
		try {
			//Creating and Initializing an ORB Object
			ORB orb = ORB.init(args, null);
			POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();
			// Creating a sensor with the given address in args[4]
			SensorImp server = new SensorImp(args[4]);
			// Get a reference for the name server
			org.omg.CORBA.Object ref = rootpoa.servant_to_reference(server);
			Sensor href = SensorHelper.narrow(ref);
			// change name of the server
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			// narrowing it to the proper type (Naming ContextExt).
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			String name = "Sensor";
			// Registering with the new name
		    NameComponent path[] = ncRef.to_name( name );
		    ncRef.rebind(path, href);
			while (true) {
				// Response to the client
			}
		} catch (Exception e) {
			
		}
	}
	
}
/**
 * This class implements the sensorPDA methods and gives them operations. (Servant Object)
 *
 */
class SensorImp extends SensorPOA {
	private HashMap<String, Integer> map; // maps commands to their temperatures.
	
	/**
	 * Constructor.
	 * read the file and puts the strings and their integers to the hash map.
	 * @param address
	 */
	public SensorImp(String address) {
		map = new HashMap<String, Integer>();
		try {
			Scanner scan = new Scanner(new File(address));
			while (scan.hasNextLine()) {
				String[] line = scan.nextLine().split(" ");
				map.put(line[0].toLowerCase(), Integer.parseInt(line[1]));
			}
			scan.close();
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found");
			System.exit(0);
		}
	}

	/**
	 * This method parses the command and response a proper answer.
	 */
	@Override
	public String response(String command, String direction) {
		if (command.equalsIgnoreCase("water quality")) {
			return "water quality dicretion " + direction + ": " +map.get(direction);
		} else if (command.equalsIgnoreCase("air quality")) {
			return  "ground: " + map.get("ground") + " 6-feet: " + map.get("6-feet") + " 20-feet: " + map.get("20-feet") + " 100-feet: " + map.get("100-feet");
		} else if (command.equalsIgnoreCase("temperature")) {
			return "temperature: " + map.get(command.toLowerCase());
		}
		return "Nothing found in the file";
	}	
}
