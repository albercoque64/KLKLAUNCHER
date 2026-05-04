package launcher.model;

import com.icecold.javascript.JavaScriptObjectNotation;
import com.icecold.javascript.parser.JavaScriptStaticParser;
import com.icecold.javascript.particulars.JavaScriptObject;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import launcher.model.objetosJs.ObjetoDeInstalacion;

public class MotorLanzamiento {

    public Process arrancarJuego(String nombreInstancia, String username, int ramMb, int opcionDistribucion) {
        String dirRaiz = System.getProperty("user.dir");
        String baseDir = dirRaiz + "/portable/.minecraft";
        String instanciaDir = dirRaiz + "/portable/instancias/" + nombreInstancia;

        File jsonFile = new File(instanciaDir + "/instrucciones.json");
        if (!jsonFile.exists()) return null;

        try {
            String contenidoJson = new String(Files.readAllBytes(jsonFile.toPath()), StandardCharsets.UTF_8);
            boolean esCleanroom = contenidoJson.toLowerCase().contains("cleanroom");

            String javaExec = esCleanroom 
                ? ((opcionDistribucion == 1) ? dirRaiz + "/portable/runtimes/jdk25Adoptium/bin/java.exe" : dirRaiz + "/portable/runtimes/jdk25Graal/bin/java.exe")
                : ((opcionDistribucion == 1) ? dirRaiz + "/portable/runtimes/Adoptium/bin/java.exe" : dirRaiz + "/portable/runtimes/Graal/bin/java.exe");

            File dirNatives = new File(baseDir + "/bin/natives");
            new File(instanciaDir).mkdirs();
            limpiarDirectorio(dirNatives);
            dirNatives.mkdirs();

            List<String> jars = new ArrayList<>();
            procesarLibreriasJson(jsonFile, baseDir, dirNatives, jars, esCleanroom);

            File vanillaJson = new File(baseDir + "/versions/1.12.2/1.12.2.json");
            if (vanillaJson.exists()) {
                procesarLibreriasJson(vanillaJson, baseDir, dirNatives, jars, esCleanroom);
            }

            String mainJar = new File(baseDir + "/versions/1.12.2/1.12.2.jar").getAbsolutePath();
            if (!jars.contains(mainJar)) jars.add(mainJar);

            StringBuilder classPathBuilder = new StringBuilder();
            for (int i = 0; i < jars.size(); i++) {
                classPathBuilder.append(jars.get(i)).append(i < jars.size() - 1 ? File.pathSeparator : "");
            }

            List<String> args = new ArrayList<>();
            args.add("-Xms" + ramMb + "m");
            args.add("-Xmx" + ramMb + "m");

            if (esCleanroom) {
                args.add("-XX:+UseZGC");
                args.add("-XX:+UseCompactObjectHeaders");
                args.add("--add-exports=java.base/jdk.internal.loader=ALL-UNNAMED");
                args.add("--add-opens=java.base/jdk.internal.loader=ALL-UNNAMED");
                args.add("--add-opens=java.base/java.lang=ALL-UNNAMED");
                args.add("--add-opens=java.base/java.security=ALL-UNNAMED");
                args.add("--add-opens=jdk.unsupported/sun.misc=ALL-UNNAMED");
            } else {
                args.add("-XX:+UseG1GC");
                args.add("-XX:+UnlockExperimentalVMOptions");
                args.add("-XX:MaxGCPauseMillis=50");
                args.add("-XX:+DisableExplicitGC");
            }

            args.add("-Djava.library.path=" + dirNatives.getAbsolutePath());
            args.add("-Dforge.disableVersionCheck=true");
            args.add("-cp");
            args.add(classPathBuilder.toString());
            args.add(esCleanroom ? "top.outlands.foundation.boot.Foundation" : "net.minecraft.launchwrapper.Launch");

            args.add("--username"); args.add(username);
            args.add("--version"); args.add(nombreInstancia);
            args.add("--gameDir"); args.add(instanciaDir);
            args.add("--assetsDir"); args.add(baseDir + "/assets");
            args.add("--assetIndex"); args.add("1.12");
            args.add("--uuid"); args.add("null");
            args.add("--accessToken"); args.add("null");
            args.add("--userType"); args.add("mojang");
            args.add("--tweakClass"); args.add("net.minecraftforge.fml.common.launcher.FMLTweaker");
            args.add("--versionType"); args.add("Forge");

            ProcessBuilder pb = new ProcessBuilder();
            List<String> fullCommand = new ArrayList<>();
            fullCommand.add(javaExec);
            fullCommand.addAll(args);
            pb.command(fullCommand);
            pb.directory(new File(instanciaDir));
            pb.redirectErrorStream(true);
            return pb.start();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void procesarLibreriasJson(File jsonFile, String baseDir, File dirNatives, List<String> jars, boolean esCleanroom) {
        try {
            String contenidoJson = new String(Files.readAllBytes(jsonFile.toPath()), StandardCharsets.UTF_8);
            JavaScriptObjectNotation jsonParsed = JavaScriptStaticParser.parse(contenidoJson);
            ObjetoDeInstalacion instalacion = new ObjetoDeInstalacion();
            instalacion.setObject((JavaScriptObject) jsonParsed);

            for (int j = 0; j < instalacion.libraries.size(); j++) {
                try {
                    boolean permitir = true;
                    if (instalacion.libraries.get(j).rules != null && !instalacion.libraries.get(j).rules.isEmpty()) {
                        if (instalacion.libraries.get(j).rules.get(0).os != null) {
                            String osName = instalacion.libraries.get(j).rules.get(0).os.name;
                            if (osName != null && !osName.equals("windows")) permitir = false;
                        }
                    }

                    if (permitir) {
                        String path = null;
                        if (instalacion.libraries.get(j).downloads != null && instalacion.libraries.get(j).downloads.artifacts != null) {
                            path = instalacion.libraries.get(j).downloads.artifacts.path;
                        }

                        if (path != null) {
                            if (esCleanroom && (path.contains("lwjgl-2") || path.contains("lwjgl_util-2"))) continue;
                            
                            String rutaAbsoluta = new File(baseDir + "/libraries/" + path).getAbsolutePath();
                            
                            if (path.contains("natives-windows") || path.contains("platform-windows")) {
                                extraerNativos(rutaAbsoluta, dirNatives);
                            }
                            if (!jars.contains(rutaAbsoluta) && new File(rutaAbsoluta).exists()) {
                                jars.add(rutaAbsoluta);
                            }
                        }
                    }
                } catch (Exception e) { }
            }
            
            if (!esCleanroom) {
                // ESCANEO DE SEGURIDAD PARA FORGE: Busca cualquier JAR de nativos y lo extrae
                File libFolder = new File(baseDir + "/libraries/org/lwjgl");
                buscarYExtraerNativos(libFolder, dirNatives);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buscarYExtraerNativos(File carpeta, File dirNatives) {
        if (!carpeta.exists() || !carpeta.isDirectory()) return;
        File[] archivos = carpeta.listFiles();
        if (archivos != null) {
            for (File f : archivos) {
                if (f.isDirectory()) {
                    buscarYExtraerNativos(f, dirNatives);
                } else if (f.getName().endsWith(".jar") && f.getName().contains("natives-windows")) {
                    extraerNativos(f.getAbsolutePath(), dirNatives);
                }
            }
        }
    }

    private static void extraerNativos(String jarPath, File dirNatives) {
        File jarFile = new File(jarPath);
        if (!jarFile.exists()) return;
        try (JarFile jar = new JarFile(jarFile)) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (!entry.isDirectory() && entry.getName().endsWith(".dll")) {
                    Files.copy(jar.getInputStream(entry), new File(dirNatives, new File(entry.getName()).getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        } catch (Exception e) { }
    }

    private static void limpiarDirectorio(File directorio) {
        File[] archivos = directorio.listFiles();
        if (archivos != null) {
            for (File f : archivos) f.delete();
        }
    }
}