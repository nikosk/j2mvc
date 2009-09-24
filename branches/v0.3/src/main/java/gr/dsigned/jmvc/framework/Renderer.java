/*
 *  Renderer.java
 * 
 *  Copyright (C) 2008 Nikosk <nikosk@dsigned.gr>
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
package gr.dsigned.jmvc.framework;

import gr.dsigned.jmvc.*;
import gr.dsigned.jmvc.types.Tuple2;
import java.lang.reflect.Method;

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Renderer {

    /**
     * Create an A html tag in this form ROOT_URL/segments/
     * 
     * @param segments the segments of the url /controller/method/etc/etc/etc
     * @param innerHtml the text to be displayed in the link. (also used in the link title attribute for better SEO)
     * @param attribute  i.e.: "class = elementClass")
     * @return Html A tag as a string
     */
    public static String anchor(String segments, String title, String attribute) {
        String href = Settings.get("ROOT_URL");
        href = (!Settings.get("SUB_DIR").isEmpty()) ? Settings.get("SUB_DIR") + "/" : "";
        href = (segments.startsWith("/") && segments.length() < 2) ? "" : segments;
        if (attribute == null || attribute.isEmpty()) {
            return String.format("<a href='%1$s' title='%2$s'>%2$s</a>", href, title);
        } else {
            return String.format("<a href='%1$s' title='%2$s' %3$s>%2$s</a>", href, title, attribute);
        }
    }

    /**
     * Create an A html tag in this form ROOT_URL/segments/
     * 
     * @param segments the segments of the url /controller/method/etc/etc/etc
     * @param innerHtml the text to be displayed in the link. (also used in the link title attribute for better SEO)
     * @param attributes Tuples2 of attribute/value pairs i.e.: o("class","elementClass")
     * @return Html A tag as a string
     */
    public static String anchor(String segments, String innerHtml, Tuple2... attributes) {
        segments = (segments.startsWith("/") && segments.length() > 2) ? segments.substring(1) : segments;
        String href = Settings.get("ROOT_URL");
        href += (segments.startsWith("/") && segments.length() < 2) ? "" : segments;
        if (attributes == null) {
            return String.format("<a href='%1$s' title='%2$s'>%2$s</a>", href, innerHtml);
        } else {
            String attr = "";
            for (Tuple2 t : attributes) {
                attr += t._1 + "='" + t._2 + "' ";
            }
            return String.format("<a href='%1$s' title='%2$s' %3$s>%2$s</a>", href, innerHtml, attr);
        }
    }

    public static String root_url() {
        return Settings.get("ROOT_URL");
    }

    public static String div(String innerHTML, Tuple2... attr) {
        return htmlTag("div", innerHTML, attr);
    }

    public static String span(String innerHTML, Tuple2... attr) {
        return htmlTag("span", innerHTML, attr);
    }

    public static String p(String innerHTML, Tuple2... attr) {
        return htmlTag("p", innerHTML, attr);
    }

    public static String li(String innerHTML, Tuple2... attr) {
        return htmlTag("li", innerHTML, attr);
    }

    public static String h1(String innerHTML, Tuple2... attr) {
        return htmlTag("h1", innerHTML, attr);
    }

    public static String h2(String innerHTML, Tuple2... attr) {
        return htmlTag("h2", innerHTML, attr);
    }

    public static String h3(String innerHTML, Tuple2... attr) {
        return htmlTag("h3", innerHTML, attr);
    }

    public static String h4(String innerHTML, Tuple2... attr) {
        return htmlTag("h4", innerHTML, attr);
    }

    public static String h5(String innerHTML, Tuple2... attr) {
        return htmlTag("h5", innerHTML, attr);
    }

    public static String h6(String innerHTML, Tuple2... attr) {
        return htmlTag("h6", innerHTML, attr);
    }

    public static String b(String innerHTML, Tuple2... attr) {
        return htmlTag("b", innerHTML, attr);
    }

    public static String a(String innerHTML, Tuple2... attr) {
        return htmlTag("a", innerHTML, attr);
    }
    

    public static String img(Tuple2... attr) {
        return htmlTag("img", attr);
    }

    private static String htmlTag(String tagName, String innerHTML, Tuple2... attr) {
        StringBuilder sb = new StringBuilder();
        sb.append("<").append(tagName);
        for (Tuple2 t : attr) {
            if (!t._1.toString().isEmpty()) {
                sb.append(" ").append(t._1).append("='").append(t._2).append("'");
            }
        }
        sb.append(">").append(innerHTML).append("</").append(tagName).append(">");
        return sb.toString();
    }

    private static String htmlTag(String tagName, Tuple2... attr) {
        StringBuilder sb = new StringBuilder();
        sb.append("<").append(tagName);
        for (Tuple2 t : attr) {
            if (!t._1.toString().isEmpty()) {
                sb.append(" ").append(t._1).append("='").append(t._2).append("'");
            }
        }
        sb.append("/>");
        return sb.toString();
    }

    /**
     * Allows dynamically calling methods on extending classes.
     * @param methodName the name of the method you need to run.
     * @param args The arguments you need to pass to the method
     * @return String (rendered HTML)
     * @throws java.lang.Exception
     */
    public String runMethod(String methodName, Object... args) throws Exception {
        Class[] classNames = new Class[args.length];
        int index = 0;
        for (Object o : args) {
            classNames[index] = o.getClass();
            index++;
        }
        Class c = this.getClass();
        Method m = c.getMethod(methodName, classNames);
        return (String) m.invoke(this, args);
    }
}
