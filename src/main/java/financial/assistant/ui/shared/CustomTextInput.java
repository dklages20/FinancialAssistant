package financial.assistant.ui.shared;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class CustomTextInput extends VBox {

    private String labelText;
    private String inputPrompt;

    private CustomTextInput(String labelText, String inputPrompt) {
        Label textLabel = new Label(labelText);
        TextField textField = new TextField();
        if(inputPrompt != null) {
            textField.setPromptText(inputPrompt);
        }

        this.getChildren().addAll(textLabel, textField);
    }

    private TextField getInput() {
        TextField textField = null;
        if(this.getChildren().size() == 2) {
            textField =  (TextField) this.getChildren().get(1);
        }
        return textField;
    }

    public String getText() {
        TextField textField = getInput();
        String response = null;
        if(textField != null) {
            response = textField.getText();
        }
        return response;
    }

    public static class CustomTextInputBuilder {

        private String text;
        private String prompt;

        public CustomTextInputBuilder() {

        }

        public CustomTextInputBuilder withText(String text) {
            this.text = text;
            return this;
        }

        public CustomTextInputBuilder withPrompt(String prompt) {
            this.prompt = prompt;
            return this;
        }

        public CustomTextInput build() {
            return new CustomTextInput(this.text, this.prompt);
        }
    }
}
