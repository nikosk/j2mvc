/*
 * $Id: JSONArray.java,v 1.1 2006/04/15 14:10:48 platform Exp $
 * Created on 2006-4-10
 */
package gr.dsigned.jmvc.libraries.external.json;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * @author FangYidong<fangyidong@yahoo.com.cn>
 */
public class JSONArray extends ArrayList {

    @Override
    public String toString() {
        ItemList list = new ItemList();

        Iterator iter = iterator();

        while (iter.hasNext()) {
            Object value = iter.next();
            if (value instanceof String) {
                list.add("\"" + JSONObject.escape((String) value) + "\"");
            } else {
                list.add(String.valueOf(value));
            }
        }
        return "[" + list.toString() + "]";
    }
}
