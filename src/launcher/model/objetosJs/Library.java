package launcher.model.objetosJs;

import com.icecold.javascript.autoload.AbstractJavaScriptObject;
import com.icecold.javascript.autoload.JavaScriptAttributeParser;
import com.icecold.javascript.autoload.JavaScriptDescriptableAttribute;
import java.util.List;
import java.util.Optional;

public class Library extends AbstractJavaScriptObject {

    @JavaScriptDescriptableAttribute(name = "downloads", parser = ColeccionDeConstructores.DownloadParser.class)
    public Download downloads;

    @JavaScriptDescriptableAttribute(name = "rules", parser = ColeccionDeConstructores.RulesListParser.class)
    public List<Rules> rules;

    public Library() {
    }

    public static class Download extends AbstractJavaScriptObject {

        @JavaScriptDescriptableAttribute(name = "artifact", parser = ColeccionDeConstructores.ArtifactParser.class)
        public Artifact artifacts;

        @JavaScriptDescriptableAttribute(name = "rules", parser = ColeccionDeConstructores.RulesParser.class)
        public Artifact rules;
        public Download() {
        }

    }

    public static class Artifact extends AbstractJavaScriptObject {

        @JavaScriptDescriptableAttribute(name = "path", parser = JavaScriptAttributeParser.StringParser.class)
        public String path;

        @JavaScriptDescriptableAttribute(name = "url", parser = JavaScriptAttributeParser.OptionalStringParser.class)
        public Optional<String> url = Optional.empty();

        public Artifact() {
        }

    }

    public static class Rules extends AbstractJavaScriptObject {

        @JavaScriptDescriptableAttribute(name = "os", parser = ColeccionDeConstructores.OsParser.class)
        public Os os;

        public Rules() {
        }
    }

    public static class Os extends AbstractJavaScriptObject {

        @JavaScriptDescriptableAttribute(name = "name", parser = JavaScriptAttributeParser.StringParser.class)
        public String name;

        public Os() {
        }
    }
}
