package launcher.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

public class PrincipalView extends JFrame {

    // ESTRUCTURA PRINCIPAL
    private OptionPanel panelOpciones = new OptionPanel();
    private EastPanel eastPanel = new EastPanel();

    public PrincipalView() {
        configurarVentana();
        iniciarComponentes();
    }

    private void configurarVentana() {
        this.setTitle("KLK Launcher");
        this.setLayout(new BorderLayout());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double ancho = screenSize.width * 0.5;
        double alto = screenSize.height * 0.5;
        this.setSize((int) ancho, (int) alto);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void iniciarComponentes() {

        this.add(panelOpciones, BorderLayout.WEST);
        this.add(eastPanel, BorderLayout.CENTER);

        this.setVisible(true);
    }

    //EAST PANEL
    public void mostrarConsola() {
        eastPanel.mostrarConsola();
    }

    public void mostrarFoto() {
        eastPanel.mostrarFoto();
    }

    public void mostrarInfo() {
        eastPanel.mostrarInfo();
    }

    public ConsolePanel getConsolePanel() {
        return eastPanel.getConsolePanel();
    }

    public InfoPanel getInfoPanel() {
        return eastPanel.getInfoPanel();
    }

    //OPCIONES
    public OptionPanel getOptionPanel() {
        return panelOpciones;
    }

    public int getJavaVersion() {
        return panelOpciones.getJavaVersion();
    }

    public int getRamEnMB() {
        return panelOpciones.getRamEnMB();
    }

    public String getUsuario() {
        return panelOpciones.getUsuario();
    }

    public void setUsuario(String user) {
        panelOpciones.setUsuario(user);
    }

    public void setRAM(int ram) {
        panelOpciones.setRam(ram);
    }

    public void setJavaVersion(int version) {
        panelOpciones.setJavaVersion(version);
    }

    public void apagarJugar() {
        panelOpciones.apagarJugar();
    }

    public void encenderJugar() {
        panelOpciones.encenderJugar();
    }

    //LISTENERS
    public void setActionListeners(ActionListener actionListener) {
        panelOpciones.setActionListener(actionListener);
        eastPanel.setActionListeners(actionListener);
    }
}
