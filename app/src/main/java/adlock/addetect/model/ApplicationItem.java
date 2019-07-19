package adlock.addetect.model;

import android.content.pm.PackageInfo;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import adlock.addetect.control.Module;


public class ApplicationItem {
    private Set<Module> mModuleSet;

    private PackageInfo mPackageInfo;

    public ApplicationItem(PackageInfo paramPackageInfo) {
        this.mPackageInfo = paramPackageInfo;
        this.mModuleSet = new HashSet();
    }

    public void addModule(Module paramModule) {
        this.mModuleSet.add(paramModule);
    }

    public List<Module> getModuleList() {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.mModuleSet);
        return arrayList;
    }

    public Set<Module> getModuleSet() { return this.mModuleSet; }

    public PackageInfo getPackageInfo() { return this.mPackageInfo; }

   /* public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator iterator = this.mModuleSet.iterator();
        while (true) {
            String str2;
            String str1;
            if (!iterator.hasNext()) {
                str2 = stringBuilder.toString();
                str1 = str2;
                if (str2 != null) {
                    str1 = str2;
                    if (str2.trim().length() > 2)
                        str1 = str2.substring(0, str2.length() - 2);
                }
                return str1;
            }
            str1.append((Module)str2.next()).append(", ");
        }
    }*/
}
