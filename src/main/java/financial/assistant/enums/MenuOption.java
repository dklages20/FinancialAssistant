package financial.assistant.enums;

public enum MenuOption {

    CREATE_ACCOUNT("Create Account", "/assets/images/add-account.png"),
    VIEW_ACCOUNTS("View Accounts", "/assets/images/view-account.png"),
    EXPORT_ACCOUNT("Export Account", "/assets/images/export-icon.png");

    private String optionName;
    private String imagePath;

    private MenuOption(String optionName, String imagePath) {
        this.optionName = optionName;
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getOptionName() {
        return optionName;
    }
}
