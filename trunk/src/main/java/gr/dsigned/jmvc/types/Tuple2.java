/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.dsigned.jmvc.types;

/**
 *
 * @author Nikos Kastamoulas <nikosk@dsigned.gr>
 */
public class Tuple2<T1, T2> extends Tuple1<T1> {
    public T2 _2 = null;

    public Tuple2(T1 o1, T2 o2) {
        super(o1);
        _2 = o2;
    }
}
