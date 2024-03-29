/*
 *  EditorField.java
 *
 *  Copyright (C) 2008 Vas Chryssikou <vchrys@gmail.com>
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
package gr.dsigned.jmvc.controls.forms.fields;

import gr.dsigned.jmvc.Settings;
import gr.dsigned.jmvc.types.Tuple2;

/**
 * @author Vas Chryssikou <vchrys@gmail.com>
 */
public class EditorField extends TextareaField {

    private String cols;
    private String rows;
    private boolean disabled = false;
    private boolean readonly = false;

    /**
     * 
     * @param labelName
     * @param fieldName
     * @param rows
     * @param cols
     * @param value
     * @param rules
     */
    public EditorField(String labelName, String fieldName, String rows, String cols, String value, Tuple2<Rule, String>... rules) {
        super(labelName, fieldName, rows, cols, value, rules);
    }

    @Override
    public String renderField() {
        String tArea = String.format("<textarea class='wysiwyg' name='%1$s' id='%2$s' rows='%3$s' cols='%4$s' %6$s %7$s >%5$s</textarea>%n", getFieldName(), "id_" + getFieldName(), getRows(), getCols(), getValue(), isDisabled(), isReadonly(), getErrors());
        String scriptTinyMCE = "<script type='text/javascript' src='"+Settings.get("ROOT_URL")+"static/js/editor/tinymce/tiny_mce.js'></script><script type='text/javascript' src='"+Settings.get("ROOT_URL")+"static/js/editor/tinymce/tiny_mce_init.js'></script>" ;
        String scriptNiceEdit = "<script type='text/javascript' src='"+Settings.get("ROOT_URL")+"static/js/editor/nicedit/nicEdit.js'></script><script type='text/javascript' src='"+Settings.get("ROOT_URL")+"static/js/editor/nicedit/nicEdit_init.js'></script>" ;
        return  tArea+scriptNiceEdit;
    }

}
