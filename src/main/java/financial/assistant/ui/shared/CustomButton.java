package financial.assistant.ui.shared;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class CustomButton extends VBox{

    private String text;
    private Image image;

    private CustomButton(String text, Image image, Pos alignment) {
        this.text = text;
        this.image = image;

        this.setAlignment(alignment);

        // if an image is available, add an image to the vbox
        if(this.image != null) {
            this.getChildren().add(new ImageView(this.image));
        }

        // if text is available, add a label to the vbox
        if(this.text != null) {
            this.getChildren().add(new Label(this.text));
        }

        buildStyingEffects();

    }

    private void buildStyingEffects() {
        this.setOnMouseEntered(event -> {
            this.setStyle("-fx-opacity: 0.7");
        });

        this.setOnMouseExited(event -> {
            this.setStyle("-fx-opacity: 1.0");
        });

        this.setCursor(Cursor.HAND);
        this.setPadding(new Insets(10, 10, 10, 10));
    }

    public static class CustomButtonBuilder {

        private String text;
        private Image image;
        private Pos alignment;

        public CustomButtonBuilder() {

        }

        public CustomButtonBuilder withText(String text) {
            this.text = text;
            return this;
        }

        public CustomButtonBuilder withImage(Image image) {
            this.image = image;
            return this;
        }

        public CustomButtonBuilder withAlignment(Pos pos) {
            this.alignment = pos;
            return this;
        }

        public CustomButton build() {
            return new CustomButton(this.text, this.image, this.alignment);
        }
    }
}
