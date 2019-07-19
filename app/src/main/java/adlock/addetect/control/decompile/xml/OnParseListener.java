package adlock.addetect.control.decompile.xml;

public interface OnParseListener {
    void endDoc() throws Exception;

    void endNode(Node node);

    void nodeValue(int i, String str, String str2);

    void startDoc(String str);

    void startNode(Node node);
}
