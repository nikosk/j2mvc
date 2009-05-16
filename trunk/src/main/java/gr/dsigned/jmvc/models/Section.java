/*
 *  Section.java
 *
 *  Copyright (C) Apr 13, 2009 Nikosk <nikosk@dsigned.gr>
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

import gr.dsigned.jmvc.db.Model;
import gr.dsigned.jmvc.db.QuerySet;
import gr.dsigned.jmvc.types.Hmap;
import gr.dsigned.jmvc.types.Node;
import java.util.ArrayList;
import static gr.dsigned.jmvc.db.QuerySet.LogicOperands.*;

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Section extends Model {

    public Section() throws Exception {
        this.tableName = "sections";
    }

    /**
     * Returns a graph of the section tree
     * @return a tree of categories
     * @throws java.lang.Exception
     */
    public SectionNode getSectionGraph() throws Exception {
        ArrayList<Hmap> rootSection = getRootSection();
        SectionNode rootNode;
        if (rootSection != null && rootSection.size() == 1) {
            rootNode = new SectionNode(rootSection.get(0));
            walkSections(rootNode);
        } else if (rootSection != null && rootSection.size() > 0) { // multiple root nodes
            rootNode = new SectionNode();
            for(Hmap nodeData : rootSection){
                SectionNode node = new SectionNode(nodeData);
                walkSections(node);
                rootNode.addChild(node);
            }
        }else {
            Hmap data = new Hmap();
            data.put("name", "empty node");
            rootNode = new SectionNode(data);
        }
        return rootNode;
    }

    private void walkSections(SectionNode walkee) throws Exception {
        ArrayList<Hmap> children = getByParentId(walkee.getData().get("id"));
        for (Hmap c : children) {
            SectionNode n = new SectionNode(c);
            walkee.addChild(n);
            walkSections(n);
        }
    }

    private ArrayList<Hmap> getByParentId(String parentid) throws Exception {
        QuerySet qs = new QuerySet();
        qs.from(tableName);
        qs.where("parent_id", parentid, EQUAL);
        return db.getList(qs);
    }

    private ArrayList<Hmap> getRootSection() throws Exception {
        QuerySet qs = new QuerySet();
        qs.from(tableName);
        qs.where("parent_id", "NULL", IS);
        return db.getList(qs);
    }

    public Hmap getSectionIdNamePair() throws Exception {
        Hmap data = new Hmap();
        QuerySet qs = new QuerySet();
        qs.select("id", "name");
        qs.from(this.tableName);
        for (Hmap hm : db.getList(qs)) {
            data.put(hm.get("id"), hm.get("name"));
        }
        return data;
    }

    public Hmap getSectionByUrlName(String name) throws Exception {
        QuerySet qs = new QuerySet();
        qs.from(this.tableName);
        qs.where("url_name", name, EQUAL);
        return db.getObject(qs);
    }

    public void update(String id, Hmap data) throws Exception {
        QuerySet qs = new QuerySet();
        qs.update(tableName, data);
        qs.where("id", id, EQUAL);
        qs.limit(1);
        db.update(qs);
    }

    /**
     * Extends Node to provide named Nodes
     * @param <HMap>
     */
    public static class SectionNode extends Node<Hmap> {

        private String name;

        public SectionNode() {
            super();
        }

        public SectionNode(Hmap data) {
            super(data);
            this.name = data != null ? data.get("url_name") : null;
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
