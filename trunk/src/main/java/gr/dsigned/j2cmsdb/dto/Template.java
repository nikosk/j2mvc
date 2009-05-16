/*
 *  Template.java
 *
 *  Copyright (C) May 10, 2009 Nikosk <nikosk@dsigned.gr>
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
package gr.dsigned.j2cmsdb.dto;

/**
 *
 * @author Nikos Kastamoulas <nikosk@dsigned.gr>
 */
public class Template {

    private String title;
    private String bodyCSSClass;
    private String css;
    private String js;
    private String content;
    private String sidebar;
    private String rightSidebar;

    public String getRightSidebar() {
        return rightSidebar;
    }

    public void setRightSidebar(String rightSidebar) {
        this.rightSidebar = rightSidebar;
    }

    public String getSidebar() {
        return sidebar;
    }

    public void setSidebar(String sidebar) {
        this.sidebar = sidebar;
    }

    public String getBodyCSSClass() {
        return bodyCSSClass;
    }

    public void setBodyCSSClass(String bodyCSSClass) {
        this.bodyCSSClass = bodyCSSClass;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }

    public String getJs() {
        return js;
    }

    public void setJs(String js) {
        this.js = js;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
