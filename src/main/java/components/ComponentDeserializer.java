package components;

import com.google.gson.*;
import components.Component;

import java.lang.reflect.Type;

public class ComponentDeserializer implements JsonSerializer<Component>, JsonDeserializer<Component> {
    @Override
    public Component deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String joType = jsonObject.get("type").getAsString();
        JsonElement element = jsonObject.get("editorProperties");

        try {
            return jsonDeserializationContext.deserialize(element, Class.forName(joType));
        } catch (ClassCastException | ClassNotFoundException e) {
            throw new JsonParseException("Unknown element type ("  + type + ")" , e);
        }
    }

    @Override
    public JsonElement serialize(Component component, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(component.getClass().getCanonicalName()));
        result.add("editorProperties", jsonSerializationContext.serialize(component, component.getClass()));
        return result;
    }
}
