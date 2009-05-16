/*
 *  Anchor.java
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
public class Anchor extends HTMLControl {

    Control childControl;
    StringBuilder sb;
    Tuple2[] attributes;

    public Anchor(Control control, Tuple2... attributes) {
        this.childControl = control;
        sb = new StringBuilder();
        this.attributes = attributes;
    }

    @Override
    public String renderControl() {
        return htmlTag("a", childControl.renderControl(), attributes);
    }
}
