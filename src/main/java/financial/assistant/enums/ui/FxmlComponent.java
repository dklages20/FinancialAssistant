package financial.assistant.enums.ui;

public enum FxmlComponent {

    MAIN_COMPONENT("/fxml/main.component.fxml"),
    EXPORT_ACCOUNT_COMPONENT("/fxml/export-account.fxml"),
    VIEW_ACCOUNT_INFO_COMPONENT("/fxml/view-account-info.component.fxml"),
    VIEW_ACCOUNT_COMPONENT("/fxml/view-account.component.fxml"),
    CREATE_ACCOUNT_COMPONENT("/fxml/create-account.component.fxml"),
    ADD_EXPENSE_COMPONENT("/fxml/add-expense.component.fxml"),
    EDIT_ACCOUNT_COMPONENT("/fxml/account-management/edit-account.fxml");

    private String resoucePath;

    private FxmlComponent(String resourcePath) {
        this.resoucePath = resourcePath;
    }
    
    public String getResoucePath() {
        return resoucePath;
    }
}