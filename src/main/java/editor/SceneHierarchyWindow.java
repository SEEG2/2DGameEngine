package editor;

import components.Sprite;
import gmen.GameObject;
import gmen.Window;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;

import java.util.List;

public class SceneHierarchyWindow {

    private static String payLoadType = "SceneHierarchy";

    public void imGUI() {
        ImGui.begin("Scene Hierarchy");

        List<GameObject> gameObjects = Window.getScene().getGameObjects();

        int index = 0;

        for (GameObject obj: gameObjects) {
            if (!obj.isSerializable()) {
                continue;
            }


            boolean treeNodeOpen = doTreeNode(index, obj);

            if (treeNodeOpen) {
                ImGui.treePop();
            }
            index++;
        }

        ImGui.end();
    }

    private boolean doTreeNode(int index, GameObject obj) {
        ImGui.pushID(index);
        boolean treeNodeOpen = ImGui.treeNodeEx(obj.name,
                ImGuiTreeNodeFlags.DefaultOpen |
                        ImGuiTreeNodeFlags.FramePadding |
                        ImGuiTreeNodeFlags.OpenOnArrow |
                        ImGuiTreeNodeFlags.SpanAvailWidth , obj.name
        );
        ImGui.popID();

        if (ImGui.beginDragDropSource()) {
            ImGui.setDragDropPayload(payLoadType, obj);
            ImGui.text(obj.name);
            //TODO add more information about the object here when dragging
            ImGui.endDragDropSource();
        }

        if (ImGui.beginDragDropTarget()) {
            Object payload = ImGui.acceptDragDropPayload(payLoadType);

            if (payload != null) {
                if (payload.getClass().isAssignableFrom(GameObject.class)) {
                    GameObject gameObject = (GameObject) payload;
                }
            }

            ImGui.endDragDropTarget();
        }

        return treeNodeOpen;
    }
}
