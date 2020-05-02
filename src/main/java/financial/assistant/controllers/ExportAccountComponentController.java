package financial.assistant.controllers;

import financial.assistant.entity.UserAccount;
import financial.assistant.enums.ExportFormat;
import financial.assistant.repository.UserAccountRepository;
import financial.assistant.utils.FileExportUtils;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class ExportAccountComponentController {

    private @Autowired UserAccountRepository userAccountRepository;
    private @Autowired FileExportUtils fileExportUtils;

    private @FXML ChoiceBox accountOptions;
    private @FXML ChoiceBox formatOptions;
    private @FXML Button exportButton;
    private @FXML VBox parentContainer;

    public void initialize() {

        // add user accounts to the account options dropdown
        List<UserAccount> userAccounts = userAccountRepository.findAll();
        userAccounts.forEach(account -> {
            this.accountOptions.getItems().add(account.getAccountName());
        });

        // add exportable formats to the format options dropdown
        for(ExportFormat format : ExportFormat.values()) {
            this.formatOptions.getItems().add(format.name());
        }

        this.exportButton.setOnAction(this::exportAccount);
    }

    public void exportAccount(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select a folder");

        File selectedDirectory = directoryChooser.showDialog(parentContainer.getScene().getWindow());
        if(selectedDirectory != null) {
            ExportFormat format = ExportFormat.valueOf((String) formatOptions.getValue());
            UserAccount userAccount = userAccountRepository.findByAccountName((String) accountOptions.getValue());
            try {
                fileExportUtils.exportUserAccount(format, selectedDirectory, userAccount);
            }catch(IOException e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Warning!");
                alert.setTitle("Warning");
                alert.setContentText("Could not save file to " + selectedDirectory.getAbsolutePath());
                alert.show();
            }
        }
    }
}
