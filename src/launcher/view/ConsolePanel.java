package launcher.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class ConsolePanel extends JPanel {

    private JTextArea areaTexto;

    public ConsolePanel() {
        this.setLayout(new BorderLayout());

        areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        areaTexto.setBackground(new Color(20, 20, 20));
        areaTexto.setForeground(new Color(46, 204, 113));
        areaTexto.setFont(new Font("Consolas", Font.PLAIN, 14));
        areaTexto.setLineWrap(true);
        areaTexto.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(areaTexto);
        scroll.setBorder(null);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        this.setBorder(new EmptyBorder(0, 0, 0, 0));

        this.add(scroll, BorderLayout.CENTER);
    }

    public void log(String texto) {
        SwingUtilities.invokeLater(() -> {
            areaTexto.append(texto + "\n");
            areaTexto.setCaretPosition(areaTexto.getDocument().getLength());
        });
    }

    public void limpiar() {
        areaTexto.setText("");
    }
}
