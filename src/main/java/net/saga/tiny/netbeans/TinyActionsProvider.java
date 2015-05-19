package net.saga.tiny.netbeans;

import java.io.File;
import java.io.IOException;
import static java.nio.CharBuffer.wrap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import net.saga.lang.tiny.analyize.Analyizer;
import net.saga.lang.tiny.analyize.SymbolTable;
import net.saga.lang.tiny.compiler.CompilerContext;
import net.saga.lang.tiny.compiler.DynamicClassLoader;
import net.saga.lang.tiny.compiler.TinyCompiler;
import net.saga.lang.tiny.parser.Node;
import net.saga.lang.tiny.parser.Parser;
import net.saga.lang.tiny.scanner.Scanner;
import net.saga.lang.tiny.scanner.Token;
import org.apache.commons.io.IOUtils;
import org.netbeans.api.debugger.DebuggerManager;
import org.netbeans.api.debugger.jpda.DebuggerStartException;
import org.netbeans.api.debugger.jpda.JPDADebugger;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.spi.java.classpath.support.ClassPathSupport;
import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.ProjectServiceProvider;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

@ProjectServiceProvider(
        service
        = ActionProvider.class,
        projectType = {
            "org-netbeans-modules-java-j2seproject",
            "org-netbeans-modules-maven"
        }
)
public class TinyActionsProvider implements ActionProvider {

    @Override
    public String[] getSupportedActions() {
        return new String[]{COMMAND_COMPILE_SINGLE, COMMAND_DEBUG_SINGLE, COMMAND_RUN_SINGLE};
    }

    @Override
    public void invokeAction(String command, Lookup context) throws IllegalArgumentException {
        Logger.getAnonymousLogger().fine("INVOKE!" + command);
        File file = null;
        for (DataObject dataObject : context.lookupAll(DataObject.class)) {
            file = FileUtil.toFile(dataObject.getPrimaryFile());
            if (file != null) {
                break;
            }
        }
        if (file != null) {
            final File _file = file;
            switch (command) {
                case COMMAND_COMPILE_SINGLE: {

                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            final JOptionPane pane = new JOptionPane("Compiling", JOptionPane.INFORMATION_MESSAGE);
                            pane.setVisible(true);
                            try {
                                compile(_file);
                            } catch (IOException ex) {
                                Exceptions.printStackTrace(ex);
                            }
                            pane.setVisible(false);
                        }
                    });

                }
                break;
                case COMMAND_DEBUG_SINGLE:

                    try {
                        compile(_file);
                        final Map properties = new HashMap();
                        properties.put("baseDir", new File("/")); // NOI18N
                        properties.put("sourcepath", ClassPathSupport.createClassPath(_file.getParent())); //NOI18N
                        properties.put("name", "anonymous"); //NOI18N

                        Runtime.getRuntime().exec("xterm -hold -e java -agentlib:jdwp=transport=dt_socket,address=localhost:9009,server=y,suspend=y -cp /tmp anonymous");
                        Thread.sleep(2000);
                        JPDADebugger.attach("localhost", 9009, new Object[]{properties});
                        

                    } catch (IOException | InterruptedException | DebuggerStartException ex) {
                        Exceptions.printStackTrace(ex);
                    }

                    break;
                case COMMAND_RUN_SINGLE: {

                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {

                            try {
                                final JOptionPane pane = new JOptionPane("Compiling", JOptionPane.INFORMATION_MESSAGE);
                                compile(_file);
                                pane.setVisible(false);

                                Runtime.getRuntime().exec("xterm -hold -e java -cp /tmp anonymous");
                            } catch (IOException ex) {
                                Exceptions.printStackTrace(ex);
                            }
                        }
                    });

                }
                break;

            }
        }

    }

    @Override
    public boolean isActionEnabled(String command, Lookup context) throws IllegalArgumentException {
        return true;
    }

    private Class<?> compile(File file) throws IOException {
        List<Token> scanned = new Scanner().scan(wrap(IOUtils.toString(file.toURI())));

        Node parseTree = new Parser().parseStatement(scanned);
        SymbolTable table = Analyizer.buildSymbolTable(parseTree);

        CompilerContext compilerContext = TinyCompiler.compileProgram(parseTree, new CompilerContext(), table);

        compilerContext.jiteClass.setSourceFile(file.getName());

        Class<?> compiledClass = new DynamicClassLoader().define(compilerContext.jiteClass);
        return compiledClass;
    }

}
