package editor;

import EventSystem.EventSystem;
import EventSystem.events.Event;
import EventSystem.events.EventType;
import gmen.MouseListener;
import gmen.Window;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector2f;

public class GameViewWindow {

    private float leftX, rightX, topY, bottomY;
    private boolean isPlaying = false;

    public void imGUI() {
        ImGui.begin("Game View", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse | ImGuiWindowFlags.MenuBar);

        ImGui.beginMenuBar();
        if (ImGui.menuItem("Play", "", isPlaying, !isPlaying)) {
            isPlaying = true;
            EventSystem.notify(null, new Event(EventType.GameEngineStartPlay));
        }

        if (ImGui.menuItem("Stop", "", !isPlaying, isPlaying)) {
            isPlaying = false;
            EventSystem.notify(null, new Event(EventType.GameEngineStopPlay));
        }

        ImGui.endMenuBar();

        ImVec2 windowSize = getViewportMaxSize();
        ImVec2 windowPos = getViewportCenter(windowSize);

        ImGui.setCursorPos(windowPos.x, windowPos.y);

        ImVec2 topLeft = new ImVec2();
        ImGui.getCursorScreenPos(topLeft);
        topLeft.x -= ImGui.getScrollX();
        topLeft.y -= ImGui.getScrollY();

        leftX = topLeft.x;
        bottomY = topLeft.y;
        rightX = topLeft.x + windowSize.x;
        topY = topLeft.y + windowSize.y;

        int textureID = Window.getFrameBuffer().getTextureID();
        ImGui.image(textureID, windowSize.x, windowSize.y, 0, 1, 1, 0);

        MouseListener.setGameViewportPos(new Vector2f(topLeft.x, topLeft.y));
        MouseListener.setGameViewportSize(new Vector2f(windowSize.x, windowSize.y));

        ImGui.end();
    }

    private ImVec2 getViewportMaxSize() {
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

    private ImVec2 getViewportCenter(ImVec2 aspectSize) {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);

        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float viewportX = (windowSize.x / 2) - (aspectSize.x / 2);
        float viewportY = (windowSize.y / 2) - (aspectSize.y / 2);

        return new ImVec2(viewportX + ImGui.getCursorPosX(), viewportY + ImGui.getCursorPosY());
    }

    public boolean getWantCaptureMouse() {
        return MouseListener.getX() > leftX && MouseListener.getX() < rightX && MouseListener.getY() > topY && MouseListener.getY() < bottomY;
    }

}
