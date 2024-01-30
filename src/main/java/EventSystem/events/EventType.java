package EventSystem.events;

import EventSystem.Observer;
import gmen.GameObject;

import java.util.ArrayList;
import java.util.List;


public enum EventType {
    GameEngineStartPlay,
    GameEngineStopPlay,
    SaveLevel,
    LoadLevel,
    UserEvent
}
