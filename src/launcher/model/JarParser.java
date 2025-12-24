package launcher.model;

import java.io.*;
import java.util.*;
import java.util.jar.*;

public class JarParser {

    public List<Mod> escanearMods() {
        List<Mod> listaMods = new ArrayList<>();
        String rutaBase = System.getProperty("user.dir");
        String rutaMods = rutaBase + "/portable/.minecraft/mods";

        System.out.println("Buscando mods en: " + rutaMods);

        File[] misJars = buscarJars(rutaMods);

        if (misJars != null && misJars.length > 0) {
            for (File archivoJar : misJars) {
                System.out.println("Procesando: " + archivoJar.getName());

                List<String> datosCrudos = analizarDependenciasEnJar(archivoJar);
                Mod nuevoMod = convertirDatosAMod(datosCrudos, archivoJar.getName());

                if (!nuevoMod.getModId().isEmpty() || !nuevoMod.getModName().isEmpty()) {
                    listaMods.add(nuevoMod);
                }
            }
        } else {
            System.out.println("No se encontraron archivos .jar.");
        }
        listaMods.sort(Comparator.comparing(Mod::getModName, String.CASE_INSENSITIVE_ORDER));
        return listaMods;
    }

    private Mod convertirDatosAMod(List<String> datos, String nombreArchivo) {
        Mod mod = new Mod();

        if (datos.isEmpty()) {
            return mod;
        }
        mod.setNombreArchivo(nombreArchivo);

        for (int i = 0; i < datos.size() - 1; i++) {
            String actual = datos.get(i);
            String siguiente = datos.get(i + 1);

            if (siguiente.contains("/") || siguiente.contains(";") || siguiente.contains("(")
                    || siguiente.contains("$") || siguiente.contains("<") || siguiente.startsWith("java.")) {
                continue;
            }

            if (actual.equals("modid") && mod.getModId().isEmpty()) {
                if (siguiente.matches("^[a-z][a-z0-9_]*$")) {
                    mod.setModId(siguiente);
                }
            }

            if (actual.equals("name") && mod.getModName().isEmpty()) {
                if (siguiente.length() > 2 && !siguiente.contains(":") && !siguiente.contains(".")) {
                    if (Character.isUpperCase(siguiente.charAt(0)) || Character.isDigit(siguiente.charAt(0))) {
                        mod.setModName(siguiente);
                    }
                }
            }

            if (actual.equals("version") && mod.getVersion().isEmpty()) {
                if (siguiente.matches("^[0-9].*")) {
                    mod.setVersion(siguiente);
                }
            }

            if ((actual.equals("acceptedMinecraftVersions") || actual.equals("mcversion")) && mod.getMinecraftVersions().isEmpty()) {
                String versionLimpia = siguiente.replaceAll("[\\[\\]()]", "");
                if (versionLimpia.matches(".*[0-9].*")) {
                    mod.setMinecraftVersions(versionLimpia);
                }
            }

            if (actual.equals("dependencies") || actual.equals("required-after")) {
                if (!mod.getDependencies().contains(siguiente)) {
                    mod.getDependencies().add(siguiente);
                }
            }

            if (actual.startsWith("OptiFine_") && actual.length() > 10) {
                mod.setModId("optifine");
                mod.setModName(actual);
                mod.setVersion(actual.replace("OptiFine_", ""));

                String[] partes = actual.split("_");
                if (partes.length > 1) {
                    mod.setMinecraftVersions(partes[1]);
                }
            }
        }

        if (mod.getModName().isEmpty() && !mod.getModId().isEmpty()) {
            mod.setModName(mod.getModId());
        }

        return mod;
    }

    public File[] buscarJars(String ruta) {
        File carpeta = new File(ruta);
        if (carpeta.exists() && carpeta.isDirectory()) {
            return carpeta.listFiles((dir, name) -> name.endsWith(".jar") || name.toLowerCase().endsWith(".jaroff"));
        }
        return null;
    }

    public List<String> analizarDependenciasEnJar(File archivoJar) {
        List<String> resultados = new ArrayList<>();

        try (JarFile jar = new JarFile(archivoJar)) {
            Enumeration<JarEntry> entradas = jar.entries();

            while (entradas.hasMoreElements()) {
                JarEntry entrada = entradas.nextElement();
                String nombre = entrada.getName();

                if (nombre.endsWith(".class")
                        && !nombre.startsWith("assets/")
                        && !nombre.startsWith("data/")
                        && !nombre.startsWith("META-INF/")
                        && !nombre.startsWith("org/")
                        && !nombre.startsWith("com/google/")
                        && !nombre.startsWith("javax/")) {

                    try (InputStream is = jar.getInputStream(entrada)) {
                        List<String> temporal = extraerStringsDeClass(is);

                        if (temporal.contains("modid") || temporal.contains("OptiFine")) {
                            resultados.addAll(temporal);
                        }

                    } catch (Exception e) {
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error leyendo JAR: " + archivoJar.getName());
        }
        return resultados;
    }

    private List<String> extraerStringsDeClass(InputStream inputStream) {
        List<String> stringsEncontrados = new ArrayList<>();

        try {
            DataInputStream dis = new DataInputStream(inputStream);

            if (dis.readInt() != 0xCAFEBABE) {
                return stringsEncontrados;
            }

            dis.readUnsignedShort();
            dis.readUnsignedShort();

            int constantPoolCount = dis.readUnsignedShort();

            for (int i = 1; i < constantPoolCount; i++) {
                int tag = dis.readUnsignedByte();

                switch (tag) {
                    case 1:
                        String content = dis.readUTF();
                        if (content.length() > 2) {
                            stringsEncontrados.add(content);
                        }
                        break;
                    case 7:
                    case 8:
                    case 16:
                    case 19:
                    case 20:
                        dis.skipBytes(2);
                        break;
                    case 3:
                    case 4:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 17:
                    case 18:
                        dis.skipBytes(4);
                        break;
                    case 5:
                    case 6:
                        dis.skipBytes(8);
                        i++;
                        break;
                    case 15:
                        dis.skipBytes(3);
                        break;
                    default:
                        return stringsEncontrados;
                }
            }
        } catch (IOException e) {
        }
        return stringsEncontrados;
    }
}
