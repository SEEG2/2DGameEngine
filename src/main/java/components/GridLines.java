package components;

import components.Component;
import gmen.Camera;
import gmen.Window;
import org.joml.Vector2f;
import renderer.DebugDraw;
import scenes.Scene;
import util.Settings;

import static Constants.Color.GREY;

public class GridLines extends Component {
    @Override
    public void editorUpdate(float dt) {
        Camera camera = Window.getScene().camera();
        Vector2f cameraPos = camera.position;
        Vector2f projectionSize = camera.getProjectionSize();

        float firstX = ((int)(cameraPos.x / Settings.GRID_WIDTH) - 1) * Settings.GRID_HEIGHT;
        float firstY = ((int)(cameraPos.y / Settings.GRID_HEIGHT) -1 ) * Settings.GRID_WIDTH;

        int numVerticalLines = (int) (projectionSize.x * camera.getZoom() / Settings.GRID_WIDTH) + 2;
        int numHorizontalLines = (int) (projectionSize.y * camera.getZoom() / Settings.GRID_HEIGHT) + 2;

        float width = (int) (projectionSize.x * camera.getZoom()) + Settings.GRID_WIDTH * 2;
        float height = (int) (projectionSize.y * camera.getZoom()) + Settings.GRID_HEIGHT * 2;

        int maxLines = Math.max(numVerticalLines, numHorizontalLines);

        //TODO replace with zoom lvl
        if (true) {
            for (int i = 0; i < maxLines; i++) {
                float x = firstX + (Settings.GRID_WIDTH * i);
                float y = firstY + (Settings.GRID_HEIGHT * i);

                if (i < numVerticalLines) {
                    DebugDraw.addLine2D(new Vector2f(x,firstY), new Vector2f(x, firstY + height), GREY, 1, false);
                }

                if (i < numVerticalLines) {
                    DebugDraw.addLine2D(new Vector2f(firstX,y), new Vector2f(firstX + width, y), GREY, 1, false);
                }
            }
        }
    }
}
