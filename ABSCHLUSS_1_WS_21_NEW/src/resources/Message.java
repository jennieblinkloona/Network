package resources;

public enum Message {



        IP_LIST("")

    ;

    private final String message;

    Message(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
