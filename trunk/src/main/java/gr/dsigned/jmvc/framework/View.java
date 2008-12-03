/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.dsigned.jmvc.framework;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author Alkis Kafkis <a.kafkis@phiresoft.com>
 */
public class View {

    private ArrayList<String> viewStructure;
    private HashMap<Integer, String> viewValues;

    public View(String view_name) throws IOException {
        ArrayList<String> textPartsFound = new ArrayList<String>();
        HashMap<Integer, String> positionsFound = new HashMap<Integer, String>();
        String template = readWithStringBuilder(Jmvc.getViewDirectory() + view_name + ".html");
        int tagCounter = 0;
        while (template.length() > 0) {
            int openTagIndex = -2;
            int closeTagIndex = -2;
            String tagFound = null;
            String partFound = null;
            openTagIndex = template.indexOf("<%", openTagIndex);
            if (openTagIndex == -1) {
                partFound = template;
                textPartsFound.add(partFound);
                //debug only
                System.out.println("No more Tags");
                System.out.println("partFound: " + partFound);
                System.out.println("----------------------------------------------");
                break;
            } else {
                closeTagIndex = template.indexOf("%>", openTagIndex);
                tagFound = template.substring(openTagIndex + 2, closeTagIndex).trim();
                partFound = template.substring(0, openTagIndex);
                template = template.substring(closeTagIndex + 2);

                textPartsFound.add(partFound);
                tagCounter++;
                positionsFound.put(Integer.valueOf(tagCounter), tagFound);
            }
        }

        //debug only
        int i = 0;
        for (String textPart : textPartsFound) {
            System.out.println("textPart" + i + ": " + textPart);
        }
        for (Integer key : positionsFound.keySet()) {
            System.out.println("tagPart" + key + ": " + positionsFound.get(key));
        }

        //set the object classes
        setTextParts(textPartsFound);
        setPositions(positionsFound);
    }

    public String renderView(LinkedHashMap<String, String> data) {
        StringBuilder output = new StringBuilder();
        Integer counter = 1;
        String tagName = null;
        for (String structureItem : viewStructure) {
            output.append(structureItem);
            tagName = viewValues.get(counter);
            if (tagName != null) {
                output.append(data.get(tagName));
            }
            counter++;
        }
        return output.toString();
    }

    private ArrayList<String> getTextParts() {
        return viewStructure;
    }

    private void setTextParts(ArrayList<String> textParts) {
        this.viewStructure = textParts;
    }

    private HashMap<Integer, String> getPositions() {
        return viewValues;
    }

    private void setPositions(HashMap<Integer, String> positions) {
        this.viewValues = positions;
    }

    /**
     * Reads the file from disk and returns the content as a
     * string. Used to load templates.
     * 
     * @param fileName
     *            The name of the file to be read.
     * @return Returns the contents of the file.
     * @throws java.io.IOException
     */
    static String readWithStringBuilder(String fileName) throws IOException {
        Reader in = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
        BufferedReader br = new BufferedReader(in);
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = br.readLine()) != null) {
            result.append(line).append("\n");
        }
        br.close();
        in.close();
        return result.toString();
    }
}
