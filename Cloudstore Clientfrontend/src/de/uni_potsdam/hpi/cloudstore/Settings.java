package de.uni_potsdam.hpi.cloudstore;

import java.text.SimpleDateFormat;

public class Settings {
	public static final int SEC = 1000;
	public static final int MIN = 60*SEC;
	public static final int HOUR = 60*MIN;
	public static final int DAY = 24*HOUR;
	public static String serverName = "local";
	public static String id = "1";
	public static boolean sysOuts = false;

	public static String remoteRepository = "cloudstoretest";
	
	/*
	 * SyncServlet
	 */
	
	private static String syncHost = "172.16.21.228";
	private static String syncPort = "8080";
	private static String syncBaseUrl = String.format("http://%s:%s/SyncServlet/SyncServlet", syncHost, syncPort);
	public static String SyncServerURL = String.format("%s?server=Server", syncBaseUrl);
	public static String ResetServerURL = String.format("%s?reset=true", syncBaseUrl);

	public static long SLEEPING_TIME = SEC;
	public static int WAITING_SECOUNDS = 5; // Sekundenwert innerhalb einer Minute (Modulo genutzt) - bestens ein Teiler von 60
	public static long MAX_PROVIDER_LIVING_TIME = 2*HOUR; // in ms (atm 2h)

	public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ss");
	
	/*
	 * MySQL
	 */
	public static String host = "localhost"; // "172.16.21.228"
	public static String dbUser = "cloudraid";
	public static String dbPass = "cloud";

	/*
	 * Logger
	 */
	public static String dbName = "cloudraid_test";
	public static String logFolder = "logs";

	/*
	 * Tests
	 */
	public static String testDataFolder = "testdata";
	public static boolean local = true;
	/* erasure tests */
	public static int min_k = 6;
	public static int max_k = 6;
	public static int min_m = 1;
	public static int max_m = 1;
	// word size during encoding 
	public static int w = 7;		
	// name of the coding method to use (e.g. liberation, cauchy_good, ...)
	public static String codingMethod = "cauchy_good"; 		
	
	
	/*
	 * MockupStorageProvider
	 */
	public static double failure = 0.01;
	/* simulated upload and download speed in kb/s. e.g. 500 => 500kb/s */
	public static int speed = 50;
}
