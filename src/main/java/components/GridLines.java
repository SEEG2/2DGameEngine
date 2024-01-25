package components;

import components.Component;
import gmen.Camera;
import gmen.Window;
import org.joml.Vector2f;
import renderer.DebugDraw;
import util.Settings;

import static Constants.Color.GREY;

public class GridLines extends Component {
    @Override
    public void update(float dt) {
        Camera camera = Window.getScene().camera();
        Vector2f cameraPos = camera.position;
        Vector2f projectionSize = camera.getProjectionSize();

        int firstX = ((int)(cameraPos.x / Settings.GRID_WIDTH) - 1) * Settings.GRID_HEIGHT;
        int firstY = ((int)(cameraPos.y / Settings.GRID_HEIGHT) -1 ) * Settings.GRID_WIDTH;

        int numVerticalLines = (int) (projectionSize.x * camera.getZoom() / Settings.GRID_WIDTH) + 2;
        int numHorizontalLines = (int) (projectionSize.y * camera.getZoom() / Settings.GRID_HEIGHT) + 2;

        int width = (int) (projectionSize.x * camera.getZoom()) + Settings.GRID_WIDTH * 2;
        int height = (int) (projectionSize.y * camera.getZoom()) + Settings.GRID_HEIGHT * 2;

        int maxLines = Math.max(numVerticalLines, numHorizontalLines);
        for (int i = 0; i < maxLines; i++) {
            int x = firstX + (Settings.GRID_WIDTH * i);
            int y = firstY + (Settings.GRID_HEIGHT * i);

            if (i < numVerticalLines) {
                DebugDraw.addLine2D(new Vector2f(x,firstY), new Vector2f(x, firstY + height), GREY, 1, false);
            }

            if (i < numVerticalLines) {
                DebugDraw.addLine2D(new Vector2f(firstX,y), new Vector2f(firstX + width, y), GREY, 1, false);
            }
        }
    }
}
