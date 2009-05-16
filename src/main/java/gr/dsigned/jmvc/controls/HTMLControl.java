/*
 *  HTMLControl.java
 *
 *  Copyright (C) Mar 16, 2009 Nikosk <nikosk@dsigned.gr>
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
package gr.dsigned.jmvc.controls;

import gr.dsigned.jmvc.types.Tuple2;

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class HTMLControl extends Control {

    public HTMLControl(Tuple2... attributes) {
    }

    @Override
    public String renderControl() {
        return "";
    }

    public String htmlTag(String tagName, String innerHTML, Tuple2... attr) {
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
}
