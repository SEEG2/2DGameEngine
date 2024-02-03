package gmen;

import com.google.gson.*;
import components.Component;
import components.EditorProperties;
import components.Transform;

import java.lang.reflect.Type;

public class GameObjectDeserializer implements JsonDeserializer<GameObject> {
    @Override
    public GameObject deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        JsonArray components = jsonObject.getAsJsonArray("components");

        GameObject gameObject = new GameObject(name);

        for (JsonElement e : components) {
            Component c = jsonDeserializationContext.deserialize(e, Component.class);
            gameObject.addComponent(c);
        }

        gameObject.transform = gameObject.getComponent(Transform.class);
        gameObject.editorProperties = gameObject.getComponent(EditorProperties.class);
        return gameObject;
    }
}
