package adlock.addetect.control.decompile.xml;

import java.util.ArrayList;

public class Node {
    public static int ROOT = 1;
    ArrayList<Attribute> attrs = new ArrayList();
    int index;
    int linenumber;
    String name;
    int namespaceLineNumber;
    String namespacePrefix;
    String namespaceURI;

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getLinenumber() {
        return this.linenumber;
    }

    public void setLinenumber(int linenumber) {
        this.linenumber = linenumber;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespacePrefix() {
        return this.namespacePrefix;
    }

    public void setNamespacePrefix(String namespacePrefix) {
        this.namespacePrefix = namespacePrefix;
    }

    public String getNamespaceURI() {
        return this.namespaceURI;
    }

    public void setNamespaceURI(String namespaceURI) {
        this.namespaceURI = namespaceURI;
    }

    public int getNamespaceLineNumber() {
        return this.namespaceLineNumber;
    }

    public void setNamespaceLineNumber(int namespaceLineNumber) {
        this.namespaceLineNumber = namespaceLineNumber;
    }

    public ArrayList<Attribute> getAttrs() {
        return this.attrs;
    }

    public void addAttribute(Attribute attr) {
        this.attrs.add(attr);
    }

    public void setAttrs(ArrayList<Attribute> attrs) {
        this.attrs = attrs;
    }
}
