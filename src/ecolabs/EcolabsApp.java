/*
 * EcolabsApp.java
 */

package ecolabs;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.jdesktop.application.Application;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class EcolabsApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        //Устанавливаем внешний вид как в системе
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(EcolabsApp.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            Logger.getLogger(EcolabsApp.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            Logger.getLogger(EcolabsApp.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (UnsupportedLookAndFeelException ex) {
//            Logger.getLogger(EcolabsApp.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
        long waitTime = 3000;
        show(new SplashJFrame());
        Date date1 = new Date();
        FrameView e = new EcolabsView(this);
        // ЭТО работает, но Splash не показывает Label сначала
//        Date date2 = new Date();
//        long d = date2.getTime() - date1.getTime();
//        long time = (d >= waitTime)
//                 ? 0
//                 : waitTime - d;
//        try {
//            Thread.sleep(time);
//        } catch (InterruptedException ex) {
//            ex.printStackTrace();
//        }
        show(e);
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
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
        launch(EcolabsApp.class, args);
    }
}
