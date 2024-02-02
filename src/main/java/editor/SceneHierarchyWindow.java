package editor;

import gmen.GameObject;
import gmen.Window;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.flag.ImGuiWindowFlags;

import java.util.List;

public class SceneHierarchyWindow {
    public void ImGUI() {
        ImGui.begin("Scene Hierarchy");

        List<GameObject> gameObjects = Window.getScene().getGameObjects();

        int index = 0;

        for (GameObject obj: gameObjects) {
            if (!obj.isSerializable()) {
                continue;
            }

            ImGui.pushID(index);
            boolean treeNodeOpen = ImGui.treeNodeEx(obj.getName(),
                    ImGuiTreeNodeFlags.DefaultOpen |
                       ImGuiTreeNodeFlags.FramePadding |
                       ImGuiTreeNodeFlags.OpenOnArrow |
                       ImGuiTreeNodeFlags.SpanAvailWidth , obj.getName()
            );
            ImGui.popID();

            if (treeNodeOpen) {
                ImGui.treePop();
            }
            index++;
        }

        ImGui.end();
    }
}
