/*
 *  operators.java
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

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class operators {

    public static <T1> Tuple1<T1> o(T1 o1) {
        return new Tuple1<T1>(o1);
    }

    public static <T1, T2> Tuple2<T1, T2> o(T1 o1, T2 o2) {
        return new Tuple2<T1, T2>(o1, o2);
    }
}
