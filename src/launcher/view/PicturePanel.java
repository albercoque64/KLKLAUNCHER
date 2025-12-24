package launcher.view;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class PicturePanel extends JPanel {

    String rutaImagen = System.getProperty("user.dir") + "/resources/images/fondo.png";
    private Image imagenFondo;

    public PicturePanel() {
        java.io.File f = new java.io.File(rutaImagen);

        if (f.exists()) {
            imagenFondo = new ImageIcon(rutaImagen).getImage();
        }

        this.setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (imagenFondo != null) {
            int panelW = this.getWidth();
            int panelH = this.getHeight();
            int imgW = imagenFondo.getWidth(this);
            int imgH = imagenFondo.getHeight(this);

            if (imgW > 0 && imgH > 0) {
                double scale = Math.max((double) panelW / imgW, (double) panelH / imgH);

                int newW = (int) (imgW * scale);
                int newH = (int) (imgH * scale);

                int x = (panelW - newW) / 2;
                int y = (panelH - newH) / 2;

                g.drawImage(imagenFondo, x, y, newW, newH, this);
            }
        }
    }
}
