/*
 *  Validator.java
 * 
 *  Copyright (C) 2008 Nikos Kastamoulas <nikosk@dsigned.gr>
 * 
 *  This module is free software: you can redistribute it and/or modify it under
 *  the terms of the GNU Lesser General Public License as published by the Free
 *  Software Foundation, either version 3 of the License, or (at your option)
 *  any later version. See http://www.gnu.org/licenses/lgpl.html.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */
package gr.dsigned.jmvc.libraries;

import gr.dsigned.jmvc.framework.Library;
import java.util.regex.Pattern;

/**
 * 02 ��� 2008, gr.dsigned.jmvc.libraries Validator.java
 * 
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Validator extends Library {
    static Pattern  validMail  = Pattern.compile("[0-9a-zA-Z]+(\\.{0,1}[0-9a-zA-Z\\+\\-_]+){0,3}@[0-9a-zA-Z\\-]+(\\.{1}[a-zA-Z]{2,6})+");
    static Pattern validDomain  = Pattern.compile("(http):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?");
    static Pattern numeric  = Pattern.compile("\\d*");
    static Pattern alphaNumeric  = Pattern.compile("\\w*");
    static Pattern alphaLatin  = Pattern.compile("[a-zA-Z]*");
    
    
    public static boolean isValidMail(String mailAddress){
        return validMail.matcher(mailAddress).matches();
    }
    
}
