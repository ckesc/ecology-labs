/*
 * EcolabsApp.java
 */
package ecolabs;

import java.awt.SplashScreen;
import javax.swing.UIManager;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class EcolabsApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            System.out.println("you have old java. Switching to windows look. Обновите java для более красивого внешнего вида");
            //Устанавливаем внешний вид как в системе
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                System.err.println(ex);
            }
           
        }

        show(new EcolabsView(this));
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override
    protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of EcolabsApp
     */
    public static EcolabsApp getApplication() {
        return Application.getInstance(EcolabsApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {

//        final SplashScreen splash = SplashScreen.getSplashScreen();
//        if (splash == null) {
//            System.out.println("SplashScreen.getSplashScreen() returned null");
//            return;
//        }

        launch(EcolabsApp.class, args);
    }
}
