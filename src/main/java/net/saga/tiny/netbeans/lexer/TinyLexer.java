package net.saga.tiny.netbeans.lexer;

import java.nio.CharBuffer;
import net.saga.lang.tiny.parser.Parser;
import net.saga.lang.tiny.scanner.Scanner;
import net.saga.lang.tiny.scanner.Token;
import static net.saga.lang.tiny.scanner.TokenType.COMMENT;

import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerInput;
import org.netbeans.spi.lexer.LexerRestartInfo;

class TinyLexer implements Lexer<TinyTokenId> {

    private final LexerRestartInfo<TinyTokenId> info;
    private Scanner scanner;
    private Parser javaParserTokenManager;
    
    private CharBuffer buffer;
    private boolean mNeedInit = true;

    TinyLexer(LexerRestartInfo<TinyTokenId> info) {
        this.info = info;
        
        
    }

    @Override
    public org.netbeans.api.lexer.Token<TinyTokenId> nextToken() {
        LexerInput input = info.input();
        
        if (mNeedInit) {
            setup();
        }
        
        if (buffer.remaining() < 1) {
            if (input.read() == LexerInput.EOF) {
                if (input.readLength() > 0) {
                    return info.tokenFactory().createToken(TinyLanguageHierarchy.getToken(COMMENT.ordinal()));
                } else {
                    return null;
                }
                
            } else {
                input.backup(1);
                //do something, say something
                return null;
            }
        }
        int startPosition = buffer.position();
        Token token = scanner.nextToken(buffer);
        int endPosition = buffer.position();
        for (; startPosition < endPosition; startPosition++) {
            input.read();
        }
        
        if (token == null) {
            if (buffer.remaining() < 1) {
            if (input.read() == LexerInput.EOF) {
                if (input.readLength() > 0) {
                    return info.tokenFactory().createToken(TinyLanguageHierarchy.getToken(COMMENT.ordinal()));
                } else {
                    return null;
                }
                
            } else {
                input.backup(1);
                //do something, say something
                return null;
            }
        }
        } 
        
        if (info.tokenFactory() == null) {
            return null;
        }
        
        return info.tokenFactory().createToken(TinyLanguageHierarchy.getToken(token.getType().ordinal()));
    }

    @Override
    public Object state() {
        return null;
    }

    @Override
    public void release() {
    }

    private void setup() {
        LexerInput input = info.input();
        int character = 0;
        
        StringBuilder builder = new StringBuilder(input.readLengthEOF());
        while ((character = input.read()) != LexerInput.EOF) {
            builder.append((char)character);
        }
        scanner = new Scanner();
        Parser parser = new Parser();
        buffer = CharBuffer.wrap(builder.toString());
        input.backup(input.readLengthEOF());
        mNeedInit = false;
    }

    
}
