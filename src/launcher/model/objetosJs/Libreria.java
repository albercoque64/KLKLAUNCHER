package launcher.model.objetosJs;

public class Libreria {

    private String path;
    private String url;
    private String so;

    public Libreria(String path, String url, String so) {
        this.path = path;
        this.url = url;
        this.so = so;
    }

    public String getPath() {
        return path;
    }

    public String getUrl() {
        return url;
    }

    public String getSo() {
        return so;
    }
}
