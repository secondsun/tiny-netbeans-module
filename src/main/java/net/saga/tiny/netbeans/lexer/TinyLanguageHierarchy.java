package net.saga.tiny.netbeans.lexer;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static net.saga.lang.tiny.scanner.TokenType.*;
import org.netbeans.api.lexer.Language;
import org.netbeans.spi.lexer.LanguageHierarchy;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

public class TinyLanguageHierarchy extends LanguageHierarchy<TinyTokenId> {

    private static List<TinyTokenId> tokens;
    private static Map<Integer, TinyTokenId> idToToken;
 
    private static void init() {
        tokens = Arrays.<TinyTokenId>asList(new TinyTokenId[]{
            new TinyTokenId(IF.name(), "keyword", IF.ordinal()),
            new TinyTokenId(THEN.name(), "keyword", THEN.ordinal()),
            new TinyTokenId(ELSE.name(), "keyword", ELSE.ordinal()),
            new TinyTokenId(END.name(), "keyword", END.ordinal()),
            new TinyTokenId(REPEAT.name(), "keyword", REPEAT.ordinal()),
            new TinyTokenId(UNTIL.name(), "keyword", UNTIL.ordinal()),
            new TinyTokenId(READ.name(), "method-declaration", READ.ordinal()),
            new TinyTokenId(WRITE.name(), "method-declaration", WRITE.ordinal()),
            new TinyTokenId(ADDITION.name(), "operator", ADDITION.ordinal()),
            new TinyTokenId(SUBTRACTION.name(), "operator", SUBTRACTION.ordinal()),
            new TinyTokenId(MULTIPLICATION.name(), "operator", MULTIPLICATION.ordinal()),
            new TinyTokenId(INT_DIVISION.name(), "operator", INT_DIVISION.ordinal()),
            new TinyTokenId(EQ.name(), "operator", EQ.ordinal()),
            new TinyTokenId(LT.name(), "operator", LT.ordinal()),
            new TinyTokenId(START_PAREN.name(), "operator", START_PAREN.ordinal()),
            new TinyTokenId(END_PAREN.name(), "operator", END_PAREN.ordinal()),
            new TinyTokenId(SEMICOLON.name(), "operator", SEMICOLON.ordinal()),
            new TinyTokenId(ASSIGNMENT.name(), "operator", ASSIGNMENT.ordinal()),
            new TinyTokenId(NUMBER.name(), "number", NUMBER.ordinal()),
            new TinyTokenId(IDENTIFIER.name(), "field", IDENTIFIER.ordinal()),
            new TinyTokenId(COMMENT.name(), "comment", COMMENT.ordinal())

        });
        idToToken = new HashMap<Integer, TinyTokenId>();
        for (TinyTokenId token : tokens) {
            idToToken.put(token.ordinal(), token);
        }
    }

    static synchronized TinyTokenId getToken(int id) {
        if (idToToken == null) {
            init();
        }
        return idToToken.get(id);
    }

    @Override
    protected synchronized Collection<TinyTokenId> createTokenIds() {
        if (tokens == null) {
            init();
        }
        return tokens;
    }

    @Override
    protected synchronized Lexer<TinyTokenId> createLexer(LexerRestartInfo<TinyTokenId> info) {
        return new TinyLexer(info);
    }

    @Override
    protected String mimeType() {
        return "text/x-tiny";
    }

    public static Language<TinyTokenId> getLanguage() {
        return new TinyLanguageHierarchy().language();
    }

}
