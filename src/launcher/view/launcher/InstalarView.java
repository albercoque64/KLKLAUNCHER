package launcher.view.launcher;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import launcher.utils.FontLoader;

public class InstalarView extends JFrame {

    String ruta = System.getProperty("user.dir") + "/resources/fonts/minecraft_font.ttf";
    Font fuenteMinecraft = FontLoader.cargarFuente(ruta, 16f);
    Font fuenteBotones = fuenteMinecraft.deriveFont(22f);
    Font fuenteLabels = fuenteMinecraft.deriveFont(22f);

    JLabel labelNombre = new JLabel();
    JTextField campoNombre = new JTextField();
    JButton btnSeleccionarJar = new JButton();
    JButton btnCancelar = new JButton();

    JPanel panelPrincipal = new JPanel();

    public InstalarView() {

        labelNombre.setText("NOMBRE DE LA INSTANCIA:");
        labelNombre.setFont(fuenteLabels);
        labelNombre.setForeground(Color.WHITE);
        labelNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        campoNombre.setFont(fuenteLabels);
        campoNombre.setBackground(new Color(43, 43, 43));
        campoNombre.setForeground(Color.WHITE);
        campoNombre.setCaretColor(Color.WHITE);
        campoNombre.setMaximumSize(new Dimension(300, 35));
        campoNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        campoNombre.setMargin(new java.awt.Insets(5, 10, 0, 10));

        btnSeleccionarJar.setText("SELECCIONAR JAR");
        btnSeleccionarJar.setFont(fuenteBotones);
        btnSeleccionarJar.setForeground(Color.WHITE);
        btnSeleccionarJar.setBackground(new Color(230, 126, 34));
        btnSeleccionarJar.setActionCommand("JAR");
        btnSeleccionarJar.setFocusPainted(false);
        btnSeleccionarJar.setMargin(new java.awt.Insets(7, 10, 0, 10));
        btnSeleccionarJar.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnCancelar.setText("CANCELAR");
        btnCancelar.setFont(fuenteBotones);
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setBackground(LauncherView.rojoMinecraft);
        btnCancelar.setActionCommand("CANCELAR");
        btnCancelar.setFocusPainted(false);
        btnCancelar.setMargin(new java.awt.Insets(7, 10, 0, 10));
        btnCancelar.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double ancho = screenSize.width * 0.25;
        double alto = screenSize.height * 0.30;
        this.setSize((int) ancho, (int) alto);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setTitle("Crear Instancia");

        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBackground(new Color(60, 63, 65));

        panelPrincipal.add(Box.createVerticalStrut(30));
        panelPrincipal.add(labelNombre);
        panelPrincipal.add(Box.createVerticalStrut(10));
        panelPrincipal.add(campoNombre);
        panelPrincipal.add(Box.createVerticalStrut(30));
        panelPrincipal.add(btnSeleccionarJar);
        panelPrincipal.add(Box.createVerticalStrut(15));
        panelPrincipal.add(btnCancelar);

        this.add(panelPrincipal);

        btnSeleccionarJar.setEnabled(false);

        campoNombre.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                verificar();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                verificar();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                verificar();
            }

            private void verificar() {
                boolean tieneTexto = !campoNombre.getText().trim().isEmpty();
                btnSeleccionarJar.setEnabled(tieneTexto);
            }
        });
    }

    public void setActionListener(ActionListener actionListener) {
        btnSeleccionarJar.addActionListener(actionListener);
        btnCancelar.addActionListener(actionListener);
    }

    public String getNombreInstancia() {
        return campoNombre.getText();
    }

    public void limpiarCampo() {
        campoNombre.setText("");
    }

    public void setPadre(JFrame padre) {
        this.setLocationRelativeTo(padre);
    }
}
