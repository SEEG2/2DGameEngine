package editor;

import gmen.Window;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiWindowFlags;

public class GameViewWindow {
    public static void ImGUI() {
        ImGui.begin("Game View", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoScrollWithMouse);
        ImVec2 windowSize = getViewportMaxSize();
        ImVec2 windowPos = getViewportCenter();

        ImGui.setCursorPos(windowPos.x, windowPos.y);
        int texturID = Window.getFramebuffer().getTextureID();
        ImGui.image(texturID, windowSize.x, windowSize.y, 0, 1, 1, 0);
        ImGui.end();
    }
}
