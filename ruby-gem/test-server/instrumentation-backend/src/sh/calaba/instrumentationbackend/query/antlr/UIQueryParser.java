// $ANTLR 3.4 antlr/UIQuery.g 2013-06-26 21:55:04

    package sh.calaba.instrumentationbackend.query.antlr;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.tree.*;


@SuppressWarnings({"all", "warnings", "unchecked"})
public class UIQueryParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ALL", "BEGINPRED", "BOOL", "DIRECTION", "DOUBLE_ESC_SEQ", "ENDPRED", "ESC_SEQ", "FILTER_COLON", "HEX_DIGIT", "INT", "NAME", "NIL", "OCTAL_ESC", "QUALIFIED_NAME", "RELATION", "STRING", "UNICODE_ESC", "VISIBLE", "WHITE", "WILDCARD"
    };

    public static final int EOF=-1;
    public static final int ALL=4;
    public static final int BEGINPRED=5;
    public static final int BOOL=6;
    public static final int DIRECTION=7;
    public static final int DOUBLE_ESC_SEQ=8;
    public static final int ENDPRED=9;
    public static final int ESC_SEQ=10;
    public static final int FILTER_COLON=11;
    public static final int HEX_DIGIT=12;
    public static final int INT=13;
    public static final int NAME=14;
    public static final int NIL=15;
    public static final int OCTAL_ESC=16;
    public static final int QUALIFIED_NAME=17;
    public static final int RELATION=18;
    public static final int STRING=19;
    public static final int UNICODE_ESC=20;
    public static final int VISIBLE=21;
    public static final int WHITE=22;
    public static final int WILDCARD=23;

    // delegates
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators


    public UIQueryParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public UIQueryParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

protected TreeAdaptor adaptor = new CommonTreeAdaptor();

public void setTreeAdaptor(TreeAdaptor adaptor) {
    this.adaptor = adaptor;
}
public TreeAdaptor getTreeAdaptor() {
    return adaptor;
}
    public String[] getTokenNames() { return UIQueryParser.tokenNames; }
    public String getGrammarFileName() { return "antlr/UIQuery.g"; }


    public static class query_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "query"
    // antlr/UIQuery.g:35:1: query : expr ( WHITE ! expr )* ;
    public final UIQueryParser.query_return query() throws RecognitionException {
        UIQueryParser.query_return retval = new UIQueryParser.query_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token WHITE2=null;
        UIQueryParser.expr_return expr1 =null;

        UIQueryParser.expr_return expr3 =null;


        CommonTree WHITE2_tree=null;

        try {
            // antlr/UIQuery.g:35:7: ( expr ( WHITE ! expr )* )
            // antlr/UIQuery.g:35:9: expr ( WHITE ! expr )*
            {
            root_0 = (CommonTree)adaptor.nil();


            pushFollow(FOLLOW_expr_in_query56);
            expr1=expr();

            state._fsp--;

            adaptor.addChild(root_0, expr1.getTree());

            // antlr/UIQuery.g:35:14: ( WHITE ! expr )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==WHITE) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // antlr/UIQuery.g:35:15: WHITE ! expr
            	    {
            	    WHITE2=(Token)match(input,WHITE,FOLLOW_WHITE_in_query59); 

            	    pushFollow(FOLLOW_expr_in_query62);
            	    expr3=expr();

            	    state._fsp--;

            	    adaptor.addChild(root_0, expr3.getTree());

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "query"


    public static class expr_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "expr"
    // antlr/UIQuery.g:39:1: expr : ( className | filter | visibility | predicate | DIRECTION ^) ;
    public final UIQueryParser.expr_return expr() throws RecognitionException {
        UIQueryParser.expr_return retval = new UIQueryParser.expr_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token DIRECTION8=null;
        UIQueryParser.className_return className4 =null;

        UIQueryParser.filter_return filter5 =null;

        UIQueryParser.visibility_return visibility6 =null;

        UIQueryParser.predicate_return predicate7 =null;


        CommonTree DIRECTION8_tree=null;

        try {
            // antlr/UIQuery.g:39:6: ( ( className | filter | visibility | predicate | DIRECTION ^) )
            // antlr/UIQuery.g:39:8: ( className | filter | visibility | predicate | DIRECTION ^)
            {
            root_0 = (CommonTree)adaptor.nil();


            // antlr/UIQuery.g:39:8: ( className | filter | visibility | predicate | DIRECTION ^)
            int alt2=5;
            switch ( input.LA(1) ) {
            case QUALIFIED_NAME:
            case WILDCARD:
                {
                alt2=1;
                }
                break;
            case NAME:
                {
                int LA2_2 = input.LA(2);

                if ( (LA2_2==FILTER_COLON) ) {
                    alt2=2;
                }
                else if ( (LA2_2==EOF||LA2_2==WHITE) ) {
                    alt2=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 2, 2, input);

                    throw nvae;

                }
                }
                break;
            case ALL:
            case VISIBLE:
                {
                alt2=3;
                }
                break;
            case BEGINPRED:
                {
                alt2=4;
                }
                break;
            case DIRECTION:
                {
                alt2=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;

            }

            switch (alt2) {
                case 1 :
                    // antlr/UIQuery.g:39:9: className
                    {
                    pushFollow(FOLLOW_className_in_expr80);
                    className4=className();

                    state._fsp--;

                    adaptor.addChild(root_0, className4.getTree());

                    }
                    break;
                case 2 :
                    // antlr/UIQuery.g:39:21: filter
                    {
                    pushFollow(FOLLOW_filter_in_expr84);
                    filter5=filter();

                    state._fsp--;

                    adaptor.addChild(root_0, filter5.getTree());

                    }
                    break;
                case 3 :
                    // antlr/UIQuery.g:39:30: visibility
                    {
                    pushFollow(FOLLOW_visibility_in_expr88);
                    visibility6=visibility();

                    state._fsp--;

                    adaptor.addChild(root_0, visibility6.getTree());

                    }
                    break;
                case 4 :
                    // antlr/UIQuery.g:39:43: predicate
                    {
                    pushFollow(FOLLOW_predicate_in_expr92);
                    predicate7=predicate();

                    state._fsp--;

                    adaptor.addChild(root_0, predicate7.getTree());

                    }
                    break;
                case 5 :
                    // antlr/UIQuery.g:39:55: DIRECTION ^
                    {
                    DIRECTION8=(Token)match(input,DIRECTION,FOLLOW_DIRECTION_in_expr96); 
                    DIRECTION8_tree = 
                    (CommonTree)adaptor.create(DIRECTION8)
                    ;
                    root_0 = (CommonTree)adaptor.becomeRoot(DIRECTION8_tree, root_0);


                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "expr"


    public static class className_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "className"
    // antlr/UIQuery.g:45:1: className : ( WILDCARD ^| NAME ^| QUALIFIED_NAME ^) ;
    public final UIQueryParser.className_return className() throws RecognitionException {
        UIQueryParser.className_return retval = new UIQueryParser.className_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token WILDCARD9=null;
        Token NAME10=null;
        Token QUALIFIED_NAME11=null;

        CommonTree WILDCARD9_tree=null;
        CommonTree NAME10_tree=null;
        CommonTree QUALIFIED_NAME11_tree=null;

        try {
            // antlr/UIQuery.g:45:13: ( ( WILDCARD ^| NAME ^| QUALIFIED_NAME ^) )
            // antlr/UIQuery.g:45:17: ( WILDCARD ^| NAME ^| QUALIFIED_NAME ^)
            {
            root_0 = (CommonTree)adaptor.nil();


            // antlr/UIQuery.g:45:17: ( WILDCARD ^| NAME ^| QUALIFIED_NAME ^)
            int alt3=3;
            switch ( input.LA(1) ) {
            case WILDCARD:
                {
                alt3=1;
                }
                break;
            case NAME:
                {
                alt3=2;
                }
                break;
            case QUALIFIED_NAME:
                {
                alt3=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;

            }

            switch (alt3) {
                case 1 :
                    // antlr/UIQuery.g:45:18: WILDCARD ^
                    {
                    WILDCARD9=(Token)match(input,WILDCARD,FOLLOW_WILDCARD_in_className140); 
                    WILDCARD9_tree = 
                    (CommonTree)adaptor.create(WILDCARD9)
                    ;
                    root_0 = (CommonTree)adaptor.becomeRoot(WILDCARD9_tree, root_0);


                    }
                    break;
                case 2 :
                    // antlr/UIQuery.g:45:30: NAME ^
                    {
                    NAME10=(Token)match(input,NAME,FOLLOW_NAME_in_className145); 
                    NAME10_tree = 
                    (CommonTree)adaptor.create(NAME10)
                    ;
                    root_0 = (CommonTree)adaptor.becomeRoot(NAME10_tree, root_0);


                    }
                    break;
                case 3 :
                    // antlr/UIQuery.g:45:38: QUALIFIED_NAME ^
                    {
                    QUALIFIED_NAME11=(Token)match(input,QUALIFIED_NAME,FOLLOW_QUALIFIED_NAME_in_className150); 
                    QUALIFIED_NAME11_tree = 
                    (CommonTree)adaptor.create(QUALIFIED_NAME11)
                    ;
                    root_0 = (CommonTree)adaptor.becomeRoot(QUALIFIED_NAME11_tree, root_0);


                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "className"


    public static class visibility_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "visibility"
    // antlr/UIQuery.g:51:1: visibility : ( ALL ^| VISIBLE ^) ;
    public final UIQueryParser.visibility_return visibility() throws RecognitionException {
        UIQueryParser.visibility_return retval = new UIQueryParser.visibility_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token ALL12=null;
        Token VISIBLE13=null;

        CommonTree ALL12_tree=null;
        CommonTree VISIBLE13_tree=null;

        try {
            // antlr/UIQuery.g:51:14: ( ( ALL ^| VISIBLE ^) )
            // antlr/UIQuery.g:51:18: ( ALL ^| VISIBLE ^)
            {
            root_0 = (CommonTree)adaptor.nil();


            // antlr/UIQuery.g:51:18: ( ALL ^| VISIBLE ^)
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==ALL) ) {
                alt4=1;
            }
            else if ( (LA4_0==VISIBLE) ) {
                alt4=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;

            }
            switch (alt4) {
                case 1 :
                    // antlr/UIQuery.g:51:19: ALL ^
                    {
                    ALL12=(Token)match(input,ALL,FOLLOW_ALL_in_visibility188); 
                    ALL12_tree = 
                    (CommonTree)adaptor.create(ALL12)
                    ;
                    root_0 = (CommonTree)adaptor.becomeRoot(ALL12_tree, root_0);


                    }
                    break;
                case 2 :
                    // antlr/UIQuery.g:51:26: VISIBLE ^
                    {
                    VISIBLE13=(Token)match(input,VISIBLE,FOLLOW_VISIBLE_in_visibility193); 
                    VISIBLE13_tree = 
                    (CommonTree)adaptor.create(VISIBLE13)
                    ;
                    root_0 = (CommonTree)adaptor.becomeRoot(VISIBLE13_tree, root_0);


                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "visibility"


    public static class filter_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "filter"
    // antlr/UIQuery.g:58:1: filter : NAME FILTER_COLON ^ ( INT | STRING | BOOL | NIL ) ;
    public final UIQueryParser.filter_return filter() throws RecognitionException {
        UIQueryParser.filter_return retval = new UIQueryParser.filter_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token NAME14=null;
        Token FILTER_COLON15=null;
        Token set16=null;

        CommonTree NAME14_tree=null;
        CommonTree FILTER_COLON15_tree=null;
        CommonTree set16_tree=null;

        try {
            // antlr/UIQuery.g:58:8: ( NAME FILTER_COLON ^ ( INT | STRING | BOOL | NIL ) )
            // antlr/UIQuery.g:58:10: NAME FILTER_COLON ^ ( INT | STRING | BOOL | NIL )
            {
            root_0 = (CommonTree)adaptor.nil();


            NAME14=(Token)match(input,NAME,FOLLOW_NAME_in_filter220); 
            NAME14_tree = 
            (CommonTree)adaptor.create(NAME14)
            ;
            adaptor.addChild(root_0, NAME14_tree);


            FILTER_COLON15=(Token)match(input,FILTER_COLON,FOLLOW_FILTER_COLON_in_filter222); 
            FILTER_COLON15_tree = 
            (CommonTree)adaptor.create(FILTER_COLON15)
            ;
            root_0 = (CommonTree)adaptor.becomeRoot(FILTER_COLON15_tree, root_0);


            set16=(Token)input.LT(1);

            if ( input.LA(1)==BOOL||input.LA(1)==INT||input.LA(1)==NIL||input.LA(1)==STRING ) {
                input.consume();
                adaptor.addChild(root_0, 
                (CommonTree)adaptor.create(set16)
                );
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "filter"


    public static class predicate_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "predicate"
    // antlr/UIQuery.g:63:1: predicate : BEGINPRED ^ NAME WHITE ! RELATION WHITE ! ( INT | STRING | BOOL | NIL ) ENDPRED !;
    public final UIQueryParser.predicate_return predicate() throws RecognitionException {
        UIQueryParser.predicate_return retval = new UIQueryParser.predicate_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token BEGINPRED17=null;
        Token NAME18=null;
        Token WHITE19=null;
        Token RELATION20=null;
        Token WHITE21=null;
        Token set22=null;
        Token ENDPRED23=null;

        CommonTree BEGINPRED17_tree=null;
        CommonTree NAME18_tree=null;
        CommonTree WHITE19_tree=null;
        CommonTree RELATION20_tree=null;
        CommonTree WHITE21_tree=null;
        CommonTree set22_tree=null;
        CommonTree ENDPRED23_tree=null;

        try {
            // antlr/UIQuery.g:63:11: ( BEGINPRED ^ NAME WHITE ! RELATION WHITE ! ( INT | STRING | BOOL | NIL ) ENDPRED !)
            // antlr/UIQuery.g:63:13: BEGINPRED ^ NAME WHITE ! RELATION WHITE ! ( INT | STRING | BOOL | NIL ) ENDPRED !
            {
            root_0 = (CommonTree)adaptor.nil();


            BEGINPRED17=(Token)match(input,BEGINPRED,FOLLOW_BEGINPRED_in_predicate258); 
            BEGINPRED17_tree = 
            (CommonTree)adaptor.create(BEGINPRED17)
            ;
            root_0 = (CommonTree)adaptor.becomeRoot(BEGINPRED17_tree, root_0);


            NAME18=(Token)match(input,NAME,FOLLOW_NAME_in_predicate261); 
            NAME18_tree = 
            (CommonTree)adaptor.create(NAME18)
            ;
            adaptor.addChild(root_0, NAME18_tree);


            WHITE19=(Token)match(input,WHITE,FOLLOW_WHITE_in_predicate263); 

            RELATION20=(Token)match(input,RELATION,FOLLOW_RELATION_in_predicate266); 
            RELATION20_tree = 
            (CommonTree)adaptor.create(RELATION20)
            ;
            adaptor.addChild(root_0, RELATION20_tree);


            WHITE21=(Token)match(input,WHITE,FOLLOW_WHITE_in_predicate268); 

            set22=(Token)input.LT(1);

            if ( input.LA(1)==BOOL||input.LA(1)==INT||input.LA(1)==NIL||input.LA(1)==STRING ) {
                input.consume();
                adaptor.addChild(root_0, 
                (CommonTree)adaptor.create(set22)
                );
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            ENDPRED23=(Token)match(input,ENDPRED,FOLLOW_ENDPRED_in_predicate287); 

            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "predicate"

    // Delegated rules


 

    public static final BitSet FOLLOW_expr_in_query56 = new BitSet(new long[]{0x0000000000400002L});
    public static final BitSet FOLLOW_WHITE_in_query59 = new BitSet(new long[]{0x0000000000A240B0L});
    public static final BitSet FOLLOW_expr_in_query62 = new BitSet(new long[]{0x0000000000400002L});
    public static final BitSet FOLLOW_className_in_expr80 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_filter_in_expr84 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_visibility_in_expr88 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_predicate_in_expr92 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DIRECTION_in_expr96 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WILDCARD_in_className140 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_className145 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUALIFIED_NAME_in_className150 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ALL_in_visibility188 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VISIBLE_in_visibility193 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_filter220 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_FILTER_COLON_in_filter222 = new BitSet(new long[]{0x000000000008A040L});
    public static final BitSet FOLLOW_set_in_filter225 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BEGINPRED_in_predicate258 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_NAME_in_predicate261 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_WHITE_in_predicate263 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_RELATION_in_predicate266 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_WHITE_in_predicate268 = new BitSet(new long[]{0x000000000008A040L});
    public static final BitSet FOLLOW_set_in_predicate271 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_ENDPRED_in_predicate287 = new BitSet(new long[]{0x0000000000000002L});

}