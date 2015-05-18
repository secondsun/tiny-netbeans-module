package net.saga.tiny.netbeans.debugger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.Set;
import org.netbeans.api.debugger.ActionsManager;
import org.netbeans.api.debugger.Breakpoint;
import org.netbeans.api.debugger.DebuggerManager;
import org.netbeans.api.debugger.jpda.JPDADebugger;
import org.netbeans.api.debugger.jpda.LineBreakpoint;
import org.netbeans.spi.debugger.ActionsProvider.Registration;
import org.netbeans.spi.debugger.ActionsProviderSupport;
import org.netbeans.spi.debugger.ContextProvider;
import org.openide.filesystems.FileObject;

/**
 *
 * Borrows HEAVILY from org.netbeans.modules.groovy.support.debug.GroovyToggleBreakpointActionProvider
 * 
 * @author summers
 */
@Registration(actions = {"toggleBreakpoint"}, activateForMIMETypes = {"text/x-tiny"})
public class TinyToggleBreakpointActionProvider extends ActionsProviderSupport implements PropertyChangeListener {

    
    private JPDADebugger debugger;
    
    public TinyToggleBreakpointActionProvider () {
        Context.addPropertyChangeListener (this);
        setEnabled (ActionsManager.ACTION_TOGGLE_BREAKPOINT, true);
    }
    
    public TinyToggleBreakpointActionProvider (ContextProvider contextProvider) {
        debugger = (JPDADebugger) contextProvider.lookupFirst(null, JPDADebugger.class);
        debugger.addPropertyChangeListener (JPDADebugger.PROP_STATE, this);
        Context.addPropertyChangeListener (this);
        setEnabled (ActionsManager.ACTION_TOGGLE_BREAKPOINT, true);
    }
    
    
    @Override
    public void doAction(Object o) {
        DebuggerManager debugManager = DebuggerManager.getDebuggerManager();

        // 1) get source name & line number
        int lineNumber = Context.getCurrentLineNumber();
        String url = Context.getCurrentURL();
        if (url == null) {
            return;
        }

        // 2) find and remove existing line breakpoint
        for (Breakpoint breakpoint : debugManager.getBreakpoints()) {
            if (breakpoint instanceof LineBreakpoint) {

                LineBreakpoint lineBreakpoint = ((LineBreakpoint) breakpoint);
                if (lineNumber == lineBreakpoint.getLineNumber() && url.equals(lineBreakpoint.getURL())) {
                    debugManager.removeBreakpoint(breakpoint);
                    return;
                }
            }
        }

        try {
            // 3) Add new tiny line breakpoint
            debugManager.addBreakpoint(TinyLineBreakpointFactory.create(url, lineNumber));
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Set getActions() {
        return Collections.singleton (ActionsManager.ACTION_TOGGLE_BREAKPOINT);
    }

    private void destroy () {
        debugger.removePropertyChangeListener (JPDADebugger.PROP_STATE, this);
        Context.removePropertyChangeListener (this);
    }
    
    @Override
    public void propertyChange (PropertyChangeEvent evt) {
        FileObject fo = Context.getCurrentFile();
        boolean isTinyFile = fo != null && 
                "text/x-tiny".equals(fo.getMIMEType()); 
        
        setEnabled(ActionsManager.ACTION_TOGGLE_BREAKPOINT, isTinyFile);
        if ( debugger != null && 
             debugger.getState () == JPDADebugger.STATE_DISCONNECTED
        ) 
            destroy ();
    }
}
