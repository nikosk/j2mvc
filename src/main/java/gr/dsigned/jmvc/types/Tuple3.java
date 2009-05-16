/*
 *  Tuple3.java
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
public class Tuple3<T1,T2,T3> extends Tuple2<T1,T2>{

    public T3 _3 = null;

    public Tuple3(T1 o1, T2 o2, T3 o3) {
        super(o1, o2);
        _3 = o3;
    }
}
