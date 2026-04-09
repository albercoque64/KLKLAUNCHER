package launcher.model.objetosJs;

import com.icecold.javascript.autoload.AbstractJavaScriptObject;
import com.icecold.javascript.autoload.JavaScriptAttributeParser;
import com.icecold.javascript.autoload.JavaScriptDescriptableAttribute;
import java.util.List;

public class ObjetoDeInstalacion extends AbstractJavaScriptObject {

    @JavaScriptDescriptableAttribute(name = "version", parser = JavaScriptAttributeParser.StringParser.class)
    public String version;

    @JavaScriptDescriptableAttribute(name = "minecraft", parser = JavaScriptAttributeParser.StringParser.class)
    public String minecraft;

    @JavaScriptDescriptableAttribute(name = "libraries", parser = ColeccionDeConstructores.LibraryListParser.class)
    public List<Library> libraries;

    @JavaScriptDescriptableAttribute(name = "rules", parser = ColeccionDeConstructores.RulesListParser.class)
    public List<Library> rules;

    public ObjetoDeInstalacion() {
    }

}
