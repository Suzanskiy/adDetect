package adlock.addetect.control.decompile.xml;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import krow.dev.addetector.util.Utils;

public class XmlConverter implements Resource {
    byte[] buf_2 = new byte[2];
    byte[] buf_4 = new byte[4];
    int chunk_size;
    byte[] chunk_size_buf = new byte[4];
    byte[] chunk_type_buf = new byte[2];
    int header_size;
    byte[] header_size_buf = new byte[2];
    OnParseListener listener = null;
    int mNameSpaceLineNumber = 0;
    int mNameSpacePrefixIndex = -1;
    int mNameSpaceUriIndex = -1;
    int nodeIndex = -1;
    int package_count;
    ArrayList<Integer> resMap = new ArrayList();
    ArrayList<String> resStringPool = new ArrayList();
    ArrayList<String> stringPool = new ArrayList();
    String tag = "Android_BX2";

    public XmlConverter(OnParseListener listner) {
        this.listener = listner;
    }

    public void parse(byte[] binaryXml) throws Exception {
        BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(binaryXml));
        if (this.listener != null) {
            this.listener.startDoc(null);
        }
        parse(in);
    }

    public void parse(String bxFile) throws Exception {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(bxFile));
        if (this.listener != null) {
            this.listener.startDoc(bxFile);
        }
        parse(in);
    }

    private void parse(BufferedInputStream in) throws Exception {
        in.read(this.chunk_type_buf);
        if (Utils.toInt(this.chunk_type_buf, false) == 3) {
            in.read(this.header_size_buf);
            int header_size = Utils.toInt(this.header_size_buf, false);
            in.read(this.chunk_size_buf);
            int chunk_size = Utils.toInt(this.chunk_size_buf, false);
            in.read(this.chunk_type_buf);
            if (Utils.toInt(this.chunk_type_buf, false) == 1) {
                in.read(this.header_size_buf);
                header_size = Utils.toInt(this.header_size_buf, false);
                in.read(this.chunk_size_buf);
                chunk_size = Utils.toInt(this.chunk_size_buf, false);
                byte[] spBuf = new byte[(chunk_size - 8)];
                in.read(spBuf);
                parseStringPool(spBuf, header_size, chunk_size);
                in.read(this.chunk_type_buf);
            }
            if (Utils.toInt(this.chunk_type_buf, false) == RES_XML_RESOURCE_MAP_TYPE) {
                in.read(this.header_size_buf);
                header_size = Utils.toInt(this.header_size_buf, false);
                in.read(this.chunk_size_buf);
                chunk_size = Utils.toInt(this.chunk_size_buf, false);
                byte[] rmBuf = new byte[(chunk_size - 8)];
                in.read(rmBuf);
                parseResMapping(rmBuf, header_size, chunk_size);
                in.read(this.chunk_type_buf);
            }
            if (Utils.toInt(this.chunk_type_buf, false) == 256) {
                in.read(this.header_size_buf);
                header_size = Utils.toInt(this.header_size_buf, false);
                in.read(this.chunk_size_buf);
                chunk_size = Utils.toInt(this.chunk_size_buf, false);
                byte[] nsStartBuf = new byte[(chunk_size - 8)];
                in.read(nsStartBuf);
                parseStartNameSpace(nsStartBuf, header_size, chunk_size);
            }
            in.read(this.chunk_type_buf);
            int chunk_type = Utils.toInt(this.chunk_type_buf, false);
            while (chunk_type != RES_XML_END_NAMESPACE_TYPE) {
                in.read(this.header_size_buf);
                header_size = Utils.toInt(this.header_size_buf, false);
                in.read(this.chunk_size_buf);
                chunk_size = Utils.toInt(this.chunk_size_buf, false);
                byte[] elementBuf = new byte[(chunk_size - 8)];
                in.read(elementBuf);
                if (chunk_type == RES_XML_START_ELEMENT_TYPE) {
                    parseXMLStart(elementBuf, header_size, chunk_size);
                } else if (chunk_type == RES_XML_END_ELEMENT_TYPE) {
                    parseXMLEnd(elementBuf, header_size, chunk_size);
                }
                in.read(this.chunk_type_buf);
                chunk_type = Utils.toInt(this.chunk_type_buf, false);
            }
            if (chunk_type == RES_XML_END_NAMESPACE_TYPE) {
                in.read(this.header_size_buf);
                header_size = Utils.toInt(this.header_size_buf, false);
                in.read(this.chunk_size_buf);
                chunk_size = Utils.toInt(this.chunk_size_buf, false);
                byte[] nsEndBuf = new byte[(chunk_size - 8)];
                in.read(nsEndBuf);
                parseEndNameSpace(nsEndBuf, header_size, chunk_size);
            }
            if (this.listener != null) {
                this.listener.endDoc();
            }
        }
    }

    private void parseStringPool(byte[] spBuf, int header_size, int chunk_size) throws Exception {
        int i;
        ByteArrayInputStream in = new ByteArrayInputStream(spBuf);
        byte[] int_buf = new byte[4];
        in.read(int_buf);
        int string_count = Utils.toInt(int_buf, false);
        in.read(int_buf);
        int style_count = Utils.toInt(int_buf, false);
        in.read(int_buf);
        int flag = Utils.toInt(int_buf, false);
        in.read(int_buf);
        int string_start = Utils.toInt(int_buf, false);
        in.read(int_buf);
        int style_start = Utils.toInt(int_buf, false);
        int[] string_indices = new int[string_count];
        if (string_count > 0) {
            for (i = 0; i < string_count; i++) {
                in.read(int_buf);
                string_indices[i] = Utils.toInt(int_buf, false);
            }
        }
        if (style_count > 0) {
            in.skip((long) (style_count * 4));
        }
        for (i = 0; i < string_count; i++) {
            int string_len;
            int actual_str_len;
            if (i != string_count - 1) {
                string_len = string_indices[i + 1] - string_indices[i];
            } else if (style_start == 0) {
                string_len = ((chunk_size - string_indices[i]) - header_size) - (string_count * 4);
            } else {
                string_len = style_start - string_indices[i];
            }
            byte[] short_buf = new byte[2];
            in.read(short_buf);
            if (short_buf[0] == short_buf[1]) {
                actual_str_len = short_buf[0];
            } else {
                actual_str_len = Utils.toInt(short_buf, false);
            }
            byte[] str_buf = new byte[actual_str_len];
            byte[] buf = new byte[(string_len - 2)];
            in.read(buf);
            int j = 0;
            for (int k = 0; k < buf.length; k++) {
                if (buf[k] != (byte) 0) {
                    int j2 = j + 1;
                    str_buf[j] = buf[k];
                    j = j2;
                }
            }
            this.stringPool.add(new String(str_buf));
        }
    }

    private void parseResMapping(byte[] rmBuf, int header_size, int chunk_size) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(rmBuf);
        int num_of_res_ids = rmBuf.length / 4;
        byte[] int_buf = new byte[4];
        for (int i = 0; i < num_of_res_ids; i++) {
            in.read(int_buf);
            this.resMap.add(Integer.valueOf(Utils.toInt(int_buf, false)));
        }
    }

    private void parseStartNameSpace(byte[] nsStartBuf, int header_size, int chunk_size) throws Exception {
        this.nodeIndex = 0;
        ByteArrayInputStream in = new ByteArrayInputStream(nsStartBuf);
        byte[] int_buf = new byte[4];
        in.read(int_buf);
        this.mNameSpaceLineNumber = Utils.toInt(int_buf, false);
        in.read(int_buf);
        int comment = Utils.toInt(int_buf, false);
        in.read(int_buf);
        this.mNameSpacePrefixIndex = Utils.toInt(int_buf, false);
        in.read(int_buf);
        this.mNameSpaceUriIndex = Utils.toInt(int_buf, false);
    }

    private void parseXMLStart(byte[] xmlStartBuf, int header_size, int chunk_size) throws Exception {
        this.nodeIndex++;
        Node node = new Node();
        node.setIndex(this.nodeIndex);
        ByteArrayInputStream in = new ByteArrayInputStream(xmlStartBuf);
        byte[] int_buf = new byte[4];
        in.read(int_buf);
        node.setLinenumber(Utils.toInt(int_buf, false));
        in.read(int_buf);
        int comment = Utils.toInt(int_buf, false);
        in.read(int_buf);
        int ns_index = Utils.toInt(int_buf, false);
        in.read(int_buf);
        int name_index = Utils.toInt(int_buf, false);
        byte[] short_buf = new byte[2];
        in.read(short_buf);
        int attributeStart = Utils.toInt(short_buf, false);
        in.read(short_buf);
        int attributeSize = Utils.toInt(short_buf, false);
        in.read(short_buf);
        int attributeCount = Utils.toInt(short_buf, false);
        in.skip(6);
        if (name_index != -1) {
            node.setName(this.stringPool.get(name_index));
            if (!(this.mNameSpacePrefixIndex == -1 || this.mNameSpaceUriIndex == -1)) {
                node.setNamespacePrefix(this.stringPool.get(this.mNameSpacePrefixIndex));
                node.setNamespaceURI(this.stringPool.get(this.mNameSpaceUriIndex));
            }
        }
        if (attributeCount != 0) {
            for (int i = 0; i < attributeCount; i++) {
                Attribute attribute = new Attribute();
                in.read(int_buf);
                int attr_ns_index = Utils.toInt(int_buf, false);
                in.read(int_buf);
                int attr_name_index = Utils.toInt(int_buf, false);
                in.read(int_buf);
                int attr_raw_value = Utils.toInt(int_buf, false);
                String attr_value = "";
                if (attr_raw_value == -1) {
                    in.read(short_buf);
                    int data_size = Utils.toInt(short_buf, false);
                    in.skip(1);
                    int data_type = in.read();
                    in.read(int_buf);
                    attr_value = String.valueOf(Utils.toInt(int_buf, false));
                } else {
                    attr_value = this.stringPool.get(attr_raw_value);
                    in.skip(8);
                }
                if (attr_name_index != -1) {
                    attribute.setName(this.stringPool.get(attr_name_index));
                    attribute.setValue(attr_value);
                    attribute.setIndex(i);
                    node.addAttribute(attribute);
                }
            }
            if (this.listener != null) {
                this.listener.startNode(node);
            }
        } else if (this.listener != null) {
            this.listener.startNode(node);
        }
    }

    private void parseXMLEnd(byte[] xmlEndBuf, int header_size, int chunk_size) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(xmlEndBuf);
        byte[] int_buf = new byte[4];
        in.read(int_buf);
        int lineNumber = Utils.toInt(int_buf, false);
        in.read(int_buf);
        int comment = Utils.toInt(int_buf, false);
        in.read(int_buf);
        int ns_index = Utils.toInt(int_buf, false);
        in.read(int_buf);
        int name_index = Utils.toInt(int_buf, false);
        if (name_index != -1) {
            Node node = new Node();
            node.setName(this.stringPool.get(name_index));
            node.setLinenumber(lineNumber);
            node.setNamespacePrefix(this.stringPool.get(this.mNameSpacePrefixIndex));
            node.setNamespaceURI(this.stringPool.get(this.mNameSpaceUriIndex));
            if (this.listener != null) {
                this.listener.endNode(node);
            }
        }
    }

    private void parseEndNameSpace(byte[] nsStartBuf, int header_size, int chunk_size) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(nsStartBuf);
        byte[] int_buf = new byte[4];
        in.read(int_buf);
        int lineNumber = Utils.toInt(int_buf, false);
        in.read(int_buf);
        int comment = Utils.toInt(int_buf, false);
        in.read(int_buf);
        int prefix_index = Utils.toInt(int_buf, false);
        in.read(int_buf);
        int uri_index = Utils.toInt(int_buf, false);
    }

    public void parseResourceTable(String arscFile) throws Exception {
        this.stringPool.clear();
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(arscFile));
        in.read(this.buf_2);
        if (Utils.toInt(this.buf_2, false) == 2) {
            in.read(this.buf_2);
            this.header_size = Utils.toInt(this.buf_2, false);
            in.read(this.buf_4);
            this.chunk_size = Utils.toInt(this.buf_4, false);
            in.read(this.buf_4);
            this.package_count = Utils.toInt(this.buf_4, false);
            in.read(this.buf_2);
            if (Utils.toInt(this.buf_2, false) == 1) {
                in.read(this.buf_2);
                this.header_size = Utils.toInt(this.buf_2, false);
                in.read(this.buf_4);
                this.chunk_size = Utils.toInt(this.buf_4, false);
                byte[] spBuf = new byte[(this.chunk_size - 8)];
                in.read(spBuf);
                parseStringPool(spBuf, this.header_size, this.chunk_size);
                in.read(this.buf_2);
            }
            if (Utils.toInt(this.buf_2, false) == 512) {
                parseResPackage(in);
            }
        }
    }

    private void parseResPackage(BufferedInputStream in) throws Exception {
        in.read(this.buf_2);
        this.header_size = Utils.toInt(this.buf_2, false);
        in.read(this.buf_4);
        this.chunk_size = Utils.toInt(this.buf_4, false);
        in.read(this.buf_4);
        int packg_id = Utils.toInt(this.buf_4, false);
        byte[] packg_name_buf = new byte[256];
        in.read(packg_name_buf);
        String packg_name = Utils.toString(packg_name_buf, false);
        in.read(this.buf_4);
        int typeStrings = Utils.toInt(this.buf_4, false);
        in.read(this.buf_4);
        int lastPublicType = Utils.toInt(this.buf_4, false);
        in.read(this.buf_4);
        int keyString = Utils.toInt(this.buf_4, false);
        in.read(this.buf_4);
        int lastPublicKey = Utils.toInt(this.buf_4, false);
        in.read(this.buf_2);
        if (Utils.toInt(this.buf_2, false) == 1) {
            in.read(this.buf_2);
            this.header_size = Utils.toInt(this.buf_2, false);
            in.read(this.buf_4);
            this.chunk_size = Utils.toInt(this.buf_4, false);
            byte[] spBuf = new byte[(this.chunk_size - 8)];
            in.read(spBuf);
            parseStringPool(spBuf, this.header_size, this.chunk_size);
            in.read(this.buf_2);
        }
        if (Utils.toInt(this.buf_2, false) == 1) {
            in.read(this.buf_2);
            this.header_size = Utils.toInt(this.buf_2, false);
            in.read(this.buf_4);
            this.chunk_size = Utils.toInt(this.buf_4, false);
            byte[] spBuf = new byte[(this.chunk_size - 8)];
            in.read(spBuf);
            parseStringPool(spBuf, this.header_size, this.chunk_size);
            in.read(this.buf_2);
        }
    }

    private void parseResType() {
    }

    private void parseResTypeSpec() {
    }
}
