package launcher.view.launcher;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import launcher.utils.FontLoader;

public class InstanciasConfig extends JFrame {

    JPanel panelContenedor = new JPanel();
    JScrollPane scroll = new JScrollPane(panelContenedor, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    List<JButton> listaBotones = new ArrayList<>();

    public static final Color rojoMinecraft = new Color(117, 0, 0);
    String ruta = System.getProperty("user.dir") + "/resources/fonts/minecraft_font.ttf";
    Font fuenteMinecraft = FontLoader.cargarFuente(ruta, 16f);
    Font fuenteBoton = fuenteMinecraft.deriveFont(22f);

    public InstanciasConfig() {

        this.setTitle("ELIMINAR INSTANCIAS");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double ancho = screenSize.width * 0.3;
        double alto = screenSize.height * 0.65;
        this.setSize((int) ancho, (int) alto);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        panelContenedor.setLayout(new BoxLayout(panelContenedor, BoxLayout.Y_AXIS));
        panelContenedor.setBackground(new Color(60, 63, 65));

        this.add(scroll);
    }

    public void setInstancias(List<String> instancias) {

        panelContenedor.removeAll();
        listaBotones.clear();

        if (instancias != null) {
            for (String nombre : instancias) {
                JButton boton = new JButton(nombre);

                boton.setBackground(rojoMinecraft);
                boton.setForeground(Color.WHITE);
                boton.setFont(fuenteBoton);
                boton.setFocusPainted(false);
                boton.setMinimumSize(new Dimension(0, 60));
                boton.setPreferredSize(new Dimension(0, 60));
                boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

                boton.setActionCommand("CONFIRMARELIMINARINSTANCIA");

                listaBotones.add(boton);
                panelContenedor.add(boton);
            }
        }

        panelContenedor.revalidate();
        panelContenedor.repaint();
    }

    public boolean borrarDirectorio(File directorio) {
        if (directorio.isDirectory()) {
            java.io.File[] archivos = directorio.listFiles();
            if (archivos != null) {
                for (java.io.File archivo : archivos) {
                    borrarDirectorio(archivo);
                }
            }
        }
        return directorio.delete();
    }

    public void setActionListener(ActionListener actionListener) {
        for (JButton boton : listaBotones) {
            boton.addActionListener(actionListener);
        }
    }

    public void setPadre(JFrame padre) {
        this.setLocationRelativeTo(padre);
    }
}
