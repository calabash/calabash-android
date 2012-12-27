// $ANTLR 3.4 antlr/UIQuery.g 2012-12-27 13:48:21

    package sh.calaba.instrumentationbackend.query.antlr;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.tree.*;


@SuppressWarnings({"all", "warnings", "unchecked"})
public class UIQueryParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "BOOL", "ESC_SEQ", "FILTER_COLON", "HEX_DIGIT", "INT", "NAME", "NIL", "OCTAL_ESC", "QUALIFIED_NAME", "STRING", "UNICODE_ESC", "WHITE"
    };

    public static final int EOF=-1;
    public static final int BOOL=4;
    public static final int ESC_SEQ=5;
    public static final int FILTER_COLON=6;
    public static final int HEX_DIGIT=7;
    public static final int INT=8;
    public static final int NAME=9;
    public static final int NIL=10;
    public static final int OCTAL_ESC=11;
    public static final int QUALIFIED_NAME=12;
    public static final int STRING=13;
    public static final int UNICODE_ESC=14;
    public static final int WHITE=15;

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


      public String getErrorMessage(RecognitionException e, String[] tokenNames)
      {
        List stack = getRuleInvocationStack(e, this.getClass().getName());
        String msg = null;
        if ( e instanceof NoViableAltException ) {
          NoViableAltException nvae = (NoViableAltException)e;
          msg = " no viable alt; token="+e.token+" (decision="+nvae.decisionNumber+" state "+nvae.stateNumber+")"+" decision=<<"+nvae.grammarDecisionDescription+">>";
        }
        else {
        msg = super.getErrorMessage(e, tokenNames);
        }
        return stack+" "+msg;
      }
      public String getTokenErrorDisplay(Token t) {
        return t.toString();
      }


    public static class query_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "query"
    // antlr/UIQuery.g:34:1: query : expr ( WHITE ! expr )* ;
    public final UIQueryParser.query_return query() throws RecognitionException {
        UIQueryParser.query_return retval = new UIQueryParser.query_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token WHITE2=null;
        UIQueryParser.expr_return expr1 =null;

        UIQueryParser.expr_return expr3 =null;


        CommonTree WHITE2_tree=null;

        try {
            // antlr/UIQuery.g:34:7: ( expr ( WHITE ! expr )* )
            // antlr/UIQuery.g:34:9: expr ( WHITE ! expr )*
            {
            root_0 = (CommonTree)adaptor.nil();


            pushFollow(FOLLOW_expr_in_query53);
            expr1=expr();

            state._fsp--;

            adaptor.addChild(root_0, expr1.getTree());

            // antlr/UIQuery.g:34:14: ( WHITE ! expr )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==WHITE) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // antlr/UIQuery.g:34:15: WHITE ! expr
            	    {
            	    WHITE2=(Token)match(input,WHITE,FOLLOW_WHITE_in_query56); 

            	    pushFollow(FOLLOW_expr_in_query59);
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
    // antlr/UIQuery.g:38:1: expr : ( className | filter ) ;
    public final UIQueryParser.expr_return expr() throws RecognitionException {
        UIQueryParser.expr_return retval = new UIQueryParser.expr_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        UIQueryParser.className_return className4 =null;

        UIQueryParser.filter_return filter5 =null;



        try {
            // antlr/UIQuery.g:38:6: ( ( className | filter ) )
            // antlr/UIQuery.g:38:8: ( className | filter )
            {
            root_0 = (CommonTree)adaptor.nil();


            // antlr/UIQuery.g:38:8: ( className | filter )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==NAME) ) {
                int LA2_1 = input.LA(2);

                if ( (LA2_1==FILTER_COLON) ) {
                    alt2=2;
                }
                else if ( (LA2_1==EOF||LA2_1==WHITE) ) {
                    alt2=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 2, 1, input);

                    throw nvae;

                }
            }
            else if ( (LA2_0==QUALIFIED_NAME) ) {
                alt2=1;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;

            }
            switch (alt2) {
                case 1 :
                    // antlr/UIQuery.g:38:9: className
                    {
                    pushFollow(FOLLOW_className_in_expr77);
                    className4=className();

                    state._fsp--;

                    adaptor.addChild(root_0, className4.getTree());

                    }
                    break;
                case 2 :
                    // antlr/UIQuery.g:38:21: filter
                    {
                    pushFollow(FOLLOW_filter_in_expr81);
                    filter5=filter();

                    state._fsp--;

                    adaptor.addChild(root_0, filter5.getTree());

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
    // antlr/UIQuery.g:41:1: className : ( NAME ^| QUALIFIED_NAME ^) ;
    public final UIQueryParser.className_return className() throws RecognitionException {
        UIQueryParser.className_return retval = new UIQueryParser.className_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token NAME6=null;
        Token QUALIFIED_NAME7=null;

        CommonTree NAME6_tree=null;
        CommonTree QUALIFIED_NAME7_tree=null;

        try {
            // antlr/UIQuery.g:41:13: ( ( NAME ^| QUALIFIED_NAME ^) )
            // antlr/UIQuery.g:41:17: ( NAME ^| QUALIFIED_NAME ^)
            {
            root_0 = (CommonTree)adaptor.nil();


            // antlr/UIQuery.g:41:17: ( NAME ^| QUALIFIED_NAME ^)
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==NAME) ) {
                alt3=1;
            }
            else if ( (LA3_0==QUALIFIED_NAME) ) {
                alt3=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;

            }
            switch (alt3) {
                case 1 :
                    // antlr/UIQuery.g:41:18: NAME ^
                    {
                    NAME6=(Token)match(input,NAME,FOLLOW_NAME_in_className99); 
                    NAME6_tree = 
                    (CommonTree)adaptor.create(NAME6)
                    ;
                    root_0 = (CommonTree)adaptor.becomeRoot(NAME6_tree, root_0);


                    }
                    break;
                case 2 :
                    // antlr/UIQuery.g:41:26: QUALIFIED_NAME ^
                    {
                    QUALIFIED_NAME7=(Token)match(input,QUALIFIED_NAME,FOLLOW_QUALIFIED_NAME_in_className104); 
                    QUALIFIED_NAME7_tree = 
                    (CommonTree)adaptor.create(QUALIFIED_NAME7)
                    ;
                    root_0 = (CommonTree)adaptor.becomeRoot(QUALIFIED_NAME7_tree, root_0);


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


    public static class filter_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "filter"
    // antlr/UIQuery.g:45:1: filter : NAME FILTER_COLON ^ ( INT | STRING | BOOL | NIL ) ;
    public final UIQueryParser.filter_return filter() throws RecognitionException {
        UIQueryParser.filter_return retval = new UIQueryParser.filter_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token NAME8=null;
        Token FILTER_COLON9=null;
        Token set10=null;

        CommonTree NAME8_tree=null;
        CommonTree FILTER_COLON9_tree=null;
        CommonTree set10_tree=null;

        try {
            // antlr/UIQuery.g:45:8: ( NAME FILTER_COLON ^ ( INT | STRING | BOOL | NIL ) )
            // antlr/UIQuery.g:45:10: NAME FILTER_COLON ^ ( INT | STRING | BOOL | NIL )
            {
            root_0 = (CommonTree)adaptor.nil();


            NAME8=(Token)match(input,NAME,FOLLOW_NAME_in_filter129); 
            NAME8_tree = 
            (CommonTree)adaptor.create(NAME8)
            ;
            adaptor.addChild(root_0, NAME8_tree);


            FILTER_COLON9=(Token)match(input,FILTER_COLON,FOLLOW_FILTER_COLON_in_filter131); 
            FILTER_COLON9_tree = 
            (CommonTree)adaptor.create(FILTER_COLON9)
            ;
            root_0 = (CommonTree)adaptor.becomeRoot(FILTER_COLON9_tree, root_0);


            set10=(Token)input.LT(1);

            if ( input.LA(1)==BOOL||input.LA(1)==INT||input.LA(1)==NIL||input.LA(1)==STRING ) {
                input.consume();
                adaptor.addChild(root_0, 
                (CommonTree)adaptor.create(set10)
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

    // Delegated rules


 

    public static final BitSet FOLLOW_expr_in_query53 = new BitSet(new long[]{0x0000000000008002L});
    public static final BitSet FOLLOW_WHITE_in_query56 = new BitSet(new long[]{0x0000000000001200L});
    public static final BitSet FOLLOW_expr_in_query59 = new BitSet(new long[]{0x0000000000008002L});
    public static final BitSet FOLLOW_className_in_expr77 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_filter_in_expr81 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_className99 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUALIFIED_NAME_in_className104 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_filter129 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_FILTER_COLON_in_filter131 = new BitSet(new long[]{0x0000000000002510L});
    public static final BitSet FOLLOW_set_in_filter134 = new BitSet(new long[]{0x0000000000000002L});

}