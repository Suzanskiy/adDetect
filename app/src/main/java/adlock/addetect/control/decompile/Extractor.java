package adlock.addetect.control.decompile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import adlock.addetect.control.decompile.xml.XmlConverter;
import adlock.addetect.control.decompile.xml.XmlGenerator;


public class Extractor {
    final int BUFFER = 2048;
    boolean debug = true;
    String dexFile = null;
    String resFile = null;
    String tag = getClass().getSimpleName();
    ArrayList<String> xmlFiles = new ArrayList();

    public void unZip(String apkFile) throws Exception {
        Object obj;
        File file = new File(apkFile);
        String apkFileName = file.getName();
        if (apkFileName.indexOf(46) != -1) {
            apkFileName = apkFileName.substring(0, apkFileName.indexOf(46));
        }
        if (file.getParent() == null) {
            obj = "";
        } else {
            obj = file.getParent() + File.separator;
        }
        File extractFolder = new File(new StringBuilder(String.valueOf(obj)).append(apkFileName).toString());
        if (!extractFolder.exists()) {
            extractFolder.mkdir();
        }
        FileInputStream fileInputStream = new FileInputStream(apkFile);
        ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(fileInputStream));
        while (true) {
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            if (zipEntry == null) {
                zipInputStream.close();
                fileInputStream.close();
                return;
            }
            String entryName = zipEntry.getName();
            String folderName = entryName;
            if (zipEntry.isDirectory()) {
                entryName = null;
            } else if (entryName.indexOf("/") == -1) {
                folderName = null;
            } else {
                folderName = entryName.substring(0, entryName.lastIndexOf("/"));
                entryName = entryName.substring(entryName.lastIndexOf("/") + 1);
            }
            File entryFile = extractFolder;
            if (folderName != null) {
                entryFile = new File(extractFolder.getPath() + File.separator + folderName);
                if (!entryFile.exists()) {
                    entryFile.mkdirs();
                }
            }
            if (entryName != null) {
                if (entryName.endsWith(".xml")) {
                    this.xmlFiles.add(entryFile.getPath() + File.separator + entryName);
                }
                if (entryName.endsWith(".dex") || entryName.endsWith(".odex")) {
                    this.dexFile = entryFile.getPath() + File.separator + entryName;
                }
                if (entryName.endsWith(".arsc")) {
                    this.resFile = entryFile.getPath() + File.separator + entryName;
                }
                byte[] data = new byte[2048];
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(entryFile.getPath() + File.separator + entryName), 2048);
                while (true) {
                    int count = zipInputStream.read(data, 0, 2048);
                    if (count == -1) {
                        break;
                    }
                    bufferedOutputStream.write(data, 0, count);
                }
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getManifest(java.lang.String r14) {
        /*
        r13 = this;
        r12 = 2048; // 0x800 float:2.87E-42 double:1.0118E-320;
        r11 = 0;
        r4 = new java.io.FileInputStream;
        r4.<init>(r14);
        r8 = new java.util.zip.ZipInputStream;
        r9 = new java.io.BufferedInputStream;
        r9.<init>(r4);
        r8.<init>(r9);
        r7 = 0;
        r1 = 0;
    L_0x0014:
        r7 = r8.getNextEntry();
        if (r7 != 0) goto L_0x0033;
    L_0x001a:
        r8.close();
        r4.close();
        r6 = new krow.dev.addetector.control.decompile.xml.XmlGenerator;
        r6.<init>();
        r5 = new krow.dev.addetector.control.decompile.xml.XmlConverter;
        r5.<init>(r6);
        r5.parse(r1);
        r5 = 0;
        r9 = r6.getXml();
        return r9;
    L_0x0033:
        r9 = r7.getName();
        r10 = "AndroidManifest.xml";
        r9 = r9.equalsIgnoreCase(r10);
        if (r9 == 0) goto L_0x0014;
    L_0x003f:
        r9 = r7.isDirectory();
        if (r9 != 0) goto L_0x0014;
    L_0x0045:
        r3 = new byte[r12];
        r0 = new java.io.ByteArrayOutputStream;
        r0.<init>();
    L_0x004c:
        r2 = r8.read(r3, r11, r12);
        r9 = -1;
        if (r2 != r9) goto L_0x005e;
    L_0x0053:
        r0.flush();
        r0.close();
        r1 = r0.toByteArray();
        goto L_0x001a;
    L_0x005e:
        r0.write(r3, r11, r2);
        goto L_0x004c;
        */
        throw new UnsupportedOperationException("Method not decompiled: krow.dev.addetector.control.decompile.Extractor.getManifest(java.lang.String):java.lang.String");
    }

    public void decodeBX() {
        for (int i = 0; i < this.xmlFiles.size(); i++) {
            try {
                new XmlConverter(new XmlGenerator()).parse(this.xmlFiles.get(i));
            } catch (Exception e) {
            } catch (Throwable th) {
            }
        }
    }

    public void decodeResource() throws Exception {
        new XmlConverter(null).parseResourceTable(this.resFile);
    }
}
