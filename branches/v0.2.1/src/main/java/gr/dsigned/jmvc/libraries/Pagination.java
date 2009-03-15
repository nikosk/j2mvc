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

import gr.dsigned.jmvc.framework.Library;
import static gr.dsigned.jmvc.framework.Renderer.div;
import static gr.dsigned.jmvc.framework.Renderer.a;
import static gr.dsigned.jmvc.types.operators.*;
import static gr.dsigned.jmvc.libraries.Localization.get;

/**
 * 16 Μαρ 2008, gr.dsigned.jmvc.libraries
 * Pagination.java
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Pagination extends Library {

    public enum PagingType {

        SEARCH, ITEM, AJAX;
    }
    public String baseUrl = "";
    public int totalRows = 0;
    public int curPage = 0;
    private int perPage = 10;
    private int numLinksToDisplay = 3;
    ///// CSS CLASSES
    private String nextLinkClass = "next_link";
    private String prevLinkClass = "prev_link";
    private String prevLinkClassDisabled = "prev_link_disabled";
    private String nextLinkDisabledClass = "next_link_disabled";
    private String startLinkClass = "start_link";
    private String startLinkClassDisabled = "start_link_disabled";
    private String lastLinkClass = "end_link";
    private String lastLinkClassDisabled = "end_link_disabled";
    //// LINK TEXT
    private String startLinkText = "&laquo; " + get("First") ;
    private String lastLinkText = get("Last") + " &raquo; ";
    private String previousLinkText = "&laquo; " + get("Prev");
    private String nextLinkText = get("Next") + " &raquo;";

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
        if (totalRows > 0) {
            if (!baseUrl.endsWith("/")) {
                baseUrl += "/";
            }
            if (type == PagingType.ITEM) {
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
                sb.append("<div class='item_page_links'>");

                sb.append("<span class='paging_item_count'>");
                sb.append((currentPage * perPage) + 1).append(" - ");
                sb.append(value).append(" / ").append(totalRows);
                sb.append(" </span>");
                sb.append(" <span class='item_page_navigation'>");
                if (currentPage != 0 && totalRows != perPage) {
                    sb.append(" <a class='").append(startLinkClass).append("' href='").append(baseUrl).append("").append("' >");
                    sb.append(startLinkText);
                    sb.append("</a>");
                    sb.append(" <a class='").append(prevLinkClass).append("' href='").append(baseUrl).append((currentPage - 1) == 0 ? "" : (currentPage - 1)).append("' >");
                    sb.append(previousLinkText);
                    sb.append("</a>");
                } else {
                    sb.append(" <span class='").append(startLinkClassDisabled).append("'>").append(startLinkText).append("</span> ");
                    sb.append(" <span class='").append(prevLinkClassDisabled).append("'>").append(previousLinkText).append("</span> ");
                }
                if (currentPage + 1 < pageCount && totalRows != perPage) {
                    sb.append(" <a class='").append(nextLinkClass).append("' href='").append(baseUrl).append(currentPage + 1).append("' >");
                    sb.append(nextLinkText);
                    sb.append("</a>");
                    sb.append(" <a class='").append(lastLinkClass).append("' href='").append(baseUrl).append(pageCount - 1).append("' >");
                    sb.append(lastLinkText);
                    sb.append("</a>");
                } else {
                    sb.append(" <span class='").append(nextLinkDisabledClass).append("'>").append(nextLinkText).append("</span> ");
                    sb.append(" <span class='").append(lastLinkClassDisabled).append("'>").append(lastLinkText).append("</span> ");
                }
                sb.append(" </span>");
                sb.append("</div>");

            } else if (type == PagingType.SEARCH) {
                if (totalRows > perPage) {
                    sb.append("<ul>");
                    sb.append("<li>");
                    if (currentPage == 0) {
                        sb.append(" <span class='").append(prevLinkClassDisabled).append("'>");
                        sb.append(previousLinkText);
                        sb.append("</span>");
                    } else {
                        sb.append(" <a class='").append(prevLinkClass).append("' href='").append(baseUrl).append((currentPage - 1) == 0 ? "" : currentPage - 1).append("' >");
                        sb.append(previousLinkText);
                        sb.append("</a>");
                    }
                    sb.append("</li>");
                    for (int i = currentPage - numLinksToDisplay; i <= currentPage + numLinksToDisplay; i++) {
                        int countPreviousLinks = 0;
                        int countNextLinks = 0;
                        boolean prev = true;
                        if (i < 0) {
                            continue;
                        } else if (i >= pageCount) {
                            break;
                        } else {
                            sb.append("<li>");
                            if (i != currentPage) {
                                sb.append(" <a href='").append(baseUrl).append((i == 0) ? "" : i).append("'>").append((i + 1)).append("</a>");
                                if (prev) {
                                    countPreviousLinks++;
                                } else {
                                    countNextLinks++;
                                }
                            } else {//CURRENT PAGE NO LINK
                                sb.append(" <b>").append(i + 1).append("</b>");
                                prev = false;
                            }
                            sb.append("</li>");
                        }
                    }
                    sb.append("<li>");
                    if (currentPage + 1 < pageCount) {
                        sb.append(" <a class='").append(nextLinkClass).append("' href='").append(baseUrl).append(currentPage + 1).append("' >").append(nextLinkText).append("</a>");
                    } else {
                        sb.append(" <span class='").append(nextLinkDisabledClass).append("'>").append(nextLinkText).append("</span>");
                    }
                    sb.append("</li>");
                    sb.append("</ul>");
                }
            } else if (type == PagingType.AJAX) {
                sb.ensureCapacity(300);
                for (int i = pageCount; i > 0; i--) {
                    int from = (totalRows - ((pageCount - i) * perPage));
                    int to = (i - 1 == 0) ? from - (totalRows % perPage) + 1 : from - perPage + 1;
                    String page = (i == pageCount) ? "" : String.valueOf(pageCount - i);
                    sb.append(
                            div(
                            a("Comments (" + from + " - " + to + ")", o("href", "javascript:void(0);"), o("title", baseUrl + page), o("class", "toggler")), o("id", "page_" + page)));
                    sb.append(div(i + ".", o("class", "actual_page")));
                }
                sb.append("<script type='text/javascript'>\n");
                sb.append("var req = new Request.HTML({});\n" +
                        "req.addEvent('complete', function(responseTree, responseElements, responseHTML, responseJavaScript){\n" +
                        "     req.options.update2.innerHTML = responseHTML;\n" +
                        "     req.options.update2.set('tween', {duration:1000});\n" +
                        "     req.options.update2.tween('height',req.options.update2.getScrollSize().y);\n" +
                        "});\n" +
                        "$$('.toggler').each( function(el,index){\n" +
                        "     el.addEvent('click' , function(evt){\n" +
                        "        close(-1);\n" +
                        "        var the_event = new Event(evt);\n" +
                        "        var act_page = $(the_event.target).getParent().nextSibling;\n" +
                        "        if(act_page.getStyle('overflow') == 'hidden'){\n" +
                        "           get_content(the_event.target.title,act_page);\n" +
                        "        }\n" +
                        "    });\n" +
                        "});\n" +
                        "function get_content(url, el){\n" +
                        "    req.options.url = url;\n" +
                        "    req.options.update2 = el;\n" +
                        "    req.get({'t': new Date().getTime()});\n" +
                        "}\n" +
                        "function close(exclude){\n" +
                        "  $$('.actual_page').each( function(el,index){\n" +
                        "      if(index != exclude){\n" +
                        "           el.tween('height',0);\n" +
                        "           el.setStyle('overflow', 'hidden');\n" +
                        "      }\n" +
                        "  });\n" +
                        "}\n" +
                        "get_content($$('.toggler')[0].title,$$('.toggler')[0].getParent().nextSibling);" +
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
        return baseUrl;
    }

    /**
     * Sets the base Url
     * @param baseUrl
     */
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
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
        return curPage;
    }

    /**
     * Sets the current Page number
     * @param curPage
     */
    public void setCurrentPage(int curPage) {
        this.curPage = curPage;
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
    public int getNumLinksToDisplay() {
        return numLinksToDisplay;
    }

    /**
     * Sets the number of page values that will be next-left and
     * next-right to the current page value in SEARCH PAGING TYPE
     * @param leftRight
     */
    public void setNumLinksToDisplay(int leftRight) {
        this.numLinksToDisplay = leftRight;
    }

    public String getClassStartArrowOn() {
        return startLinkClass;
    }

    public void setClassStartArrowOn(String classStartArrowOn) {
        this.startLinkClass = classStartArrowOn;
    }

    public String getClassEndArrowOn() {
        return nextLinkClass;
    }

    public void setClassEndArrowOn(String classEndArrowOn) {
        this.nextLinkClass = classEndArrowOn;
    }

    public String getClassBackArrowOn() {
        return prevLinkClass;
    }

    public void setClassBackArrowOn(String classBackArrowOn) {
        this.prevLinkClass = classBackArrowOn;
    }

    public String getClassFwdArrowOn() {
        return nextLinkClass;
    }

    public void setClassFwdArrowOn(String classFwdArrowOn) {
        this.nextLinkClass = classFwdArrowOn;
    }

    public String getImgStartArrowOn() {
        return startLinkText;
    }

    public void setImgStartArrowOn(String imgStartArrowOn) {
        this.startLinkText = imgStartArrowOn;
    }

    public String getImgEndArrowOn() {
        return nextLinkText;
    }

    public void setImgEndArrowOn(String imgEndArrowOn) {
        this.nextLinkText = imgEndArrowOn;
    }

    public String getImgBackArrowOn() {
        return previousLinkText;
    }

    public void setImgBackArrowOn(String imgBackArrowOn) {
        this.previousLinkText = imgBackArrowOn;
    }

    public String getImgFwdArrowOn() {
        return nextLinkText;
    }

    public void setImgFwdArrowOn(String imgFwdArrowOn) {
        this.nextLinkText = imgFwdArrowOn;
    }

    public int pageCount() {
        int pageCount = (int) Math.floor(totalRows / perPage);
        if (totalRows % perPage > 0) {
            pageCount = (int) Math.floor(totalRows / perPage) + 1;
        }
        return pageCount;
    }

    public int getLastPage() {
        return pageCount() - 1;
    }

    public boolean goToEndAfterAddition() {
        return (totalRows > getPerPage()) ? true : false;
    }

    public boolean lastRecordToDelete() {
        return (curPage != 0 && ((curPage + 1) - ((double) totalRows / getPerPage())) == 1) ? true : false;
    }
}