package launcher.main;

import launcher.controller.Controller;
import launcher.model.TextParser;
import launcher.view.PrincipalView;
import launcher.view.launcher.InstalarView;
import launcher.view.launcher.InstanciasConfig;
import launcher.view.launcher.LauncherView;
import launcher.view.mods.ModsConfig;
import launcher.view.mods.ModsInfo;
import launcher.view.mods.ModsView;
import launcher.view.options.Options;

public class MainLauncher {

    public static void main(String[] args) {

        System.setProperty("sun.java2d.uiScale", "1.0");

        Options opciones = new Options();
        LauncherView launcher = new LauncherView();
        ModsView modsView = new ModsView();
        ModsInfo modsInfo = new ModsInfo();
        ModsConfig mods = new ModsConfig();
        InstalarView instalation = new InstalarView();
        PrincipalView ventana = new PrincipalView();
        InstanciasConfig instancias = new InstanciasConfig();

        TextParser parser = new TextParser();

        Controller controller = new Controller(ventana, opciones, parser, mods, modsInfo, modsView, launcher, instalation, instancias);

        controller.setModListener();
        mods.setModsVista();

    }

}
