package net.saga.tiny.netbeans.lexer;

import org.netbeans.api.lexer.Language;
import org.netbeans.api.lexer.TokenId;

public class TinyTokenId implements TokenId {
    
    private final String name;
    private final String primaryCategory;
    private final int id;

    public TinyTokenId(String name, String primaryCategory, int id) {
        this.name = name;
        this.primaryCategory = primaryCategory;
        this.id = id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String primaryCategory() {
        return primaryCategory;
    }

    @Override
    public int ordinal() {
        return id;
    }
    
 public static Language<TinyTokenId> getLanguage() {
    return new TinyLanguageHierarchy().language();
}

}
