/*
 [The "BSD license"]
 Copyright (c) 2005-2009 Terence Parr
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:
 1. Redistributions of source code must retain the above copyright
     notice, this list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in the
     documentation and/or other materials provided with the distribution.
 3. The name of the author may not be used to endorse or promote products
     derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.antlr.runtime;

import org.antlr.runtime.misc.LookaheadStream;

import java.util.List;
import java.util.NoSuchElementException;

/** A token stream that pulls tokens from the code source on-demand and
 *  without tracking a complete buffer of the tokens. This stream buffers
 *  the minimum number of tokens possible.  It's the same as
 *  OnDemandTokenStream except that OnDemandTokenStream buffers all tokens.
 *
 *  You can't use this stream if you pass whitespace or other off-channel
 *  tokens to the parser. The stream can't ignore off-channel tokens.
 * 
 *  You can only look backwards 1 token: LT(-1).
 *
 *  Use this when you need to read from a socket or other infinite stream.
 *
 *  @see BufferedTokenStream
 *  @see CommonTokenStream
 */
public class UnbufferedTokenStream extends LookaheadStream<Token> implements TokenStream {
	protected TokenSource tokenSource;
    protected int tokenIndex = 0; // simple counter to set token index in tokens

    /** Skip tokens on any channel but this one; this is how we skip whitespace... */
    protected int channel = Token.DEFAULT_CHANNEL;

	public UnbufferedTokenStream(TokenSource tokenSource) {
		this.tokenSource = tokenSource;
	}

	public Token nextElement() {
		Token t = tokenSource.nextToken();
        t.setTokenIndex(tokenIndex++);
		return t;
	}

    public boolean isEOF(Token o) { return o.getType() == Token.EOF; }    

	public TokenSource getTokenSource() { return tokenSource; }

	public String toString(int start, int stop) { return "n/a"; }

	public String toString(Token start, Token stop) { return "n/a"; }

    public int LA(int i) { return LT(i).getType(); }

    public Token get(int i) {
        throw new UnsupportedOperationException("Absolute token indexes are meaningless in an unbuffered stream");
    }

	public String getSourceName() {	return tokenSource.getSourceName();	}
}
