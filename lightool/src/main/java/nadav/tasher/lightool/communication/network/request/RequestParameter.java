package nadav.tasher.lightool.communication.network.request;

public class RequestParameter {
    private String name;
    private String value;

    public RequestParameter(String n, String v) {
        name = n;
        value = v;
    }

    public String getName() {
        return name;
    }

    public void setName(String newname) {
        name = newname;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String newvalue) {
        value = newvalue;
    }
}
