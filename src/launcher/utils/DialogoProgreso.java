package launcher.utils;

import javax.swing.JDialog;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.BorderLayout;

public class DialogoProgreso extends JDialog {

    private JProgressBar barraProgreso;
    private JLabel etiquetaEstado;

    public DialogoProgreso(Component parent, int maximo) {
        super(SwingUtilities.getWindowAncestor(parent), "Instalando dependencias", Dialog.ModalityType.APPLICATION_MODAL);
        setSize(400, 120);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        etiquetaEstado = new JLabel("Preparando instalación...");
        etiquetaEstado.setHorizontalAlignment(SwingConstants.CENTER);
        add(etiquetaEstado, BorderLayout.NORTH);

        barraProgreso = new JProgressBar(0, maximo);
        barraProgreso.setStringPainted(true);
        add(barraProgreso, BorderLayout.CENTER);

        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    }

    public void actualizarProgreso(int valor, String texto) {
        SwingUtilities.invokeLater(() -> {
            barraProgreso.setValue(valor);
            etiquetaEstado.setText(texto);
        });
    }

    public void cerrar() {
        SwingUtilities.invokeLater(this::dispose);
    }
}
