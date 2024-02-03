package components;

public class Properties extends Component {
    private boolean isEditorObject = false;
    public boolean allowMouseSelection = true;


    public boolean getIsEditorObject() {
        return this.isEditorObject;
    }

    public void setIsEditorObject(boolean b) {
        this.isEditorObject = b;
    }
}
