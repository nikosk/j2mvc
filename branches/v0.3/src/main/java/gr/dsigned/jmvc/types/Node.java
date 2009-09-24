/*
 *  Node.java
 *
 *  Copyright (C) Apr 9, 2009 Nikosk <nikosk@dsigned.gr>
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
package gr.dsigned.jmvc.types;

import java.util.ArrayList;
import java.util.List;

/**
 * General purpose Heap Tree
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Node<T> {

    protected List<Node<T>> children = new ArrayList<Node<T>>();
    protected Node parent;
    protected T data;

    public Node() {
    }

    public Node(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getNode(T data) {
        if (data != null && data.equals(data)) {
            return this;
        } else {
            return walkIn(this, data);
        }
    }

    private Node walkIn(Node<T> node, T data) {
        for (Node n : node.getChildren()) {
            if (n.equals(node.getData())) {
                return n;
            } else {
                return walkIn(n, data);
            }
        }
        return null;
    }

    /**
     * Override the equals method so that
     * equality is determined by the node's data.
     * @param obj
     * @return true if this.data equals obj.data
     */
    @Override
    public boolean equals(Object obj) {
        try {
            Node n = (Node) obj;
            return data.equals(n.getData());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Add a node as a child of this node.
     * @param child
     * @return
     */
    public Node addChild(Node child) throws NodeExistsException {
        child.setParent(parent);
        children.add(child);
        return this;
    }

    /**
     * Return node's children
     * @return
     */
    public List<Node<T>> getChildren() {
        return this.children;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getData());
        sb.append("\n");
        int depthCounter = 0;
        printToString(this, sb, depthCounter);
        return sb.toString();
    }

    private void printToString(Node<T> node, StringBuilder sb, int depthCounter) {
        depthCounter++;
        for (Node<T> n : node.getChildren()) {
            for (int i = 0; i < depthCounter; i++) {
                sb.append("\t");
            }
            sb.append("Child : " + depthCounter + " ");
            sb.append(n.getData());
            sb.append("\n");
            if (n.getChildren().size() > 0) {
                printToString(n, sb, depthCounter);
                sb.append("\n");
            }
        }
    }

    public static class NodeExistsException extends Exception {

        private String msg = "There is already a node with the same data under this node.";

        @Override
        public String getMessage() {
            return msg;
        }

        @Override
        public String toString() {
            return msg;
        }
    }
}
