package editor;

import components.HierarchyHoverMarker;
import components.Sprite;
import components.SpriteRenderer;
import components.Transform;
import gmen.GameObject;
import gmen.Window;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import org.joml.Vector4f;

import java.util.List;

import static Constants.Color.GREEN;

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


        if (ImGui.isItemHovered()) {
            if (ImGui.isItemClicked()) {
                Window.getImGUILayer().getPropertiesWindow().setActiveGameObject(obj);
            }
            Window.getLevelEditorSceneInitializer().getLevelEditorComponents().getComponent(HierarchyHoverMarker.class).bindObject(obj);
        } else {
            Window.getLevelEditorSceneInitializer().getLevelEditorComponents().getComponent(HierarchyHoverMarker.class).unbind();
        }

        if (ImGui.beginDragDropSource()) {
            ImGui.setDragDropPayload(payLoadType, obj);
            ImGui.text(obj.name);
            ImGui.text("X: " + obj.transform.position.x);
            ImGui.text("Y: " + obj.transform.position.y);
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
