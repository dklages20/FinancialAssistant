package financial.assistant.controllers;

import financial.assistant.enums.MenuOption;
import financial.assistant.enums.ui.FxmlComponent;
import financial.assistant.exceptions.ComponentLoadException;
import financial.assistant.ui.shared.CustomButton;
import financial.assistant.utils.ui.ComponentLoader;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MainComponentController {

    private static final Logger logger = LoggerFactory.getLogger(MainComponentController.class);

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
                        openComponent(FxmlComponent.CREATE_ACCOUNT_COMPONENT);
                    });
                    break;
                case VIEW_ACCOUNTS:
                    button.setOnMouseClicked(event -> {
                        openComponent(FxmlComponent.VIEW_ACCOUNT_COMPONENT);
                    });
                    break;
                case EXPORT_ACCOUNT:
                    button.setOnMouseClicked(event -> {
                        openComponent(FxmlComponent.EXPORT_ACCOUNT_COMPONENT);
                    });
                    break;
            }
            this.menuToolbar.getItems().add(button);
        }

    }

    private void clear() {
        if(this.mainBox.getChildren().size() > 1) {
            this.mainBox.getChildren().remove(1);
        }
    }

    private void openComponent(FxmlComponent component) {
        try {
            Parent root = ComponentLoader.loadComponent(component, applicationContext::getBean);
            clear();
            this.mainBox.getChildren().add(root);
        }catch(ComponentLoadException e) {
            logger.error("An exception occurred while trying to load component = {} from = {}", component, component.getResoucePath(), e);
        }
    }
}
