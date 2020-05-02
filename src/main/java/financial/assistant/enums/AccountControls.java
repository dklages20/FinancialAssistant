package financial.assistant.enums;

public enum AccountControls {

    EDIT_ACCOUNT("Edit Account", "/assets/images/edit-button.png"),
    DELETE_ACCOUNT("Delete Account", "/assets/images/delete-icon.png");

    private String options;
    private String imagePath;

    private AccountControls(String options, String imagePath) {
        this.options = options;
        this.imagePath = imagePath;
    }

    public String getOptions() {
        return options;
    }

    public String getImagePath() {
        return imagePath;
    }
}
