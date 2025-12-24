package launcher.utils;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

public class DiscordHandler {

    private static final String APP_ID = "1448261765375725659";

    private static DiscordRPC lib;
    private static DiscordRichPresence presence;

    public static void conectar() {
        lib = DiscordRPC.INSTANCE;
        String applicationId = APP_ID;
        String steamId = "";

        DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.ready = (user) -> System.out.println("Discord conectado!");

        lib.Discord_Initialize(applicationId, handlers, true, steamId);

        presence = new DiscordRichPresence();
        presence.startTimestamp = System.currentTimeMillis() / 1000;

        // CONFIGURACIÓN DE LA IMAGEN
        presence.largeImageKey = "klklauncher";
        presence.largeImageText = "KLK Launcher v1.0"; 

        actualizarEstado("En el Menú", "Configurando opciones...");

        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                lib.Discord_RunCallbacks();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
            }
        }).start();
    }

    public static void actualizarEstado(String lineaArriba, String lineaAbajo) {
        presence.details = lineaArriba; 
        presence.state = lineaAbajo;   
        lib.Discord_UpdatePresence(presence);
    }

    public static void desconectar() {
        lib.Discord_Shutdown();
    }
}
