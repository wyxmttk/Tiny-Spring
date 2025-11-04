package wyxmttk.event;

public class EventObject {
    private Object source;

    public EventObject(Object source) {
        this.source = source;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }
}
