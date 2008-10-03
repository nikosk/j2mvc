package gr.dsigned.jmvc.db;

/**
 *
 * @author USER
 */
public enum OrderBy {
    ASC(" ASC "),
    DESC(" DESC ");

    
    private final String value;

    OrderBy(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
