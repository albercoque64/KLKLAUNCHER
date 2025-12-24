package launcher.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import launcher.utils.FontLoader;

public class OptionPanel extends JPanel {

    //FUENTE
    String ruta = System.getProperty("user.dir") + "/resources/fonts/minecraft_font.ttf";
    Font fuenteMinecraft = FontLoader.cargarFuente(ruta, 20f);
    Font fuenteBoton = fuenteMinecraft.deriveFont(20f);

    Font fuenteMinecraftGrande = FontLoader.cargarFuente(ruta, 20f);
    Font fuenteBotonGrande = fuenteMinecraftGrande.deriveFont(30f);

    //BOTONES
    JButton botonJugar = new JButton("JUGAR");

    //COMBO BOX
    JComboBox<String> javaVersion = new JComboBox<>();
    JComboBox<String> minecraftVersion = new JComboBox<>();

    //PANELES
    JPanel panelControles = new JPanel();
    JPanel panelJugar = new JPanel();
    //JLABELS
    JLabel labelVacio = new JLabel();
    JLabel userText = new JLabel("USUARIO:");
    JLabel javaVersionText = new JLabel("VERSION JAVA:");
    JLabel minecraftVersionText = new JLabel("VERSION MINECRAFT:");
    JLabel ramText = new JLabel("");

    //CAMPO DE TEXTO
    JTextField user = new JTextField("");

    //SLIDER
    private final int[] ram = {2, 4, 6, 8, 12, 16};
    JSlider sliderRam = new JSlider(0, ram.length - 1, 2);

    public OptionPanel() {
        //LAYOUT
        this.setLayout(new BorderLayout(0, 20));
        this.setBackground(new Color(60, 63, 65));
        this.setBorder(new EmptyBorder(15, 15, 15, 15));

        //LABELS
        userText.setForeground(Color.WHITE);
        userText.setFont(fuenteMinecraft);

        minecraftVersionText.setForeground(Color.WHITE);
        minecraftVersionText.setFont(fuenteMinecraft);
        minecraftVersion.setFont(fuenteMinecraft);
        minecraftVersion.addItem("1.12.2");
        estilarComboBox(minecraftVersion);

        //COMBOBOXES
        javaVersionText.setForeground(Color.WHITE);
        javaVersionText.setFont(fuenteMinecraft);
        javaVersion.setFont(fuenteMinecraft);
        javaVersion.setForeground(Color.WHITE);
        javaVersion.setBackground(new Color(230, 126, 34));
        estilarComboBox(javaVersion);
        javaVersion.addItem("Java");
        javaVersion.addItem("Adoptium");
        javaVersion.addItem("Graal");
        ramText.setFont(fuenteMinecraft);
        ramText.setForeground(Color.WHITE);
        user.setFont(fuenteMinecraft);

        //SLIDER CONFIG
        sliderRam.setOpaque(false);
        sliderRam.setBackground(new Color(60, 63, 65));
        sliderRam.setForeground(Color.WHITE);
        sliderRam.setMajorTickSpacing(1);
        sliderRam.setPaintTicks(true);
        sliderRam.setPaintLabels(true);
        sliderRam.setSnapToTicks(true);

        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        for (int i = 0; i < ram.length; i++) {
            JLabel lbl = new JLabel(ram[i] + "GB");
            lbl.setForeground(Color.LIGHT_GRAY);
            lbl.setFont(fuenteMinecraft.deriveFont(10f));
            labelTable.put(i, lbl);
        }
        sliderRam.setLabelTable(labelTable);

        sliderRam.addChangeListener(e -> {
            ramText.setText("RAM: " + ram[sliderRam.getValue()] + " GB");
        });
        ramText.setText("RAM: " + ram[sliderRam.getValue()] + " GB");

        //BOTONES
        botonJugar.setBackground(new Color(46, 204, 113));
        botonJugar.setForeground(Color.WHITE);
        botonJugar.setFont(fuenteBotonGrande);
        botonJugar.setFocusPainted(false);
        botonJugar.setMinimumSize(new Dimension(0, 120));
        botonJugar.setPreferredSize(new Dimension(0, 60));
        botonJugar.setActionCommand("JUGAR");
        botonJugar.setMargin(new java.awt.Insets(7, 0, 0, 0));
        
        //CONSTRUCCION PANEL
        panelControles.setLayout(new GridLayout(0, 1, 0, 10));
        panelControles.setOpaque(false);
        panelControles.setBorder(new EmptyBorder(0, 4, 0, 0));

        panelControles.add(userText);
        panelControles.add(user);
        panelControles.add(minecraftVersionText);
        panelControles.add(minecraftVersion);
        panelControles.add(javaVersionText);
        panelControles.add(javaVersion);
        panelControles.add(ramText);
        panelControles.add(sliderRam);

        panelJugar.setLayout(new GridLayout(0, 1, 0, 10));
        panelJugar.setOpaque(false);
        panelJugar.setBorder(new EmptyBorder(0, 4, 0, 0));

        panelJugar.add(botonJugar);

        //DISTRIBUCION
        this.add(panelControles, BorderLayout.NORTH);
        this.add(panelJugar, BorderLayout.SOUTH);
    }

    public String getUsuario() {
        return user.getText();
    }

    public int getJavaVersion() {
        return javaVersion.getSelectedIndex() + 1;
    }

    public int getRamEnMB() {
        return ram[sliderRam.getValue()] * 1024;
    }

    public void setRam(int ramEnMB) {
        int ramEnGB = ramEnMB / 1024;
        int indiceEncontrado = 1;

        for (int i = 0; i < ram.length; i++) {
            if (ram[i] == ramEnGB) {
                indiceEncontrado = i;
                break;
            }
        }
        sliderRam.setValue(indiceEncontrado);
        ramText.setText("RAM: " + ram[indiceEncontrado] + " GB");
    }

    public void setUsuario(String nombreUsuario) {
        user.setText(nombreUsuario);
    }

    public void setJavaVersion(int Version) {
        if (Version > 0) {
            javaVersion.setSelectedIndex(Version - 1);
        }
    }

    public void apagarJugar() {
        botonJugar.setEnabled(false);
        botonJugar.setText("JUGANDO...");
    }

    public void encenderJugar() {
        botonJugar.setEnabled(true);
        botonJugar.setText("JUGAR");
    }

    public void setActionListener(ActionListener actionListener) {
        botonJugar.addActionListener(actionListener);
    }

    public void estilarComboBox(javax.swing.JComboBox combo) {

        combo.setBackground(new java.awt.Color(230, 126, 34));
        combo.setForeground(java.awt.Color.WHITE);
        combo.setFocusable(false);

        combo.setRenderer(new BasicComboBoxRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                //setHorizontalAlignment(javax.swing.JLabel.CENTER);
                setVerticalAlignment(javax.swing.JLabel.CENTER);
                setBorder(new javax.swing.border.EmptyBorder(5, 10, 0, 0));

                if (isSelected) {
                    setBackground(new java.awt.Color(46, 204, 113));
                    setForeground(java.awt.Color.WHITE);
                } else {
                    setBackground(new java.awt.Color(230, 126, 34));
                    setForeground(java.awt.Color.WHITE);
                }
                return this;
            }
        });
    }

}
