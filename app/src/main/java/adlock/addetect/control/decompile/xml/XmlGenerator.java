package adlock.addetect.control.decompile.xml;

import java.io.FileOutputStream;
import java.util.ArrayList;

public class XmlGenerator implements OnParseListener {
    int cl = 1;
    Node currentNode = null;
    String tag = getClass().getSimpleName();
    StringBuffer xml = new StringBuffer();
    String xmlFile;

    public void startDoc(String xmlFile) {
        this.xmlFile = xmlFile;
        this.xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
    }

    public void startNode(Node node) {
        if (node != null) {
            this.currentNode = node;
            if (this.cl == node.getLinenumber()) {
                this.xml.append("\n");
            } else {
                while (this.cl < node.getLinenumber()) {
                    ln();
                }
            }
            this.xml.append("<" + node.getName());
            if (node.getIndex() == Node.ROOT) {
                this.xml.append(" xmlns:" + node.getNamespacePrefix() + "=\"" + node.getNamespaceURI() + "\"");
            }
            ArrayList<Attribute> attrs = node.getAttrs();
            if (attrs.size() == 0) {
                this.xml.append(">");
                ln();
                return;
            }
            for (int i = 0; i < attrs.size(); i++) {
                Attribute attr = attrs.get(i);
                ln();
                this.xml.append(" " + attr.getName() + "=\"" + attr.getValue() + "\"");
            }
            this.xml.append(">");
        }
    }

    public void nodeValue(int lineNumber, String name, String value) {
    }

    public void endNode(Node node) {
        if (this.cl == node.getLinenumber()) {
            this.xml.append("\n");
        } else if (this.cl < node.getLinenumber()) {
            ln();
        }
        if (this.currentNode.getName().equals(node.getName())) {
            int index = this.xml.lastIndexOf("\"");
            if (index != -1) {
                this.xml.delete(index + 1, this.xml.length());
                this.xml.append("/>");
                return;
            }
            this.xml.append("</" + node.getName() + ">");
            return;
        }
        this.xml.append("</" + node.getName() + ">");
    }

    public void endDoc() throws Exception {
        if (this.xmlFile != null && !this.xmlFile.isEmpty()) {
            FileOutputStream out = new FileOutputStream(this.xmlFile + ".xml");
            out.write(this.xml.toString().getBytes());
            out.flush();
            out.close();
        }
    }

    public String getXml() {
        return this.xml.toString();
    }

    private void ln() {
        this.cl++;
        this.xml.append("\n");
    }
}
