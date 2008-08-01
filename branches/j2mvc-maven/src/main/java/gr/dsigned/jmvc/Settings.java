package gr.dsigned.jmvc;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.util.Properties;

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Settings  {
   
    public static long fileTimeStamp = 0;
    
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
  
    public static Properties properties = null;
    
    public static String get(String property) {
        String value = "";

        try {
            File file = new File(new URI(Settings.class.getResource("settings.properties").toString()));

            if ( properties == null || file.lastModified() > fileTimeStamp) {

                fileTimeStamp = file.lastModified();
                properties = new Properties();
                properties.load(new FileInputStream(file));
                value = properties.getProperty(property);
            } else {
                value = properties.getProperty(property);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return value;
    }
}
