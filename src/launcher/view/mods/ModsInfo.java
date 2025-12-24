package launcher.view.mods;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import launcher.model.Mod;
import launcher.utils.FontLoader;

public class ModsInfo extends JFrame {

    public static final Color verdeMinecraft = new Color(46, 204, 113);
    public static final Color rojoMinecraft = new Color(117, 0, 0);

    String ruta = System.getProperty("user.dir") + "/resources/fonts/minecraft_font.ttf";
    Font fuenteMinecraft = FontLoader.cargarFuente(ruta, 16f);
    Font fuente = fuenteMinecraft.deriveFont(16f);

    List<Mod> listaMods = new ArrayList<>();
    JList<String> lista = new JList<>();
    JScrollPane scroll = new JScrollPane(lista);

    public ModsInfo() {

        this.setTitle("INFORMACION DE MODS");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double ancho = screenSize.width * 0.5;
        double alto = screenSize.height * 0.65;
        this.setSize((int) ancho, (int) alto);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        lista.setFont(fuente);
        lista.setBackground(new Color(60, 63, 65));
        lista.setForeground(Color.WHITE);
        
        //COSA GIGADUPA MAGICA QUE SAQUE DEL GEMINI
        lista.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                Color colorLinea = new Color(80, 83, 85);
                Border linea = BorderFactory.createMatteBorder(0, 0, 1, 0, colorLinea);
                Border espacio = BorderFactory.createEmptyBorder(10, 0, 10, 0);

                label.setBorder(BorderFactory.createCompoundBorder(linea, espacio));

                return label;
            }
        });

        lista.setBorder(new MatteBorder(0, 20, 0, 20, new Color(60, 63, 65)));

        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setBorder(null);

        this.add(scroll);
    }

    public void setMods(List<Mod> listaMods) {
        this.listaMods = listaMods;
        setModsVista();
    }

    public void setModsVista() {
        List<String> laLista = new ArrayList<>();
        String hexVerde = "#2ECC71";
        String hexRojo = "#750000";

        if (listaMods != null) {
            for (Mod mod : this.listaMods) {
                String texto = "<html>" + mod.getModName()+" " 
                        + "<font color='" + hexVerde + "'>Versiones:</font> (" + mod.getMinecraftVersions() + ") "
                        + "<font color='" + hexRojo + "'>Dependencias:</font> " + mod.getDependencies()
                        + "</html>";
                laLista.add(texto);
            }
        }
        String[] elArray = laLista.toArray(new String[0]);
        lista.setListData(elArray);
        lista.revalidate();
        lista.repaint();
    }
}
