/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.dsigned.jmvc.libraries;

import gr.dsigned.jmvc.types.Bean;
import gr.dsigned.jmvc.framework.Library;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Christos Peppas <c.peppas@phiresoft.com>
 */
public class Validation extends Library {
    
    public static String parseString(String input, String inputName, int minLength, int maxLength, Bean errors)
    {
        String output = "";

        if (input == null || input.trim().equals(""))
        {
            errors.put(inputName, inputName +" is required");
        }
        else
        {
            int inputLength = input.trim().length();

            if (inputLength < minLength || inputLength > maxLength)
            {
                errors.put(inputName, inputName +" out of range");
            }
            else
            {
                output = input.trim();
            }
        }

        return output;
    }
    
    public static String parseStringAllowEmpty(String input, String inputName, int minLength, int maxLength, Bean errors)
    {
        String output = "";

        if (input == null)
        {
            errors.put(inputName, inputName +" is required");
        }
        else
        {
            int inputLength = input.trim().length();

            if (inputLength < minLength || inputLength > maxLength)
            {
                errors.put(inputName, inputName +" out of range");
            }
            else
            {
                output = input.trim();
            }
        }

        return output;
    }
    
    public static String parseEmail(String input, String inputName, int minLength, int maxLength, Bean errors)
    {
        Pattern p = Pattern.compile("[0-9a-zA-Z]+(\\.{0,1}[0-9a-zA-Z\\+\\-_]+)*@[0-9a-zA-Z\\-]+(\\.{1}[a-zA-Z]{2,6})+");
        Matcher m = null;
        boolean matchFound = false;

        String output = "";

        if (input == null || input.trim().equals(""))
        {
            errors.put(inputName, inputName +" is required");
            //errors.add(new Error(errorRb.getString("PARAM_MISSING") + inputName, formFieldId));
        }
        else
        {
            int inputLength = input.trim().length();

            if (inputLength < minLength || inputLength > maxLength)
            {
                errors.put(inputName, inputName +" out of range");
            }
            else
            {
                m = p.matcher(input.trim());
                matchFound = m.matches();
                if (!matchFound)
                {
                    errors.put(inputName, inputName +" is not a valid email");
                }
                else
                {
                    output = input.trim();
                }
            }
        }

        return output;
    }

}
