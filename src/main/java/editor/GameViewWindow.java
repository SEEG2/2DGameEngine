package editor;

import gmen.Window;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiWindowFlags;
import renderer.FrameBuffer;

public class GameViewWindow {
    public static void ImGUI() {
        ImGui.begin("Game View", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse);
        ImVec2 windowSize = getViewportMaxSize();
        ImVec2 windowPos = getViewportCenter(windowSize);

        ImGui.setCursorPos(windowPos.x, windowPos.y);
        int textureID = Window.getFrameBuffer().getTextureID();
        ImGui.image(textureID, windowSize.x, windowSize.y, 0, 1, 1, 0);
        ImGui.end();
    }

    private static ImVec2 getViewportMaxSize() {
        ImVec2 maxSize = new ImVec2();
        ImGui.getContentRegionAvail(maxSize);
        maxSize.x -= ImGui.getScrollX();
        maxSize.y -= ImGui.getScrollY();

        float aspectWidth = maxSize.x;
        float aspectHeight = aspectWidth / Window.getTargetAspectRatio();

        if (aspectHeight > maxSize.y) {
            aspectHeight = maxSize.y;
            aspectWidth = aspectHeight * Window.getTargetAspectRatio();
        }

        return new ImVec2(aspectWidth, aspectHeight);
    }

    private static ImVec2 getViewportCenter(ImVec2 aspectSize) {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);

        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float viewportX = (windowSize.x / 2) - (aspectSize.x / 2);
        float viewportY = (windowSize.y / 2) - (aspectSize.y / 2);

        return new ImVec2(viewportX + ImGui.getCursorPosX(), viewportY + ImGui.getCursorPosY());
    }

}
