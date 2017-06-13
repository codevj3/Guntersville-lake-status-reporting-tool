import java.util.Scanner;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import SensorApp.*;

/**
 * Client for the sensor server.
 */
public class SensorClient {
	private static Scanner scan;

	public static void main(String[] args) {
		while (true) {
			try {
				ORB orb = ORB.init(args, null);
		        org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
	            // Use NamingContextExt instead of NamingContext. This is 
	            // part of the Interoperable naming Service.  
	            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
	     
	            // resolve the Object Reference in Naming
	            String name = "Sensor";
	            Sensor sensor = SensorHelper.narrow(ncRef.resolve_str(name));
	            
	            // Scanner for reading user input from keyboard.
	            scan = new Scanner(System.in);
	            System.out.println("Enter what condition do you want?\n"
	            		+ "air quality, water quality, temperature");
	            String command = scan.nextLine();
	            String direction = "";
	            if (command.equalsIgnoreCase("water quality")) { // If the command is water quality, get the direction.
	            	System.out.println("Enter what direction do you want?");
	            	direction = scan.nextLine();
	            }
	            System.out.println(sensor.response(command, direction)); // get the response from server.
	            System.out.println("Press y to continue.");
	            String quit = scan.nextLine();
	            if (!quit.equalsIgnoreCase("y")) {
	            	scan.close();
	            	break;
	            }
			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
			}
		}
	}
}
