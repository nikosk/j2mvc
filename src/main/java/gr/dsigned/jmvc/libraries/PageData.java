/*
 *  PageData.java
 * 
 *  Copyright (C) 2008 Nikos Kastamoulas <nikosk@dsigned.gr>
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

import java.util.LinkedHashMap;

/**
 * 12 Μαρ 2008, gr.dsigned.jmvc.libraries 
 * PageData.java
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class PageData extends LinkedHashMap<String, String> {

    private static final String cssLinkTemplate = "<link href='%1$s' media='screen' rel='stylesheet' type='text/css' />";
    private static final String scriptFileTemplate = "<script src='%1$s' type='text/javascript'></script>";
    private LinkedHashMap<String, StringBuilder> data = new LinkedHashMap<String, StringBuilder>();

    public PageData() {
        append("scripts", "");
        append("css", "");
    }

    public void append(String tag, String value) {
        StringBuilder sb = data.get(tag);
        if (sb == null) {
            sb = new StringBuilder();
        }
        sb.append(value);
        data.put(tag, sb);
        this.put(tag, null);//create the key for $.loadView
    }

    @Override
    public String get(Object key) {
        String result = "";
        StringBuilder sb = data.get(key);
        if( sb != null){
            result = sb.toString();
        }else{
            result = super.get(key);
        }
        return result;
    }

    public void appendScript(String s) {
        append("scripts", s);
    }

    public void appendScriptFile(String s) {
        append("scripts", String.format(scriptFileTemplate, s));
    }

    public void appendCss(String s) {
        append("css", s);
    }

    public void appendCssFile(String s) {
        append("css", String.format(cssLinkTemplate, s));
    }
}
