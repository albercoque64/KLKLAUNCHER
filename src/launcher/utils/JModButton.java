package launcher.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import launcher.model.Mod;

public class JModButton extends JButton {

    //COLORES
    public static final Color verdeMinecraft = new Color(46, 204, 113);
    public static final Color rojoMinecraft = new Color(117, 0, 0);

    //FUENTES
    String ruta = System.getProperty("user.dir") + "/resources/fonts/minecraft_font.ttf";
    Font fuenteMinecraft = FontLoader.cargarFuente(ruta, 16f);
    Font fuenteBoton = fuenteMinecraft.deriveFont(22f);

    //MOD XD
    Mod mod;

    public JModButton(Mod mod) {

        this.mod = mod;
        this.setText(mod.getModName());

        //JBUTTONS
        setColor();
        this.setForeground(Color.WHITE);
        this.setFont(fuenteBoton);
        this.setFocusPainted(false);
        this.setMinimumSize(new Dimension(0, 120));
        this.setPreferredSize(new Dimension(0, 60));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, this.getPreferredSize().height));

    }

    public String getRuta() {

        return ruta;

    }

    public Mod getMod() {

        return this.mod;

    }

    public void setMod(Mod mod) {

        this.mod = mod;

    }

    public void setColor() {

        if (mod.getNombreArchivo().endsWith(".jaroff")) {

            super.setBackground(rojoMinecraft);

        }

        if (mod.getNombreArchivo().endsWith(".jar")) {

            super.setBackground(verdeMinecraft);

        }

    }

    public void setActionListener(ActionListener actionListener) {

        this.addActionListener(actionListener);

    }

}
