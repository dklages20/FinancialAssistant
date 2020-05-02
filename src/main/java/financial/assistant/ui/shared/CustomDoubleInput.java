package financial.assistant.ui.shared;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;

public class CustomDoubleInput extends HBox {

    private String text1Label;
    private String text1Prompt;
    private String text2Label;
    private String text2Prompt;

    private CustomDoubleInput(String text1Label, String text1Prompt, String text2Label, String text2Prompt) {
        CustomTextInput text1Input = new CustomTextInput.CustomTextInputBuilder().withText(text1Label).withPrompt(text1Prompt).build();
        CustomTextInput text2Input = new CustomTextInput.CustomTextInputBuilder().withText(text2Label).withPrompt(text2Prompt).build();

        // build constraints
        this.setPadding(new Insets(10, 10, 10 , 10));
        this.getChildren().addAll(text1Input, text2Input);
    }

    private String getExpenseName() {
        return ((CustomTextInput) this.getChildren().get(0)).getText();
    }

    private String getExpenseCost() {
        return ((CustomTextInput) this.getChildren().get(1)).getText();
    }

    public static class CustomDoubleInputBuilder {

        private String text1Label;
        private String text1Prompt;
        private String text2Label;
        private String text2Prompt;

        public CustomDoubleInputBuilder() {

        }

        public CustomDoubleInputBuilder withText1Label(String text) {
            this.text1Label = text;
            return this;
        }

        public CustomDoubleInputBuilder withText2Label(String text) {
            this.text2Label = text;
            return this;
        }

        public CustomDoubleInputBuilder withText1Prompt(String prompt) {
            this.text1Prompt = prompt;
            return this;
        }

        public CustomDoubleInputBuilder withText2Prompt(String prompt) {
            this.text2Prompt = prompt;
            return this;
        }

        public CustomDoubleInput build() {
            return new CustomDoubleInput(this.text1Label, this.text1Prompt, this.text2Label, this.text2Prompt);
        }
    }
}
