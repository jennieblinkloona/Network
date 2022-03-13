package resources;

public enum ErrorMessage {

    /**
     * The error message printed if the inputted ipv4 address does not match the required format
     */
    ILLEGAL_FORMAT("the input could not be parsed"),
    ILLEGAL_CHILDREN_LIST_SIZE("the size of the children list needs to be at least of 1"),
    ILLEGAL_CHILDREN_LIST_ROOT("the root cannot be a child")

    ;

    private static final String PREFIX = "Error, ";
    private final String message;
    ErrorMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return PREFIX + this.message;
    }
}
