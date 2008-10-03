/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.dsigned.jmvc.db.enums;

/**
 *
 * @author USER
 */
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
