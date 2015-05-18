package net.saga.tiny.netbeans.debugger;

import java.net.MalformedURLException;
import java.net.URL;
import org.netbeans.api.debugger.Breakpoint;
import org.netbeans.api.debugger.jpda.LineBreakpoint;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.URLMapper;

class TinyLineBreakpointFactory {

    static Breakpoint create(String url, int lineNumber) throws MalformedURLException {
        LineBreakpoint tinyBreakpoint = LineBreakpoint.create(url, lineNumber);
        FileObject file = URLMapper.findFileObject(new URL(url));
        tinyBreakpoint.setStratum("Tiny");
        tinyBreakpoint.setSourceName(file.getNameExt());
        tinyBreakpoint.setSourcePath(file.getPath());
        tinyBreakpoint.setPreferredClassName("anonymous");
        tinyBreakpoint.setPrintText(String.format("Breakpoint @%d", lineNumber));
        tinyBreakpoint.setHidden(false);

        return tinyBreakpoint;
    }
    
}
