package launcher.utils;
import launcher.utils.FontLoader;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;

public class FontLoader {

    public static Font cargarFuente(String rutaArchivo, float tamano) {
        try {

            File file = new File(rutaArchivo);
            Font fontBase = Font.createFont(Font.TRUETYPE_FONT, file);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(fontBase);

            return fontBase.deriveFont(tamano);

        } catch (Exception e) {
            System.err.println("Error cargando fuente, usando defecto.");
            e.printStackTrace();
            return new Font("Arial", Font.BOLD, (int) tamano);
        }
    }
}
