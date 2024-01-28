package editor;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiStyleVar;
import org.joml.Vector2f;

public class ImGUIExtension {
    private static float defaultColumnWidth = 220;
    private static float resetValue = 0;

    public static void drawVec2Control(String label, Vector2f values, float resetValue, float columnWidth) {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, columnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);

        float lineHeight = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2;
        Vector2f buttonSize = new Vector2f(lineHeight + 3f, lineHeight);
        float widthEach = (ImGui.calcItemWidth() - buttonSize.x * 2f) / 2f;

        ImGui.pushItemWidth(widthEach);

        ImGui.pushStyleColor(ImGuiCol.Button, 0.8f, 0.1f, 0.15f, 0.8f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.8f, 0.1f, 0.15f, 0.4f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.8f, 0.1f, 0.15f, 1);
        if (ImGui.button("X", buttonSize.x, buttonSize.y)) {
            values.x = resetValue;
        }

        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] floatX = {values.x};
        ImGui.dragFloat("##x", floatX, 0.1f);
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.pushItemWidth(widthEach);

        ImGui.pushStyleColor(ImGuiCol.Button, 0.2f, 0.7f, 0.2f, 0.8f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.2f, 0.7f, 0.2f, 0.4f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.2f, 0.7f, 0.2f, 1);
        if (ImGui.button("Y", buttonSize.x, buttonSize.y)) {
            values.y = resetValue;
        }

        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] floatY = {values.y};
        ImGui.dragFloat("##y", floatY, 0.1f);
        ImGui.popItemWidth();
        ImGui.sameLine();
        ImGui.pushItemWidth(widthEach);

        ImGui.nextColumn();

        values.x = floatX[0];
        values.y = floatY[0];

        ImGui.popStyleVar();
        ImGui.columns(1);
        ImGui.popID();
    }

    public static void drawVec2Control(String label, Vector2f values, float resetValue) {
        drawVec2Control(label, values, resetValue, defaultColumnWidth);
    }

    public static void drawVec2Control(String label, Vector2f values) {
        drawVec2Control(label, values, resetValue, 220.0f);
    }
}
