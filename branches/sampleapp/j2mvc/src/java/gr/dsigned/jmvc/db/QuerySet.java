/*
 *  QuerySet.java
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
package gr.dsigned.jmvc.db;

/**
 * This object permits you to chain 
 * commands to build a query. QuerySets do not
 * make database connections until they are explicitly 
 * executed.
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class QuerySet {

    private String selectSet;
    private boolean distinctSet;
    private String fromSet;
    private String joinSet;
    private String whereSet;
    private String orderBySet;
    private String limitSet;

    /**
     * Build the select statement.
     * @param str The columns you need to select as string
     * @return QuerySet
     */
    public QuerySet select(String str) {
        selectSet = str;
        return this;
    }

    /**
     * Build the select statement.
     * @param str The columns you need to select as string array
     * @return QuerySet
     */
    public QuerySet select(String[] str) {
        String cols = "";
        for (int i = 0; i < str.length; i++) {
            cols += str[i];
            if (str.length != i + 1) {
                cols += ", ";
            }
        }
        selectSet = cols;
        return this;
    }

    /**
     * Select distinct ?
     * @param bool
     * @return QuerySet
     */
    public QuerySet distinct(boolean bool) {
        this.distinctSet = bool;
        return this;
    }

    /**
     * Build the FROM of the query.
     * @param str
     * @return QuerySet
     */
    public QuerySet from(String str) {
        this.fromSet = " FROM " + str;
        return this;
    }

    /**
     * Builds the join part.
     * @param table
     * @param condition i.e. post.id = comment.post_id
     * @param type INNER, OUTER, LEFT, RIGHT
     * @return QuerySet
     */
    public QuerySet join(String table, String condition, String type) {
        joinSet = (joinSet == null) ? "\n" + type + " JOIN " + table + " ON " + condition : joinSet + "\n" + type + " JOIN " + table + " ON " + condition;
        return this;
    }

    /**
     * Builds the where part. If its called more than once it seperates 
     * clauses with AND
     * @param clause post.id = "something"
     * @return QuerySet
     */
    public QuerySet where(String clause) {
        whereSet = (whereSet == null) ? "\nWHERE " + clause : whereSet + "\nAND " + clause;
        return this;
    }

    /**
     * Builds the where part. If its called more than once it seperates 
     * clauses with OR
     * @param clause post.id = "something"
     * @return QuerySet
     */
    public QuerySet orWhere(String clause) {
        whereSet = (whereSet == null || whereSet.isEmpty()) ? whereSet + clause : whereSet + " OR " + clause;
        return this;
    }

    public QuerySet orderBy(String field, String direction) {
        orderBySet = "\nORDER BY " + field + " " + direction;
        return this;
    }

    /**
     * Build the LIMIT part
     * @param start 
     * @return
     */
    public QuerySet limit(int limit) {
        limitSet = "\nLIMIT " + limit;
        return this;
    }
    /**
     * Build the LIMIT part
     * @param limit
     * @param offset
     * @return
     */
    public QuerySet limit(int limit, int offset) {
        limitSet = "\nLIMIT " + limit + ", " + offset;
        return this;
    }
    /**
     * Builds the query and returns an sql string.
     * @return SQL query
     */
    protected String compileSelect() {
        String sql = (distinctSet) ? "SELECT DISTINCT " : "SELECT ";
        sql += (selectSet == null) ? "*" : selectSet;
        sql += (fromSet == null) ? "FROM" : fromSet;
        sql += (joinSet == null) ? "" : joinSet;
        sql += (whereSet == null) ? "" : whereSet;
        sql += (orderBySet == null) ? "" : orderBySet;
        sql += (limitSet == null) ? "" : limitSet;
        reset();
        System.out.println(sql);
        return sql;
    }
    
    protected String compileCount() {
        String sql = (distinctSet) ? "SELECT DISTINCT " : "SELECT ";
        sql += "count(*) AS count";
        sql += (fromSet == null) ? "FROM" : fromSet;
        sql += (joinSet == null) ? "" : joinSet;
        sql += (whereSet == null) ? "" : whereSet;
        sql += (orderBySet == null) ? "" : orderBySet;
        sql += (limitSet == null) ? "" : limitSet;
        reset();
        System.out.println(sql);
        return sql;
    }
    /**
     * Reset for re-use.
     */
    private void reset() {
        selectSet = null;
        distinctSet = false;
        fromSet = null;
        joinSet = null;
        whereSet = null;
        orderBySet = null;
        limitSet = null;
    }
}
