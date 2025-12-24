package launcher.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class EastPanel extends JPanel {

    private PicturePanel panelFoto = new PicturePanel();
    private ConsolePanel panelConsola = new ConsolePanel();
    private InfoPanel panelInfo = new InfoPanel();

    private JPanel panelCambiante = new JPanel();
    private CardLayout layoutCartas = new CardLayout();

    public EastPanel() {

        this.setLayout(new BorderLayout());
        panelCambiante();
        this.add(panelCambiante, BorderLayout.CENTER);
        this.add(panelInfo, BorderLayout.SOUTH);

    }

    public void panelCambiante() {

        panelCambiante.setLayout(layoutCartas);
        panelCambiante.add(panelFoto, "FOTO");
        panelCambiante.add(panelConsola, "CONSOLA");

        mostrarFoto();
    }

    public void mostrarFoto() {
        layoutCartas.show(panelCambiante, "FOTO");
    }

    public void mostrarConsola() {
        panelConsola.limpiar();
        layoutCartas.show(panelCambiante, "CONSOLA");
    }

    public void mostrarInfo() {
        layoutCartas.show(this, "INFO");
    }

    public ConsolePanel getConsolePanel() {
        return panelConsola;
    }

    public InfoPanel getInfoPanel() {
        return panelInfo;
    }

    public void setActionListeners(ActionListener al) {
        panelInfo.setActionListener(al);

    }
}
