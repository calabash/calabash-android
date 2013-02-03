// $ANTLR 3.4 antlr/UIQuery.g 2013-02-02 18:08:57

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
    public static final int DIRECTION=7;
    public static final int ENDPRED=8;
    public static final int ESC_SEQ=9;
    public static final int FILTER_COLON=10;
    public static final int HEX_DIGIT=11;
    public static final int INT=12;
    public static final int NAME=13;
    public static final int NIL=14;
    public static final int OCTAL_ESC=15;
    public static final int QUALIFIED_NAME=16;
    public static final int RELATION=17;
    public static final int STRING=18;
    public static final int UNICODE_ESC=19;
    public static final int VISIBLE=20;
    public static final int WHITE=21;
    public static final int WILDCARD=22;

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

    // $ANTLR start "DIRECTION"
    public final void mDIRECTION() throws RecognitionException {
        try {
            int _type = DIRECTION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // antlr/UIQuery.g:42:11: ( 'descendant' | 'child' | 'parent' | 'sibling' )
            int alt1=4;
            switch ( input.LA(1) ) {
            case 'd':
                {
                alt1=1;
                }
                break;
            case 'c':
                {
                alt1=2;
                }
                break;
            case 'p':
                {
                alt1=3;
                }
                break;
            case 's':
                {
                alt1=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;

            }

            switch (alt1) {
                case 1 :
                    // antlr/UIQuery.g:42:13: 'descendant'
                    {
                    match("descendant"); 



                    }
                    break;
                case 2 :
                    // antlr/UIQuery.g:42:28: 'child'
                    {
                    match("child"); 



                    }
                    break;
                case 3 :
                    // antlr/UIQuery.g:42:38: 'parent'
                    {
                    match("parent"); 



                    }
                    break;
                case 4 :
                    // antlr/UIQuery.g:42:49: 'sibling'
                    {
                    match("sibling"); 



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
    // $ANTLR end "DIRECTION"

    // $ANTLR start "WILDCARD"
    public final void mWILDCARD() throws RecognitionException {
        try {
            int _type = WILDCARD;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // antlr/UIQuery.g:47:10: ( '*' )
            // antlr/UIQuery.g:47:12: '*'
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
            // antlr/UIQuery.g:49:16: ( NAME ( '.' NAME )+ )
            // antlr/UIQuery.g:49:18: NAME ( '.' NAME )+
            {
            mNAME(); 


            // antlr/UIQuery.g:49:23: ( '.' NAME )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0=='.') ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // antlr/UIQuery.g:49:24: '.' NAME
            	    {
            	    match('.'); 

            	    mNAME(); 


            	    }
            	    break;

            	default :
            	    if ( cnt2 >= 1 ) break loop2;
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
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
            // antlr/UIQuery.g:53:5: ( 'all' )
            // antlr/UIQuery.g:53:7: 'all'
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
            // antlr/UIQuery.g:55:9: ( 'visible' )
            // antlr/UIQuery.g:55:11: 'visible'
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
            // antlr/UIQuery.g:60:15: ( ':' )
            // antlr/UIQuery.g:60:17: ':'
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
            // antlr/UIQuery.g:65:11: ( '{' )
            // antlr/UIQuery.g:65:13: '{'
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
            // antlr/UIQuery.g:67:11: ( '}' )
            // antlr/UIQuery.g:67:13: '}'
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
            // antlr/UIQuery.g:70:10: (| '=' | '>' | '>=' | '<' | '<=' | ( ( 'BEGINSWITH' | 'ENDSWITH' | 'CONTAINS' | 'LIKE' | 'beginswith' | 'endswith' | 'contains' | 'like' ) ( '[' ( 'a' .. 'z' | 'A' .. 'Z' )* ']' )? ) )
            int alt6=7;
            switch ( input.LA(1) ) {
            case '=':
                {
                alt6=2;
                }
                break;
            case '>':
                {
                int LA6_3 = input.LA(2);

                if ( (LA6_3=='=') ) {
                    alt6=4;
                }
                else {
                    alt6=3;
                }
                }
                break;
            case '<':
                {
                int LA6_4 = input.LA(2);

                if ( (LA6_4=='=') ) {
                    alt6=6;
                }
                else {
                    alt6=5;
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
                alt6=7;
                }
                break;
            default:
                alt6=1;
            }

            switch (alt6) {
                case 1 :
                    // antlr/UIQuery.g:70:12: 
                    {
                    }
                    break;
                case 2 :
                    // antlr/UIQuery.g:70:14: '='
                    {
                    match('='); 

                    }
                    break;
                case 3 :
                    // antlr/UIQuery.g:70:20: '>'
                    {
                    match('>'); 

                    }
                    break;
                case 4 :
                    // antlr/UIQuery.g:70:26: '>='
                    {
                    match(">="); 



                    }
                    break;
                case 5 :
                    // antlr/UIQuery.g:70:33: '<'
                    {
                    match('<'); 

                    }
                    break;
                case 6 :
                    // antlr/UIQuery.g:70:39: '<='
                    {
                    match("<="); 



                    }
                    break;
                case 7 :
                    // antlr/UIQuery.g:71:4: ( ( 'BEGINSWITH' | 'ENDSWITH' | 'CONTAINS' | 'LIKE' | 'beginswith' | 'endswith' | 'contains' | 'like' ) ( '[' ( 'a' .. 'z' | 'A' .. 'Z' )* ']' )? )
                    {
                    // antlr/UIQuery.g:71:4: ( ( 'BEGINSWITH' | 'ENDSWITH' | 'CONTAINS' | 'LIKE' | 'beginswith' | 'endswith' | 'contains' | 'like' ) ( '[' ( 'a' .. 'z' | 'A' .. 'Z' )* ']' )? )
                    // antlr/UIQuery.g:71:5: ( 'BEGINSWITH' | 'ENDSWITH' | 'CONTAINS' | 'LIKE' | 'beginswith' | 'endswith' | 'contains' | 'like' ) ( '[' ( 'a' .. 'z' | 'A' .. 'Z' )* ']' )?
                    {
                    // antlr/UIQuery.g:71:5: ( 'BEGINSWITH' | 'ENDSWITH' | 'CONTAINS' | 'LIKE' | 'beginswith' | 'endswith' | 'contains' | 'like' )
                    int alt3=8;
                    switch ( input.LA(1) ) {
                    case 'B':
                        {
                        alt3=1;
                        }
                        break;
                    case 'E':
                        {
                        alt3=2;
                        }
                        break;
                    case 'C':
                        {
                        alt3=3;
                        }
                        break;
                    case 'L':
                        {
                        alt3=4;
                        }
                        break;
                    case 'b':
                        {
                        alt3=5;
                        }
                        break;
                    case 'e':
                        {
                        alt3=6;
                        }
                        break;
                    case 'c':
                        {
                        alt3=7;
                        }
                        break;
                    case 'l':
                        {
                        alt3=8;
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 3, 0, input);

                        throw nvae;

                    }

                    switch (alt3) {
                        case 1 :
                            // antlr/UIQuery.g:71:7: 'BEGINSWITH'
                            {
                            match("BEGINSWITH"); 



                            }
                            break;
                        case 2 :
                            // antlr/UIQuery.g:71:22: 'ENDSWITH'
                            {
                            match("ENDSWITH"); 



                            }
                            break;
                        case 3 :
                            // antlr/UIQuery.g:71:35: 'CONTAINS'
                            {
                            match("CONTAINS"); 



                            }
                            break;
                        case 4 :
                            // antlr/UIQuery.g:71:48: 'LIKE'
                            {
                            match("LIKE"); 



                            }
                            break;
                        case 5 :
                            // antlr/UIQuery.g:72:12: 'beginswith'
                            {
                            match("beginswith"); 



                            }
                            break;
                        case 6 :
                            // antlr/UIQuery.g:72:27: 'endswith'
                            {
                            match("endswith"); 



                            }
                            break;
                        case 7 :
                            // antlr/UIQuery.g:72:40: 'contains'
                            {
                            match("contains"); 



                            }
                            break;
                        case 8 :
                            // antlr/UIQuery.g:72:53: 'like'
                            {
                            match("like"); 



                            }
                            break;

                    }


                    // antlr/UIQuery.g:72:61: ( '[' ( 'a' .. 'z' | 'A' .. 'Z' )* ']' )?
                    int alt5=2;
                    int LA5_0 = input.LA(1);

                    if ( (LA5_0=='[') ) {
                        alt5=1;
                    }
                    switch (alt5) {
                        case 1 :
                            // antlr/UIQuery.g:72:62: '[' ( 'a' .. 'z' | 'A' .. 'Z' )* ']'
                            {
                            match('['); 

                            // antlr/UIQuery.g:72:66: ( 'a' .. 'z' | 'A' .. 'Z' )*
                            loop4:
                            do {
                                int alt4=2;
                                int LA4_0 = input.LA(1);

                                if ( ((LA4_0 >= 'A' && LA4_0 <= 'Z')||(LA4_0 >= 'a' && LA4_0 <= 'z')) ) {
                                    alt4=1;
                                }


                                switch (alt4) {
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
                            	    break loop4;
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
            // antlr/UIQuery.g:76:5: ( ( '0' .. '9' )+ )
            // antlr/UIQuery.g:76:7: ( '0' .. '9' )+
            {
            // antlr/UIQuery.g:76:7: ( '0' .. '9' )+
            int cnt7=0;
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( ((LA7_0 >= '0' && LA7_0 <= '9')) ) {
                    alt7=1;
                }


                switch (alt7) {
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
            	    if ( cnt7 >= 1 ) break loop7;
                        EarlyExitException eee =
                            new EarlyExitException(7, input);
                        throw eee;
                }
                cnt7++;
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
            // antlr/UIQuery.g:79:6: ( 'true' | 'false' )
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0=='t') ) {
                alt8=1;
            }
            else if ( (LA8_0=='f') ) {
                alt8=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;

            }
            switch (alt8) {
                case 1 :
                    // antlr/UIQuery.g:79:8: 'true'
                    {
                    match("true"); 



                    }
                    break;
                case 2 :
                    // antlr/UIQuery.g:79:17: 'false'
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
            // antlr/UIQuery.g:82:5: ( 'nil' | 'null' )
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0=='n') ) {
                int LA9_1 = input.LA(2);

                if ( (LA9_1=='i') ) {
                    alt9=1;
                }
                else if ( (LA9_1=='u') ) {
                    alt9=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 9, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;

            }
            switch (alt9) {
                case 1 :
                    // antlr/UIQuery.g:82:7: 'nil'
                    {
                    match("nil"); 



                    }
                    break;
                case 2 :
                    // antlr/UIQuery.g:82:15: 'null'
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
            // antlr/UIQuery.g:85:7: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
            // antlr/UIQuery.g:85:9: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            {
            if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // antlr/UIQuery.g:85:33: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( ((LA10_0 >= '0' && LA10_0 <= '9')||(LA10_0 >= 'A' && LA10_0 <= 'Z')||LA10_0=='_'||(LA10_0 >= 'a' && LA10_0 <= 'z')) ) {
                    alt10=1;
                }


                switch (alt10) {
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
            	    break loop10;
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
            // antlr/UIQuery.g:89:5: ( '\\'' ( ESC_SEQ |~ ( '\\\\' | '\\'' ) )* '\\'' )
            // antlr/UIQuery.g:89:8: '\\'' ( ESC_SEQ |~ ( '\\\\' | '\\'' ) )* '\\''
            {
            match('\''); 

            // antlr/UIQuery.g:89:13: ( ESC_SEQ |~ ( '\\\\' | '\\'' ) )*
            loop11:
            do {
                int alt11=3;
                int LA11_0 = input.LA(1);

                if ( (LA11_0=='\\') ) {
                    alt11=1;
                }
                else if ( ((LA11_0 >= '\u0000' && LA11_0 <= '&')||(LA11_0 >= '(' && LA11_0 <= '[')||(LA11_0 >= ']' && LA11_0 <= '\uFFFF')) ) {
                    alt11=2;
                }


                switch (alt11) {
            	case 1 :
            	    // antlr/UIQuery.g:89:15: ESC_SEQ
            	    {
            	    mESC_SEQ(); 


            	    }
            	    break;
            	case 2 :
            	    // antlr/UIQuery.g:89:25: ~ ( '\\\\' | '\\'' )
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
            	    break loop11;
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
            // antlr/UIQuery.g:92:9: ( ( ' ' )+ )
            // antlr/UIQuery.g:92:11: ( ' ' )+
            {
            // antlr/UIQuery.g:92:11: ( ' ' )+
            int cnt12=0;
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( (LA12_0==' ') ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // antlr/UIQuery.g:92:11: ' '
            	    {
            	    match(' '); 

            	    }
            	    break;

            	default :
            	    if ( cnt12 >= 1 ) break loop12;
                        EarlyExitException eee =
                            new EarlyExitException(12, input);
                        throw eee;
                }
                cnt12++;
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
            // antlr/UIQuery.g:95:11: ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )
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
            // antlr/UIQuery.g:99:5: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\'' | '\\\\' ) | UNICODE_ESC | OCTAL_ESC )
            int alt13=3;
            int LA13_0 = input.LA(1);

            if ( (LA13_0=='\\') ) {
                switch ( input.LA(2) ) {
                case '\'':
                case '\\':
                case 'b':
                case 'f':
                case 'n':
                case 'r':
                case 't':
                    {
                    alt13=1;
                    }
                    break;
                case 'u':
                    {
                    alt13=2;
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
                    alt13=3;
                    }
                    break;
                default:
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
                    // antlr/UIQuery.g:99:9: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\'' | '\\\\' )
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
                    // antlr/UIQuery.g:100:9: UNICODE_ESC
                    {
                    mUNICODE_ESC(); 


                    }
                    break;
                case 3 :
                    // antlr/UIQuery.g:101:9: OCTAL_ESC
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
            // antlr/UIQuery.g:106:5: ( '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) )
            int alt14=3;
            int LA14_0 = input.LA(1);

            if ( (LA14_0=='\\') ) {
                int LA14_1 = input.LA(2);

                if ( ((LA14_1 >= '0' && LA14_1 <= '3')) ) {
                    int LA14_2 = input.LA(3);

                    if ( ((LA14_2 >= '0' && LA14_2 <= '7')) ) {
                        int LA14_4 = input.LA(4);

                        if ( ((LA14_4 >= '0' && LA14_4 <= '7')) ) {
                            alt14=1;
                        }
                        else {
                            alt14=2;
                        }
                    }
                    else {
                        alt14=3;
                    }
                }
                else if ( ((LA14_1 >= '4' && LA14_1 <= '7')) ) {
                    int LA14_3 = input.LA(3);

                    if ( ((LA14_3 >= '0' && LA14_3 <= '7')) ) {
                        alt14=2;
                    }
                    else {
                        alt14=3;
                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 14, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 14, 0, input);

                throw nvae;

            }
            switch (alt14) {
                case 1 :
                    // antlr/UIQuery.g:106:9: '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' )
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
                    // antlr/UIQuery.g:107:9: '\\\\' ( '0' .. '7' ) ( '0' .. '7' )
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
                    // antlr/UIQuery.g:108:9: '\\\\' ( '0' .. '7' )
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
            // antlr/UIQuery.g:113:5: ( '\\\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT )
            // antlr/UIQuery.g:113:9: '\\\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
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
        // antlr/UIQuery.g:1:8: ( DIRECTION | WILDCARD | QUALIFIED_NAME | ALL | VISIBLE | FILTER_COLON | BEGINPRED | ENDPRED | RELATION | INT | BOOL | NIL | NAME | STRING | WHITE )
        int alt15=15;
        alt15 = dfa15.predict(input);
        switch (alt15) {
            case 1 :
                // antlr/UIQuery.g:1:10: DIRECTION
                {
                mDIRECTION(); 


                }
                break;
            case 2 :
                // antlr/UIQuery.g:1:20: WILDCARD
                {
                mWILDCARD(); 


                }
                break;
            case 3 :
                // antlr/UIQuery.g:1:29: QUALIFIED_NAME
                {
                mQUALIFIED_NAME(); 


                }
                break;
            case 4 :
                // antlr/UIQuery.g:1:44: ALL
                {
                mALL(); 


                }
                break;
            case 5 :
                // antlr/UIQuery.g:1:48: VISIBLE
                {
                mVISIBLE(); 


                }
                break;
            case 6 :
                // antlr/UIQuery.g:1:56: FILTER_COLON
                {
                mFILTER_COLON(); 


                }
                break;
            case 7 :
                // antlr/UIQuery.g:1:69: BEGINPRED
                {
                mBEGINPRED(); 


                }
                break;
            case 8 :
                // antlr/UIQuery.g:1:79: ENDPRED
                {
                mENDPRED(); 


                }
                break;
            case 9 :
                // antlr/UIQuery.g:1:87: RELATION
                {
                mRELATION(); 


                }
                break;
            case 10 :
                // antlr/UIQuery.g:1:96: INT
                {
                mINT(); 


                }
                break;
            case 11 :
                // antlr/UIQuery.g:1:100: BOOL
                {
                mBOOL(); 


                }
                break;
            case 12 :
                // antlr/UIQuery.g:1:105: NIL
                {
                mNIL(); 


                }
                break;
            case 13 :
                // antlr/UIQuery.g:1:109: NAME
                {
                mNAME(); 


                }
                break;
            case 14 :
                // antlr/UIQuery.g:1:114: STRING
                {
                mSTRING(); 


                }
                break;
            case 15 :
                // antlr/UIQuery.g:1:121: WHITE
                {
                mWHITE(); 


                }
                break;

        }

    }


    protected DFA15 dfa15 = new DFA15(this);
    static final String DFA15_eotS =
        "\1\14\4\34\1\uffff\3\34\4\uffff\7\34\1\uffff\3\34\2\uffff\2\34\2"+
        "\uffff\26\34\1\106\12\34\1\121\6\34\1\uffff\4\34\1\14\2\34\1\14"+
        "\1\136\1\34\1\uffff\1\121\1\34\1\141\11\34\1\uffff\1\136\1\34\1"+
        "\uffff\1\34\1\141\11\34\1\141\1\166\6\34\1\14\1\uffff\1\34\2\14"+
        "\1\34\1\14\3\34\1\141\2\14";
    static final String DFA15_eofS =
        "\u0082\uffff";
    static final String DFA15_minS =
        "\1\40\4\56\1\uffff\3\56\4\uffff\7\56\1\uffff\3\56\2\uffff\2\56\2"+
        "\uffff\50\56\1\uffff\12\56\1\uffff\14\56\1\uffff\2\56\1\uffff\24"+
        "\56\1\uffff\13\56";
    static final String DFA15_maxS =
        "\1\175\4\172\1\uffff\3\172\4\uffff\7\172\1\uffff\3\172\2\uffff\2"+
        "\172\2\uffff\50\172\1\uffff\12\172\1\uffff\14\172\1\uffff\2\172"+
        "\1\uffff\24\172\1\uffff\13\172";
    static final String DFA15_acceptS =
        "\5\uffff\1\2\3\uffff\1\6\1\7\1\10\1\11\7\uffff\1\12\3\uffff\1\16"+
        "\1\17\2\uffff\1\15\1\3\50\uffff\1\4\12\uffff\1\14\14\uffff\1\13"+
        "\2\uffff\1\1\24\uffff\1\5\13\uffff";
    static final String DFA15_specialS =
        "\u0082\uffff}>";
    static final String[] DFA15_transitionS = {
            "\1\31\6\uffff\1\30\2\uffff\1\5\5\uffff\12\24\1\11\6\uffff\1"+
            "\27\1\10\1\16\1\27\1\15\6\27\1\17\16\27\4\uffff\1\27\1\uffff"+
            "\1\6\1\20\1\2\1\1\1\21\1\25\5\27\1\22\1\27\1\26\1\27\1\3\2\27"+
            "\1\4\1\23\1\27\1\7\4\27\1\12\1\uffff\1\13",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\4\33"+
            "\1\32\25\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\7\33"+
            "\1\36\6\33\1\37\13\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\1\40"+
            "\31\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\10\33"+
            "\1\41\21\33",
            "",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\13\33"+
            "\1\42\16\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\10\33"+
            "\1\43\21\33",
            "\1\35\1\uffff\12\33\7\uffff\4\33\1\44\25\33\4\uffff\1\33\1"+
            "\uffff\32\33",
            "",
            "",
            "",
            "",
            "\1\35\1\uffff\12\33\7\uffff\15\33\1\45\14\33\4\uffff\1\33\1"+
            "\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\16\33\1\46\13\33\4\uffff\1\33\1"+
            "\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\10\33\1\47\21\33\4\uffff\1\33\1"+
            "\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\4\33"+
            "\1\50\25\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\15\33"+
            "\1\51\14\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\10\33"+
            "\1\52\21\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\21\33"+
            "\1\53\10\33",
            "",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\1\54"+
            "\31\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\10\33"+
            "\1\55\13\33\1\56\5\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "",
            "",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\22\33"+
            "\1\57\7\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "",
            "",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\10\33"+
            "\1\60\21\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\15\33"+
            "\1\61\14\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\21\33"+
            "\1\62\10\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\1\33"+
            "\1\63\30\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\13\33"+
            "\1\64\16\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\22\33"+
            "\1\65\7\33",
            "\1\35\1\uffff\12\33\7\uffff\6\33\1\66\23\33\4\uffff\1\33\1"+
            "\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\3\33\1\67\26\33\4\uffff\1\33\1"+
            "\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\15\33\1\70\14\33\4\uffff\1\33\1"+
            "\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\12\33\1\71\17\33\4\uffff\1\33\1"+
            "\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\6\33"+
            "\1\72\23\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\3\33"+
            "\1\73\26\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\12\33"+
            "\1\74\17\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\24\33"+
            "\1\75\5\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\13\33"+
            "\1\76\16\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\13\33"+
            "\1\77\16\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\13\33"+
            "\1\100\16\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\2\33"+
            "\1\101\27\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\13\33"+
            "\1\102\16\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\23\33"+
            "\1\103\6\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\4\33"+
            "\1\104\25\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\13\33"+
            "\1\105\16\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\10\33"+
            "\1\107\21\33",
            "\1\35\1\uffff\12\33\7\uffff\10\33\1\110\21\33\4\uffff\1\33"+
            "\1\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\22\33\1\111\7\33\4\uffff\1\33\1"+
            "\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\23\33\1\112\6\33\4\uffff\1\33\1"+
            "\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\4\33\1\113\25\33\4\uffff\1\33\1"+
            "\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\10\33"+
            "\1\114\21\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\22\33"+
            "\1\115\7\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\4\33"+
            "\1\116\25\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\4\33"+
            "\1\117\25\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\22\33"+
            "\1\120\7\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\13\33"+
            "\1\122\16\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\4\33"+
            "\1\123\25\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\3\33"+
            "\1\124\26\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\1\125"+
            "\31\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\15\33"+
            "\1\126\14\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\10\33"+
            "\1\127\21\33",
            "",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\1\33"+
            "\1\130\30\33",
            "\1\35\1\uffff\12\33\7\uffff\15\33\1\131\14\33\4\uffff\1\33"+
            "\1\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\26\33\1\132\3\33\4\uffff\1\33\1"+
            "\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\1\133\31\33\4\uffff\1\33\1\uffff"+
            "\32\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\15\33"+
            "\1\134\14\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\26\33"+
            "\1\135\3\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\4\33"+
            "\1\137\25\33",
            "",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\15\33"+
            "\1\140\14\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\10\33"+
            "\1\142\21\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\23\33"+
            "\1\143\6\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\15\33"+
            "\1\144\14\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\13\33"+
            "\1\145\16\33",
            "\1\35\1\uffff\12\33\7\uffff\22\33\1\146\7\33\4\uffff\1\33\1"+
            "\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\10\33\1\147\21\33\4\uffff\1\33"+
            "\1\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\10\33\1\150\21\33\4\uffff\1\33"+
            "\1\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\22\33"+
            "\1\151\7\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\10\33"+
            "\1\152\21\33",
            "",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\3\33"+
            "\1\153\26\33",
            "",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\15\33"+
            "\1\154\14\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\6\33"+
            "\1\155\23\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\4\33"+
            "\1\156\25\33",
            "\1\35\1\uffff\12\33\7\uffff\26\33\1\157\3\33\4\uffff\1\33\1"+
            "\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\23\33\1\160\6\33\4\uffff\1\33\1"+
            "\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\15\33\1\161\14\33\4\uffff\1\33"+
            "\1\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\26\33"+
            "\1\162\3\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\23\33"+
            "\1\163\6\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\1\164"+
            "\31\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\22\33"+
            "\1\165\7\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\10\33\1\167\21\33\4\uffff\1\33"+
            "\1\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\7\33\1\170\22\33\4\uffff\1\33\1"+
            "\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\22\33\1\171\7\33\4\uffff\1\33\1"+
            "\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\10\33"+
            "\1\172\21\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\7\33"+
            "\1\173\22\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\15\33"+
            "\1\174\14\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "",
            "\1\35\1\uffff\12\33\7\uffff\23\33\1\175\6\33\4\uffff\1\33\1"+
            "\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\23\33"+
            "\1\176\6\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\23\33"+
            "\1\177\6\33",
            "\1\35\1\uffff\12\33\7\uffff\7\33\1\u0080\22\33\4\uffff\1\33"+
            "\1\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\7\33"+
            "\1\u0081\22\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\35\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33"
    };

    static final short[] DFA15_eot = DFA.unpackEncodedString(DFA15_eotS);
    static final short[] DFA15_eof = DFA.unpackEncodedString(DFA15_eofS);
    static final char[] DFA15_min = DFA.unpackEncodedStringToUnsignedChars(DFA15_minS);
    static final char[] DFA15_max = DFA.unpackEncodedStringToUnsignedChars(DFA15_maxS);
    static final short[] DFA15_accept = DFA.unpackEncodedString(DFA15_acceptS);
    static final short[] DFA15_special = DFA.unpackEncodedString(DFA15_specialS);
    static final short[][] DFA15_transition;

    static {
        int numStates = DFA15_transitionS.length;
        DFA15_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA15_transition[i] = DFA.unpackEncodedString(DFA15_transitionS[i]);
        }
    }

    class DFA15 extends DFA {

        public DFA15(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 15;
            this.eot = DFA15_eot;
            this.eof = DFA15_eof;
            this.min = DFA15_min;
            this.max = DFA15_max;
            this.accept = DFA15_accept;
            this.special = DFA15_special;
            this.transition = DFA15_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( DIRECTION | WILDCARD | QUALIFIED_NAME | ALL | VISIBLE | FILTER_COLON | BEGINPRED | ENDPRED | RELATION | INT | BOOL | NIL | NAME | STRING | WHITE );";
        }
    }
 

}