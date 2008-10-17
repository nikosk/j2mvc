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

import gr.dsigned.jmvc.types.Hmap;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This object permits you to chain 
 * commands to build a query. QuerySets do not
 * make database connections until they are explicitly 
 * executed.
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class QuerySet {

    private String selectSet;
    private String selectDistinctSet;
    private String fromSet;
    private String joinSet;
    private String whereSet;
    private String andClauseSet;
    private String orderBySet;
    private String limitSet;
    private String groupBySet;
    private String setSet;
    private String insertSet;
    private String updateSet;
    private String deleteSet;
    private String unionSet;
    private ArrayList<String> data = new ArrayList<String>();
    private ArrayList<String> setData = new ArrayList<String>();
    private ArrayList<String> whereData = new ArrayList<String>();
    private ArrayList<String> andClauseData = new ArrayList<String>();
    private ArrayList<String> unionData = new ArrayList<String>();
    private ArrayList<String> fromData = new ArrayList<String>();
    private ArrayList<String> updatedTables = new ArrayList<String>();
    private ArrayList<String> sourceTables = new ArrayList<String>();
    public enum Join {
        INNER("INNER"),
        OUTER("OUTER"),
        LEFT("LEFT"),
        RIGHT("RIGHT");
        private final String value;
        Join(String value) {
            this.value = value;
        }
        @Override
        public String toString() {
            return value;
        }
    }
    
    private boolean hasRan = false;

    /**
     * Build the select statement.
     * @param str The columns you need to select as string
     * @return QuerySet
     */
    public QuerySet select(String str) throws SQLException {
        hasRan();
        selectSet = str;
        return this;
    }

    /**
     * Build the select statement.
     * @param str The columns you need to select as string parameters
     * @return QuerySet
     */
    public QuerySet select(String... str) throws SQLException {
        hasRan();
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
     * Select distinct (same as select but builds a distict select)
     * @param bool
     * @return QuerySet
     */
    public QuerySet selectDistinct(String str) throws SQLException {
        hasRan();
        selectDistinctSet = str;
        return this;
    }

    /**
     * Select distinct (same as select but builds a distict select)
     * @param bool
     * @return QuerySet
     */
    public QuerySet selectDistinct(String... str) throws SQLException {
        hasRan();
        String cols = "";
        for (int i = 0; i < str.length; i++) {
            cols += str[i];
            if (str.length != i + 1) {
                cols += ", ";
            }
        }
        selectDistinctSet = cols;
        return this;
    }

    /**
     * Builds the where part of the query. Used internally by the public 
     * where methods.
     * @param column the column used by the where 
     * @param value the value to compare
     * @param type type of where (AND or OR)
     * @return
     */
    public QuerySet whereIn(String column, String value, String type) throws SQLException {
        hasRan();
        if (whereSet == null) {
            whereSet = "\nWHERE ";
        } else {
            whereSet += "\n" + type + " ";
        }
        checkSqlInValue(value);
        whereSet += column + " " + Operand.IN + " (" + value + ")";
        return this;
    }

    /**
     * Builds the where part of the query. 
     * @param column the column used by the where 
     * @param values 
     * @param value the value to compare
     * @param type type of where (AND or OR)
     * @return
     */
    public QuerySet whereIn(String column, ArrayList<String> values, String type) throws SQLException {
        hasRan();
        if (whereSet == null) {
            whereSet = "\nWHERE ";
        } else {
            whereSet += "\n" + type + " ";
        }
        whereSet += column + " " + Operand.IN + " (" + renderArrayListWithIds(values) + ")";
        return this;
    }

    /**
     * Builds the where part of the query. 
     * This methods compiles a query set if a sub query in select is needed 
     * @param column the column used by the where 
     * @param values 
     * @param value the value to compare
     * @param type type of where (AND or OR)
     * @return
     */
    public QuerySet whereIn(String column, QuerySet innerQuery, String type) throws SQLException {
        hasRan();
        if (whereSet == null) {
            whereSet = "\nWHERE ";
        } else {
            whereSet += "\n" + type + " ";
        }
        whereSet += column + " " + Operand.IN + " (" + innerQuery.compileSelect() + ")";
        whereData.addAll(innerQuery.getData());
        sourceTables.addAll(innerQuery.getSourceTables());
        return this;
    }

    private String renderArrayListWithIds(ArrayList<String> values) throws SQLException {
        hasRan();
        String result = "";
        StringBuilder sb = new StringBuilder();
        for (String value : values) {
            sb.append(value).append(",");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        result = sb.toString();
        //Additional check to verify that String in list are integers throws exception if not
        checkSqlInValue(result);
        return result;
    }

    private void checkSqlInValue(String input) {
        String stringValues[] = input.split(",");
        for (int i = 0; i < stringValues.length; i++) {
            Integer.parseInt(stringValues[i]);
        }
    }

    /**
     * Builds the where part of the query. Use with caution
     * @deprecated 
     * @param sql 
     * @param values 
     * @param column the column used by the where
     * @param type type of where (AND or OR)
     * @return
     */
    public QuerySet flexibleWhere(String sql, ArrayList<String> values, String type) throws SQLException {
        hasRan();
        if (whereSet == null) {
            whereSet = "\nWHERE ";
        } else {
            whereSet += "\n" + type + " ";
        }
        whereSet += " ( " + sql + " ) ";
        for (String value : values) {
            whereData.add(value);
        }
        return this;
    }

    public QuerySet andClause(String column, String value, Operand operand, String type) throws SQLException {
        hasRan();
        String s = "\n" + column + " " + operand + " ? ";
        andClauseSet = andClauseSet == null ? s : type + " " + andClauseSet + s;
        andClauseData.add(value);
        return this;
    }

    /**
     * Builds the where part of the query. Used internally by the public 
     * where methods.
     * @param column the column used by the where 
     * @param value the value to compare
     * @param operand the type of the comparison (=,<> etc)
     * @param type type of where (AND or OR)
     * @return
     */
    private QuerySet where(String column, String value, Operand operand, String type) throws SQLException {
        hasRan();
        if (whereSet == null) {
            whereSet = "\nWHERE ";
        } else {
            whereSet += "\n" + type + " ";
        }
        whereSet += column + " " + operand + " ? ";
        whereData.add(value);
        return this;
    }

    /**
     * Builds the where part of the query. Used internally by the public 
     * where methods.
     * @deprecated 
     * @param column the column used by the where 
     * @param type type of where (AND or OR)
     * @return
     */
    public QuerySet whereIsNull(String column, String type) throws SQLException {
        hasRan();
        if (whereSet == null) {
            whereSet = "\nWHERE ";
        } else {
            whereSet += "\n" + type + " ";
        }
        whereSet += column + " " + " IS NULL ";
        return this;
    }

    /**
     * Builds the where part of the query. Used internally by the public 
     * where methods.
     * @deprecated 
     * @param column the column used by the where 
     * @param type type of where (AND or OR)
     * @return
     */
    public QuerySet whereIsNotNull(String column, String type) throws SQLException {
        hasRan();
        if (whereSet == null) {
            whereSet = "\nWHERE ";
        } else {
            whereSet += "\n" + type + " ";
        }
        whereSet += column + " IS NOT NULL ";
        return this;
    }

    public QuerySet orWhere(String key, String value, Operand operand) throws SQLException {
        hasRan();
        return where(key, value, operand, "OR");
    }

    public QuerySet where(String key, String value, Operand operand) throws SQLException {
        hasRan();
        return where(key, value, operand, "AND");
    }

    /**
     * Build the FROM of the query.
     * CAUTION: DON'T PLACE MULTIPLE TABLE NAMES HERE. JUST CALL from() MULTIPLE TIMES
     * @param str A SINGLE TABLE NAME
     * @return QuerySet
     */
    public QuerySet from(String tableName) throws SQLException {
        hasRan();
        fromSet = " FROM " + tableName;
        sourceTables.add(tableName);
        return this;
    }

    /**
     * Builds a composite FROM part of the query.
     * 
     * @param qs A QuerySet that produces data (a SELECT)
     * @param alias The alias for the intermediate table
     * @return queryset
     */
    public QuerySet from(QuerySet qs, String alias) throws SQLException {
        hasRan();
        fromSet = "\nFROM (" + qs.compileSelect() + ") as " + alias + " ";
        fromData.addAll(qs.getData());
        sourceTables.addAll(qs.getSourceTables());
        return this;
    }

    /**
     * Builds the join part.
     * @param table
     * @param condition i.e. post.id = comment.post_id
     * @param type INNER, OUTER, LEFT, RIGHT
     * @return QuerySet
     */
    public QuerySet join(String table, String condition, Join type) throws SQLException {
        hasRan();
        joinSet = (joinSet == null) ? "\n" + type + " JOIN " + table + " ON " + condition : joinSet + "\n" + type + " JOIN " + table + " ON " + condition;
        sourceTables.add(table);
        return this;
    }

    public QuerySet orderBy(OrderBy orderType, String... fields) throws SQLException {
        hasRan();
        StringBuilder sb = new StringBuilder();
        sb.append("\nORDER BY ");
        for (String field : fields) {
            sb.append(field).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(orderType);
        orderBySet = sb.toString();
        return this;
    }

    /**
     * Build the LIMIT part
     * @param limit 
     * @return
     */
    public QuerySet limit(int limit) throws SQLException {
        hasRan();
        limitSet = "\nLIMIT " + limit;
        return this;
    }

    /**
     * Build the LIMIT part
     * @param limit
     * @param offset
     * @return
     */
    public QuerySet limit(int offset, int limit) throws SQLException {
        hasRan();
        limitSet = "\nLIMIT " + offset + ", " + limit;
        return this;
    }

    public QuerySet update(String tableName) throws SQLException {
        hasRan();
        updateSet = "UPDATE " + tableName;
        updatedTables.add(tableName);
        return this;
    }

    public QuerySet delete(String tableName) throws SQLException {
        hasRan();
        deleteSet = "DELETE ";
        from(tableName);
        updatedTables.add(tableName);
        return this;
    }

    public QuerySet update(String tableName, Hmap data) throws SQLException {
        hasRan();
        update(tableName);
        updatedTables.add(tableName);
        for (String k : data.keySet()) {
            set(k, data.get(k));
        }
        return this;
    }

    /**
     * Builds the SET part of an UPDATE or INSERT.
     * Use multiple set calls to set all the data
     * then call QuerySet.update(String tableName); to execute.
     * @param key
     * @param value
     * @return
     */
    public QuerySet set(String key, String value) throws SQLException {
        hasRan();
        if (setSet == null) {
            setSet = "\nSET ";
        } else {
            setSet += ",";
        }
        setSet += key + "=?";
        setData.add(value);
        return this;
    }

    public QuerySet insert(String tableName) throws SQLException {
        hasRan();
        this.insertSet = "INSERT INTO " + tableName;
        updatedTables.add(tableName);
        return this;
    }

    public QuerySet insert(String tableName, Hmap bean) throws SQLException {
        hasRan();
        insert(tableName);
        updatedTables.add(tableName);
        for (String key : bean.keySet()) {
            set(key, bean.get(key));
        }
        return this;
    }

    public QuerySet groupBy(String groupByField) throws SQLException {
        hasRan();
        this.groupBySet = "\nGROUP BY " + groupByField;
        return this;
    }

    /**
     * Accepts a SELECT type queryset and builds 
     * a union select between them.
     * @param qs A select type queryset
     * @return
     */
    public QuerySet union(QuerySet qs) throws SQLException {
        hasRan();
        unionSet = (unionSet == null) ? "\nUNION " + qs.compileSelect() : unionSet + "\nUNION " + qs.compileSelect();
        unionData.addAll(qs.getData());
        sourceTables.addAll(qs.getSourceTables());
        return this;
    }

    /**
     * Builds the query and returns an sql string.
     * @return SQL query
     */
    protected String compileSelect() {
        data.clear();
        StringBuilder sb = new StringBuilder();
        sb.append(selectDistinctSet == null ? "SELECT " : "SELECT DISTINCT ");
        sb.append(selectSet == null ? "*" : selectSet);
        sb.append(fromSet == null ? "" : fromSet);
        if (fromData.size() > 0) {
            data.addAll(fromData);
        }
        sb.append(joinSet == null ? "" : joinSet);
        sb.append(whereSet == null ? "" : whereSet);
        sb.append(andClauseSet == null ? "" : " AND (" + andClauseSet + ") ");
        if (andClauseSet != null) {
            data.addAll(andClauseData);
        }
        sb.append(groupBySet == null ? "" : groupBySet);
        sb.append(orderBySet == null ? "" : orderBySet);
        sb.append(limitSet == null ? "" : limitSet);
        if (whereSet != null) {
            data.addAll(whereData);
        }
        if (unionSet != null) {
            sb.append(unionSet);
            data.addAll(unionData);
        }
        hasRan = true;
        return sb.toString();
    }

    protected String compileCount() {
        data.clear();
        StringBuilder sb = new StringBuilder();
        sb.append(selectDistinctSet == null ? "SELECT " : "SELECT DISTINCT ");
        sb.append("count(*) AS count");
        sb.append(fromSet == null ? "FROM" : fromSet);
        sb.append(joinSet == null ? "" : joinSet);
        sb.append(whereSet == null ? "" : whereSet);
        sb.append(limitSet == null ? "" : limitSet);
        data.addAll(whereData);
        hasRan = true;
        return sb.toString();
    }

    protected String compileUpdate() {
        data.clear();
        StringBuilder sb = new StringBuilder();
        sb.append(updateSet);
        sb.append(setSet);
        sb.append(whereSet == null ? "" : whereSet);
        data.addAll(setData);
        data.addAll(whereData);
        hasRan = true;
        return sb.toString();
    }

    protected String compileDelete() throws SQLException {
        data.clear();
        StringBuilder sb = new StringBuilder();
        if (fromSet == null) {
            throw new SQLException("Empty from in delete");  // Be strict, this is a destructive operation.
        }
        sb.append(deleteSet == null ? "DELETE " : deleteSet);
        sb.append(fromSet);
        sb.append(whereSet == null ? "" : whereSet);
        sb.append(limitSet == null ? "" : limitSet);
        data.addAll(whereData);
        updatedTables.addAll(sourceTables);
        hasRan = true;
        return sb.toString();
    }

    protected String compileInsert() {
        data.clear();
        StringBuilder sb = new StringBuilder();
        sb.append(insertSet);
        sb.append(setSet);
        data.addAll(setData);
        hasRan = true;
        return sb.toString();
    }

    public ArrayList getData() {
        return data;
    }

    private void hasRan() throws SQLException {
        if (hasRan) {
            throw new SQLException("Tried to modify query set after it has been compiled");
        }
    }

    protected ArrayList<String> getSourceTables() {
        return sourceTables;
    }

    protected ArrayList<String> getUpdatedTables() {
        return updatedTables;
    }
}
