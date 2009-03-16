/*
 *  Anchor.java
 *
 *  Copyright (C) Mar 16, 2009 Nikos Kastamoulas <nikosk@dsigned.gr>
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

/**
 *
 * @author Nikos Kastamoulas <nikosk@dsigned.gr>
 */
public class Anchor extends HTMLControl {

    Control control;
    StringBuilder sb;

    public Anchor(Control control) {
        this.control = control;
        sb = new StringBuilder();
    }

    @Override
    public String renderControl() {
        return "<"+tagName+" href='#' class='"+cssClass+"'>" + control.renderControl() + "</a>";
    }
}
