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
    public static long c = 0;
    
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
  
    public static Properties propertiesCached = null;
    
    public static String get(String property) {
        c++;
        String value = "";
        System.out.println("Counter: " + c + " TimeStamp:" + fileTimeStamp);
        try {
            File file = new File(new URI(Settings.class.getResource("settings.properties").toString()));
            System.out.println("Last modified before check:"+file.lastModified());
            if (file.lastModified() > fileTimeStamp) {

                fileTimeStamp = file.lastModified();
                Properties properties = new Properties();
                properties.load(new FileInputStream(file));

                System.out.println("Reading from settings.properties file.....");
                System.out.println(properties);
                
                value = properties.getProperty(property);
                propertiesCached = properties;
            } else {
                System.out.println("File is cached!");
                value = propertiesCached.getProperty(property);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return value;
    }
    
    public static Long getTime()throws Exception {
        File file = new File(new URI(Settings.class.getResource("settings.properties").toString()));
        return file.lastModified();
    }
}
