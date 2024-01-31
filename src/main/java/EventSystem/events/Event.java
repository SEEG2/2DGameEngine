package EventSystem.events;

public class Event {
    public EventType eventType;

    public Event(EventType eventType) {
        this.eventType = eventType;
    }

    public Event() {
        this.eventType = EventType.UserEvent;
    }
}
