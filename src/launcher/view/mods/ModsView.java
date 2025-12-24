package launcher.view.mods;

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

public class ModsView extends JFrame {

    //FUENTES
    String ruta = System.getProperty("user.dir") + "/resources/fonts/minecraft_font.ttf";
    Font fuenteMinecraft = FontLoader.cargarFuente(ruta, 16f);
    Font fuente = fuenteMinecraft.deriveFont(22f);

    //JBUTONS
    JButton configuracion = new JButton();
    JButton informacion = new JButton();

    JPanel panel = new JPanel();

    public ModsView() {

        //BOTONES
        configuracion.setText("CONFIGURACION");
        configuracion.setFont(fuente);
        configuracion.setForeground(Color.WHITE);
        configuracion.setBackground(new Color(230, 126, 34));
        configuracion.setActionCommand("MODCONFIG");
        configuracion.setFocusPainted(false);
        configuracion.setMargin(new java.awt.Insets(7, 10, 0, 10));

        informacion.setText("INFORMACION");
        informacion.setFont(fuente);
        informacion.setForeground(Color.WHITE);
        informacion.setBackground(new Color(230, 126, 34));
        informacion.setActionCommand("MODINFO");
        informacion.setFocusPainted(false);
        informacion.setMargin(new java.awt.Insets(7, 10, 0, 10));

        this.setTitle("MODS");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double ancho = screenSize.width * 0.25;
        double alto = screenSize.height * 0.1;
        this.setSize((int) ancho, (int) alto);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panel.setBackground(new Color(60, 63, 65));
        panel.setBorder(new MatteBorder(15, 0, 0, 0, new Color(60, 63, 65)));
        panel.add(configuracion);
        panel.add(informacion);

        this.add(panel);

    }

    public void setActionListener(ActionListener actionListener) {
        configuracion.addActionListener(actionListener);
        informacion.addActionListener(actionListener);
    }

}
