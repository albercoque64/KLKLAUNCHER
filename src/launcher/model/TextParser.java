package launcher.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextParser {

    List<String> textoBat;
    List<String> textoOpciones;
    List<String> textoOpcionesOf;

    //RUTAS
    String java = "\"%~dp0portable\\runtimes\\java\\bin\\java.exe\"";
    String adoptium = "\"%~dp0portable\\runtimes\\adoptium\\bin\\java.exe\"";
    String graal = "\"%~dp0portable\\runtimes\\graal\\bin\\java.exe\"";

    //OPCIONES
    boolean fullscreen = false;
    boolean useVbo = false;
    boolean advancedItemTooltips = false;
    int lang = 0;
    int guiScale = 0;

    //OPCIONES OF
    boolean ofFastRender = false;
    boolean ofSmartAnimations = false;
    boolean ofShowFps = false;
    int ofDynamicLights = 3;
    int ofConnectedTextures = 3;

    public TextParser() {
        batReader();
        optionsReader();
        optionsOfReader();

    }

    //COSAS BAT
    public void batReader() {
        List<String> lineas = new ArrayList<>();
        String rutaBase = System.getProperty("user.dir");
        java.io.File archivoBat = new java.io.File(rutaBase, "start.bat");

        try (BufferedReader br = new BufferedReader(new FileReader(archivoBat))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.textoBat = lineas;
    }

    public String getUsuario() {
        for (String linea : this.textoBat) {
            String l = linea.trim();
            if (l.startsWith("set USUARIO=")) {
                String usuarioEncontrado = l.substring("set USUARIO=".length());
                System.out.println("[PARSER] Usuario leido del .bat: " + usuarioEncontrado);
                return usuarioEncontrado;
            }
        }
        System.out.println("[PARSER] Usuario no encontrado, devolviendo default: Steve");
        return "Steve";
    }

    public int getRam() {
        for (String linea : this.textoBat) {
            String l = linea.trim();
            if (l.startsWith("set MAX_RAM=")) {
                try {
                    String valor = l.substring("set MAX_RAM=".length()).replace("m", "").trim();
                    int ramEncontrada = Integer.parseInt(valor);
                    System.out.println("[PARSER] RAM leida del .bat: " + ramEncontrada);
                    return ramEncontrada;
                } catch (NumberFormatException e) {
                    System.out.println("[PARSER] Error formato RAM, devolviendo default: 2048");
                    return 2048;
                }
            }
        }
        System.out.println("[PARSER] RAM no encontrada, devolviendo default: 2048");
        return 2048;
    }

    public int getJavaOpcion() {
        for (String linea : this.textoBat) {
            String l = linea.trim();
            if (l.startsWith("set JAVA_EXEC=")) {
                String rutaLeida = l.substring("set JAVA_EXEC=".length()).trim();
                System.out.println("[PARSER] Ruta Java leida: " + rutaLeida);

                if (rutaLeida.equals(this.adoptium)) {
                    System.out.println("[PARSER] Detectado Adoptium (Opcion 2)");
                    return 2;
                } else if (rutaLeida.equals(this.graal)) {
                    System.out.println("[PARSER] Detectado Graal (Opcion 3)");
                    return 3;
                } else {
                    System.out.println("[PARSER] Detectado Java Standard o Desconocido (Opcion 1)");
                    return 1;
                }
            }
        }
        System.out.println("[PARSER] Java no encontrado, devolviendo default: 1");
        return 1;
    }

    public void actualizarVariablesBat(int opcionJava, int ramMB, String usuario) {

        String rutaFinal = this.java;
        switch (opcionJava) {
            case 2:
                rutaFinal = this.adoptium;
                break;
            case 3:
                rutaFinal = this.graal;
                break;
            default:
                rutaFinal = this.java;
                break;
        }

        String ramString = ramMB + "m";
        boolean hayCambios = false;

        for (int i = 0; i < this.textoBat.size(); i++) {
            String linea = this.textoBat.get(i).trim();

            if (linea.startsWith("set JAVA_EXEC=")) {
                this.textoBat.set(i, "set JAVA_EXEC=" + rutaFinal);
                hayCambios = true;
            } else if (linea.startsWith("set MAX_RAM=")) {
                this.textoBat.set(i, "set MAX_RAM=" + ramString);
                hayCambios = true;
            } else if (linea.startsWith("set USUARIO=")) {
                this.textoBat.set(i, "set USUARIO=" + usuario);
                hayCambios = true;
            }
        }

        if (hayCambios) {
            guardarCambiosBat();
        }
    }

    private void guardarCambiosBat() {
        try {
            String rutaBase = System.getProperty("user.dir");
            java.io.File archivoBat = new java.io.File(rutaBase, "start.bat");

            String contenidoFinal = String.join("\r\n", this.textoBat);
            FileWriter escritor = new FileWriter(archivoBat, false);
            escritor.write(contenidoFinal);
            escritor.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //COSAS OPCIONES
    public void optionsReader() {
        List<String> lineas = new ArrayList<>();
        String rutaBase = System.getProperty("user.dir");
        java.io.File archivoOpciones = new java.io.File(rutaBase, "/portable/.minecraft/options.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(archivoOpciones))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.textoOpciones = lineas;
    }

    public boolean getFullscreen() {
        for (String linea : this.textoOpciones) {
            String l = linea.trim();
            if (l.startsWith("fullscreen:")) {
                String fullscreen = l.substring("fullscreen:".length());
                System.out.println("[PARSER OP] Opcion FULLSCREEN: " + fullscreen);
                return this.fullscreen = Boolean.parseBoolean(fullscreen);
            }
        }
        System.out.println("Eiñ?¿ Fullscreen");

        return false;
    }

    public boolean getUseVbo() {
        for (String linea : this.textoOpciones) {
            String l = linea.trim();
            if (l.startsWith("useVbo:")) {
                String useVbo = l.substring("useVbo:".length());
                System.out.println("[PARSER OP] Opcion useVbo: " + useVbo);
                return this.useVbo = Boolean.parseBoolean(useVbo);
            }
        }
        System.out.println("Eiñ?¿ useVbo");
        return false;
    }

    public boolean getAdvancedItemTooltips() {
        for (String linea : this.textoOpciones) {
            String l = linea.trim();
            if (l.startsWith("advancedItemTooltips:")) {
                String advancedItemTooltips = l.substring("advancedItemTooltips:".length());
                System.out.println("[PARSER OP] Opcion advancedItemTooltips: " + advancedItemTooltips);
                return this.advancedItemTooltips = Boolean.parseBoolean(advancedItemTooltips);
            }
        }
        System.out.println("Eiñ?¿ advancedItemTooltips");
        return false;
    }

    public int getLang() {
        for (String linea : this.textoOpciones) {
            String l = linea.trim();
            if (l.startsWith("lang:")) {
                String langTexto = l.substring("lang:".length()).trim();

                System.out.println("[PARSER OP] Texto idioma encontrado: " + langTexto);

                if (langTexto.equals("es_es")) {
                    return 0;
                } else {
                    return 1;
                }
            }
        }
        return 0;
    }

    public int getGuiScale() {
        for (String linea : this.textoOpciones) {
            String l = linea.trim();
            if (l.startsWith("guiScale:")) {
                try {
                    String val = l.substring("guiScale:".length());
                    System.out.println("[PARSER OP] guiScale:" + val);
                    return this.guiScale = Integer.parseInt(val);
                } catch (NumberFormatException e) {
                    return 1;
                }
            }
        }
        return 1;
    }

    public void actualizarOpciones(boolean opcionFullscreen, boolean opcionUseVbo, boolean opcionAdvancedItemTooltips, int opcionLang, int guiScale) {

        this.fullscreen = opcionFullscreen;
        this.useVbo = opcionUseVbo;
        this.advancedItemTooltips = opcionAdvancedItemTooltips;
        this.lang = opcionLang;
        this.guiScale = guiScale;

        for (int i = 0; i < this.textoOpciones.size(); i++) {
            String linea = this.textoOpciones.get(i).trim();

            if (linea.startsWith("fullscreen:")) {
                this.textoOpciones.set(i, "fullscreen:" + fullscreen);

            } else if (linea.startsWith("useVbo:")) {
                this.textoOpciones.set(i, "useVbo:" + useVbo);

            } else if (linea.startsWith("advancedItemTooltips:")) {
                this.textoOpciones.set(i, "advancedItemTooltips:" + advancedItemTooltips);

            } else if (linea.startsWith("guiScale:")) {
                this.textoOpciones.set(i, "guiScale:" + guiScale);

            } else if (linea.startsWith("lang:")) {
                if (opcionLang == 0) {
                    this.textoOpciones.set(i, "lang:es_es");
                } else {
                    this.textoOpciones.set(i, "lang:en_us");
                }
            }
        }

        guardarCambiosOpciones();

    }

    private void guardarCambiosOpciones() {
        try {
            String rutaBase = System.getProperty("user.dir");
            java.io.File archivoOpciones = new java.io.File(rutaBase, "/portable/.minecraft/options.txt");

            String contenidoFinal = String.join("\r\n", this.textoOpciones);
            FileWriter escritor = new FileWriter(archivoOpciones, false);
            escritor.write(contenidoFinal);
            escritor.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //COSAS OPTIFINE
    public void optionsOfReader() {
        List<String> lineas = new ArrayList<>();
        String rutaBase = System.getProperty("user.dir");
        java.io.File archivoOpcionesOf = new java.io.File(rutaBase, "/portable/.minecraft/optionsof.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(archivoOpcionesOf))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.textoOpcionesOf = lineas;
    }

    public boolean getOfFastRender() {
        for (String linea : this.textoOpcionesOf) {
            String l = linea.trim();
            if (l.startsWith("ofFastRender:")) {
                String val = l.substring("ofFastRender:".length());
                System.out.println("[PARSER OF] FastRender: " + val);
                return this.ofFastRender = Boolean.parseBoolean(val);
            }
        }
        return false;
    }

    public boolean getOfSmartAnimations() {
        for (String linea : this.textoOpcionesOf) {
            String l = linea.trim();
            if (l.startsWith("ofSmartAnimations:")) {
                String val = l.substring("ofSmartAnimations:".length());
                System.out.println("[PARSER OF] SmartAnimations: " + val);
                return this.ofSmartAnimations = Boolean.parseBoolean(val);
            }
        }
        return false;
    }

    public boolean getOfShowFps() {
        for (String linea : this.textoOpcionesOf) {
            String l = linea.trim();
            if (l.startsWith("ofShowFps:")) {
                String val = l.substring("ofShowFps:".length());
                System.out.println("[PARSER OF] ShowFps: " + val);
                return this.ofShowFps = Boolean.parseBoolean(val);
            }
        }
        return false;
    }

    public int getOfDynamicLights() {
        for (String linea : this.textoOpcionesOf) {
            String l = linea.trim();
            if (l.startsWith("ofDynamicLights:")) {
                try {
                    String val = l.substring("ofDynamicLights:".length());
                    System.out.println("[PARSER OF] DynamicLights: " + val);
                    return this.ofDynamicLights = Integer.parseInt(val);
                } catch (NumberFormatException e) {
                    return 3;
                }
            }
        }
        return 3;
    }

    public int getOfConnectedTextures() {
        for (String linea : this.textoOpcionesOf) {
            String l = linea.trim();
            if (l.startsWith("ofConnectedTextures:")) {
                try {
                    String val = l.substring("ofConnectedTextures:".length());
                    System.out.println("[PARSER OF] ConnectedTextures: " + val);
                    return this.ofConnectedTextures = Integer.parseInt(val);
                } catch (NumberFormatException e) {
                    return 1;
                }
            }
        }
        return 1;
    }

    public void actualizarOpcionesOf(boolean fastRender, boolean smartAnimations, boolean showFps, int dynamicLights, int connectedTextures) {

        this.ofFastRender = fastRender;
        this.ofSmartAnimations = smartAnimations;
        this.ofShowFps = showFps;
        this.ofDynamicLights = dynamicLights;
        this.ofConnectedTextures = connectedTextures;

        if (this.textoOpcionesOf == null || this.textoOpcionesOf.isEmpty()) {
            System.out.println("[PARSER OF] Lista vacia, no se puede guardar.");
            return;
        }

        for (int i = 0; i < this.textoOpcionesOf.size(); i++) {
            String linea = this.textoOpcionesOf.get(i).trim();

            if (linea.startsWith("ofFastRender:")) {
                this.textoOpcionesOf.set(i, "ofFastRender:" + ofFastRender);
            } else if (linea.startsWith("ofSmartAnimations:")) {
                this.textoOpcionesOf.set(i, "ofSmartAnimations:" + ofSmartAnimations);
            } else if (linea.startsWith("ofShowFps:")) {
                this.textoOpcionesOf.set(i, "ofShowFps:" + ofShowFps);
            } else if (linea.startsWith("ofDynamicLights:")) {
                this.textoOpcionesOf.set(i, "ofDynamicLights:" + ofDynamicLights);
            } else if (linea.startsWith("ofConnectedTextures:")) {
                this.textoOpcionesOf.set(i, "ofConnectedTextures:" + ofConnectedTextures);
            }
        }
        guardarCambiosOpcionesOf();
    }

    private void guardarCambiosOpcionesOf() {
        try {
            String rutaBase = System.getProperty("user.dir");
            java.io.File archivoOpcionesOf = new java.io.File(rutaBase, "/portable/.minecraft/optionsof.txt");

            // Usamos join con salto de línea
            String contenidoFinal = String.join("\r\n", this.textoOpcionesOf);

            FileWriter escritor = new FileWriter(archivoOpcionesOf, false);
            escritor.write(contenidoFinal);
            escritor.close();
            System.out.println("[PARSER OF] Cambios guardados en optionsof.txt");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
