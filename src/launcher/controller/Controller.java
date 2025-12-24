package launcher.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import launcher.model.JarParser;
import launcher.model.Mod;
import launcher.model.TextParser;
import launcher.utils.JModButton;
import launcher.view.PrincipalView;
import launcher.view.mods.ModsConfig;
import launcher.view.mods.ModsInfo;
import launcher.view.mods.ModsView;
import launcher.view.options.Options;

public class Controller {

    private PrincipalView view;
    private TextParser model;
    private Options options;
    private ModsConfig modConfig;
    private ModsInfo modInfo;
    private ModsView modView;

    KLKLauncherActionListener onlyModelActionListener = new KLKLauncherActionListener();
    ModManager modGestionerListener = new ModManager();

    public Controller(PrincipalView view, Options options, TextParser textParser, ModsConfig modConfig, ModsInfo modInfo, ModsView modView) {

        this.view = view;
        this.model = textParser;
        this.options = options;

        this.modConfig = modConfig;
        this.modInfo = modInfo;
        this.modView = modView;

        this.modView.setActionListener(onlyModelActionListener);
        this.view.setActionListeners(onlyModelActionListener);
        this.options.setActionListeners(onlyModelActionListener);

        inicializacion();

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

        this.modConfig.setMods(inicializadorMods());
    }

    class KLKLauncherActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String command = e.getActionCommand();
            System.out.println(command);
            switch (command) {

                case "JUGAR":

                    model.actualizarVariablesBat(view.getJavaVersion(), view.getRamEnMB(), view.getUsuario());

                    lanzarJuego();

                    break;
                //MODS
                case "CARPETAMODS":
                    
                    try {
                    java.io.File carpetaMods = new java.io.File(System.getProperty("user.dir") + "\\portable\\.minecraft\\mods");

                    if (!carpetaMods.exists()) {
                        carpetaMods.mkdirs();
                    }
                    java.awt.Desktop.getDesktop().open(carpetaMods);

                    break;

                } catch (IOException ex) {
                    javax.swing.JOptionPane.showMessageDialog(view, "No se pudo abrir la carpeta: " + ex.getMessage());
                }

                break;

                case "MODS":
                    modView.setVisible(true);
                    break;

                case "MODINFO":
                    modView.setVisible(false);
                    modInfo.setVisible(true);
                    break;

                case "MODCONFIG":
                    modView.setVisible(false);
                    modConfig.setVisible(true);
                    break;
                //OPCIONES
                case "OPCIONES":

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
                    options.setVisible(true);

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

            System.out.println(e.getSource());
            JModButton fuente = ((JModButton) e.getSource());

            File ficherin = new File(fuente.getMod().getNombreArchivo());

            File ficheroRenombrado;
            if (ficherin.getName().endsWith(".jaroff")) {
                ficheroRenombrado = new File(fuente.getMod().getNombreArchivo().substring(0, fuente.getMod().getNombreArchivo().length() - ".jaroff".length()) + ".jar");
            } else {
                ficheroRenombrado = new File(fuente.getMod().getNombreArchivo().substring(0, fuente.getMod().getNombreArchivo().length() - ".jar".length()) + ".jaroff");
            }

            ficherin.renameTo(ficheroRenombrado);
            fuente.getMod().setNombreArchivo(ficheroRenombrado.getName());
            fuente.setColor();

        }
    }

    private void lanzarJuego() {

        view.apagarJugar();
        try {
            String usuario = view.getOptionPanel().getUsuario();
            if (usuario.isEmpty()) {
                usuario = "Steve";
            }
            int ram = view.getOptionPanel().getRamEnMB();

            try {
                launcher.utils.DiscordHandler.actualizarEstado("Jugando Minecraft 1.12.2", "Usuario: " + usuario);
            } catch (Exception e) {
            }

            view.mostrarConsola();
            view.getConsolePanel().log("--- INICIANDO KLK LAUNCHER ---");
            view.getConsolePanel().log("> Usuario: " + usuario);
            view.getConsolePanel().log("> RAM: " + ram + "MB");

            String rutaBase = System.getProperty("user.dir");
            java.io.File scriptBat = new java.io.File(rutaBase, "start.bat");

            if (!scriptBat.exists()) {
                view.getConsolePanel().log("ERROR: No encuentro start.bat en: " + scriptBat.getAbsolutePath());
                return;
            }

            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "\"" + scriptBat.getAbsolutePath() + "\"");
            pb.directory(new java.io.File(rutaBase));
            pb.redirectErrorStream(true);

            Process proceso = pb.start();

            Thread hiloLector = new Thread(() -> {
                try (java.io.BufferedReader reader = new java.io.BufferedReader(
                        new java.io.InputStreamReader(proceso.getInputStream()))) {

                    String linea;
                    while ((linea = reader.readLine()) != null) {
                        view.getConsolePanel().log(linea);
                    }

                    proceso.waitFor();

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
        view.setJavaVersion(model.getJavaOpcion());
        view.setRAM(model.getRam());
        view.setUsuario(model.getUsuario());
    }

    public void setModListener() {
        this.modConfig.setActionListener(modGestionerListener);
    }

    public List<JModButton> inicializadorMods() {
        JarParser jarParser = new JarParser();
        List<Mod> modsEncontrados = jarParser.escanearMods();
        List<JModButton> botones = new ArrayList();

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
