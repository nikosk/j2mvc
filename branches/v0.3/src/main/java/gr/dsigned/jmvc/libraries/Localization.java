package gr.dsigned.jmvc.libraries;

import gr.dsigned.jmvc.Settings;
import gr.dsigned.jmvc.framework.Jmvc;
import gr.dsigned.jmvc.framework.Library;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Localization extends Library {

    private static long fileTimeStamp = 0;
    private static long nextCheck = 0;
    private static long recheckInterval = 60000; // Check once a minute for changes
    private static Properties properties = null;
    private static File file;
    private static String locale = Settings.get("DEFAULT_LOCALE").toLowerCase();

    /**
     * 
     * @param property
     * @param value
     */
    public static void set(String property, String value) {
        if (file == null) {
            init();
        }
        try {
            if (properties == null) {
                properties = new Properties();
                properties.loadFromXML(new FileInputStream(file));
            }
            properties.setProperty(property, value);
            properties.storeToXML(new FileOutputStream(file), value);
        } catch (IOException ioe) {
            Jmvc.logError(ioe.toString());
        }
    }

    /**
     * Shorthand for Localization.get()
     * @param property
     * @return
     */
    public static String i(String property) {
        return get(property);
    }

    /**
     * Get a property from the locale file
     * @param property
     * @return
     */
    public static String get(String property) {
        String value = "";
        if (file == null) {
            init();
        }
        try {
            boolean checkForChanges = false;
            if (nextCheck - new Date().getTime() < 0) {
                checkForChanges = file.lastModified() > fileTimeStamp;
                nextCheck = new Date().getTime() + recheckInterval;
            }
            if (properties == null || checkForChanges) {
                fileTimeStamp = file.lastModified();
                properties = new Properties();
                properties.clear();
                properties.loadFromXML(new FileInputStream(file));
                value = properties.getProperty(property);
            } else {
                value = properties.getProperty(property);
            }
        } catch (Exception e) {
            Jmvc.logError(e.toString());
        }
        if (value == null) {
            set(property, "");
            return property;
        } else if (value.isEmpty()) {
            return property;
        } else {
            return value;
        }
    }

    private static void init() {
        try {
            file = new File(new URI(Localization.class.getResource(locale + ".xml").toString()));
        } catch (Exception e) {
            Jmvc.logError(e.toString());
        }
    }

    public static String getLocale() {
        return locale;
    }

    public static void setLocale(String locale) {
        Localization.locale = locale;
    }

    public static Properties getProperties() {
        return properties;
    }

    public static Set<String> getLocalizationKeys(){
        return properties.stringPropertyNames();
    }    
}
