package financial.assistant.controllers;

import financial.assistant.entity.UserAccount;
import financial.assistant.repository.UserAccountRepository;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ViewAccountComponentController {

    private @Autowired UserAccountRepository userAccountRepository;
    private @Autowired ApplicationContext applicationContext;

    private @FXML ChoiceBox accountChoices;
    private @FXML Button submitButton;
    private @FXML VBox accountContainer;

    public void initialize() {

        // find user accounts
        List<UserAccount> userAccounts = userAccountRepository.findAll();
        if(userAccounts != null && userAccounts.size() > 0) {
            List<String> accountNames = userAccounts.stream().map(account -> account.getAccountName()).collect(Collectors.toList());
            this.accountChoices.setItems(FXCollections.observableArrayList(accountNames));
        }

        this.submitButton.setOnAction(this::loadAccountFromChoicebox);
    }

    public void loadAccountFromChoicebox(ActionEvent event) {
        clear();
        if(accountChoices.getValue() != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/view-account-info.component.fxml"));
                loader.setControllerFactory(applicationContext::getBean);
                Parent root = loader.load();
                loader.<ViewAccountInfoComponentController>getController().setUserAccountName((String) accountChoices.getValue());
                accountContainer.getChildren().add(root);
            }catch(IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void clear() {
        accountContainer.getChildren().clear();
    }
}
