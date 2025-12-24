package launcher.main;

import launcher.controller.Controller;
import launcher.model.TextParser;
import launcher.view.PrincipalView;
import launcher.view.mods.ModsConfig;
import launcher.view.mods.ModsInfo;
import launcher.view.mods.ModsView;
import launcher.view.options.Options;

public class MainLauncher {

    public static void main(String[] args) {

        System.setProperty("sun.java2d.uiScale", "1.0");

        TextParser parser = new TextParser();
        PrincipalView ventana = new PrincipalView();
        Options opciones = new Options();

        ModsView modsView = new ModsView();
        ModsInfo modsInfo = new ModsInfo();
        ModsConfig mods = new ModsConfig();

        Controller controller = new Controller(ventana, opciones, parser, mods, modsInfo, modsView);

        controller.setModListener();
        mods.setModsVista();

    }

}
