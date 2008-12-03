package gr.dsigned.jmvc.framework;

import java.io.BufferedReader;
import java.io.FileInputStream;
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

    private ArrayList<String> textParts;
    private HashMap<Integer, String> positions;

    private ArrayList<String> getTextParts() {
        return textParts;
    }

    private void setTextParts(ArrayList<String> textParts) {
        this.textParts = textParts;
    }

    public HashMap<Integer, String> getPositions() {
        return positions;
    }

    private void setPositions(HashMap<Integer, String> positions) {
        this.positions = positions;
    }

    public View(String input) {
        ArrayList<String> textPartsFound = new ArrayList<String>();
        HashMap<Integer, String> positionsFound = new HashMap<Integer, String>();
        String template = input;
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
                break;
            } else {
                closeTagIndex = template.indexOf("%>", openTagIndex);
                tagFound = template.substring(openTagIndex + 2, closeTagIndex);
                partFound = template.substring(0, openTagIndex);
                template = template.substring(closeTagIndex + 2);
                textPartsFound.add(partFound);
                tagCounter++;
                positionsFound.put(Integer.valueOf(tagCounter), tagFound.trim());
            }
        }
        setTextParts(textPartsFound);
        setPositions(positionsFound);
    }

    public String format(LinkedHashMap<String, String> data) {
        StringBuilder output = new StringBuilder();
        Integer counter = 1;

        String tagName = null;

        for (String textPart : textParts) {
            output.append(textPart);

            tagName = positions.get(counter);
            if (tagName != null) {
                output.append(data.get(tagName));
            }
            counter++;
        }
        return output.toString();
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
    static String readViewTemplate(String fileName) throws IOException {
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
