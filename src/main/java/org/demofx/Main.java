package org.demofx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main extends Application {

    private static AbstractApplicationContext context = new ClassPathXmlApplicationContext ("applicationContext.xml");

    @Override
    public void start(Stage primaryStage) throws Exception{

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample.fxml"));
            loader.setControllerFactory(new Callback<Class<?>, Object>() {
                @Override
                public Object call(Class<?> aClass) {
                    return context.getBean(aClass);
                }
            });


        primaryStage.setTitle("Task List Light");
        primaryStage.setScene(new Scene((Parent) loader.load(), 950, 650));

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}