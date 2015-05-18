package net.saga.tiny.netbeans;

import java.util.logging.Logger;
import org.netbeans.jellytools.actions.Action;
import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.ProjectServiceProvider;
import org.openide.util.Lookup;

@ProjectServiceProvider(
    service =
        ActionProvider.class,
    projectType = {
        "org-netbeans-modules-java-j2seproject",
        "org-netbeans-modules-maven"
    }
)
public class TinyActionsProvider implements ActionProvider {

    Action a;
    
    @Override
    public String[] getSupportedActions() {
        return new String[] {COMMAND_COMPILE_SINGLE, COMMAND_DEBUG_SINGLE, COMMAND_RUN_SINGLE};
    }

    @Override
    public void invokeAction(String command, Lookup context) throws IllegalArgumentException {
        Logger.getAnonymousLogger().fine("INVOKE!" + command);
    }

    @Override
    public boolean isActionEnabled(String command, Lookup context) throws IllegalArgumentException {
        return true;
    }
    
}
