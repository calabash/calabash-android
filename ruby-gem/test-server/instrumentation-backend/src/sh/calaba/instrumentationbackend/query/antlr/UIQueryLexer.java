// $ANTLR 3.4 antlr/UIQuery.g 2013-01-23 13:00:35

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

    // $ANTLR start "BEGINPRED"
    public final void mBEGINPRED() throws RecognitionException {
        try {
            int _type = BEGINPRED;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // antlr/UIQuery.g:61:11: ( '{' )
            // antlr/UIQuery.g:61:13: '{'
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
            // antlr/UIQuery.g:63:11: ( '}' )
            // antlr/UIQuery.g:63:13: '}'
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
            // antlr/UIQuery.g:66:10: ( ( 'BEGINSWITH' | 'ENDSWITH' | 'CONTAINS' | 'LIKE' ) ( '[c]' )? )
            // antlr/UIQuery.g:66:12: ( 'BEGINSWITH' | 'ENDSWITH' | 'CONTAINS' | 'LIKE' ) ( '[c]' )?
            {
            // antlr/UIQuery.g:66:12: ( 'BEGINSWITH' | 'ENDSWITH' | 'CONTAINS' | 'LIKE' )
            int alt2=4;
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
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;

            }

            switch (alt2) {
                case 1 :
                    // antlr/UIQuery.g:66:13: 'BEGINSWITH'
                    {
                    match("BEGINSWITH"); 



                    }
                    break;
                case 2 :
                    // antlr/UIQuery.g:66:28: 'ENDSWITH'
                    {
                    match("ENDSWITH"); 



                    }
                    break;
                case 3 :
                    // antlr/UIQuery.g:66:41: 'CONTAINS'
                    {
                    match("CONTAINS"); 



                    }
                    break;
                case 4 :
                    // antlr/UIQuery.g:66:54: 'LIKE'
                    {
                    match("LIKE"); 



                    }
                    break;

            }


            // antlr/UIQuery.g:66:61: ( '[c]' )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='[') ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // antlr/UIQuery.g:66:62: '[c]'
                    {
                    match("[c]"); 



                    }
                    break;

            }


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
            // antlr/UIQuery.g:69:5: ( ( '0' .. '9' )+ )
            // antlr/UIQuery.g:69:7: ( '0' .. '9' )+
            {
            // antlr/UIQuery.g:69:7: ( '0' .. '9' )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0 >= '0' && LA4_0 <= '9')) ) {
                    alt4=1;
                }


                switch (alt4) {
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
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
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
            // antlr/UIQuery.g:72:6: ( 'true' | 'false' )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0=='t') ) {
                alt5=1;
            }
            else if ( (LA5_0=='f') ) {
                alt5=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;

            }
            switch (alt5) {
                case 1 :
                    // antlr/UIQuery.g:72:8: 'true'
                    {
                    match("true"); 



                    }
                    break;
                case 2 :
                    // antlr/UIQuery.g:72:17: 'false'
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
            // antlr/UIQuery.g:75:5: ( 'nil' | 'null' )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0=='n') ) {
                int LA6_1 = input.LA(2);

                if ( (LA6_1=='i') ) {
                    alt6=1;
                }
                else if ( (LA6_1=='u') ) {
                    alt6=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;

            }
            switch (alt6) {
                case 1 :
                    // antlr/UIQuery.g:75:7: 'nil'
                    {
                    match("nil"); 



                    }
                    break;
                case 2 :
                    // antlr/UIQuery.g:75:15: 'null'
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
            // antlr/UIQuery.g:78:7: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
            // antlr/UIQuery.g:78:9: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            {
            if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // antlr/UIQuery.g:78:33: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( ((LA7_0 >= '0' && LA7_0 <= '9')||(LA7_0 >= 'A' && LA7_0 <= 'Z')||LA7_0=='_'||(LA7_0 >= 'a' && LA7_0 <= 'z')) ) {
                    alt7=1;
                }


                switch (alt7) {
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
            	    break loop7;
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
            // antlr/UIQuery.g:82:5: ( '\\'' ( ESC_SEQ |~ ( '\\\\' | '\\'' ) )* '\\'' )
            // antlr/UIQuery.g:82:8: '\\'' ( ESC_SEQ |~ ( '\\\\' | '\\'' ) )* '\\''
            {
            match('\''); 

            // antlr/UIQuery.g:82:13: ( ESC_SEQ |~ ( '\\\\' | '\\'' ) )*
            loop8:
            do {
                int alt8=3;
                int LA8_0 = input.LA(1);

                if ( (LA8_0=='\\') ) {
                    alt8=1;
                }
                else if ( ((LA8_0 >= '\u0000' && LA8_0 <= '&')||(LA8_0 >= '(' && LA8_0 <= '[')||(LA8_0 >= ']' && LA8_0 <= '\uFFFF')) ) {
                    alt8=2;
                }


                switch (alt8) {
            	case 1 :
            	    // antlr/UIQuery.g:82:15: ESC_SEQ
            	    {
            	    mESC_SEQ(); 


            	    }
            	    break;
            	case 2 :
            	    // antlr/UIQuery.g:82:25: ~ ( '\\\\' | '\\'' )
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
            	    break loop8;
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
            // antlr/UIQuery.g:85:9: ( ( ' ' )+ )
            // antlr/UIQuery.g:85:11: ( ' ' )+
            {
            // antlr/UIQuery.g:85:11: ( ' ' )+
            int cnt9=0;
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==' ') ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // antlr/UIQuery.g:85:11: ' '
            	    {
            	    match(' '); 

            	    }
            	    break;

            	default :
            	    if ( cnt9 >= 1 ) break loop9;
                        EarlyExitException eee =
                            new EarlyExitException(9, input);
                        throw eee;
                }
                cnt9++;
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
            // antlr/UIQuery.g:88:11: ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )
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
            // antlr/UIQuery.g:92:5: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\'' | '\\\\' ) | UNICODE_ESC | OCTAL_ESC )
            int alt10=3;
            int LA10_0 = input.LA(1);

            if ( (LA10_0=='\\') ) {
                switch ( input.LA(2) ) {
                case '\'':
                case '\\':
                case 'b':
                case 'f':
                case 'n':
                case 'r':
                case 't':
                    {
                    alt10=1;
                    }
                    break;
                case 'u':
                    {
                    alt10=2;
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
                    alt10=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 10, 1, input);

                    throw nvae;

                }

            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;

            }
            switch (alt10) {
                case 1 :
                    // antlr/UIQuery.g:92:9: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\'' | '\\\\' )
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
                    // antlr/UIQuery.g:93:9: UNICODE_ESC
                    {
                    mUNICODE_ESC(); 


                    }
                    break;
                case 3 :
                    // antlr/UIQuery.g:94:9: OCTAL_ESC
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
            // antlr/UIQuery.g:99:5: ( '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) )
            int alt11=3;
            int LA11_0 = input.LA(1);

            if ( (LA11_0=='\\') ) {
                int LA11_1 = input.LA(2);

                if ( ((LA11_1 >= '0' && LA11_1 <= '3')) ) {
                    int LA11_2 = input.LA(3);

                    if ( ((LA11_2 >= '0' && LA11_2 <= '7')) ) {
                        int LA11_4 = input.LA(4);

                        if ( ((LA11_4 >= '0' && LA11_4 <= '7')) ) {
                            alt11=1;
                        }
                        else {
                            alt11=2;
                        }
                    }
                    else {
                        alt11=3;
                    }
                }
                else if ( ((LA11_1 >= '4' && LA11_1 <= '7')) ) {
                    int LA11_3 = input.LA(3);

                    if ( ((LA11_3 >= '0' && LA11_3 <= '7')) ) {
                        alt11=2;
                    }
                    else {
                        alt11=3;
                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 11, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;

            }
            switch (alt11) {
                case 1 :
                    // antlr/UIQuery.g:99:9: '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' )
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
                    // antlr/UIQuery.g:100:9: '\\\\' ( '0' .. '7' ) ( '0' .. '7' )
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
                    // antlr/UIQuery.g:101:9: '\\\\' ( '0' .. '7' )
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
            // antlr/UIQuery.g:106:5: ( '\\\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT )
            // antlr/UIQuery.g:106:9: '\\\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
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
        int alt12=14;
        alt12 = dfa12.predict(input);
        switch (alt12) {
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


    protected DFA12 dfa12 = new DFA12(this);
    static final String DFA12_eotS =
        "\2\uffff\3\24\3\uffff\4\24\1\uffff\3\24\2\uffff\2\24\2\uffff\11"+
        "\24\1\51\7\24\1\61\1\24\1\uffff\4\24\1\67\1\70\1\24\1\uffff\1\61"+
        "\4\24\2\uffff\1\70\4\24\1\102\3\24\1\uffff\1\24\2\67\1\24\1\67";
    static final String DFA12_eofS =
        "\110\uffff";
    static final String DFA12_minS =
        "\1\40\1\uffff\3\56\3\uffff\4\56\1\uffff\3\56\2\uffff\2\56\2\uffff"+
        "\23\56\1\uffff\7\56\1\uffff\5\56\2\uffff\11\56\1\uffff\5\56";
    static final String DFA12_maxS =
        "\1\175\1\uffff\3\172\3\uffff\4\172\1\uffff\3\172\2\uffff\2\172\2"+
        "\uffff\23\172\1\uffff\7\172\1\uffff\5\172\2\uffff\11\172\1\uffff"+
        "\5\172";
    static final String DFA12_acceptS =
        "\1\uffff\1\1\3\uffff\1\5\1\6\1\7\4\uffff\1\11\3\uffff\1\15\1\16"+
        "\2\uffff\1\14\1\2\23\uffff\1\3\7\uffff\1\13\5\uffff\1\10\1\12\11"+
        "\uffff\1\4\5\uffff";
    static final String DFA12_specialS =
        "\110\uffff}>";
    static final String[] DFA12_transitionS = {
            "\1\21\6\uffff\1\20\2\uffff\1\1\5\uffff\12\14\1\5\6\uffff\1\17"+
            "\1\4\1\11\1\17\1\10\6\17\1\12\16\17\4\uffff\1\17\1\uffff\1\2"+
            "\4\17\1\15\7\17\1\16\5\17\1\13\1\17\1\3\4\17\1\6\1\uffff\1\7",
            "",
            "\1\25\1\uffff\12\23\7\uffff\32\23\4\uffff\1\23\1\uffff\13\23"+
            "\1\22\16\23",
            "\1\25\1\uffff\12\23\7\uffff\32\23\4\uffff\1\23\1\uffff\10\23"+
            "\1\26\21\23",
            "\1\25\1\uffff\12\23\7\uffff\4\23\1\27\25\23\4\uffff\1\23\1"+
            "\uffff\32\23",
            "",
            "",
            "",
            "\1\25\1\uffff\12\23\7\uffff\15\23\1\30\14\23\4\uffff\1\23\1"+
            "\uffff\32\23",
            "\1\25\1\uffff\12\23\7\uffff\16\23\1\31\13\23\4\uffff\1\23\1"+
            "\uffff\32\23",
            "\1\25\1\uffff\12\23\7\uffff\10\23\1\32\21\23\4\uffff\1\23\1"+
            "\uffff\32\23",
            "\1\25\1\uffff\12\23\7\uffff\32\23\4\uffff\1\23\1\uffff\21\23"+
            "\1\33\10\23",
            "",
            "\1\25\1\uffff\12\23\7\uffff\32\23\4\uffff\1\23\1\uffff\1\34"+
            "\31\23",
            "\1\25\1\uffff\12\23\7\uffff\32\23\4\uffff\1\23\1\uffff\10\23"+
            "\1\35\13\23\1\36\5\23",
            "\1\25\1\uffff\12\23\7\uffff\32\23\4\uffff\1\23\1\uffff\32\23",
            "",
            "",
            "\1\25\1\uffff\12\23\7\uffff\32\23\4\uffff\1\23\1\uffff\13\23"+
            "\1\37\16\23",
            "\1\25\1\uffff\12\23\7\uffff\32\23\4\uffff\1\23\1\uffff\32\23",
            "",
            "",
            "\1\25\1\uffff\12\23\7\uffff\32\23\4\uffff\1\23\1\uffff\22\23"+
            "\1\40\7\23",
            "\1\25\1\uffff\12\23\7\uffff\6\23\1\41\23\23\4\uffff\1\23\1"+
            "\uffff\32\23",
            "\1\25\1\uffff\12\23\7\uffff\3\23\1\42\26\23\4\uffff\1\23\1"+
            "\uffff\32\23",
            "\1\25\1\uffff\12\23\7\uffff\15\23\1\43\14\23\4\uffff\1\23\1"+
            "\uffff\32\23",
            "\1\25\1\uffff\12\23\7\uffff\12\23\1\44\17\23\4\uffff\1\23\1"+
            "\uffff\32\23",
            "\1\25\1\uffff\12\23\7\uffff\32\23\4\uffff\1\23\1\uffff\24\23"+
            "\1\45\5\23",
            "\1\25\1\uffff\12\23\7\uffff\32\23\4\uffff\1\23\1\uffff\13\23"+
            "\1\46\16\23",
            "\1\25\1\uffff\12\23\7\uffff\32\23\4\uffff\1\23\1\uffff\13\23"+
            "\1\47\16\23",
            "\1\25\1\uffff\12\23\7\uffff\32\23\4\uffff\1\23\1\uffff\13\23"+
            "\1\50\16\23",
            "\1\25\1\uffff\12\23\7\uffff\32\23\4\uffff\1\23\1\uffff\32\23",
            "\1\25\1\uffff\12\23\7\uffff\32\23\4\uffff\1\23\1\uffff\10\23"+
            "\1\52\21\23",
            "\1\25\1\uffff\12\23\7\uffff\10\23\1\53\21\23\4\uffff\1\23\1"+
            "\uffff\32\23",
            "\1\25\1\uffff\12\23\7\uffff\22\23\1\54\7\23\4\uffff\1\23\1"+
            "\uffff\32\23",
            "\1\25\1\uffff\12\23\7\uffff\23\23\1\55\6\23\4\uffff\1\23\1"+
            "\uffff\32\23",
            "\1\25\1\uffff\12\23\7\uffff\4\23\1\56\25\23\4\uffff\1\23\1"+
            "\uffff\32\23",
            "\1\25\1\uffff\12\23\7\uffff\32\23\4\uffff\1\23\1\uffff\4\23"+
            "\1\57\25\23",
            "\1\25\1\uffff\12\23\7\uffff\32\23\4\uffff\1\23\1\uffff\22\23"+
            "\1\60\7\23",
            "\1\25\1\uffff\12\23\7\uffff\32\23\4\uffff\1\23\1\uffff\32\23",
            "\1\25\1\uffff\12\23\7\uffff\32\23\4\uffff\1\23\1\uffff\13\23"+
            "\1\62\16\23",
            "",
            "\1\25\1\uffff\12\23\7\uffff\32\23\4\uffff\1\23\1\uffff\1\23"+
            "\1\63\30\23",
            "\1\25\1\uffff\12\23\7\uffff\15\23\1\64\14\23\4\uffff\1\23\1"+
            "\uffff\32\23",
            "\1\25\1\uffff\12\23\7\uffff\26\23\1\65\3\23\4\uffff\1\23\1"+
            "\uffff\32\23",
            "\1\25\1\uffff\12\23\7\uffff\1\66\31\23\4\uffff\1\23\1\uffff"+
            "\32\23",
            "\1\25\1\uffff\12\23\7\uffff\32\23\4\uffff\1\23\1\uffff\32\23",
            "\1\25\1\uffff\12\23\7\uffff\32\23\4\uffff\1\23\1\uffff\32\23",
            "\1\25\1\uffff\12\23\7\uffff\32\23\4\uffff\1\23\1\uffff\4\23"+
            "\1\71\25\23",
            "",
            "\1\25\1\uffff\12\23\7\uffff\32\23\4\uffff\1\23\1\uffff\32\23",
            "\1\25\1\uffff\12\23\7\uffff\32\23\4\uffff\1\23\1\uffff\13\23"+
            "\1\72\16\23",
            "\1\25\1\uffff\12\23\7\uffff\22\23\1\73\7\23\4\uffff\1\23\1"+
            "\uffff\32\23",
            "\1\25\1\uffff\12\23\7\uffff\10\23\1\74\21\23\4\uffff\1\23\1"+
            "\uffff\32\23",
            "\1\25\1\uffff\12\23\7\uffff\10\23\1\75\21\23\4\uffff\1\23\1"+
            "\uffff\32\23",
            "",
            "",
            "\1\25\1\uffff\12\23\7\uffff\32\23\4\uffff\1\23\1\uffff\32\23",
            "\1\25\1\uffff\12\23\7\uffff\32\23\4\uffff\1\23\1\uffff\4\23"+
            "\1\76\25\23",
            "\1\25\1\uffff\12\23\7\uffff\26\23\1\77\3\23\4\uffff\1\23\1"+
            "\uffff\32\23",
            "\1\25\1\uffff\12\23\7\uffff\23\23\1\100\6\23\4\uffff\1\23\1"+
            "\uffff\32\23",
            "\1\25\1\uffff\12\23\7\uffff\15\23\1\101\14\23\4\uffff\1\23"+
            "\1\uffff\32\23",
            "\1\25\1\uffff\12\23\7\uffff\32\23\4\uffff\1\23\1\uffff\32\23",
            "\1\25\1\uffff\12\23\7\uffff\10\23\1\103\21\23\4\uffff\1\23"+
            "\1\uffff\32\23",
            "\1\25\1\uffff\12\23\7\uffff\7\23\1\104\22\23\4\uffff\1\23\1"+
            "\uffff\32\23",
            "\1\25\1\uffff\12\23\7\uffff\22\23\1\105\7\23\4\uffff\1\23\1"+
            "\uffff\32\23",
            "",
            "\1\25\1\uffff\12\23\7\uffff\23\23\1\106\6\23\4\uffff\1\23\1"+
            "\uffff\32\23",
            "\1\25\1\uffff\12\23\7\uffff\32\23\4\uffff\1\23\1\uffff\32\23",
            "\1\25\1\uffff\12\23\7\uffff\32\23\4\uffff\1\23\1\uffff\32\23",
            "\1\25\1\uffff\12\23\7\uffff\7\23\1\107\22\23\4\uffff\1\23\1"+
            "\uffff\32\23",
            "\1\25\1\uffff\12\23\7\uffff\32\23\4\uffff\1\23\1\uffff\32\23"
    };

    static final short[] DFA12_eot = DFA.unpackEncodedString(DFA12_eotS);
    static final short[] DFA12_eof = DFA.unpackEncodedString(DFA12_eofS);
    static final char[] DFA12_min = DFA.unpackEncodedStringToUnsignedChars(DFA12_minS);
    static final char[] DFA12_max = DFA.unpackEncodedStringToUnsignedChars(DFA12_maxS);
    static final short[] DFA12_accept = DFA.unpackEncodedString(DFA12_acceptS);
    static final short[] DFA12_special = DFA.unpackEncodedString(DFA12_specialS);
    static final short[][] DFA12_transition;

    static {
        int numStates = DFA12_transitionS.length;
        DFA12_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA12_transition[i] = DFA.unpackEncodedString(DFA12_transitionS[i]);
        }
    }

    class DFA12 extends DFA {

        public DFA12(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 12;
            this.eot = DFA12_eot;
            this.eof = DFA12_eof;
            this.min = DFA12_min;
            this.max = DFA12_max;
            this.accept = DFA12_accept;
            this.special = DFA12_special;
            this.transition = DFA12_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( WILDCARD | QUALIFIED_NAME | ALL | VISIBLE | FILTER_COLON | BEGINPRED | ENDPRED | RELATION | INT | BOOL | NIL | NAME | STRING | WHITE );";
        }
    }
 

}