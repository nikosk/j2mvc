/*
 *  Images.java
 *
 *  Copyright (C) Apr 12, 2009 Nikosk <nikosk@dsigned.gr>
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

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Images extends Model {
    public Images() throws Exception {
        this.tableName = "images";
    }

    public Hmap getArticleImage(String articleId) throws Exception{
        QuerySet qs = new QuerySet();
        qs.from(tableName);
        qs.where("article_id", articleId, QuerySet.LogicOperands.EQUAL);
        return db.getObject(qs);
    }
}
