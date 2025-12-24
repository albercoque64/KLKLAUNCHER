package launcher.view.mods;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import launcher.utils.JModButton;

public class ModsConfig extends JFrame {

    JPanel lasAventurasDeNeo = new JPanel();
    JScrollPane scroll = new JScrollPane(lasAventurasDeNeo, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    List<JModButton> listaBotones = new ArrayList<>();

    public ModsConfig() {

        //PANTALLA
        this.setTitle("CONFIGURACION DE MODS");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double ancho = screenSize.width * 0.3;
        double alto = screenSize.height * 0.65;
        this.setSize((int) ancho, (int) alto);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        
        lasAventurasDeNeo.setLayout(new BoxLayout(lasAventurasDeNeo, BoxLayout.Y_AXIS));
        lasAventurasDeNeo.setBackground(new Color(60, 63, 65));

        this.add(scroll);

    }

    public void setMods(List<JModButton> botones) {

        for (JModButton boton : botones) {
            // lasAventurasDeNeo.add(boton);
            listaBotones.add(boton);
        }

    }

    public void setModsVista() {
        for (JModButton boton : listaBotones) {
            lasAventurasDeNeo.add(boton);
        }
        lasAventurasDeNeo.revalidate();
        lasAventurasDeNeo.repaint();
    }

    public void setActionListener(ActionListener actionListener) {

        for (JModButton boton : listaBotones) {
            boton.setActionListener(actionListener);
        }
    }
}
