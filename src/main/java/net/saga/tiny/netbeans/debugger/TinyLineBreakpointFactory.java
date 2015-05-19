package net.saga.tiny.netbeans.debugger;

import java.net.MalformedURLException;
import java.net.URL;
import org.netbeans.api.debugger.Breakpoint;
import org.netbeans.api.debugger.jpda.LineBreakpoint;
import org.netbeans.api.java.classpath.ClassPath;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.URLMapper;

class TinyLineBreakpointFactory {

    static Breakpoint create(String url, int lineNumber) throws MalformedURLException {
        LineBreakpoint tinyBreakpoint = LineBreakpoint.create(url, lineNumber);
        FileObject file = URLMapper.findFileObject(new URL(url));
        tinyBreakpoint.setStratum("Tiny");
        tinyBreakpoint.setSourceName(file.getNameExt());
        tinyBreakpoint.setSourcePath(getGroovyPath(url));
        tinyBreakpoint.setPreferredClassName("anonymous");
        tinyBreakpoint.setPrintText(String.format("Breakpoint @%d", lineNumber));
        tinyBreakpoint.setHidden(false);

        return tinyBreakpoint;
    }
 
    private static String getGroovyPath(String url) throws MalformedURLException {
        FileObject fo = URLMapper.findFileObject(new URL(url));
        String relativePath = url;

        if (fo != null) {
            ClassPath cp = ClassPath.getClassPath(fo, ClassPath.SOURCE);
            if (cp == null) {
                return null;
            }
            FileObject root = cp.findOwnerRoot(fo);
            if (root == null) {
                return null;
            }
            relativePath = FileUtil.getRelativePath(root, fo);
        }

        return relativePath;
    }
    
}
