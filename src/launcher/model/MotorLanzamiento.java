package launcher.model;

import com.icecold.javascript.JavaScriptObjectNotation;
import com.icecold.javascript.parser.JavaScriptStaticParser;
import com.icecold.javascript.particulars.JavaScriptObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

        if (!jsonFile.exists()) {
            return null;
        }

        try {
            String contenidoJson = new String(Files.readAllBytes(jsonFile.toPath()), StandardCharsets.UTF_8);
            boolean esCleanroom = contenidoJson.toLowerCase().contains("cleanroom");

            String javaExec;
            if (esCleanroom) {
                if (opcionDistribucion == 1) {
                    javaExec = dirRaiz + "/portable/runtimes/jdk25Adoptium/bin/java.exe";
                } else {
                    javaExec = dirRaiz + "/portable/runtimes/jdk25Graal/bin/java.exe";
                }
            } else {
                if (opcionDistribucion == 1) {
                    javaExec = dirRaiz + "/portable/runtimes/Adoptium/bin/java.exe";
                } else {
                    javaExec = dirRaiz + "/portable/runtimes/Graal/bin/java.exe";
                }
            }

            File dirNatives = new File(baseDir + "/bin/natives");

            new File(instanciaDir).mkdirs();
            limpiarDirectorio(dirNatives);
            dirNatives.mkdirs();

            File configDir = new File(instanciaDir + "/config");
            configDir.mkdirs();
            File forgeConfig = new File(configDir, "forge.cfg");

            if (!forgeConfig.exists()) {
                String cfgOptimo = "version_checking {\n    B:disableVersionCheck=true\n}\n";
                try {
                    Files.write(forgeConfig.toPath(), cfgOptimo.getBytes(StandardCharsets.UTF_8));
                } catch (Exception e) {
                }
            }

            JavaScriptObjectNotation jsonParsed = JavaScriptStaticParser.parse(contenidoJson);
            ObjetoDeInstalacion instalacion = new ObjetoDeInstalacion();
            instalacion.setObject((JavaScriptObject) jsonParsed);

            List<String> jars = new ArrayList<>();

            for (int j = 0; j < instalacion.libraries.size(); j++) {
                boolean permitir = true;
                if (instalacion.libraries.get(j).rules != null && !instalacion.libraries.get(j).rules.isEmpty()) {
                    if (instalacion.libraries.get(j).rules.get(0).os != null) {
                        String osName = instalacion.libraries.get(j).rules.get(0).os.name;
                        if (!osName.equals("windows")) {
                            permitir = false;
                        }
                    }
                }

                if (permitir) {
                    String path = instalacion.libraries.get(j).downloads.artifacts.path;
                    String rutaAbsoluta = new File(baseDir + "/libraries/" + path).getAbsolutePath();

                    if (path.contains("natives-windows")) {
                        extraerNativos(rutaAbsoluta, dirNatives);
                    }
                    jars.add(rutaAbsoluta);
                }
            }

            List<String> todasLasLibrerias = new ArrayList<>();
            recorrer(new File(baseDir + "/libraries"), todasLasLibrerias);

            for (String libExtra : todasLasLibrerias) {

                if (esCleanroom) {
                    if (libExtra.contains("lwjgl-2.") || libExtra.contains("lwjgl_util-2.") || libExtra.contains("lwjgl-platform-2.")) {
                        continue;
                    }
                    if (libExtra.contains("lwjgl") && !libExtra.contains("3.3.6") && !libExtra.contains("lwjglxx") && !libExtra.contains("librarylwjglopenal")) {
                        continue;
                    }
                } else {
                    if (libExtra.contains("cleanroom") || libExtra.contains("foundation") || libExtra.contains("imaginebreaker") || libExtra.contains("lwjglxx") || libExtra.contains("mixinextras")) {
                        continue;
                    }
                    if (libExtra.contains("lwjgl") && (libExtra.contains("-3.") || libExtra.contains("/3.") || libExtra.contains("\\3.") || libExtra.contains("lwjgl3"))) {
                        continue;
                    }
                }

                if (libExtra.contains("natives-windows")) {
                    extraerNativos(libExtra, dirNatives);
                    if (!esCleanroom) {
                        continue;
                    }
                }

                if (!jars.contains(libExtra)) {
                    jars.add(libExtra);
                }
            }

            jars.add(new File(baseDir + "/versions/1.12.2/1.12.2.jar").getAbsolutePath());
            String classpathCompleto = String.join(File.pathSeparator, jars);

            List<String> args = new ArrayList<>();

            args.add("-Xms" + ramMb + "m");
            args.add("-Xmx" + ramMb + "m");

            if (esCleanroom) {
                args.add("-XX:+UseZGC");
                args.add("-XX:+ZGenerational");
            }

            args.add("-Djava.library.path=" + dirNatives.getAbsolutePath());
            args.add("-Djdk.attach.allowAttachSelf=true");

            if (esCleanroom) {
                args.add("--add-exports=java.base/jdk.internal.loader=ALL-UNNAMED");
                args.add("--add-opens=java.base/jdk.internal.loader=ALL-UNNAMED");
                args.add("--add-opens=java.base/java.lang=ALL-UNNAMED");
                args.add("--add-opens=java.base/java.security=ALL-UNNAMED");
                args.add("--add-opens=jdk.unsupported/sun.misc=ALL-UNNAMED");
            }

            args.add("-Dforge.disableVersionCheck=true");
            args.add("-Djna.tmpdir=" + dirNatives.getAbsolutePath());
            args.add("-cp");
            args.add(classpathCompleto);

            if (esCleanroom) {
                args.add("top.outlands.foundation.boot.Foundation");
            } else {
                args.add("net.minecraft.launchwrapper.Launch");
            }

            args.add("--username");
            args.add(username);
            args.add("--version");
            args.add(nombreInstancia);
            args.add("--gameDir");
            args.add(instanciaDir);
            args.add("--assetsDir");
            args.add(baseDir + "/assets");
            args.add("--assetIndex");
            args.add("1.12");
            args.add("--uuid");
            args.add("null");
            args.add("--accessToken");
            args.add("null");
            args.add("--userType");
            args.add("mojang");
            args.add("--tweakClass");
            args.add("net.minecraftforge.fml.common.launcher.FMLTweaker");
            args.add("--versionType");
            args.add("Forge");

            List<String> comandoLaunch = new ArrayList<>();
            comandoLaunch.add(javaExec);
            comandoLaunch.addAll(args);

            ProcessBuilder pb = new ProcessBuilder(comandoLaunch);
            pb.directory(new File(instanciaDir));
            pb.redirectErrorStream(true);
            return pb.start();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void recorrer(File carpeta, List<String> lista) {
        File[] archivos = carpeta.listFiles();
        if (archivos != null) {
            for (File f : archivos) {
                if (f.isDirectory()) {
                    recorrer(f, lista);
                } else if (f.getName().endsWith(".jar")) {
                    lista.add(f.getAbsolutePath());
                }
            }
        }
    }

    private static void extraerNativos(String jarPath, File dirNatives) {
        File jarFile = new File(jarPath);
        if (!jarFile.exists()) {
            return;
        }

        try (JarFile jar = new JarFile(jarFile)) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (!entry.isDirectory() && (name.endsWith(".dll") || name.endsWith(".so") || name.endsWith(".dylib") || name.endsWith(".jnilib"))) {
                    File dest = new File(dirNatives, new File(name).getName());
                    try (InputStream is = jar.getInputStream(entry)) {
                        Files.copy(is, dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    private static void limpiarDirectorio(File directorio) {
        if (directorio.exists() && directorio.isDirectory()) {
            File[] archivos = directorio.listFiles();
            if (archivos != null) {
                for (File f : archivos) {
                    if (!f.isDirectory()) {
                        f.delete();
                    }
                }
            }
        }
    }
}
