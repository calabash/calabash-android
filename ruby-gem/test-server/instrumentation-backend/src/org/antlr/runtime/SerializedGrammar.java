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

import java.io.IOException;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.util.List;
import java.util.ArrayList;

public class SerializedGrammar {
    public static final String COOKIE = "$ANTLR";
    public static final int FORMAT_VERSION = 1;
    //public static org.antlr.tool.Grammar gr; // TESTING ONLY; remove later

    private static final String TAG = "ANTLR:SerializedGrammar";

    public String name;
    public char type; // in {l, p, t, c}
    public List rules;

    class Rule {
        String name;
        Block block;
        public Rule(String name, Block block) {
            this.name = name;
            this.block = block;
        }
        public String toString() {
            return name+":"+block;
        }
    }

    class Block {
        List[] alts;
        public Block(List[] alts) {
            this.alts = alts;
        }
        public String toString() {
            StringBuffer buf = new StringBuffer();
            buf.append("(");
            for (int i = 0; i < alts.length; i++) {
                List alt = alts[i];
                if ( i>0 ) buf.append("|");
                buf.append(alt.toString());
            }
            buf.append(")");
            return buf.toString();
        }
    }

    class TokenRef {
        int ttype;
        public TokenRef(int ttype) { this.ttype = ttype; }
        public String toString() { return String.valueOf(ttype); }
    }

    class RuleRef {
        int ruleIndex;
        public RuleRef(int ruleIndex) { this.ruleIndex = ruleIndex; }
        public String toString() { return String.valueOf(ruleIndex); }
    }

    public SerializedGrammar(String filename) throws IOException {
        //System.out.println("loading "+filename);
	android.util.Log.i(TAG, "loading "+filename);
        FileInputStream fis = new FileInputStream(filename);
        BufferedInputStream bos = new BufferedInputStream(fis);
        DataInputStream in = new DataInputStream(bos);
        readFile(in);
        in.close();
    }

    protected void readFile(DataInputStream in) throws IOException {
        String cookie = readString(in); // get $ANTLR
        if ( !cookie.equals(COOKIE) ) throw new IOException("not a serialized grammar file");
        int version = in.readByte();
        char grammarType = (char)in.readByte();
        this.type = grammarType;
        String grammarName = readString(in);
        this.name = grammarName;
        //System.out.println(grammarType+" grammar "+grammarName);
	android.util.Log.i(TAG, grammarType+" grammar "+grammarName);
        int numRules = in.readShort();
        //System.out.println("num rules = "+numRules);
	android.util.Log.i(TAG, "num rules = "+numRules);
        rules = readRules(in, numRules);
    }

    protected List readRules(DataInputStream in, int numRules) throws IOException {
        List rules = new ArrayList();
        for (int i=0; i<numRules; i++) {
            Rule r = readRule(in);
            rules.add(r);
        }
        return rules;
    }

    protected Rule readRule(DataInputStream in) throws IOException {
        byte R = in.readByte();
        if ( R!='R' ) throw new IOException("missing R on start of rule");
        String name = readString(in);
        //System.out.println("rule: "+name);
	android.util.Log.i(TAG, "rule: "+name);
        byte B = in.readByte();
        Block b = readBlock(in);
        byte period = in.readByte();
        if ( period!='.' ) throw new IOException("missing . on end of rule");
        return new Rule(name, b);
    }

    protected Block readBlock(DataInputStream in) throws IOException {
        int nalts = in.readShort();
        List[] alts = new List[nalts];
        //System.out.println("enter block n="+nalts);
        for (int i=0; i<nalts; i++) {
            List alt = readAlt(in);
            alts[i] = alt;
        }
        //System.out.println("exit block");
        return new Block(alts);
    }

    protected List readAlt(DataInputStream in) throws IOException {
        List alt = new ArrayList();
        byte A = in.readByte();
        if ( A!='A' ) throw new IOException("missing A on start of alt");
        byte cmd = in.readByte();
        while ( cmd!=';' ) {
            switch (cmd) {
                case 't' :
                    int ttype = in.readShort();
                    alt.add(new TokenRef(ttype));
                    //System.out.println("read token "+gr.getTokenDisplayName(ttype));
                    break;
                case 'r' :
                    int ruleIndex = in.readShort();
                    alt.add(new RuleRef(ruleIndex));
                    //System.out.println("read rule "+gr.getRuleName(ruleIndex));
                    break;
                case '.' : // wildcard
                    break;
                case '-' : // range
                    int from = in.readChar();
                    int to = in.readChar();
                    break;
                case '~' : // not
                    int notThisTokenType = in.readShort();
                    break;
                case 'B' : // nested block
                    Block b = readBlock(in);
                    alt.add(b);
                    break;
            }
            cmd = in.readByte();
        }
        //System.out.println("exit alt");
        return alt;
    }

    protected String readString(DataInputStream in) throws IOException {
        byte c = in.readByte();
        StringBuffer buf = new StringBuffer();
        while ( c!=';' ) {
            buf.append((char)c);
            c = in.readByte();
        }
        return buf.toString();
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(type+" grammar "+name);
        buf.append(rules);
        return buf.toString();
    }
}
