package launcher.model;

import com.icecold.javascript.JavaScriptObjectNotation;
import com.icecold.javascript.parser.JavaScriptStaticParser;
import com.icecold.javascript.particulars.JavaScriptObject;
import java.awt.Component;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.swing.SwingUtilities;
import launcher.model.objetosJs.Libreria;
import launcher.model.objetosJs.ObjetoDeInstalacion;
import launcher.utils.DialogoProgreso;

public class VersionManager {

    public static void instalador(Component parent, String nombreInstancia) {
        File instaladorJar = seleccionarInstalador(parent);

        if (instaladorJar == null) {
            return;
        }

        List<EntradaJar> jsons = extraerJsons(instaladorJar);

        String contenidoParaInstancia = "";
        for (EntradaJar j : jsons) {
            if (j.getNombre().equals("version.json")) {
                contenidoParaInstancia = j.getContenido();
                break;
            }
        }

        if (contenidoParaInstancia.isEmpty() && !jsons.isEmpty()) {
            contenidoParaInstancia = jsons.get(0).getContenido();
        }

        if (!prepararInstancia(nombreInstancia, contenidoParaInstancia)) {
            return;
        }

        List<Libreria> lista = extractorLibrerias(jsons);

        DialogoProgreso dialogo = new DialogoProgreso(parent, lista.size());

        new Thread(() -> {
            String rutaAlmacen = System.getProperty("user.dir") + "/portable/.minecraft/libraries/";
            int progreso = 0;
            List<String> libreriasFallidas = new ArrayList<>();

            System.out.println("--- INICIANDO INSTALACION ---");

            for (Libreria lib : lista) {
                String so = lib.getSo();

                if (so.equals("universal") || so.equals("windows")) {
                    File archivoDestino = new File(rutaAlmacen, lib.getPath());
                    archivoDestino.getParentFile().mkdirs();

                    final int p = progreso;
                    SwingUtilities.invokeLater(() -> dialogo.actualizarProgreso(p, "Procesando: " + archivoDestino.getName()));

                    boolean exito = false;

                    if (lib.getUrl() != null && !lib.getUrl().trim().isEmpty()) {
                        exito = descargarDeInternet(lib.getUrl(), archivoDestino);
                    }

                    if (!exito) {
                        exito = extraerDelJar(instaladorJar, lib.getPath(), archivoDestino);
                    }

                    if (exito) {
                        System.out.println("[OK] " + lib.getPath());
                    } else {
                        System.out.println("[ERROR] Fallo al obtener: " + lib.getPath());
                        libreriasFallidas.add(lib.getPath());
                    }
                }
                progreso++;
            }

            System.out.println("\n--- REPORTE DE INSTALACION ---");
            if (libreriasFallidas.isEmpty()) {
                System.out.println("Instalacion perfecta. 0 fallos.");
            } else {
                System.out.println("ATENCION: Fallaron " + libreriasFallidas.size() + " librerias:");
                for (String fallo : libreriasFallidas) {
                    System.out.println("- " + fallo);
                }
            }
            System.out.println("------------------------------\n");

            SwingUtilities.invokeLater(() -> {
                dialogo.cerrar();
                javax.swing.JOptionPane.showMessageDialog(parent, "Instalación completada para la instancia: " + nombreInstancia, "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            });

        }).start();

        dialogo.setVisible(true);
    }

    public static boolean extraerDelJar(File archivoJar, String rutaInterna, File archivoDestino) {
        try (JarFile jar = new JarFile(archivoJar)) {
            String rutaLimpia = rutaInterna.replace("\\", "/");
            JarEntry entrada = jar.getJarEntry("maven/" + rutaLimpia);

            if (entrada == null) {
                entrada = jar.getJarEntry(rutaLimpia);
            }

            if (entrada != null) {
                try (InputStream is = jar.getInputStream(entrada)) {
                    Files.copy(is, archivoDestino.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("[EXTRAER JAR ERROR] " + rutaInterna);
            e.printStackTrace();
        }
        return false;
    }

    public static File seleccionarInstalador(java.awt.Component parent) {

        java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(parent);
        java.awt.Frame frame = (window instanceof java.awt.Frame) ? (java.awt.Frame) window : null;

        java.awt.FileDialog fileDialog = new java.awt.FileDialog(frame, "Seleccionar instalador", java.awt.FileDialog.LOAD);

        String rutaHome = System.getProperty("user.home");
        java.io.File carpetaDescargas = new java.io.File(rutaHome, "Downloads");
        fileDialog.setDirectory(carpetaDescargas.getAbsolutePath());

        fileDialog.setFile("*.jar");

        fileDialog.setVisible(true);

        String directorioSeleccionado = fileDialog.getDirectory();
        String archivoSeleccionado = fileDialog.getFile();

        if (directorioSeleccionado != null && archivoSeleccionado != null) {
            return new java.io.File(directorioSeleccionado, archivoSeleccionado);
        }

        return null;
    }

    public static List<EntradaJar> extraerJsons(File archivoJar) {
        java.util.List<EntradaJar> jsonsExtraidos = new java.util.ArrayList<>();

        try (java.util.jar.JarFile jar = new java.util.jar.JarFile(archivoJar)) {
            java.util.Enumeration<java.util.jar.JarEntry> entradas = jar.entries();

            while (entradas.hasMoreElements()) {
                java.util.jar.JarEntry entrada = entradas.nextElement();
                String nombre = entrada.getName();

                if (nombre.equals("version.json") || nombre.equals("install_profile.json")) {
                    String contenido = "";
                    try (InputStream is = jar.getInputStream(entrada)) {
                        contenido = new String(is.readAllBytes());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    jsonsExtraidos.add(new EntradaJar(nombre, contenido));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonsExtraidos;
    }

    public static List<Libreria> extractorLibrerias(List<EntradaJar> contenido) {
        ObjetoDeInstalacion installation = new ObjetoDeInstalacion();
        List<Libreria> lista = new ArrayList<>();
        JavaScriptObject goffyAhhVariable;

        for (EntradaJar i : contenido) {
            if (i.getNombre().equals("version.json") || i.getNombre().equals("install_profile.json")) {
                JavaScriptObjectNotation json = JavaScriptStaticParser.parse(i.getContenido());
                goffyAhhVariable = (JavaScriptObject) json;
                installation.setObject(goffyAhhVariable);

                for (int j = 0; j < installation.libraries.size(); j++) {
                    String path = installation.libraries.get(j).downloads.artifacts.path;
                    String url = "";

                    if (installation.libraries.get(j).downloads.artifacts.url.isPresent()) {
                        url = installation.libraries.get(j).downloads.artifacts.url.get();
                    }

                    if (installation.libraries.get(j).rules != null && !installation.libraries.get(j).rules.isEmpty() && installation.libraries.get(j).rules.get(0).os != null) {
                        lista.add(new Libreria(path, url, installation.libraries.get(j).rules.get(0).os.name));
                    } else {
                        lista.add(new Libreria(path, url, "universal"));
                    }
                }
            }
        }
        return lista;
    }

    public static boolean descargarDeInternet(String urlLibreria, File archivoDestino) {
        try {
            java.net.URL url = new java.net.URL(urlLibreria);
            try (java.io.InputStream in = url.openStream()) {
                java.nio.file.Files.copy(in, archivoDestino.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                return true;
            }
        } catch (Exception e) {
            System.out.println("[DESCARGA ERROR] " + urlLibreria + " -> " + e.getMessage());
            return false;
        }
    }

    public static boolean prepararInstancia(String nombreCrudo, String contenidoJson) {
        if (nombreCrudo == null || nombreCrudo.trim().isEmpty() || contenidoJson == null || contenidoJson.trim().isEmpty()) {
            return false;
        }

        String nombreLimpio = nombreCrudo.replaceAll("[\\\\/:*?\"<>|]", "_");
        String rutaBase = System.getProperty("user.dir");

        java.io.File carpetaInstancia = new java.io.File(rutaBase + "/portable/instancias/" + nombreLimpio);

        if (carpetaInstancia.exists()) {
            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    "Ya existe una instalación con el nombre: " + nombreLimpio + "\nPor favor, elige otro nombre.",
                    "Error de Instalación",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        carpetaInstancia.mkdirs();

        File destinoJson = new File(carpetaInstancia, "instrucciones.json");

        try {
            Files.write(destinoJson.toPath(), contenidoJson.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return true;
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
