package controller;

import constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.MainForm;

import javax.swing.*;
import java.io.File;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(final String[] args) {
        Thread.currentThread().setUncaughtExceptionHandler((thread, throwable) ->
                logger.error("Uncaught Exception!")
        );

        SwingUtilities.invokeLater(() -> {
            String localDB = Constants.PATH.concat("\\local_db");
            File directory = new File(localDB);
            if (!directory.exists()) {
                directory.mkdir();
            }

            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
            MainForm ui = new MainForm();
            ui.setVisible(true);
        });
    }
}
