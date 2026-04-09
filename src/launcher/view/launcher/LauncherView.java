package launcher.view.launcher;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;
import launcher.utils.FontLoader;

public class LauncherView extends JFrame {

    //COLORES
    public static final Color verdeMinecraft = new Color(46, 204, 113);
    public static final Color rojoMinecraft = new Color(117, 0, 0);

    //FUENTES
    String ruta = System.getProperty("user.dir") + "/resources/fonts/minecraft_font.ttf";
    Font fuenteMinecraft = FontLoader.cargarFuente(ruta, 16f);
    Font fuente = fuenteMinecraft.deriveFont(22f);

    //JBUTONS
    JButton instalar = new JButton();
    JButton versiones = new JButton();

    //PANEL
    JPanel panel = new JPanel();

    public LauncherView() {

        //BOTONES
        instalar.setText("INSTALAR PERFIL");
        instalar.setFont(fuente);
        instalar.setForeground(Color.WHITE);
        instalar.setBackground(new Color(230, 126, 34));
        instalar.setActionCommand("INSTALAR");
        instalar.setFocusPainted(false);
        instalar.setMargin(new java.awt.Insets(7, 10, 0, 10));

        versiones.setText("ELIMINAR PERFIL");
        versiones.setFont(fuente);
        versiones.setForeground(Color.WHITE);
        versiones.setBackground(new Color(230, 126, 34));
        versiones.setActionCommand("PERFILES");
        versiones.setFocusPainted(false);
        versiones.setMargin(new java.awt.Insets(7, 10, 0, 10));

        //ESTO
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double ancho = screenSize.width * 0.25;
        double alto = screenSize.height * 0.25;
        this.setSize((int) ancho, (int) alto);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setTitle("Launcher");

        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panel.setBackground(new Color(60, 63, 65));
        panel.setBorder(new MatteBorder(15, 0, 0, 0, new Color(60, 63, 65)));
        panel.add(versiones);
        panel.add(instalar);

        this.add(panel);
    }

    public void setActionListener(ActionListener actionListener) {
        instalar.addActionListener(actionListener);
        versiones.addActionListener(actionListener);
    }
}
