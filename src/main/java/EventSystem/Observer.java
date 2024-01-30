package EventSystem;

import EventSystem.events.Event;
import gmen.GameObject;

public interface Observer {
    void onNotify(GameObject gameObject, Event event);
}
