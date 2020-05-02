package financial.assistant;

import javafx.application.Application;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;


public class FinancialAssistantApplication extends Application{

    private ConfigurableApplicationContext context;

    @Override
    public void init() throws Exception {
        ApplicationContextInitializer<GenericApplicationContext> initializer = applicationContext ->
             {
                applicationContext.registerBean(Application.class, () -> FinancialAssistantApplication.this);
                applicationContext.registerBean(Parameters.class, this::getParameters);
                applicationContext.registerBean(HostServices.class, this::getHostServices);
            };

        this.context = new SpringApplicationBuilder()
                        .sources(FinancialAssistantSpringEntrypoint.class)
                        .initializers(initializer)
                        .headless(false)
                        .run(getParameters().getRaw().toArray(new String[0]));
    }

    @Override
    public void stop() throws Exception {
        this.context.stop();
        Platform.exit();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.context.publishEvent(new StageReadyEvent(primaryStage));
    }
}

class StageReadyEvent extends ApplicationEvent {

    public StageReadyEvent(Stage source){
        super(source);
    }

    public Stage getStage() {
        return (Stage) this.source;
    }
}
