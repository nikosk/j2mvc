/*
 *  collection.java
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
package gr.dsigned.jmvc.types;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class collection {

    public static <K, V> Map<K, V> Map(Tuple2<K, V>... entries) {
        Map<K, V> map = new LinkedHashMap<K, V>();

        for (Tuple2<K, V> entry : entries) {
            map.put(entry._1, entry._2);
        }
        return map;
    }
}
