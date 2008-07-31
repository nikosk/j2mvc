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

import gr.dsigned.jmvc.db.Model.OrderBy;
import gr.dsigned.jmvc.types.Bean;

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
    private String updateSet;
    private String insertSet;
    private String valuesSet;
    private String table;
    private Bean data;

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

    public QuerySet orderBy(OrderBy orderType, String... fields) {
        if (data == null) {
            data = new Bean();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\nORDER BY ");
        int i = 0;
        for (String field : fields) {
            sb.append("?,");
            data.put("field-" + i, field);
        }
        sb.deleteCharAt(sb.length() - 1);

        if (orderType == OrderBy.ASC) {
            sb.append(" ASC ");
        } else {
            sb.append(" DESC ");
        }
        orderBySet = sb.toString();
        return this;
    }

    /**
     * Build the LIMIT part
     * @param limit 
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
    public QuerySet limit(int offset, int limit) {
        limitSet = "\nLIMIT " + offset + ", " + limit;
        return this;
    }

    /**
     * Builds the query and returns an sql string.
     * @return SQL query
     */
    protected String compileSelect() {
        StringBuilder sb = new StringBuilder();
        sb.append(distinctSet ? "SELECT DISTINCT " : "SELECT ");
        sb.append(selectSet == null ? "*" : selectSet);
        sb.append(fromSet == null ? "FROM" : fromSet);
        sb.append(joinSet == null ? "" : joinSet);
        sb.append(whereSet == null ? "" : whereSet);
        sb.append(orderBySet == null ? "" : orderBySet);
        sb.append(limitSet == null ? "" : limitSet);

        return sb.toString();
    }

    protected String compileCount() {
        StringBuilder sb = new StringBuilder();
        sb.append(distinctSet ? "SELECT DISTINCT " : "SELECT ");
        sb.append("count(*) AS count");
        sb.append(fromSet == null ? "FROM" : fromSet);
        sb.append(joinSet == null ? "" : joinSet);
        sb.append(whereSet == null ? "" : whereSet);
        sb.append(orderBySet == null ? "" : orderBySet);
        sb.append(limitSet == null ? "" : limitSet);

        return sb.toString();
    }

    protected String compileUpdate() {

        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ").append(table);
        sb.append(updateSet);
        sb.append(whereSet == null ? "" : whereSet);

        return sb.toString();
    }

    public Bean getData() {
        return data;
    }

    public void setData(Bean data) {
        this.data = data;
    }

    public QuerySet table(String tableName) {
        table = tableName;
        return this;
    }

    public QuerySet update(String key, String value) {
        if (updateSet == null) {
            updateSet = "\nSET ";
        } else {
            updateSet += ",";
        }
        updateSet += key + "=?";

        if (data == null) {
            data = new Bean();
        }
        data.put(key, value);
        return this;
    }

    private QuerySet where(String key, String value, String operand, String type) {
        if (whereSet == null) {
            whereSet = "\nWHERE ";
        } else {
            whereSet += "\n" + type + " ";
        }
        whereSet += key + operand + "? ";

        if (data == null) {
            data = new Bean();
        }
        data.put(key, value);
        return this;
    }

    public QuerySet orWhere(String key, String value, String operand) {
        return where(key, value, operand, "OR");
    }

    public QuerySet where(String key, String value, String operand) {
        return where(key, value, operand, "AND");
    }

    public QuerySet insert(String key, String value) {
        if (insertSet == null) {
            insertSet = "";
            valuesSet = "";
        } else {
            insertSet += ",";
            valuesSet += ",";
        }
        insertSet += key;
        valuesSet += "?";

        if (data == null) {
            data = new Bean();
        }
        data.put(key, value);
        return this;
    }

    public QuerySet insert(Bean bean) {
        for (String key : bean.keySet()) {
            insert(key, bean.get(key));
        }
        return this;
    }

    protected String compileInsert() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(table);
        sb.append("(").append(insertSet).append(")");
        sb.append(" VALUES ");
        sb.append("(").append(valuesSet).append(")");

        return sb.toString();
    }
}