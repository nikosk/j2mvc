package gr.dsigned.jmvc;

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Settings {

    public static String ROOT_URL = "http:local/";  // http://domain/
    public static String REAL_PATH = "AUTO"; // AUTO to autodetect
    public static String SUB_DIR = ""; // Set this if you deployed Jmvc in a subdirectory.
    public static String DEFAULT_ENCODING = "utf-8"; // Sets default encoding
    public static int	 SESSION_EXPIRY = 7200; // Time in seconds until a session becomes inactive
    public static String DEFAULT_CONTROLLER = "Sites"; // the controller to handle initial requests
    public static String VIEW_PATH = ""; // Where to find templates, leave empty for www-root/views
    public static String IMG_PATH = "";  // Where to find images, leave empty for www-root/images
    public static String JS_PATH = "";  // Where to find scripts, leave empty for www-root/js
    /************************************** DB Settings ****************************************/
    public static String DATABASE_TYPE = "mysql";    // mysql only for now.
    public static String DB_URL = "localhost"; 
    public static int DB_PORT = 3306;  // default (3306).
    public static String DB_NAME = "todo_list"; //Name of the database 

    public static String DB_USER = "root";
    public static String DB_PASS = "";
    
    /************************************** Auto load classes Settings ****************************************/
    
    public enum AutoLoad{
    	SESSION(true);
    	
    	public boolean load;
    	AutoLoad(boolean autoLoad) {
    		this.load = autoLoad;
    	}
    	
    	public boolean loadIt(){
    		return this.load;
    	}
    }
}
