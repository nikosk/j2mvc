/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.dsigned.jmvc.db;

/**
 *
 * @author USER
 */
public enum Operand {

    IS("IS"),
    IS_NOT("IS NOT"),
    IN("IN"),
    NOT_IN("NOT IN"),
    NULL("NULL"),
    EQUAL("="),
    NOT_EQUAL("!="),
    GREATER_THAN(">"),
    LESS_THAN("<"),
    GREATER_THAN_OR_EQUAL(">="),
    LESS_THAN_OR_EQUAL("<=");
    private final String value;

    Operand(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return " " + value + " ";
    }
}
