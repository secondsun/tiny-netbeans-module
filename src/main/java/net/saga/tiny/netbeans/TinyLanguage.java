
package net.saga.tiny.netbeans;

import net.saga.tiny.netbeans.lexer.TinyTokenId;
import org.netbeans.api.lexer.Language;
import org.netbeans.modules.csl.spi.DefaultLanguageConfig;
import org.netbeans.modules.csl.spi.LanguageRegistration;

@LanguageRegistration(mimeType = "text/x-tiny")
public class TinyLanguage extends DefaultLanguageConfig {

    @Override
    public Language getLexerLanguage() {
        return TinyTokenId.getLanguage();
    }

    @Override
    public String getDisplayName() {
        return "Tiny";
    }
}
