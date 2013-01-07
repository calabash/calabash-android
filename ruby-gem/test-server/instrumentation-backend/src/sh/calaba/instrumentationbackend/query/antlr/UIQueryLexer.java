// $ANTLR 3.4 antlr/UIQuery.g 2013-01-07 13:26:07

    package sh.calaba.instrumentationbackend.query.antlr;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class UIQueryLexer extends Lexer {
    public static final int EOF=-1;
    public static final int ALL=4;
    public static final int BOOL=5;
    public static final int ESC_SEQ=6;
    public static final int FILTER_COLON=7;
    public static final int HEX_DIGIT=8;
    public static final int INT=9;
    public static final int NAME=10;
    public static final int NIL=11;
    public static final int OCTAL_ESC=12;
    public static final int QUALIFIED_NAME=13;
    public static final int STRING=14;
    public static final int UNICODE_ESC=15;
    public static final int VISIBLE=16;
    public static final int WHITE=17;
    public static final int WILDCARD=18;

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
            // antlr/UIQuery.g:43:10: ( '*' )
            // antlr/UIQuery.g:43:12: '*'
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
            // antlr/UIQuery.g:45:16: ( NAME ( '.' NAME )+ )
            // antlr/UIQuery.g:45:18: NAME ( '.' NAME )+
            {
            mNAME(); 


            // antlr/UIQuery.g:45:23: ( '.' NAME )+
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
            	    // antlr/UIQuery.g:45:24: '.' NAME
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
            // antlr/UIQuery.g:49:5: ( 'all' )
            // antlr/UIQuery.g:49:7: 'all'
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
            // antlr/UIQuery.g:51:9: ( 'visible' )
            // antlr/UIQuery.g:51:11: 'visible'
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
            // antlr/UIQuery.g:56:15: ( ':' )
            // antlr/UIQuery.g:56:17: ':'
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

    // $ANTLR start "INT"
    public final void mINT() throws RecognitionException {
        try {
            int _type = INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // antlr/UIQuery.g:60:5: ( ( '0' .. '9' )+ )
            // antlr/UIQuery.g:60:7: ( '0' .. '9' )+
            {
            // antlr/UIQuery.g:60:7: ( '0' .. '9' )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0 >= '0' && LA2_0 <= '9')) ) {
                    alt2=1;
                }


                switch (alt2) {
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
    // $ANTLR end "INT"

    // $ANTLR start "BOOL"
    public final void mBOOL() throws RecognitionException {
        try {
            int _type = BOOL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // antlr/UIQuery.g:63:6: ( 'true' | 'false' )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='t') ) {
                alt3=1;
            }
            else if ( (LA3_0=='f') ) {
                alt3=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;

            }
            switch (alt3) {
                case 1 :
                    // antlr/UIQuery.g:63:8: 'true'
                    {
                    match("true"); 



                    }
                    break;
                case 2 :
                    // antlr/UIQuery.g:63:17: 'false'
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
            // antlr/UIQuery.g:66:5: ( 'nil' | 'null' )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0=='n') ) {
                int LA4_1 = input.LA(2);

                if ( (LA4_1=='i') ) {
                    alt4=1;
                }
                else if ( (LA4_1=='u') ) {
                    alt4=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 4, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;

            }
            switch (alt4) {
                case 1 :
                    // antlr/UIQuery.g:66:7: 'nil'
                    {
                    match("nil"); 



                    }
                    break;
                case 2 :
                    // antlr/UIQuery.g:66:15: 'null'
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
            // antlr/UIQuery.g:69:7: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
            // antlr/UIQuery.g:69:9: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            {
            if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // antlr/UIQuery.g:69:33: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0 >= '0' && LA5_0 <= '9')||(LA5_0 >= 'A' && LA5_0 <= 'Z')||LA5_0=='_'||(LA5_0 >= 'a' && LA5_0 <= 'z')) ) {
                    alt5=1;
                }


                switch (alt5) {
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
            	    break loop5;
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
            // antlr/UIQuery.g:73:5: ( '\\'' ( ESC_SEQ |~ ( '\\\\' | '\\'' ) )* '\\'' )
            // antlr/UIQuery.g:73:8: '\\'' ( ESC_SEQ |~ ( '\\\\' | '\\'' ) )* '\\''
            {
            match('\''); 

            // antlr/UIQuery.g:73:13: ( ESC_SEQ |~ ( '\\\\' | '\\'' ) )*
            loop6:
            do {
                int alt6=3;
                int LA6_0 = input.LA(1);

                if ( (LA6_0=='\\') ) {
                    alt6=1;
                }
                else if ( ((LA6_0 >= '\u0000' && LA6_0 <= '&')||(LA6_0 >= '(' && LA6_0 <= '[')||(LA6_0 >= ']' && LA6_0 <= '\uFFFF')) ) {
                    alt6=2;
                }


                switch (alt6) {
            	case 1 :
            	    // antlr/UIQuery.g:73:15: ESC_SEQ
            	    {
            	    mESC_SEQ(); 


            	    }
            	    break;
            	case 2 :
            	    // antlr/UIQuery.g:73:25: ~ ( '\\\\' | '\\'' )
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
            	    break loop6;
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
            // antlr/UIQuery.g:76:9: ( ( ' ' )+ )
            // antlr/UIQuery.g:76:11: ( ' ' )+
            {
            // antlr/UIQuery.g:76:11: ( ' ' )+
            int cnt7=0;
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==' ') ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // antlr/UIQuery.g:76:11: ' '
            	    {
            	    match(' '); 

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
    // $ANTLR end "WHITE"

    // $ANTLR start "HEX_DIGIT"
    public final void mHEX_DIGIT() throws RecognitionException {
        try {
            // antlr/UIQuery.g:79:11: ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )
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
            // antlr/UIQuery.g:83:5: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\'' | '\\\\' ) | UNICODE_ESC | OCTAL_ESC )
            int alt8=3;
            int LA8_0 = input.LA(1);

            if ( (LA8_0=='\\') ) {
                switch ( input.LA(2) ) {
                case '\'':
                case '\\':
                case 'b':
                case 'f':
                case 'n':
                case 'r':
                case 't':
                    {
                    alt8=1;
                    }
                    break;
                case 'u':
                    {
                    alt8=2;
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
                    alt8=3;
                    }
                    break;
                default:
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
                    // antlr/UIQuery.g:83:9: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\'' | '\\\\' )
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
                    // antlr/UIQuery.g:84:9: UNICODE_ESC
                    {
                    mUNICODE_ESC(); 


                    }
                    break;
                case 3 :
                    // antlr/UIQuery.g:85:9: OCTAL_ESC
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
            // antlr/UIQuery.g:90:5: ( '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) )
            int alt9=3;
            int LA9_0 = input.LA(1);

            if ( (LA9_0=='\\') ) {
                int LA9_1 = input.LA(2);

                if ( ((LA9_1 >= '0' && LA9_1 <= '3')) ) {
                    int LA9_2 = input.LA(3);

                    if ( ((LA9_2 >= '0' && LA9_2 <= '7')) ) {
                        int LA9_4 = input.LA(4);

                        if ( ((LA9_4 >= '0' && LA9_4 <= '7')) ) {
                            alt9=1;
                        }
                        else {
                            alt9=2;
                        }
                    }
                    else {
                        alt9=3;
                    }
                }
                else if ( ((LA9_1 >= '4' && LA9_1 <= '7')) ) {
                    int LA9_3 = input.LA(3);

                    if ( ((LA9_3 >= '0' && LA9_3 <= '7')) ) {
                        alt9=2;
                    }
                    else {
                        alt9=3;
                    }
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
                    // antlr/UIQuery.g:90:9: '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' )
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
                    // antlr/UIQuery.g:91:9: '\\\\' ( '0' .. '7' ) ( '0' .. '7' )
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
                    // antlr/UIQuery.g:92:9: '\\\\' ( '0' .. '7' )
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
            // antlr/UIQuery.g:97:5: ( '\\\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT )
            // antlr/UIQuery.g:97:9: '\\\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
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
        // antlr/UIQuery.g:1:8: ( WILDCARD | QUALIFIED_NAME | ALL | VISIBLE | FILTER_COLON | INT | BOOL | NIL | NAME | STRING | WHITE )
        int alt10=11;
        alt10 = dfa10.predict(input);
        switch (alt10) {
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
                // antlr/UIQuery.g:1:59: INT
                {
                mINT(); 


                }
                break;
            case 7 :
                // antlr/UIQuery.g:1:63: BOOL
                {
                mBOOL(); 


                }
                break;
            case 8 :
                // antlr/UIQuery.g:1:68: NIL
                {
                mNIL(); 


                }
                break;
            case 9 :
                // antlr/UIQuery.g:1:72: NAME
                {
                mNAME(); 


                }
                break;
            case 10 :
                // antlr/UIQuery.g:1:77: STRING
                {
                mSTRING(); 


                }
                break;
            case 11 :
                // antlr/UIQuery.g:1:84: WHITE
                {
                mWHITE(); 


                }
                break;

        }

    }


    protected DFA10 dfa10 = new DFA10(this);
    static final String DFA10_eotS =
        "\2\uffff\3\16\2\uffff\3\16\2\uffff\2\16\2\uffff\5\16\1\33\3\16\1"+
        "\37\1\16\1\uffff\1\16\1\42\1\16\1\uffff\1\37\1\16\1\uffff\1\42\1"+
        "\16\1\46\1\uffff";
    static final String DFA10_eofS =
        "\47\uffff";
    static final String DFA10_minS =
        "\1\40\1\uffff\3\56\2\uffff\3\56\2\uffff\2\56\2\uffff\13\56\1\uffff"+
        "\3\56\1\uffff\2\56\1\uffff\3\56\1\uffff";
    static final String DFA10_maxS =
        "\1\172\1\uffff\3\172\2\uffff\3\172\2\uffff\2\172\2\uffff\13\172"+
        "\1\uffff\3\172\1\uffff\2\172\1\uffff\3\172\1\uffff";
    static final String DFA10_acceptS =
        "\1\uffff\1\1\3\uffff\1\5\1\6\3\uffff\1\12\1\13\2\uffff\1\11\1\2"+
        "\13\uffff\1\3\3\uffff\1\10\2\uffff\1\7\3\uffff\1\4";
    static final String DFA10_specialS =
        "\47\uffff}>";
    static final String[] DFA10_transitionS = {
            "\1\13\6\uffff\1\12\2\uffff\1\1\5\uffff\12\6\1\5\6\uffff\32\11"+
            "\4\uffff\1\11\1\uffff\1\2\4\11\1\7\7\11\1\10\5\11\1\4\1\11\1"+
            "\3\4\11",
            "",
            "\1\17\1\uffff\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\13\15"+
            "\1\14\16\15",
            "\1\17\1\uffff\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\10\15"+
            "\1\20\21\15",
            "\1\17\1\uffff\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\21\15"+
            "\1\21\10\15",
            "",
            "",
            "\1\17\1\uffff\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\1\22"+
            "\31\15",
            "\1\17\1\uffff\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\10\15"+
            "\1\23\13\15\1\24\5\15",
            "\1\17\1\uffff\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\32\15",
            "",
            "",
            "\1\17\1\uffff\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\13\15"+
            "\1\25\16\15",
            "\1\17\1\uffff\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\32\15",
            "",
            "",
            "\1\17\1\uffff\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\22\15"+
            "\1\26\7\15",
            "\1\17\1\uffff\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\24\15"+
            "\1\27\5\15",
            "\1\17\1\uffff\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\13\15"+
            "\1\30\16\15",
            "\1\17\1\uffff\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\13\15"+
            "\1\31\16\15",
            "\1\17\1\uffff\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\13\15"+
            "\1\32\16\15",
            "\1\17\1\uffff\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\32\15",
            "\1\17\1\uffff\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\10\15"+
            "\1\34\21\15",
            "\1\17\1\uffff\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\4\15"+
            "\1\35\25\15",
            "\1\17\1\uffff\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\22\15"+
            "\1\36\7\15",
            "\1\17\1\uffff\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\32\15",
            "\1\17\1\uffff\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\13\15"+
            "\1\40\16\15",
            "",
            "\1\17\1\uffff\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\1\15"+
            "\1\41\30\15",
            "\1\17\1\uffff\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\32\15",
            "\1\17\1\uffff\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\4\15"+
            "\1\43\25\15",
            "",
            "\1\17\1\uffff\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\32\15",
            "\1\17\1\uffff\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\13\15"+
            "\1\44\16\15",
            "",
            "\1\17\1\uffff\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\32\15",
            "\1\17\1\uffff\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\4\15"+
            "\1\45\25\15",
            "\1\17\1\uffff\12\15\7\uffff\32\15\4\uffff\1\15\1\uffff\32\15",
            ""
    };

    static final short[] DFA10_eot = DFA.unpackEncodedString(DFA10_eotS);
    static final short[] DFA10_eof = DFA.unpackEncodedString(DFA10_eofS);
    static final char[] DFA10_min = DFA.unpackEncodedStringToUnsignedChars(DFA10_minS);
    static final char[] DFA10_max = DFA.unpackEncodedStringToUnsignedChars(DFA10_maxS);
    static final short[] DFA10_accept = DFA.unpackEncodedString(DFA10_acceptS);
    static final short[] DFA10_special = DFA.unpackEncodedString(DFA10_specialS);
    static final short[][] DFA10_transition;

    static {
        int numStates = DFA10_transitionS.length;
        DFA10_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA10_transition[i] = DFA.unpackEncodedString(DFA10_transitionS[i]);
        }
    }

    class DFA10 extends DFA {

        public DFA10(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 10;
            this.eot = DFA10_eot;
            this.eof = DFA10_eof;
            this.min = DFA10_min;
            this.max = DFA10_max;
            this.accept = DFA10_accept;
            this.special = DFA10_special;
            this.transition = DFA10_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( WILDCARD | QUALIFIED_NAME | ALL | VISIBLE | FILTER_COLON | INT | BOOL | NIL | NAME | STRING | WHITE );";
        }
    }
 

}