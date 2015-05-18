package net.saga.tiny.netbeans;

import javax.swing.Action;

import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.ui.support.FileSensitiveActions;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;

public class TinyActions {

    

    @ActionID(id = "net.saga.tiny.TinyProjectModule.compile", category = "Tiny")
    @ActionRegistration(lazy = false, displayName = "Compile File")
    @ActionReference(path = "Loaders/text/x-tiny/Actions", position = 550)
    public static Action compile() {
        return FileSensitiveActions.fileCommandAction(
                ActionProvider.COMMAND_COMPILE_SINGLE,
                "Compile File",
                null);
    }

    @ActionID(id = "net.saga.tiny.TinyProjectModule.run", category = "Tiny")
    @ActionRegistration(lazy = false, displayName = "Run file")
    @ActionReferences(value = {
        @ActionReference(path = "Loaders/text/x-tiny/Actions", position = 560),
        @ActionReference(path = "Editors/text/x-tiny/Popup", position = 810, separatorBefore = 800)
    })
    public static Action run() {
        return FileSensitiveActions.fileCommandAction(
                ActionProvider.COMMAND_RUN_SINGLE,
                "Run file",
                null);
    }

    
    @ActionID(id = "net.saga.tiny.TinyProjectModule.debug", category = "Tiny")
    @ActionRegistration(lazy = false, displayName = "Debug File")
    @ActionReferences(value = {
        @ActionReference(path = "Loaders/text/x-tiny/Actions", position = 570),
        @ActionReference(path = "Editors/text/x-tiny/Popup", position = 820)
    })
    public static Action debug() {
        return FileSensitiveActions.fileCommandAction(
                ActionProvider.COMMAND_DEBUG_SINGLE,
                "Debug File",
                null);
    }
    
}
