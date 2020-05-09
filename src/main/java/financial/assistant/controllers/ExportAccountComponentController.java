package financial.assistant.controllers;

import financial.assistant.entity.UserAccount;
import financial.assistant.enums.ExportFormat;
import financial.assistant.exceptions.ExportAccountException;
import financial.assistant.repository.UserAccountRepository;
import financial.assistant.utils.FileExportUtils;
import financial.assistant.utils.ui.AlertProvider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

@Component
public class ExportAccountComponentController {

    private static final Logger logger = LoggerFactory.getLogger(ExportAccountComponentController.class);

    private @Autowired UserAccountRepository userAccountRepository;
    private @Autowired FileExportUtils fileExportUtils;

    private @FXML ChoiceBox<String> accountOptions;
    private @FXML ChoiceBox<String> formatOptions;
    private @FXML Button exportButton;
    private @FXML VBox parentContainer;
    private @FXML Label messageText;

    public void initialize() {

        // add user accounts to the account options dropdown
        List<UserAccount> userAccounts = userAccountRepository.findAll();
        userAccounts.forEach(account -> this.accountOptions.getItems().add(account.getAccountName()));

        // add exportable formats to the format options dropdown
        for (ExportFormat format : ExportFormat.values()) {
            this.formatOptions.getItems().add(format.name());
        }

        this.exportButton.setOnAction(this::exportAccount);
    }

    public void exportAccount(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select a folder");

        File selectedDirectory = directoryChooser.showDialog(parentContainer.getScene().getWindow());
        if (selectedDirectory != null) {
            ExportFormat format = ExportFormat.valueOf((String) formatOptions.getValue());
            UserAccount userAccount = userAccountRepository.findByAccountName((String) accountOptions.getValue());
            try {
                Path p = fileExportUtils.exportUserAccount(format, selectedDirectory, userAccount);
                messageText.setText("Account successfully exported to " + p.toAbsolutePath());
                messageText.setStyle("-fx-text-fill: green");
            } catch (ExportAccountException e) {
                AlertProvider.openAlert(AlertType.WARNING, "Error", "Unable to export account",
                        "Unable to export account to " + selectedDirectory.getAbsolutePath());
                logger.error("An exception occurred while trying to export account with name = {}",
                        accountOptions.getValue(), e);
            }
        }
    }
}
