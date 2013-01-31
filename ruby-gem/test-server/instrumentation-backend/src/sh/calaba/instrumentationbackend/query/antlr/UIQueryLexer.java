// $ANTLR 3.4 antlr/UIQuery.g 2013-01-31 12:10:57

    package sh.calaba.instrumentationbackend.query.antlr;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class UIQueryLexer extends Lexer {
    public static final int EOF=-1;
    public static final int ALL=4;
    public static final int BEGINPRED=5;
    public static final int BOOL=6;
    public static final int ENDPRED=7;
    public static final int ESC_SEQ=8;
    public static final int FILTER_COLON=9;
    public static final int HEX_DIGIT=10;
    public static final int INT=11;
    public static final int NAME=12;
    public static final int NIL=13;
    public static final int OCTAL_ESC=14;
    public static final int QUALIFIED_NAME=15;
    public static final int RELATION=16;
    public static final int STRING=17;
    public static final int UNICODE_ESC=18;
    public static final int VISIBLE=19;
    public static final int WHITE=20;
    public static final int WILDCARD=21;

      public String getErrorMessage(RecognitionException e, String[] tokenNames)
      {
        List stack = getRuleInvocationStack(e, this.getClass().getName());
        String msg = null;
        if ( e instanceof NoViableAltException ) {
          NoViableAltException nvae = (NoViableAltException)e;
          msg = " no viable alt; token="+e.token+" (decision="+nvae.decisionNumber+" state "+nvae.stateNumber+")"+" decision=<<"+nvae.grammarDecisionDescription+">>";
          throw new RuntimeException(msg, e);
        }
        else {
        msg = super.getErrorMessage(e, tokenNames);
        }
        return stack+" "+msg;
      }
      public String getTokenErrorDisplay(Token t) {
        return t.toString();
      }


    // delegates
    // delegators
    public Lexer[] getDelegates() {
        return new Lexer[] {};
    }

    public UIQueryLexer() {} 
    public UIQueryLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public UIQueryLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);
    }
    public String getGrammarFileName() { return "antlr/UIQuery.g"; }

    // $ANTLR start "WILDCARD"
    public final void mWILDCARD() throws RecognitionException {
        try {
            int _type = WILDCARD;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // antlr/UIQuery.g:44:10: ( '*' )
            // antlr/UIQuery.g:44:12: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WILDCARD"

    // $ANTLR start "QUALIFIED_NAME"
    public final void mQUALIFIED_NAME() throws RecognitionException {
        try {
            int _type = QUALIFIED_NAME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // antlr/UIQuery.g:46:16: ( NAME ( '.' NAME )+ )
            // antlr/UIQuery.g:46:18: NAME ( '.' NAME )+
            {
            mNAME(); 


            // antlr/UIQuery.g:46:23: ( '.' NAME )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='.') ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // antlr/UIQuery.g:46:24: '.' NAME
            	    {
            	    match('.'); 

            	    mNAME(); 


            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "QUALIFIED_NAME"

    // $ANTLR start "ALL"
    public final void mALL() throws RecognitionException {
        try {
            int _type = ALL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // antlr/UIQuery.g:50:5: ( 'all' )
            // antlr/UIQuery.g:50:7: 'all'
            {
            match("all"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ALL"

    // $ANTLR start "VISIBLE"
    public final void mVISIBLE() throws RecognitionException {
        try {
            int _type = VISIBLE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // antlr/UIQuery.g:52:9: ( 'visible' )
            // antlr/UIQuery.g:52:11: 'visible'
            {
            match("visible"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "VISIBLE"

    // $ANTLR start "FILTER_COLON"
    public final void mFILTER_COLON() throws RecognitionException {
        try {
            int _type = FILTER_COLON;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // antlr/UIQuery.g:57:15: ( ':' )
            // antlr/UIQuery.g:57:17: ':'
            {
            match(':'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "FILTER_COLON"

    // $ANTLR start "BEGINPRED"
    public final void mBEGINPRED() throws RecognitionException {
        try {
            int _type = BEGINPRED;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // antlr/UIQuery.g:62:11: ( '{' )
            // antlr/UIQuery.g:62:13: '{'
            {
            match('{'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "BEGINPRED"

    // $ANTLR start "ENDPRED"
    public final void mENDPRED() throws RecognitionException {
        try {
            int _type = ENDPRED;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // antlr/UIQuery.g:64:11: ( '}' )
            // antlr/UIQuery.g:64:13: '}'
            {
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ENDPRED"

    // $ANTLR start "RELATION"
    public final void mRELATION() throws RecognitionException {
        try {
            int _type = RELATION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // antlr/UIQuery.g:67:10: (| '=' | '>' | '>=' | '<' | '<=' | ( ( 'BEGINSWITH' | 'ENDSWITH' | 'CONTAINS' | 'LIKE' | 'beginswith' | 'endswith' | 'contains' | 'like' ) ( '[' ( 'a' .. 'z' | 'A' .. 'Z' )* ']' )? ) )
            int alt5=7;
            switch ( input.LA(1) ) {
            case '=':
                {
                alt5=2;
                }
                break;
            case '>':
                {
                int LA5_3 = input.LA(2);

                if ( (LA5_3=='=') ) {
                    alt5=4;
                }
                else {
                    alt5=3;
                }
                }
                break;
            case '<':
                {
                int LA5_4 = input.LA(2);

                if ( (LA5_4=='=') ) {
                    alt5=6;
                }
                else {
                    alt5=5;
                }
                }
                break;
            case 'B':
            case 'C':
            case 'E':
            case 'L':
            case 'b':
            case 'c':
            case 'e':
            case 'l':
                {
                alt5=7;
                }
                break;
            default:
                alt5=1;
            }

            switch (alt5) {
                case 1 :
                    // antlr/UIQuery.g:67:12: 
                    {
                    }
                    break;
                case 2 :
                    // antlr/UIQuery.g:67:14: '='
                    {
                    match('='); 

                    }
                    break;
                case 3 :
                    // antlr/UIQuery.g:67:20: '>'
                    {
                    match('>'); 

                    }
                    break;
                case 4 :
                    // antlr/UIQuery.g:67:26: '>='
                    {
                    match(">="); 



                    }
                    break;
                case 5 :
                    // antlr/UIQuery.g:67:33: '<'
                    {
                    match('<'); 

                    }
                    break;
                case 6 :
                    // antlr/UIQuery.g:67:39: '<='
                    {
                    match("<="); 



                    }
                    break;
                case 7 :
                    // antlr/UIQuery.g:68:4: ( ( 'BEGINSWITH' | 'ENDSWITH' | 'CONTAINS' | 'LIKE' | 'beginswith' | 'endswith' | 'contains' | 'like' ) ( '[' ( 'a' .. 'z' | 'A' .. 'Z' )* ']' )? )
                    {
                    // antlr/UIQuery.g:68:4: ( ( 'BEGINSWITH' | 'ENDSWITH' | 'CONTAINS' | 'LIKE' | 'beginswith' | 'endswith' | 'contains' | 'like' ) ( '[' ( 'a' .. 'z' | 'A' .. 'Z' )* ']' )? )
                    // antlr/UIQuery.g:68:5: ( 'BEGINSWITH' | 'ENDSWITH' | 'CONTAINS' | 'LIKE' | 'beginswith' | 'endswith' | 'contains' | 'like' ) ( '[' ( 'a' .. 'z' | 'A' .. 'Z' )* ']' )?
                    {
                    // antlr/UIQuery.g:68:5: ( 'BEGINSWITH' | 'ENDSWITH' | 'CONTAINS' | 'LIKE' | 'beginswith' | 'endswith' | 'contains' | 'like' )
                    int alt2=8;
                    switch ( input.LA(1) ) {
                    case 'B':
                        {
                        alt2=1;
                        }
                        break;
                    case 'E':
                        {
                        alt2=2;
                        }
                        break;
                    case 'C':
                        {
                        alt2=3;
                        }
                        break;
                    case 'L':
                        {
                        alt2=4;
                        }
                        break;
                    case 'b':
                        {
                        alt2=5;
                        }
                        break;
                    case 'e':
                        {
                        alt2=6;
                        }
                        break;
                    case 'c':
                        {
                        alt2=7;
                        }
                        break;
                    case 'l':
                        {
                        alt2=8;
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 2, 0, input);

                        throw nvae;

                    }

                    switch (alt2) {
                        case 1 :
                            // antlr/UIQuery.g:68:7: 'BEGINSWITH'
                            {
                            match("BEGINSWITH"); 



                            }
                            break;
                        case 2 :
                            // antlr/UIQuery.g:68:22: 'ENDSWITH'
                            {
                            match("ENDSWITH"); 



                            }
                            break;
                        case 3 :
                            // antlr/UIQuery.g:68:35: 'CONTAINS'
                            {
                            match("CONTAINS"); 



                            }
                            break;
                        case 4 :
                            // antlr/UIQuery.g:68:48: 'LIKE'
                            {
                            match("LIKE"); 



                            }
                            break;
                        case 5 :
                            // antlr/UIQuery.g:69:12: 'beginswith'
                            {
                            match("beginswith"); 



                            }
                            break;
                        case 6 :
                            // antlr/UIQuery.g:69:27: 'endswith'
                            {
                            match("endswith"); 



                            }
                            break;
                        case 7 :
                            // antlr/UIQuery.g:69:40: 'contains'
                            {
                            match("contains"); 



                            }
                            break;
                        case 8 :
                            // antlr/UIQuery.g:69:53: 'like'
                            {
                            match("like"); 



                            }
                            break;

                    }


                    // antlr/UIQuery.g:69:61: ( '[' ( 'a' .. 'z' | 'A' .. 'Z' )* ']' )?
                    int alt4=2;
                    int LA4_0 = input.LA(1);

                    if ( (LA4_0=='[') ) {
                        alt4=1;
                    }
                    switch (alt4) {
                        case 1 :
                            // antlr/UIQuery.g:69:62: '[' ( 'a' .. 'z' | 'A' .. 'Z' )* ']'
                            {
                            match('['); 

                            // antlr/UIQuery.g:69:66: ( 'a' .. 'z' | 'A' .. 'Z' )*
                            loop3:
                            do {
                                int alt3=2;
                                int LA3_0 = input.LA(1);

                                if ( ((LA3_0 >= 'A' && LA3_0 <= 'Z')||(LA3_0 >= 'a' && LA3_0 <= 'z')) ) {
                                    alt3=1;
                                }


                                switch (alt3) {
                            	case 1 :
                            	    // antlr/UIQuery.g:
                            	    {
                            	    if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                            	        input.consume();
                            	    }
                            	    else {
                            	        MismatchedSetException mse = new MismatchedSetException(null,input);
                            	        recover(mse);
                            	        throw mse;
                            	    }


                            	    }
                            	    break;

                            	default :
                            	    break loop3;
                                }
                            } while (true);


                            match(']'); 

                            }
                            break;

                    }


                    }


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "RELATION"

    // $ANTLR start "INT"
    public final void mINT() throws RecognitionException {
        try {
            int _type = INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // antlr/UIQuery.g:73:5: ( ( '0' .. '9' )+ )
            // antlr/UIQuery.g:73:7: ( '0' .. '9' )+
            {
            // antlr/UIQuery.g:73:7: ( '0' .. '9' )+
            int cnt6=0;
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( ((LA6_0 >= '0' && LA6_0 <= '9')) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // antlr/UIQuery.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt6 >= 1 ) break loop6;
                        EarlyExitException eee =
                            new EarlyExitException(6, input);
                        throw eee;
                }
                cnt6++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "INT"

    // $ANTLR start "BOOL"
    public final void mBOOL() throws RecognitionException {
        try {
            int _type = BOOL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // antlr/UIQuery.g:76:6: ( 'true' | 'false' )
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0=='t') ) {
                alt7=1;
            }
            else if ( (LA7_0=='f') ) {
                alt7=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;

            }
            switch (alt7) {
                case 1 :
                    // antlr/UIQuery.g:76:8: 'true'
                    {
                    match("true"); 



                    }
                    break;
                case 2 :
                    // antlr/UIQuery.g:76:17: 'false'
                    {
                    match("false"); 



                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "BOOL"

    // $ANTLR start "NIL"
    public final void mNIL() throws RecognitionException {
        try {
            int _type = NIL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // antlr/UIQuery.g:79:5: ( 'nil' | 'null' )
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0=='n') ) {
                int LA8_1 = input.LA(2);

                if ( (LA8_1=='i') ) {
                    alt8=1;
                }
                else if ( (LA8_1=='u') ) {
                    alt8=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 8, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;

            }
            switch (alt8) {
                case 1 :
                    // antlr/UIQuery.g:79:7: 'nil'
                    {
                    match("nil"); 



                    }
                    break;
                case 2 :
                    // antlr/UIQuery.g:79:15: 'null'
                    {
                    match("null"); 



                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "NIL"

    // $ANTLR start "NAME"
    public final void mNAME() throws RecognitionException {
        try {
            int _type = NAME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // antlr/UIQuery.g:82:7: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
            // antlr/UIQuery.g:82:9: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            {
            if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // antlr/UIQuery.g:82:33: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( ((LA9_0 >= '0' && LA9_0 <= '9')||(LA9_0 >= 'A' && LA9_0 <= 'Z')||LA9_0=='_'||(LA9_0 >= 'a' && LA9_0 <= 'z')) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // antlr/UIQuery.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "NAME"

    // $ANTLR start "STRING"
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // antlr/UIQuery.g:86:5: ( '\\'' ( ESC_SEQ |~ ( '\\\\' | '\\'' ) )* '\\'' )
            // antlr/UIQuery.g:86:8: '\\'' ( ESC_SEQ |~ ( '\\\\' | '\\'' ) )* '\\''
            {
            match('\''); 

            // antlr/UIQuery.g:86:13: ( ESC_SEQ |~ ( '\\\\' | '\\'' ) )*
            loop10:
            do {
                int alt10=3;
                int LA10_0 = input.LA(1);

                if ( (LA10_0=='\\') ) {
                    alt10=1;
                }
                else if ( ((LA10_0 >= '\u0000' && LA10_0 <= '&')||(LA10_0 >= '(' && LA10_0 <= '[')||(LA10_0 >= ']' && LA10_0 <= '\uFFFF')) ) {
                    alt10=2;
                }


                switch (alt10) {
            	case 1 :
            	    // antlr/UIQuery.g:86:15: ESC_SEQ
            	    {
            	    mESC_SEQ(); 


            	    }
            	    break;
            	case 2 :
            	    // antlr/UIQuery.g:86:25: ~ ( '\\\\' | '\\'' )
            	    {
            	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '&')||(input.LA(1) >= '(' && input.LA(1) <= '[')||(input.LA(1) >= ']' && input.LA(1) <= '\uFFFF') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);


            match('\''); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "STRING"

    // $ANTLR start "WHITE"
    public final void mWHITE() throws RecognitionException {
        try {
            int _type = WHITE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // antlr/UIQuery.g:89:9: ( ( ' ' )+ )
            // antlr/UIQuery.g:89:11: ( ' ' )+
            {
            // antlr/UIQuery.g:89:11: ( ' ' )+
            int cnt11=0;
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( (LA11_0==' ') ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // antlr/UIQuery.g:89:11: ' '
            	    {
            	    match(' '); 

            	    }
            	    break;

            	default :
            	    if ( cnt11 >= 1 ) break loop11;
                        EarlyExitException eee =
                            new EarlyExitException(11, input);
                        throw eee;
                }
                cnt11++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WHITE"

    // $ANTLR start "HEX_DIGIT"
    public final void mHEX_DIGIT() throws RecognitionException {
        try {
            // antlr/UIQuery.g:92:11: ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )
            // antlr/UIQuery.g:
            {
            if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "HEX_DIGIT"

    // $ANTLR start "ESC_SEQ"
    public final void mESC_SEQ() throws RecognitionException {
        try {
            // antlr/UIQuery.g:96:5: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\'' | '\\\\' ) | UNICODE_ESC | OCTAL_ESC )
            int alt12=3;
            int LA12_0 = input.LA(1);

            if ( (LA12_0=='\\') ) {
                switch ( input.LA(2) ) {
                case '\'':
                case '\\':
                case 'b':
                case 'f':
                case 'n':
                case 'r':
                case 't':
                    {
                    alt12=1;
                    }
                    break;
                case 'u':
                    {
                    alt12=2;
                    }
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                    {
                    alt12=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 12, 1, input);

                    throw nvae;

                }

            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 12, 0, input);

                throw nvae;

            }
            switch (alt12) {
                case 1 :
                    // antlr/UIQuery.g:96:9: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\'' | '\\\\' )
                    {
                    match('\\'); 

                    if ( input.LA(1)=='\''||input.LA(1)=='\\'||input.LA(1)=='b'||input.LA(1)=='f'||input.LA(1)=='n'||input.LA(1)=='r'||input.LA(1)=='t' ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;
                case 2 :
                    // antlr/UIQuery.g:97:9: UNICODE_ESC
                    {
                    mUNICODE_ESC(); 


                    }
                    break;
                case 3 :
                    // antlr/UIQuery.g:98:9: OCTAL_ESC
                    {
                    mOCTAL_ESC(); 


                    }
                    break;

            }

        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ESC_SEQ"

    // $ANTLR start "OCTAL_ESC"
    public final void mOCTAL_ESC() throws RecognitionException {
        try {
            // antlr/UIQuery.g:103:5: ( '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) )
            int alt13=3;
            int LA13_0 = input.LA(1);

            if ( (LA13_0=='\\') ) {
                int LA13_1 = input.LA(2);

                if ( ((LA13_1 >= '0' && LA13_1 <= '3')) ) {
                    int LA13_2 = input.LA(3);

                    if ( ((LA13_2 >= '0' && LA13_2 <= '7')) ) {
                        int LA13_4 = input.LA(4);

                        if ( ((LA13_4 >= '0' && LA13_4 <= '7')) ) {
                            alt13=1;
                        }
                        else {
                            alt13=2;
                        }
                    }
                    else {
                        alt13=3;
                    }
                }
                else if ( ((LA13_1 >= '4' && LA13_1 <= '7')) ) {
                    int LA13_3 = input.LA(3);

                    if ( ((LA13_3 >= '0' && LA13_3 <= '7')) ) {
                        alt13=2;
                    }
                    else {
                        alt13=3;
                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;

            }
            switch (alt13) {
                case 1 :
                    // antlr/UIQuery.g:103:9: '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' )
                    {
                    match('\\'); 

                    if ( (input.LA(1) >= '0' && input.LA(1) <= '3') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;
                case 2 :
                    // antlr/UIQuery.g:104:9: '\\\\' ( '0' .. '7' ) ( '0' .. '7' )
                    {
                    match('\\'); 

                    if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;
                case 3 :
                    // antlr/UIQuery.g:105:9: '\\\\' ( '0' .. '7' )
                    {
                    match('\\'); 

                    if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;

            }

        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "OCTAL_ESC"

    // $ANTLR start "UNICODE_ESC"
    public final void mUNICODE_ESC() throws RecognitionException {
        try {
            // antlr/UIQuery.g:110:5: ( '\\\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT )
            // antlr/UIQuery.g:110:9: '\\\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
            {
            match('\\'); 

            match('u'); 

            mHEX_DIGIT(); 


            mHEX_DIGIT(); 


            mHEX_DIGIT(); 


            mHEX_DIGIT(); 


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "UNICODE_ESC"

    public void mTokens() throws RecognitionException {
        // antlr/UIQuery.g:1:8: ( WILDCARD | QUALIFIED_NAME | ALL | VISIBLE | FILTER_COLON | BEGINPRED | ENDPRED | RELATION | INT | BOOL | NIL | NAME | STRING | WHITE )
        int alt14=14;
        alt14 = dfa14.predict(input);
        switch (alt14) {
            case 1 :
                // antlr/UIQuery.g:1:10: WILDCARD
                {
                mWILDCARD(); 


                }
                break;
            case 2 :
                // antlr/UIQuery.g:1:19: QUALIFIED_NAME
                {
                mQUALIFIED_NAME(); 


                }
                break;
            case 3 :
                // antlr/UIQuery.g:1:34: ALL
                {
                mALL(); 


                }
                break;
            case 4 :
                // antlr/UIQuery.g:1:38: VISIBLE
                {
                mVISIBLE(); 


                }
                break;
            case 5 :
                // antlr/UIQuery.g:1:46: FILTER_COLON
                {
                mFILTER_COLON(); 


                }
                break;
            case 6 :
                // antlr/UIQuery.g:1:59: BEGINPRED
                {
                mBEGINPRED(); 


                }
                break;
            case 7 :
                // antlr/UIQuery.g:1:69: ENDPRED
                {
                mENDPRED(); 


                }
                break;
            case 8 :
                // antlr/UIQuery.g:1:77: RELATION
                {
                mRELATION(); 


                }
                break;
            case 9 :
                // antlr/UIQuery.g:1:86: INT
                {
                mINT(); 


                }
                break;
            case 10 :
                // antlr/UIQuery.g:1:90: BOOL
                {
                mBOOL(); 


                }
                break;
            case 11 :
                // antlr/UIQuery.g:1:95: NIL
                {
                mNIL(); 


                }
                break;
            case 12 :
                // antlr/UIQuery.g:1:99: NAME
                {
                mNAME(); 


                }
                break;
            case 13 :
                // antlr/UIQuery.g:1:104: STRING
                {
                mSTRING(); 


                }
                break;
            case 14 :
                // antlr/UIQuery.g:1:111: WHITE
                {
                mWHITE(); 


                }
                break;

        }

    }


    protected DFA14 dfa14 = new DFA14(this);
    static final String DFA14_eotS =
        "\1\10\1\uffff\3\31\4\uffff\10\31\1\uffff\3\31\2\uffff\2\31\2\uffff"+
        "\15\31\1\66\13\31\1\102\1\31\1\uffff\4\31\1\10\3\31\1\10\1\113\1"+
        "\31\1\uffff\1\102\7\31\1\uffff\1\113\7\31\1\133\6\31\1\uffff\1\31"+
        "\2\10\1\31\2\10\2\31\2\10";
    static final String DFA14_eofS =
        "\146\uffff";
    static final String DFA14_minS =
        "\1\40\1\uffff\3\56\4\uffff\10\56\1\uffff\3\56\2\uffff\2\56\2\uffff"+
        "\33\56\1\uffff\13\56\1\uffff\10\56\1\uffff\17\56\1\uffff\12\56";
    static final String DFA14_maxS =
        "\1\175\1\uffff\3\172\4\uffff\10\172\1\uffff\3\172\2\uffff\2\172"+
        "\2\uffff\33\172\1\uffff\13\172\1\uffff\10\172\1\uffff\17\172\1\uffff"+
        "\12\172";
    static final String DFA14_acceptS =
        "\1\uffff\1\1\3\uffff\1\5\1\6\1\7\1\10\10\uffff\1\11\3\uffff\1\15"+
        "\1\16\2\uffff\1\14\1\2\33\uffff\1\3\13\uffff\1\13\10\uffff\1\12"+
        "\17\uffff\1\4\12\uffff";
    static final String DFA14_specialS =
        "\146\uffff}>";
    static final String[] DFA14_transitionS = {
            "\1\26\6\uffff\1\25\2\uffff\1\1\5\uffff\12\21\1\5\6\uffff\1\24"+
            "\1\4\1\12\1\24\1\11\6\24\1\13\16\24\4\uffff\1\24\1\uffff\1\2"+
            "\1\14\1\16\1\24\1\15\1\22\5\24\1\17\1\24\1\23\5\24\1\20\1\24"+
            "\1\3\4\24\1\6\1\uffff\1\7",
            "",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\13\30"+
            "\1\27\16\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\10\30"+
            "\1\33\21\30",
            "\1\32\1\uffff\12\30\7\uffff\4\30\1\34\25\30\4\uffff\1\30\1"+
            "\uffff\32\30",
            "",
            "",
            "",
            "",
            "\1\32\1\uffff\12\30\7\uffff\15\30\1\35\14\30\4\uffff\1\30\1"+
            "\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\16\30\1\36\13\30\4\uffff\1\30\1"+
            "\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\10\30\1\37\21\30\4\uffff\1\30\1"+
            "\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\4\30"+
            "\1\40\25\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\15\30"+
            "\1\41\14\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\16\30"+
            "\1\42\13\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\10\30"+
            "\1\43\21\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\21\30"+
            "\1\44\10\30",
            "",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\1\45"+
            "\31\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\10\30"+
            "\1\46\13\30\1\47\5\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\32\30",
            "",
            "",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\13\30"+
            "\1\50\16\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\32\30",
            "",
            "",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\22\30"+
            "\1\51\7\30",
            "\1\32\1\uffff\12\30\7\uffff\6\30\1\52\23\30\4\uffff\1\30\1"+
            "\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\3\30\1\53\26\30\4\uffff\1\30\1"+
            "\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\15\30\1\54\14\30\4\uffff\1\30\1"+
            "\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\12\30\1\55\17\30\4\uffff\1\30\1"+
            "\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\6\30"+
            "\1\56\23\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\3\30"+
            "\1\57\26\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\15\30"+
            "\1\60\14\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\12\30"+
            "\1\61\17\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\24\30"+
            "\1\62\5\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\13\30"+
            "\1\63\16\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\13\30"+
            "\1\64\16\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\13\30"+
            "\1\65\16\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\10\30"+
            "\1\67\21\30",
            "\1\32\1\uffff\12\30\7\uffff\10\30\1\70\21\30\4\uffff\1\30\1"+
            "\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\22\30\1\71\7\30\4\uffff\1\30\1"+
            "\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\23\30\1\72\6\30\4\uffff\1\30\1"+
            "\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\4\30\1\73\25\30\4\uffff\1\30\1"+
            "\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\10\30"+
            "\1\74\21\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\22\30"+
            "\1\75\7\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\23\30"+
            "\1\76\6\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\4\30"+
            "\1\77\25\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\4\30"+
            "\1\100\25\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\22\30"+
            "\1\101\7\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\13\30"+
            "\1\103\16\30",
            "",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\1\30"+
            "\1\104\30\30",
            "\1\32\1\uffff\12\30\7\uffff\15\30\1\105\14\30\4\uffff\1\30"+
            "\1\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\26\30\1\106\3\30\4\uffff\1\30\1"+
            "\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\1\107\31\30\4\uffff\1\30\1\uffff"+
            "\32\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\15\30"+
            "\1\110\14\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\26\30"+
            "\1\111\3\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\1\112"+
            "\31\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\4\30"+
            "\1\114\25\30",
            "",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\13\30"+
            "\1\115\16\30",
            "\1\32\1\uffff\12\30\7\uffff\22\30\1\116\7\30\4\uffff\1\30\1"+
            "\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\10\30\1\117\21\30\4\uffff\1\30"+
            "\1\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\10\30\1\120\21\30\4\uffff\1\30"+
            "\1\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\22\30"+
            "\1\121\7\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\10\30"+
            "\1\122\21\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\10\30"+
            "\1\123\21\30",
            "",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\4\30"+
            "\1\124\25\30",
            "\1\32\1\uffff\12\30\7\uffff\26\30\1\125\3\30\4\uffff\1\30\1"+
            "\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\23\30\1\126\6\30\4\uffff\1\30\1"+
            "\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\15\30\1\127\14\30\4\uffff\1\30"+
            "\1\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\26\30"+
            "\1\130\3\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\23\30"+
            "\1\131\6\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\15\30"+
            "\1\132\14\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\10\30\1\134\21\30\4\uffff\1\30"+
            "\1\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\7\30\1\135\22\30\4\uffff\1\30\1"+
            "\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\22\30\1\136\7\30\4\uffff\1\30\1"+
            "\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\10\30"+
            "\1\137\21\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\7\30"+
            "\1\140\22\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\22\30"+
            "\1\141\7\30",
            "",
            "\1\32\1\uffff\12\30\7\uffff\23\30\1\142\6\30\4\uffff\1\30\1"+
            "\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\23\30"+
            "\1\143\6\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\7\30\1\144\22\30\4\uffff\1\30\1"+
            "\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\7\30"+
            "\1\145\22\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\32\30",
            "\1\32\1\uffff\12\30\7\uffff\32\30\4\uffff\1\30\1\uffff\32\30"
    };

    static final short[] DFA14_eot = DFA.unpackEncodedString(DFA14_eotS);
    static final short[] DFA14_eof = DFA.unpackEncodedString(DFA14_eofS);
    static final char[] DFA14_min = DFA.unpackEncodedStringToUnsignedChars(DFA14_minS);
    static final char[] DFA14_max = DFA.unpackEncodedStringToUnsignedChars(DFA14_maxS);
    static final short[] DFA14_accept = DFA.unpackEncodedString(DFA14_acceptS);
    static final short[] DFA14_special = DFA.unpackEncodedString(DFA14_specialS);
    static final short[][] DFA14_transition;

    static {
        int numStates = DFA14_transitionS.length;
        DFA14_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA14_transition[i] = DFA.unpackEncodedString(DFA14_transitionS[i]);
        }
    }

    class DFA14 extends DFA {

        public DFA14(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 14;
            this.eot = DFA14_eot;
            this.eof = DFA14_eof;
            this.min = DFA14_min;
            this.max = DFA14_max;
            this.accept = DFA14_accept;
            this.special = DFA14_special;
            this.transition = DFA14_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( WILDCARD | QUALIFIED_NAME | ALL | VISIBLE | FILTER_COLON | BEGINPRED | ENDPRED | RELATION | INT | BOOL | NIL | NAME | STRING | WHITE );";
        }
    }
 

}