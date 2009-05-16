/*
 *  DataForm.java
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
package gr.dsigned.jmvc.libraries;

import gr.dsigned.jmvc.framework.Library;

import java.util.LinkedHashMap;

/**
 * 02 ��� 2008, gr.dsigned.jmvc.libraries DataForm.java
 * 
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class DataForm extends Library {
    private StringBuilder output = new StringBuilder();
    private String baseUrl;
    private LinkedHashMap<String, String> fields = new LinkedHashMap<String, String>();
    private LinkedHashMap<String, String> attributes = new LinkedHashMap<String, String>();
    private boolean customFormTag = false;
    public DataForm() {
        this.baseUrl = "";
    }

    public DataForm(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setBaseUrl(String bu) {
        this.baseUrl = bu;
    }
    public String build() {
        StringBuilder out = new StringBuilder();
        if (!customFormTag) {
            out.append(String.format(FormElems.FORM_OPEN.toString(), "jmvc_form", this.baseUrl, "POST", ""));
        } else {
        }
        if (!fields.containsKey("formClose")) {
            formClose();
        }
        return out.toString();
    }

    public void addField(String name, FormElems type, String... attributes) {
        String attr = "";
        for (String s : attributes) {
            attr += s + ", ";
        }
        fields.put(name, String.format(type.toString(), attr));
    }

    /**
     * Adds a form open tag to the build.
     * 
     * @param name
     *            The form name. This will also be used as
     *            the tag id.
     * @param method
     *            The method of the form. GET, POST etc
     * @param attributes
     *            Extra attributes for the tag. Pass this as
     *            plain text e.g. "class='formclass'"
     */
    public void formOpen(String name, String method, String... attributes) {
        String tagBody = FormElems.FORM_OPEN.toString();
        String attr = "";
        for (String s : attributes) {
            attr += s + ", ";
        }
        tagBody = String.format(tagBody, name, this.baseUrl, method, attr);
        fields.put(name, tagBody);
    }

    /**
     * Adds a form close tag i.e. </form>
     */
    public void formClose() {
        fields.put("formClose", FormElems.FORM_CLOSE.toString());
    }

    public void submit(String name, String value) {
        fields.put(name, String.format(FormElems.SUBMIT.toString(), name, value));
    }

    /**
     * enum type that returns a field html tag with hooks to
     * pass parameters. Params : 1. name: will be used as id
     * as well. Required. 2. value: Pass it to fields that
     * need a value. 3. method: Only pass it to a FORM_OPEN
     * 4. selected: Pass the string selected if the field is
     * an OPTION.
     */
    public enum FormElems {

        FORM_OPEN("<form id='%1$s' name='%1$s' action='%2$s' method='%3$s' %4$s >%n"), FORM_CLOSE("</form>%n"), SUBMIT(
        "<input id='%1$s' type='submit' value='%2$s'/>%n"), INPUT_TEXT(
        "<input type='text' name='%1$s' id='%1$s' value='%2$s'/>%n"), INPUT_PASS("%n"), TEXTAREA(
        "<textarea name='%1$s' id='%1$s'>%2$s</textarea>%n"), HIDDEN(
        "<input type='hidden' name='%1$s' value='%2$s' />%n"), CHECKBOX(
        "<input type='checkbox' name='%1$s' value='%2$s' id='%1$s' />%n"), BUTTON(""), RADIOBUTTON(
        "<input name='%1$s' type='radio' value='%2$s' id='%1$s' />%n"), FILE(
        "<input type='file' name='%1$s' id='%1$s' />%n"), SELECT(
        "<select name='%1$s' id='%1$s'>%2$s</select>%n"), OPTION("<option value='%2$s' %4$s>%3$s</option>%n");
        private final String body;

        FormElems(String body) {
            this.body = body;
        }

        public String toString() {
            return this.body;
        }
    }
}
