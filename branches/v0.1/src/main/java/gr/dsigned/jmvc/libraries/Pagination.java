/*
 *  Pagination.java
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

import gr.dsigned.jmvc.framework.Jmvc;
import gr.dsigned.jmvc.framework.Library;
import static gr.dsigned.jmvc.framework.Renderer.div;
import static gr.dsigned.jmvc.framework.Renderer.a;
import static gr.dsigned.jmvc.types.operators.*;

/**
 * 16 Μαρ 2008, gr.dsigned.jmvc.libraries
 * Pagination.java
 * @author Christos Peppas <c.peppas@phiresoft.com>
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Pagination extends Library {

    public enum PagingType {

        SEARCH, ITEM, AJAX;
    }
    private String baseURL = "";
    private int totalRows = 0;
    private int currentPage = 0;
    private int noItemsPerQuery = 0;    //DEFAULT VALUES
    private int perPage = 10;
    private int linksNum = 5;
    private String separationChar = "&nbsp;&nbsp;";
    private String classStartArrowOn = "";
    private String classStartArrowOff = "";
    private String classEndArrowOn = "";
    private String classEndArrowOff = "";
    private String classBackArrowOn = "";
    private String classBackArrowOff = "";
    private String classFwdArrowOn = "";
    private String classFwdArrowOff = "";
    private String imgStartArrowOn = "First  &iota;&laquo;";
    private String imgStartArrowOff = "First &iota;&laquo;";
    private String imgEndArrowOn = "&raquo;&iota; Last";
    private String imgEndArrowOff = "&raquo;&iota; Last";
    private String imgBackArrowOn = "Prev &laquo;";
    private String imgBackArrowOff = "Prev &laquo;";
    private String imgFwdArrowOn = "&raquo; Next";
    private String imgFwdArrowOff = "&raquo; Next";

    public String createLinks(int currentPage) {
        String out = "";
        if (totalRows < perPage || perPage == 0) {
            return "";
        }
        int pageCount = Math.round(totalRows / perPage);
        if (pageCount == 0) {
            return "";
        }
        if (!baseURL.endsWith("/")) {
            baseURL += "/";
        }
        for (int i = 0; i <= pageCount; i++) {
            if (i != currentPage) {
                if (i != 0) {
                    out += "<a href='" + baseURL + i + "' >" + (i + 1) + "</a> | ";
                } else {
                    out += "<a href='" + baseURL + "' >" + (i + 1) + "</a> | ";
                }
            } else {
                out += "<b>" + (i + 1) + "</b> | ";
            }
        }
        return out;
    }

    public String createPagingLinks(int currentPage) {
        return this.createLinks(currentPage, PagingType.SEARCH);
    }

    /**
     * Method that creates pagination links of the given pagination type
     * @param currentPage
     * @return (string) html links of pagination
     */
    public String createLinks(int currentPage, PagingType type) {

        StringBuilder sb = new StringBuilder();
        int pageCount = pageCount();
        Jmvc.logDebug("[Pagination:createPagingLinks] " + "PAGE COUNT: " + pageCount);

        if (totalRows > 0) {

            if (!baseURL.endsWith("/")) {
                baseURL += "/";
            }

            if (type == PagingType.ITEM) {

                /*int diff = 0;
                if (noItemsPerQuery < perPage) {
                diff = perPage - noItemsPerQuery;
                }*/

                int value = 0;

                if (totalRows <= perPage) {
                    value = totalRows;
                } else {
                    int page = currentPage + 1;
                    if ((totalRows / page) >= perPage) {
                        value = page * perPage;
                    } else {
                        value = (totalRows % perPage) + (perPage * currentPage);
                    }
                }

                //int value = ((currentPage + 1) * perPage) - diff;
                sb.append("<span>").append((currentPage * perPage) + 1).append(" - ").append(value).append(" of ").append(totalRows).append(" </span>");

                if (currentPage != 0 && totalRows != perPage) {
                    sb.append(" <a class='").append(classStartArrowOn).append("' href='").append(baseURL).append("").append("' >").append(imgStartArrowOn).append("</a>");
                    sb.append(" <a class='").append(classBackArrowOn).append("' href='").append(baseURL).append((currentPage - 1) == 0 ? "" : (currentPage - 1)).append("' >").append(imgBackArrowOn).append("</a>");
                } else {
                    sb.append(" <span>").append(imgStartArrowOff).append("</span> ");
                    sb.append(" <span>").append(imgBackArrowOff).append("</span> ");
                }

                if (currentPage + 1 < pageCount && totalRows != perPage) {
                    sb.append(" <a class='").append(classFwdArrowOn).append("' href='").append(baseURL).append(currentPage + 1).append("' >").append(imgFwdArrowOn).append("</a>");
                    sb.append(" <a class='").append(classEndArrowOn).append("' href='").append(baseURL).append(pageCount - 1).append("' >").append(imgEndArrowOn).append("</a>");
                } else {
                    sb.append(" <span>").append(imgFwdArrowOff).append("</span> ");
                    sb.append(" <span>").append(imgEndArrowOff).append("</span> ");
                }
            } else if (type == PagingType.SEARCH) {
                //PREV LINK IF NEEDED
                //&& totalRows!=perPage
                if (totalRows > perPage) {
                    if (currentPage != 0) {
                        sb.append(" <a class='").append(classBackArrowOn).append("' href='").append(baseURL).append((currentPage - 1) == 0 ? "" : currentPage - 1).append("' >").append(imgBackArrowOn).append("</a>");
                    }

                    //PAGES NUMBER LINKS
                    for (int i = currentPage - linksNum; i <= currentPage + linksNum; i++) {
                        if (i < 0 || i >= pageCount) {
                            continue;
                        } else {
                            if (i != currentPage) {
                                sb.append(" <a href='").append(baseURL).append((i == 0) ? "" : i).append("'>").append((i + 1)).append("</a>").append(separationChar);
                            } else {//CURRENT PAGE NO LINK
                                sb.append(" <b>").append(i + 1).append("</b>").append(separationChar);
                            }
                        }
                    }

                    //NEXT LINK IF NEEDED
                    if (currentPage + 1 < pageCount) {
                        sb.append(" <a class='").append(classFwdArrowOn).append("' href='").append(baseURL).append(currentPage + 1).append("' >").append(imgFwdArrowOn).append("</a>");
                    }
                }
            } else if (type == PagingType.AJAX) {
                sb.ensureCapacity(300);
                for (int i = pageCount; i > 0; i--) {
                    int from = (totalRows - ((pageCount - i) * perPage));
                    int to = (i - 1 == 0) ? from - (totalRows % perPage)+1 : from - perPage;
                    String page = (i == pageCount) ? "" : String.valueOf(pageCount-i);
                    sb.append(
                            div(
                                a("Comments (" + from + " - " + to + ")", o("href", "javascript:void(0);"),o("title", baseURL + page), o("class","toggler")),o("id", "page_" + page)
                               )
                            );
                    sb.append( div(i+".", o("class","actual_page")));
                }
                sb.append("<script type='text/javascript'>\n");
                sb.append("var req = new Request.HTML({});\n" +
                        "req.addEvent('complete', function(responseTree, responseElements, responseHTML, responseJavaScript){\n" +
                        "     req.options.update2.innerHTML = responseHTML;\n" +
                        "     req.options.update2.set('tween', {duration:1500});\n" +
                        "     req.options.update2.tween('height',req.options.update2.scrollHeight);\n" +
                        "});\n" +
                        "$$('.toggler').each( function(el,index){\n" +
                        "    if(index == 0){\n" +
                        "       get_content(el.title,el.getParent().nextSibling);\n" +
                        "     }\n" +
                        "     el.addEvent('click' , function(evt){\n" +
                        "        close(-1);\n" +
                        "        var the_event = new Event(evt);\n" +
                        "        $(the_event.target)\n" +
                        "        var act_page = $(the_event.target).getParent().nextSibling;\n" +
                        "        get_content(the_event.target.title,act_page);\n" +
                        "    });\n" +
                        "});\n" +
                        "function get_content(url, el){\n" +
                        "    req.options.url = url;\n" +
                        "    req.options.update2 = el;\n" +
                        "    req.get();\n" +
                        "}\n" +
                        "function close(exclude){\n" +
                        "  $$('.actual_page').each( function(el,index){\n" +
                        "      if(index != exclude){\n" +
                        "           el.tween('height',0);\n" +
                        "           el.setStyle('overflow', 'hidden');\n" +
                        "      }\n" +
                        "  });    \n" +
                        "}\n" +
                        "close(0);");                        
                sb.append("</script>\n");
            }
        } else {
            return "";
        }
        return sb.toString();
    }

    /**
     * @return the baseUrl
     */
    public String getBaseUrl() {
        return baseURL;
    }

    /**
     * Sets the base Url
     * @param baseUrl
     */
    public void setBaseUrl(String baseUrl) {
        this.baseURL = baseUrl;
    }

    /**
     * @return the totalRows representing the total records
     */
    public int getTotalRows() {
        return totalRows;
    }

    /**
     * Sets the number of rows
     * @param totalRows
     */
    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    /**
     * @return the current page
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * Sets the current Page number
     * @param curPage
     */
    public void setCurrentPage(int curPage) {
        this.currentPage = curPage;
    }

    /**
     * @return the number of records retruned per query with specific limit offset
     */
    public int getNoItemsPerQuery() {
        return noItemsPerQuery;
    }

    /**
     * Sets the number of records per query with specific limit offset
     * @param noItemsPerQuery
     */
    public void setNoItemsPerQuery(int noItemsPerQuery) {
        this.noItemsPerQuery = noItemsPerQuery;
    }

    /**
     * @return the max visible number of items per page
     */
    public int getPerPage() {
        return perPage;
    }

    /**
     * Sets the visible max number of items per page
     * @param perPage
     */
    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    /**
     * @return the number of page values that will be next-left and
     * next-right to the current page value in SEARCH PAGING TYPE
     */
    public int getLinksNum() {
        return linksNum;
    }

    /**
     * Sets the number of page values that will be next-left and
     * next-right to the current page value in SEARCH PAGING TYPE
     * @param leftRight
     */
    public void setLinksNum(int linksNum) {
        this.linksNum = linksNum;
    }

    /**
     * @return the separation character between page numbers in SEARCH PAGING TYPE
     */
    public String getSeparationChar() {
        return separationChar;
    }

    /**
     * Sets the separation character between page numbers in SEARCH PAGING TYPE
     * @param separationChar
     */
    public void setSeparationChar(String separationChar) {
        this.separationChar = separationChar;
    }

    public String getClassStartArrowOn() {
        return classStartArrowOn;
    }

    public void setClassStartArrowOn(String classStartArrowOn) {
        this.classStartArrowOn = classStartArrowOn;
    }

    public String getClassStartArrowOff() {
        return classStartArrowOff;
    }

    public void setClassStartArrowOff(String classStartArrowOff) {
        this.classStartArrowOff = classStartArrowOff;
    }

    public String getClassEndArrowOn() {
        return classEndArrowOn;
    }

    public void setClassEndArrowOn(String classEndArrowOn) {
        this.classEndArrowOn = classEndArrowOn;
    }

    public String getClassEndArrowOff() {
        return classEndArrowOff;
    }

    public void setClassEndArrowOff(String classEndArrowOff) {
        this.classEndArrowOff = classEndArrowOff;
    }

    public String getClassBackArrowOn() {
        return classBackArrowOn;
    }

    public void setClassBackArrowOn(String classBackArrowOn) {
        this.classBackArrowOn = classBackArrowOn;
    }

    public String getClassBackArrowOff() {
        return classBackArrowOff;
    }

    public void setClassBackArrowOff(String classBackArrowOff) {
        this.classBackArrowOff = classBackArrowOff;
    }

    public String getClassFwdArrowOn() {
        return classFwdArrowOn;
    }

    public void setClassFwdArrowOn(String classFwdArrowOn) {
        this.classFwdArrowOn = classFwdArrowOn;
    }

    public String getClassFwdArrowOff() {
        return classFwdArrowOff;
    }

    public void setClassFwdArrowOff(String classFwdArrowOff) {
        this.classFwdArrowOff = classFwdArrowOff;
    }

    public String getImgStartArrowOn() {
        return imgStartArrowOn;
    }

    public void setImgStartArrowOn(String imgStartArrowOn) {
        this.imgStartArrowOn = imgStartArrowOn;
    }

    public String getImgStartArrowOff() {
        return imgStartArrowOff;
    }

    public void setImgStartArrowOff(String imgStartArrowOff) {
        this.imgStartArrowOff = imgStartArrowOff;
    }

    public String getImgEndArrowOn() {
        return imgEndArrowOn;
    }

    public void setImgEndArrowOn(String imgEndArrowOn) {
        this.imgEndArrowOn = imgEndArrowOn;
    }

    public String getImgEndArrowOff() {
        return imgEndArrowOff;
    }

    public void setImgEndArrowOff(String imgEndArrowOff) {
        this.imgEndArrowOff = imgEndArrowOff;
    }

    public String getImgBackArrowOn() {
        return imgBackArrowOn;
    }

    public void setImgBackArrowOn(String imgBackArrowOn) {
        this.imgBackArrowOn = imgBackArrowOn;
    }

    public String getImgBackArrowOff() {
        return imgBackArrowOff;
    }

    public void setImgBackArrowOff(String imgBackArrowOff) {
        this.imgBackArrowOff = imgBackArrowOff;
    }

    public String getImgFwdArrowOn() {
        return imgFwdArrowOn;
    }

    public void setImgFwdArrowOn(String imgFwdArrowOn) {
        this.imgFwdArrowOn = imgFwdArrowOn;
    }

    public String getImgFwdArrowOff() {
        return imgFwdArrowOff;
    }

    public void setImgFwdArrowOff(String imgFwdArrowOff) {
        this.imgFwdArrowOff = imgFwdArrowOff;
    }
    
    public int pageCount(){
        int pageCount = (int) Math.floor(totalRows / perPage);
        if (totalRows % perPage > 0) {
            pageCount = (int) Math.floor(totalRows / perPage) + 1;
        }
        return pageCount;
    }

    public int getLastPage(){
        return pageCount() -1 ;
    }
    
    public boolean goToEndAfterAddition(){
        return (totalRows>getPerPage())?true:false;
    }

    public boolean lastRecordToDelete(){
        return (currentPage!=0 && ((currentPage+1) -  ((double)totalRows / getPerPage())) == 1  )?true:false;
    }
}