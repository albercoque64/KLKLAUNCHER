package launcher.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;
import launcher.utils.FontLoader;

public class InfoPanel extends JPanel {

    //FUENTE
    String ruta = System.getProperty("user.dir") + "/resources/fonts/minecraft_font.ttf";
    Font fuenteMinecraft = FontLoader.cargarFuente(ruta, 22f);
    Font fuenteBoton = fuenteMinecraft.deriveFont(25f);

    //JLABELS
    JLabel labelVacio = new JLabel();

    //JBUTONS
    JButton opciones = new JButton();
    JButton launcher = new JButton();
    JButton mods = new JButton();
    JButton carpetaMods = new JButton();

    public InfoPanel() {

        //LABELS
        labelVacio.setPreferredSize(new Dimension(80, 0));

        //BOTONES
        opciones.setText("OPCIONES");
        opciones.setFont(fuenteBoton);
        opciones.setForeground(Color.WHITE);
        opciones.setBackground(new Color(230, 126, 34));
        opciones.setActionCommand("OPCIONES");
        opciones.setFocusPainted(false);
        opciones.setMargin(new java.awt.Insets(7, 10, 0, 10));

        launcher.setText("LAUNCHER");
        launcher.setFont(fuenteBoton);
        launcher.setForeground(Color.WHITE);
        launcher.setBackground(new Color(230, 126, 34));
        launcher.setActionCommand("LAUNCHER");
        launcher.setFocusPainted(false);
        launcher.setMargin(new java.awt.Insets(7, 10, 0, 10));

        mods.setText("MODS");
        mods.setFont(fuenteBoton);
        mods.setForeground(Color.WHITE);
        mods.setBackground(new Color(230, 126, 34));
        mods.setActionCommand("MODS");
        mods.setFocusPainted(false);
        mods.setMargin(new java.awt.Insets(7, 10, 0, 10));

        carpetaMods.setText("ABRIR MODS");
        carpetaMods.setFont(fuenteBoton);
        carpetaMods.setForeground(Color.WHITE);
        carpetaMods.setBackground(new Color(230, 126, 34));
        carpetaMods.setActionCommand("CARPETAMODS");
        carpetaMods.setFocusPainted(false);
        carpetaMods.setMargin(new java.awt.Insets(7, 10, 0, 10));

        this.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 0));
        this.setBackground(new Color(60, 63, 65));
        this.setPreferredSize(new java.awt.Dimension(0, 88));
        this.setBorder(new MatteBorder(25, 0, 0, 0, new Color(60, 63, 65)));

        this.add(carpetaMods);
        this.add(labelVacio);
        this.add(mods);
        this.add(launcher);
        this.add(opciones);

    }

    public void setActionListener(ActionListener actionListener) {
        opciones.addActionListener(actionListener);
        launcher.addActionListener(actionListener);
        mods.addActionListener(actionListener);
        carpetaMods.addActionListener(actionListener);
    }
}
