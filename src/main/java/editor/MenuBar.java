package editor;

import EventSystem.EventSystem;
import EventSystem.events.Event;
import EventSystem.events.EventType;
import imgui.ImGui;

public class MenuBar {
    public void imGUI() {
        ImGui.beginMenuBar();

        if (ImGui.beginMenu("File")) {

            if (ImGui.menuItem("Save", "Ctrl+S")) {
                EventSystem.notify(null, new Event(EventType.SaveLevel));
            }

            if (ImGui.menuItem("Load", "Ctrl+O")) {
                EventSystem.notify(null, new Event(EventType.LoadLevel));
            }

            ImGui.endMenu();
        }

        ImGui.endMenuBar();
    }
}
