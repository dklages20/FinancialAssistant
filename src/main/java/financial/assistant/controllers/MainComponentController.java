package financial.assistant.controllers;

import financial.assistant.enums.MenuOption;
import financial.assistant.ui.shared.CustomButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MainComponentController {

    private @Autowired ApplicationContext applicationContext;

    private @FXML ToolBar menuToolbar;
    private @FXML VBox mainBox;

    public void initialize() {
        buildToolbarOptions();
    }

    public void buildToolbarOptions() {

        for(MenuOption menuOption: MenuOption.values()) {
            CustomButton button = new CustomButton.CustomButtonBuilder().withAlignment(Pos.CENTER).withImage(new Image(menuOption.getImagePath())).withText(menuOption.getOptionName()).build();

            switch (menuOption) {
                case CREATE_ACCOUNT:
                    button.setOnMouseClicked(event -> {
                        clear();
                        openCreateAccountComponent();
                    });
                    break;
                case VIEW_ACCOUNTS:
                    button.setOnMouseClicked(event -> {
                        clear();
                        openViewAccountsComponent();
                    });
                    break;
                case EXPORT_ACCOUNT:
                    button.setOnMouseClicked(event -> {
                        clear();
                        openExportAccountComponent();
                    });
                    break;
            }

            this.menuToolbar.getItems().add(button);
        }

    }

    public void clear() {
        if(this.mainBox.getChildren().size() > 1) {
            this.mainBox.getChildren().remove(1);
        }
    }

    public void openCreateAccountComponent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/create-account.component.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            Parent root = loader.load();
            this.mainBox.getChildren().add(root);
        }catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void openViewAccountsComponent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/view-account.component.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            Parent root = loader.load();
            this.mainBox.getChildren().add(root);
        }catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void openExportAccountComponent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/export-account.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            Parent root = loader.load();
            this.mainBox.getChildren().add(root);
        }catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}
