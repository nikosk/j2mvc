/*
 *  Category.java
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
package gr.dsigned.jmvc.models;

import gr.dsigned.jmvc.types.Hmap;
import java.util.ArrayList;

import gr.dsigned.jmvc.db.Model;
import gr.dsigned.jmvc.db.QuerySet;
import gr.dsigned.jmvc.db.QuerySet.LogicOperands;
import gr.dsigned.jmvc.types.Node;
import java.util.List;

/**
 * 15 Μαρ 2008, gr.dsigned.jmvc.models 
 * Category.java
 * @author Nikosk <nikosk@dsigned.gr>
 * @author Vas Chryssikou <vchrys@gmail.com>
 */
public class Category extends Model {

    public Category() throws Exception {
        this.tableName = "categories";
    }

    public ArrayList<Hmap> getCategories() throws Exception {
        QuerySet qs = new QuerySet();
        qs.from(this.tableName);
        return db.getList(qs);
    }

    public ArrayList<Hmap> getTopCategories() throws Exception {
        QuerySet qs = new QuerySet();
        qs.from(this.tableName);
        qs.where("parent_id", "NULL", LogicOperands.IS);
        return db.getList(qs);
    }

    /**
     * Returns a graph of the category tree
     * @return a tree of categories
     * @throws java.lang.Exception
     */
    public CategoryNode getCategoryGraph() throws Exception {
        CategoryNode categoryRoot = new CategoryNode();
        for (Hmap c : getTopCategories()) {
            CategoryNode categoryNode = new CategoryNode(c);
            walkCategories(categoryNode);
            categoryRoot.addChild(categoryNode);
        }
        return categoryRoot;
    }

    private void walkCategories(CategoryNode walkee) throws Exception {
        ArrayList<Hmap> children = getByParentId(walkee.getData().get("id"));
        for (Hmap c : children) {
            CategoryNode n = new CategoryNode(c);
            walkee.addChild(n);
            walkCategories(n);
        }
    }

    private ArrayList<Hmap> getByParentId(String parentid) throws Exception {
        QuerySet qs = new QuerySet();
        qs.from(tableName);
        qs.where("parent_id", parentid, QuerySet.LogicOperands.EQUAL);
        return db.getList(qs);
    }

    public Hmap getCategoryIdNamePair() throws Exception {
        Hmap data = new Hmap();
        QuerySet qs = new QuerySet();
        qs.select("id", "display_name", "name");
        qs.from(this.tableName);
        for (Hmap hm : db.getList(qs)) {
            data.put(hm.get("id"), hm.get("display_name") + "-" + hm.get("name"));
        }
        return data;
    }

    public Hmap getCategoryById(String id) throws Exception {
        QuerySet qs = new QuerySet();
        qs.from(this.tableName);
        qs.where("id", id, LogicOperands.EQUAL);
        return db.getObject(qs);
    }

    public Hmap getCategoryByName(String name) throws Exception {
        QuerySet qs = new QuerySet();
        qs.from(this.tableName);
        qs.where("name", name, LogicOperands.EQUAL);
        return db.getObject(qs);
    }

    public void update(String id, Hmap data) throws Exception {
        QuerySet qs = new QuerySet();
        qs.update(tableName, data);
        qs.where("id", id, LogicOperands.EQUAL);
        qs.limit(1);
        db.update(qs);
    }

    /**
     * Extends Node to provide named Nodes
     * @param <HMap>
     */
    public static class CategoryNode extends Node<Hmap> {

        private String name;

        public CategoryNode() {
            super();
        }

        public CategoryNode(Hmap data) {
            super(data);
            this.name = data != null ? data.get("name") : null;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name + ": " + super.toString();
        }
    }
}
