package launcher.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import launcher.model.JarParser;
import launcher.model.Mod;
import launcher.model.MotorLanzamiento;
import launcher.model.TextParser;
import launcher.model.VersionManager;
import launcher.utils.JModButton;
import launcher.view.PrincipalView;
import launcher.view.launcher.InstalarView;
import launcher.view.launcher.InstanciasConfig;
import launcher.view.launcher.LauncherView;
import launcher.view.mods.ModsConfig;
import launcher.view.mods.ModsInfo;
import launcher.view.mods.ModsView;
import launcher.view.options.Options;

public class Controller {

    //VISTAS
    private PrincipalView view;
    private Options options;
    private LauncherView launcherView;
    private ModsConfig modConfig;
    private ModsInfo modInfo;
    private ModsView modView;
    private InstalarView instalation;
    private InstanciasConfig instanciasConfig;

    //MODELO
    private TextParser model;

    //CLASES EMPOTRADAS
    KLKLauncherActionListener onlyModelActionListener = new KLKLauncherActionListener();
    ModManager modGestionerListener = new ModManager();

    public Controller(PrincipalView view, Options options, TextParser textParser, ModsConfig modConfig, ModsInfo modInfo, ModsView modView, LauncherView launcherView,
            InstalarView instalation, InstanciasConfig instanciasConfig) {

        this.view = view;
        this.model = textParser;
        this.options = options;
        this.launcherView = launcherView;

        this.instalation = instalation;
        this.instanciasConfig = instanciasConfig;

        this.modConfig = modConfig;
        this.modInfo = modInfo;
        this.modView = modView;

        this.modView.setActionListener(onlyModelActionListener);
        this.view.setActionListeners(onlyModelActionListener);
        this.options.setActionListeners(onlyModelActionListener);
        this.launcherView.setActionListener(onlyModelActionListener);
        this.instalation.setActionListener(onlyModelActionListener);
        this.instanciasConfig.setActionListener(onlyModelActionListener);

        try {

            String rutaBase = System.getProperty("user.dir");
            String rutaTmp = rutaBase + "\\portable\\.minecraft\\tmp";
            java.io.File carpetaTmp = new java.io.File(rutaTmp);

            if (!carpetaTmp.exists()) {
                carpetaTmp.mkdirs();
            }

            System.setProperty("jna.tmpdir", rutaTmp);
            System.setProperty("java.io.tmpdir", rutaTmp);

            launcher.utils.DiscordHandler.conectar();

        } catch (Throwable e) {
            System.err.println("No se pudo conectar a Discord (¿Falta la librería?): " + e.getMessage());
        }

        inicializacion();
    }

    class KLKLauncherActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String command = e.getActionCommand();
            System.out.println(command);
            switch (command) {

                case "JUGAR":

                    lanzarJuego();

                    guardalizador();

                    break;

                //LAUNCHER    
                case "LAUNCHER":
                    launcherView.setPadre(view);
                    launcherView.setVisible(true);

                    break;

                case "INSTALAR":
                    instalation.setPadre(view);
                    instalation.setVisible(true);

                    break;

                case "PERFILES":

                    java.io.File carpetaInstancias = new java.io.File(System.getProperty("user.dir") + "\\portable\\instancias");
                    java.util.List<String> nombresInstancias = new java.util.ArrayList<>();

                    if (carpetaInstancias.exists() && carpetaInstancias.isDirectory()) {
                        for (java.io.File f : carpetaInstancias.listFiles()) {
                            if (f.isDirectory()) {
                                nombresInstancias.add(f.getName());
                            }
                        }
                    }
                    instanciasConfig.setPadre(view);
                    instanciasConfig.setInstancias(nombresInstancias);
                    instanciasConfig.setActionListener(this);
                    instanciasConfig.setVisible(true);

                    break;

                case "CONFIRMARELIMINARINSTANCIA":

                    JButton botonPulsado = (javax.swing.JButton) e.getSource();
                    String instanciaAEliminar = botonPulsado.getText();

                    int confirmacion1 = javax.swing.JOptionPane.showConfirmDialog(
                            null,
                            "¿Estás seguro de que quieres eliminar la instancia '" + instanciaAEliminar + "'?\nSe borrarán todos sus mods, opciones y mundos.",
                            "Eliminar Instancia",
                            javax.swing.JOptionPane.YES_NO_OPTION,
                            javax.swing.JOptionPane.WARNING_MESSAGE
                    );

                    if (confirmacion1 == javax.swing.JOptionPane.YES_OPTION) {

                        int confirmacion2 = javax.swing.JOptionPane.showConfirmDialog(
                                null,
                                "Esta acción NO se puede deshacer. ¿Borrar definitivamente?",
                                "Último aviso",
                                javax.swing.JOptionPane.YES_NO_OPTION,
                                javax.swing.JOptionPane.ERROR_MESSAGE
                        );

                        if (confirmacion2 == javax.swing.JOptionPane.YES_OPTION) {

                            String rutaInstancia = System.getProperty("user.dir") + "\\portable\\instancias\\" + instanciaAEliminar;
                            File carpetaInstancia = new java.io.File(rutaInstancia);

                            boolean exito = instanciasConfig.borrarDirectorio(carpetaInstancia);

                            if (exito) {
                                javax.swing.JOptionPane.showMessageDialog(null, "Instancia eliminada.");
                                launcherView.setVisible(false);
                                java.awt.Window ventana = javax.swing.SwingUtilities.getWindowAncestor(botonPulsado);
                                if (ventana != null) {
                                    ventana.setVisible(false);
                                }

                            } else {
                                javax.swing.JOptionPane.showMessageDialog(null, "Error al borrar la instancia. Puede que algún archivo esté abierto.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                    view.setPerfil(model.escanearInstancias(), model.leerAtributo("instancia"));

                    break;

                case "JAR":

                    VersionManager.instalador(view, instalation.getNombreInstancia());
                    view.revalidate();
                    view.repaint();
                    view.setPerfil(model.escanearInstancias(), model.leerAtributo("instancia"));
                    instalation.setPadre(view);
                    instalation.setVisible(false);
                    launcherView.setVisible(false);

                    break;

                case "CANCELAR":
                    instalation.setPadre(view);
                    instalation.limpiarCampo();
                    instalation.setVisible(false);

                    break;

                //MODS
                case "CARPETAMODS":
                    
                    try {
                    String instanciaActual = view.getPerfil();

                    String rutaMods = System.getProperty("user.dir") + "\\portable\\instancias\\" + instanciaActual + "\\mods";
                    java.io.File carpetaMods = new java.io.File(rutaMods);

                    if (!carpetaMods.exists()) {
                        carpetaMods.mkdirs();
                    }

                    java.awt.Desktop.getDesktop().open(carpetaMods);

                } catch (IOException ex) {
                    javax.swing.JOptionPane.showMessageDialog(view, "No se pudo abrir la carpeta: " + ex.getMessage());
                }

                break;

                case "MODS":

                    String instanciaMods = view.getPerfil();

                    if (instanciaMods != null && !instanciaMods.isEmpty()) {

                        //BARRA
                        javax.swing.JDialog dialogoCarga = new javax.swing.JDialog(view, "Escaneando Mods", true); // true = bloquea la ventana principal
                        javax.swing.JProgressBar barraCarga = new javax.swing.JProgressBar(0, 100);
                        barraCarga.setStringPainted(true);

                        dialogoCarga.add(barraCarga);
                        dialogoCarga.setSize(300, 70);
                        dialogoCarga.setLocationRelativeTo(view); 

                        //WORKER
                        javax.swing.SwingWorker<java.util.List<launcher.utils.JModButton>, Integer> worker = new javax.swing.SwingWorker<java.util.List<launcher.utils.JModButton>, Integer>() {

                            @Override
                            protected java.util.List<launcher.utils.JModButton> doInBackground() throws Exception {

                                return inicializadorMods(instanciaMods, porcentaje -> publish(porcentaje));
                            }

                            //BARRITA CON ACTUALIZACIÓN DE PORCENTAJE
                            @Override
                            protected void process(java.util.List<Integer> chunks) {
                                int ultimoPorcentaje = chunks.get(chunks.size() - 1);
                                barraCarga.setValue(ultimoPorcentaje);
                            }

                            @Override
                            protected void done() {
                                try {
                                    java.util.List<launcher.utils.JModButton> modsCargados = get();

                                    modConfig.setMods(modsCargados);
                                    modConfig.setActionListener(modGestionerListener);
                                    modView.setPadre(view);

                                    dialogoCarga.dispose(); // CERRAMOS Y DESTRUIMOS LA VENTANA DE CARGA
                                    modView.setVisible(true);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    dialogoCarga.dispose();
                                }
                            }
                        };

                        worker.execute();

                        dialogoCarga.setVisible(true);

                    } else {
                        System.out.println("No se puede abrir: No hay instancia seleccionada.");
                    }

                    break;

                case "MODINFO":
                    modView.setVisible(false);
                    modInfo.setPadre(view);
                    modInfo.setVisible(true);
                    break;

                case "MODCONFIG":
                    modView.setVisible(false);
                    modConfig.setVisible(true);
                    modConfig.setPadre(view);
                    break;

                //OPCIONES
                case "OPCIONES":

                    String nuevaInstancia = view.getPerfil();

                    if (nuevaInstancia != null && !nuevaInstancia.isEmpty()) {

                        model.cargarOpcionesDeInstancia(nuevaInstancia);
                        model.escribitAtributo("instancia", nuevaInstancia);

                        options.setFullScreenButton(model.getFullscreen());
                        options.setVboButton(model.getUseVbo());
                        options.setAdvancedToolTipsButton(model.getAdvancedItemTooltips());
                        options.setLang(model.getLang());
                        options.setGuiScale(model.getGuiScale());

                        options.setOptifineHabilitado(model.hasOptifine());
                        options.setConnectedTextures(model.getOfConnectedTextures());
                        options.setFastRender(model.getOfFastRender());
                        options.setShowFps(model.getOfShowFps());
                        options.setDynamicLights(model.getOfDynamicLights());
                        options.setSmartAnimations(model.getOfSmartAnimations());
                        options.setPadre(view);
                        options.setVisible(true);
                    }
                    /*
                    options.setFullScreenButton(model.getFullscreen());
                    options.setVboButton(model.getUseVbo());
                    options.setAdvancedToolTipsButton(model.getAdvancedItemTooltips());
                    options.setLang(model.getLang());
                    options.setGuiScale(model.getGuiScale());
                    options.setConnectedTextures(model.getOfConnectedTextures());
                    options.setFastRender(model.getOfFastRender());
                    options.setShowFps(model.getOfShowFps());
                    options.setDynamicLights(model.getOfDynamicLights());
                    options.setSmartAnimations(model.getOfSmartAnimations());
                    
                     */
                    break;

                case "FULLSCREEN":

                    options.setFullScreenButton(!options.getFullScreenButton());

                    break;

                case "VBO":

                    options.setVboButton(!options.getVboButton());

                    break;

                case "ADVANCEDTOOLTIPS":

                    options.setAdvancedToolTipsButton(!options.getAdvancedToolTipsButton());

                    break;

                case "LANG":

                    break;

                //OF OPCIONES
                case "FASTRENDER":
                    options.setFastRender(!options.getFastRender());
                    break;

                case "SMARTANIMATIONS":
                    options.setSmartAnimations(!options.getSmartAnimations());
                    break;

                case "SHOWFPS":
                    options.setShowFps(!options.getShowFps());
                    break;

                case "GUARDAR":
                    model.actualizarOpciones(options.getFullScreenButton(), options.getVboButton(), options.getAdvancedToolTipsButton(), options.getLang(), options.getGuiScale());
                    model.actualizarOpcionesOf(options.getFastRender(), options.getSmartAnimations(), options.getShowFps(), options.getDynamicLights(), options.getConnectedTextures());
                    options.setVisible(false);
                    break;
                default:
                    break;

            }
        }
    }

    class ModManager implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            JModButton fuente = ((JModButton) e.getSource());

            String instanciaActual = view.getPerfil();
            String rutaMods = System.getProperty("user.dir") + "\\portable\\instancias\\" + instanciaActual + "\\mods\\";

            String nombreArchivoLimpio = new File(fuente.getMod().getNombreArchivo()).getName();

            File ficherin = new File(rutaMods + nombreArchivoLimpio);
            File ficheroRenombrado;

            if (nombreArchivoLimpio.endsWith(".jaroff")) {
                String nuevoNombre = nombreArchivoLimpio.replace(".jaroff", ".jar");
                ficheroRenombrado = new File(rutaMods + nuevoNombre);
            } else {
                String nuevoNombre = nombreArchivoLimpio.replace(".jar", ".jaroff");
                ficheroRenombrado = new File(rutaMods + nuevoNombre);
            }

            boolean exito = ficherin.renameTo(ficheroRenombrado);

            if (exito) {
                fuente.getMod().setNombreArchivo(ficheroRenombrado.getName());
                fuente.setColor();
                System.out.println("Mod actualizado con éxito: " + ficheroRenombrado.getName());
            } else {
                System.out.println("¡ERROR! No se pudo renombrar el archivo: " + ficherin.getAbsolutePath());
            }
        }
    }

    private void lanzarJuego() {

        view.apagarJugar();
        try {
            guardalizador();

            String usuario = view.getUsuario();
            if (usuario == null || usuario.isEmpty()) {
                usuario = "Steve";
            }
            int ram = view.getRamEnMB();

            try {
                launcher.utils.DiscordHandler.actualizarEstado("Jugando Minecraft 1.12.2", "Usuario: " + usuario);
            } catch (Exception e) {
            }

            view.mostrarConsola();
            view.getConsolePanel().log("--- INICIANDO KLK LAUNCHER ---");
            view.getConsolePanel().log("> Usuario: " + usuario);
            view.getConsolePanel().log("> RAM: " + ram + "MB");

            Thread hiloLector = new Thread(() -> {
                try {
                    MotorLanzamiento motor = new MotorLanzamiento();
                    Process proceso = motor.arrancarJuego(view.getPerfil(), view.getUsuario(), view.getRamEnMB(), view.getJavaVersion());

                    if (proceso != null) {
                        try (java.io.BufferedReader reader = new java.io.BufferedReader(
                                new java.io.InputStreamReader(proceso.getInputStream()))) {

                            String linea;
                            //ANTI RALLAMIENTOS
                            while (proceso.isAlive()) {
                                if (reader.ready()) {
                                    linea = reader.readLine();
                                    if (linea != null) {
                                        final String l = linea;
                                        javax.swing.SwingUtilities.invokeLater(() -> view.getConsolePanel().log(l));
                                    }
                                } else {
                                    Thread.sleep(100);
                                }
                            }
                        }
                    } else {
                        view.getConsolePanel().log("ERROR: No se pudo iniciar el proceso de Minecraft.");
                    }

                    try {
                        launcher.utils.DiscordHandler.actualizarEstado("En el Menú", "Descansando del cubo");
                    } catch (Exception e) {
                    }

                    view.getConsolePanel().log("--- JUEGO CERRADO ---");

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ie) {
                    }

                    javax.swing.SwingUtilities.invokeLater(() -> {
                        view.mostrarFoto();
                        view.encenderJugar();
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            hiloLector.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void inicializacion() {

        view.setJavaVersion(Integer.parseInt(model.leerAtributo("motor")));
        view.setRAM(Integer.parseInt(model.leerAtributo("ram")));
        view.setUsuario(model.leerAtributo("usuario"));
        view.setPerfil(model.escanearInstancias(), model.leerAtributo("instancia"));

    }

    public void guardalizador() {
        model.escribitAtributo("usuario", view.getUsuario());
        model.escribitAtributo("ram", String.valueOf(view.getRamEnMB()));
        model.escribitAtributo("instancia", view.getPerfil());
        model.escribitAtributo("motor", String.valueOf(view.getJavaVersion()));

    }

    public void setModListener() {
        this.modConfig.setActionListener(modGestionerListener);
    }

    private List<JModButton> inicializadorMods(String nombreInstancia, java.util.function.Consumer<Integer> actualizadorProgreso) {

        JarParser miParser = new JarParser();
        List<Mod> modsEncontrados = miParser.escanearMods(nombreInstancia, actualizadorProgreso);

        List<JModButton> botones = new ArrayList<>();

        if (modsEncontrados != null) {
            for (Mod mod : modsEncontrados) {
                System.out.println(mod);
                JModButton boton = new JModButton(mod);
                botones.add(boton);
            }
        }
        this.modInfo.setMods(modsEncontrados);
        return botones;
    }
}
