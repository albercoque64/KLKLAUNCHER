package launcher.model.objetosJs;

import com.icecold.javascript.autoload.JavaScriptAttributeParser;
import com.icecold.javascript.particulars.JavaScriptObject;
import launcher.model.objetosJs.Library.Artifact;
import launcher.model.objetosJs.Library.Download;
import launcher.model.objetosJs.Library.Os;
import launcher.model.objetosJs.Library.Rules;

public class ColeccionDeConstructores {

    public static class LibraryParser extends JavaScriptAttributeParser.ObjectParser<Library> {

        @Override
        public Library getNew() {
            return new Library();
        }
    }

    public static class DownloadParser extends JavaScriptAttributeParser.ObjectParser<Library.Download> {

        @Override
        public Download getNew() {
            return new Download();
        }
    }

    public static class ArtifactParser extends JavaScriptAttributeParser.ObjectParser<Library.Artifact> {

        @Override
        public Artifact getNew() {
            return new Artifact();
        }
    }

    public static class RulesParser extends JavaScriptAttributeParser.ObjectParser<Library.Rules> {

        @Override
        public Rules getNew() {
            return new Rules();
        }
    }

    public static class RulesListParser extends JavaScriptAttributeParser.ListParser<Library.Rules> {

        public RulesListParser() {
            super.mapJavaScriptTypeToParser(JavaScriptObject.class, new RulesParser());
        }
    }

    public static class OsParser extends JavaScriptAttributeParser.ObjectParser<Library.Os> {

        @Override
        public Os getNew() {
            return new Os();
        }
    }

    public static class LibraryListParser extends JavaScriptAttributeParser.ListParser<Library> {

        public LibraryListParser() {
            super.mapJavaScriptTypeToParser(JavaScriptObject.class, new LibraryParser());
        }
    }

}
