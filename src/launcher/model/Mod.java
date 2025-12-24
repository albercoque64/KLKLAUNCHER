package launcher.model;

import java.util.ArrayList;
import java.util.List;

public class Mod {

    private String modId;
    private String modName;
    private String version;
    private String minecraftVersions;
    private String ruta = System.getProperty("user.dir") + "/portable/.minecraft/mods/";
    private String nombreArchivo = "";
    private List<String> dependencies;

    public Mod() {
        modId = "";
        modName = "";
        version = "";
        minecraftVersions = "";
        dependencies = new ArrayList();

    }

    @Override
    public String toString() {

        return "Mod {"
                + "ID='" + modId + '\''
                + ", Nombre='" + modName + '\''
                + ", Version='" + version + '\''
                + ", MC='" + minecraftVersions + '\''
                + ", Dependencias=" + dependencies + '\''
                + ", Nombre Archvio=" + nombreArchivo
                + '}';
    }

    public String toStringBonito() {

        return "Mod {"
                + "ID='" + modId + '\''
                + ", Nombre='" + modName + '\''
                + ", Version='" + version + '\''
                + ", MC='" + minecraftVersions + '\''
                + ", Dependencias=" + dependencies + '\''
                + '}';
    }

    public void setModId(String modId) {
        this.modId = modId;
    }

    public void setModName(String modName) {
        this.modName = modName;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setMinecraftVersions(String minecraftVersions) {
        this.minecraftVersions = minecraftVersions;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = ruta + nombreArchivo;
    }

    public void setDependencies(List<String> dependencies) {
        this.dependencies = dependencies;
    }

    public String getModId() {
        return modId;
    }

    public String getModName() {
        return modName;
    }

    public String getVersion() {
        return version;
    }

    public String getMinecraftVersions() {
        return minecraftVersions;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public List<String> getDependencies() {
        return dependencies;
    }

}
