package launcher.view.options;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import launcher.utils.FontLoader;

public class Options extends JFrame {

    //COLORES
    public static final Color verdeMinecraft = new Color(46, 204, 113);
    public static final Color rojoMinecraft = new Color(117, 0, 0);

    //JCOMBOBOXES
    JComboBox<String> dynamicLights = new JComboBox<>();
    JComboBox<String> connectedTextures = new JComboBox<>();
    JComboBox<String> guiScale = new JComboBox<>();
    JComboBox<String> lang = new JComboBox<>();

    //BOTONES
    JButton fastRender = new JButton();
    JButton smartAnimations = new JButton();
    JButton showFps = new JButton();
    JButton fullScreen = new JButton();
    JButton vbo = new JButton();
    JButton advancedToolTips = new JButton();
    JButton guardar = new JButton();

    //LABELS
    JLabel labelVacio = new JLabel();
    JLabel textFullScreen = new JLabel("PANTALLA COMPLETA");
    JLabel textVbo = new JLabel("VBO:");
    JLabel textAdvacedToolTips = new JLabel("TIPS AVANZADOS");
    JLabel textLang = new JLabel("IDIOMA");
    JLabel textGuiScale = new JLabel("ESCALA HUD");

    JLabel textFastRender = new JLabel("OF RENDERIZADO RAPIDO");
    JLabel textSmartAnimations = new JLabel("OF ANIMACIONES INTELIGENTES");
    JLabel textShowFps = new JLabel("OF MOSTRAR FPS");
    JLabel textDynamicLights = new JLabel("OF LUCES DINAMICAS:");
    JLabel textConnectedTextures = new JLabel("OF TEXTURAS CONECTADAS");

    //PANELES
    JPanel panel = new JPanel();
    JPanel infraPanel = new JPanel();

    //FUENTES
    String ruta = System.getProperty("user.dir") + "/resources/fonts/minecraft_font.ttf";
    Font fuenteMinecraft = FontLoader.cargarFuente(ruta, 16f);
    Font fuenteBoton = fuenteMinecraft.deriveFont(22f);

    public Options() {

        //PANTALLA
        this.setTitle("OPCIONES");
        this.setLayout(new BorderLayout());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double ancho = screenSize.width * 0.65;
        double alto = screenSize.height * 0.65;
        this.setSize((int) ancho, (int) alto);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        //PANEL
        panel.setLayout(new GridLayout(0, 2, 70, 10));
        panel.setBackground(new Color(60, 63, 65));
        panel.setBorder(new EmptyBorder(15, 200, 15, 200));

        infraPanel.setBackground(new Color(60, 63, 65));
        infraPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        //LABELS
        textFullScreen.setForeground(Color.WHITE);
        textFullScreen.setFont(fuenteBoton);

        textVbo.setForeground(Color.WHITE);
        textVbo.setFont(fuenteBoton);

        textAdvacedToolTips.setForeground(Color.WHITE);
        textAdvacedToolTips.setFont(fuenteBoton);

        textLang.setForeground(Color.WHITE);
        textLang.setFont(fuenteBoton);

        textFastRender.setForeground(Color.WHITE);
        textFastRender.setFont(fuenteBoton);

        textSmartAnimations.setForeground(Color.WHITE);
        textSmartAnimations.setFont(fuenteBoton);

        textShowFps.setForeground(Color.WHITE);
        textShowFps.setFont(fuenteBoton);

        textDynamicLights.setForeground(Color.WHITE);
        textDynamicLights.setFont(fuenteBoton);

        textConnectedTextures.setForeground(Color.WHITE);
        textConnectedTextures.setFont(fuenteBoton);

        textGuiScale.setForeground(Color.WHITE);
        textGuiScale.setFont(fuenteBoton);

        //COMBOBOX
        estilarComboBox(dynamicLights);
        estilarComboBox(connectedTextures);
        estilarComboBox(guiScale);
        estilarComboBox(lang);

        dynamicLights.addItem("Desactivado");
        dynamicLights.addItem("Rapida");
        dynamicLights.addItem("Detallada");

        connectedTextures.addItem("Desactivado");
        connectedTextures.addItem("Rapidos");
        connectedTextures.addItem("Detallados");

        guiScale.addItem("Auto");
        guiScale.addItem("Pequeña");
        guiScale.addItem("Normal");
        guiScale.addItem("Grande");

        lang.addItem("Español");
        lang.addItem("Ingles");

        //JBUTTONS
        fullScreen.setBackground(verdeMinecraft);
        fullScreen.setForeground(Color.WHITE);
        fullScreen.setFont(fuenteBoton);
        fullScreen.setFocusPainted(false);
        fullScreen.setMinimumSize(new Dimension(0, 120));
        fullScreen.setPreferredSize(new Dimension(0, 60));
        fullScreen.setActionCommand("FULLSCREEN");

        vbo.setBackground(verdeMinecraft);
        vbo.setForeground(Color.WHITE);
        vbo.setFont(fuenteBoton);
        vbo.setFocusPainted(false);
        vbo.setMinimumSize(new Dimension(0, 120));
        vbo.setPreferredSize(new Dimension(0, 60));
        vbo.setActionCommand("VBO");

        advancedToolTips.setBackground(verdeMinecraft);
        advancedToolTips.setForeground(Color.WHITE);
        advancedToolTips.setFont(fuenteBoton);
        advancedToolTips.setFocusPainted(false);
        advancedToolTips.setMinimumSize(new Dimension(0, 120));
        advancedToolTips.setPreferredSize(new Dimension(0, 60));
        advancedToolTips.setActionCommand("ADVANCEDTOOLTIPS");

        fastRender.setBackground(verdeMinecraft);
        fastRender.setForeground(Color.WHITE);
        fastRender.setFont(fuenteBoton);
        fastRender.setFocusPainted(false);
        fastRender.setPreferredSize(new Dimension(0, 60));
        fastRender.setActionCommand("FASTRENDER");

        smartAnimations.setBackground(verdeMinecraft);
        smartAnimations.setForeground(Color.WHITE);
        smartAnimations.setFont(fuenteBoton);
        smartAnimations.setFocusPainted(false);
        smartAnimations.setPreferredSize(new Dimension(0, 60));
        smartAnimations.setActionCommand("SMARTANIMATIONS");

        showFps.setBackground(verdeMinecraft);
        showFps.setForeground(Color.WHITE);
        showFps.setFont(fuenteBoton);
        showFps.setFocusPainted(false);
        showFps.setPreferredSize(new Dimension(0, 60));
        showFps.setActionCommand("SHOWFPS");

        guardar.setBackground(verdeMinecraft);
        guardar.setForeground(Color.WHITE);
        guardar.setFont(fuenteBoton);
        guardar.setFocusPainted(false);
        guardar.setPreferredSize(new Dimension(200, 50));
        guardar.setText("GUARDAR");
        guardar.setActionCommand("GUARDAR");

        panel.add(textFullScreen);
        panel.add(textVbo);
        panel.add(fullScreen);
        panel.add(vbo);

        panel.add(textAdvacedToolTips);
        panel.add(textLang);
        panel.add(advancedToolTips);
        panel.add(lang);

        panel.add(textGuiScale);
        panel.add(textConnectedTextures);
        panel.add(guiScale);
        panel.add(connectedTextures);

        panel.add(textFastRender);
        panel.add(textShowFps);
        panel.add(fastRender);
        panel.add(showFps);

        panel.add(textDynamicLights);
        panel.add(textSmartAnimations);

        panel.add(dynamicLights);
        panel.add(smartAnimations);

        infraPanel.add(guardar);

        super.add(panel);
        super.add(infraPanel, BorderLayout.SOUTH);
    }

    public void setFullScreenButton(boolean texto) {

        if (texto) {
            fullScreen.setBackground(verdeMinecraft);
        } else {
            fullScreen.setBackground(rojoMinecraft);
        }
        fullScreen.setText(String.valueOf(texto));

    }

    public void setVboButton(boolean texto) {
        if (texto) {
            vbo.setBackground(verdeMinecraft);
        } else {
            vbo.setBackground(rojoMinecraft);
        }
        vbo.setText(String.valueOf(texto));

    }

    public void setAdvancedToolTipsButton(boolean texto) {

        if (texto) {
            advancedToolTips.setBackground(verdeMinecraft);
        } else {
            advancedToolTips.setBackground(rojoMinecraft);
        }
        advancedToolTips.setText(String.valueOf(texto));

    }

    public void setShowFps(boolean texto) {

        if (texto) {
            showFps.setBackground(verdeMinecraft);
        } else {
            showFps.setBackground(rojoMinecraft);
        }
        showFps.setText(String.valueOf(texto));

    }

    public void setLang(int texto) {

        switch (texto) {
            case 1:
                lang.setSelectedIndex(1);
                break;
            case 2:
                lang.setSelectedIndex(2);
                break;
            default:
                lang.setSelectedIndex(0);
                break;
        }

    }

    public void setGuiScale(int valorVanilla) {
        switch (valorVanilla) {

            case 0:
                guiScale.setSelectedIndex(0);
                break;
            case 1:
                guiScale.setSelectedIndex(1);
                break;
            case 2:
                guiScale.setSelectedIndex(2);
                break;
            case 3:
                guiScale.setSelectedIndex(3);
                break;
            default:
                guiScale.setSelectedIndex(1);
                break;
        }
    }

    public boolean getShowFps() {
        return Boolean.parseBoolean(showFps.getText());
    }

    public boolean getFullScreenButton() {

        return Boolean.parseBoolean(fullScreen.getText());

    }

    public boolean getVboButton() {

        return Boolean.parseBoolean(vbo.getText());

    }

    public int getLang() {

        return lang.getSelectedIndex();

    }

    //OPTIFINE
    public int getDynamicLights() {

        return dynamicLights.getSelectedIndex();

    }

    public boolean getSmartAnimations() {

        return Boolean.parseBoolean(smartAnimations.getText());

    }

    public int getConnectedTextures() {

        return connectedTextures.getSelectedIndex();

    }

    public int getGuiScale() {
        return guiScale.getSelectedIndex();
    }

    public boolean getAdvancedToolTipsButton() {

        return Boolean.parseBoolean(advancedToolTips.getText());

    }

    public boolean getFastRender() {
        return Boolean.parseBoolean(fastRender.getText());
    }

    public void setSmartAnimations(boolean texto) {
        if (texto) {
            smartAnimations.setBackground(verdeMinecraft);
        } else {
            smartAnimations.setBackground(rojoMinecraft);
        }
        smartAnimations.setText(String.valueOf(texto));
    }

    public void setFastRender(boolean texto) {
        if (texto) {
            fastRender.setBackground(verdeMinecraft);
        } else {
            fastRender.setBackground(rojoMinecraft);
        }
        fastRender.setText(String.valueOf(texto));
    }

    public void setDynamicLights(int valorOptifine) {
        switch (valorOptifine) {

            case 0:
                dynamicLights.setSelectedIndex(0);
                break;
            case 1:
                dynamicLights.setSelectedIndex(1);
                break;
            case 2:
                dynamicLights.setSelectedIndex(2);
                break;
            default:
                dynamicLights.setSelectedIndex(0);
                break;
        }
    }

    public void setConnectedTextures(int valorOptifine) {

        System.out.println("COSA RARISIMA" + valorOptifine);

        switch (valorOptifine) {

            case 0:
                connectedTextures.setSelectedIndex(0);
                break;
            case 1:
                connectedTextures.setSelectedIndex(1);
                break;
            case 2:
                connectedTextures.setSelectedIndex(2);
                break;
            default:
                connectedTextures.setSelectedIndex(0);
                break;
        }
    }

    //ACTION LISTENER
    public void setActionListeners(ActionListener actionListener) {
        fullScreen.addActionListener(actionListener);
        vbo.addActionListener(actionListener);
        advancedToolTips.addActionListener(actionListener);
        lang.addActionListener(actionListener);

        showFps.addActionListener(actionListener);
        fastRender.addActionListener(actionListener);
        smartAnimations.addActionListener(actionListener);

        guardar.addActionListener(actionListener);
    }

    //XD MOMENTO
    public void estilarComboBox(javax.swing.JComboBox combo) {

        combo.setBackground(new java.awt.Color(230, 126, 34));
        combo.setForeground(java.awt.Color.WHITE);
        combo.setFocusable(false);
        combo.setFont(fuenteBoton);
        ((javax.swing.JLabel) combo.getRenderer()).setHorizontalAlignment(javax.swing.JLabel.CENTER);

        combo.setRenderer(new BasicComboBoxRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setHorizontalAlignment(javax.swing.JLabel.CENTER);
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
