// $ANTLR 3.4 antlr/UIQuery.g 2013-01-07 13:26:07

    package sh.calaba.instrumentationbackend.query.antlr;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.tree.*;


@SuppressWarnings({"all", "warnings", "unchecked"})
public class UIQueryParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ALL", "BOOL", "ESC_SEQ", "FILTER_COLON", "HEX_DIGIT", "INT", "NAME", "NIL", "OCTAL_ESC", "QUALIFIED_NAME", "STRING", "UNICODE_ESC", "VISIBLE", "WHITE", "WILDCARD"
    };

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
    // antlr/UIQuery.g:38:1: expr : ( className | filter | visibility ) ;
    public final UIQueryParser.expr_return expr() throws RecognitionException {
        UIQueryParser.expr_return retval = new UIQueryParser.expr_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        UIQueryParser.className_return className4 =null;

        UIQueryParser.filter_return filter5 =null;

        UIQueryParser.visibility_return visibility6 =null;



        try {
            // antlr/UIQuery.g:38:6: ( ( className | filter | visibility ) )
            // antlr/UIQuery.g:38:8: ( className | filter | visibility )
            {
            root_0 = (CommonTree)adaptor.nil();


            // antlr/UIQuery.g:38:8: ( className | filter | visibility )
            int alt2=3;
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
            default:
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
                case 3 :
                    // antlr/UIQuery.g:38:30: visibility
                    {
                    pushFollow(FOLLOW_visibility_in_expr85);
                    visibility6=visibility();

                    state._fsp--;

                    adaptor.addChild(root_0, visibility6.getTree());

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
    // antlr/UIQuery.g:41:1: className : ( WILDCARD ^| NAME ^| QUALIFIED_NAME ^) ;
    public final UIQueryParser.className_return className() throws RecognitionException {
        UIQueryParser.className_return retval = new UIQueryParser.className_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token WILDCARD7=null;
        Token NAME8=null;
        Token QUALIFIED_NAME9=null;

        CommonTree WILDCARD7_tree=null;
        CommonTree NAME8_tree=null;
        CommonTree QUALIFIED_NAME9_tree=null;

        try {
            // antlr/UIQuery.g:41:13: ( ( WILDCARD ^| NAME ^| QUALIFIED_NAME ^) )
            // antlr/UIQuery.g:41:17: ( WILDCARD ^| NAME ^| QUALIFIED_NAME ^)
            {
            root_0 = (CommonTree)adaptor.nil();


            // antlr/UIQuery.g:41:17: ( WILDCARD ^| NAME ^| QUALIFIED_NAME ^)
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
                    // antlr/UIQuery.g:41:18: WILDCARD ^
                    {
                    WILDCARD7=(Token)match(input,WILDCARD,FOLLOW_WILDCARD_in_className103); 
                    WILDCARD7_tree = 
                    (CommonTree)adaptor.create(WILDCARD7)
                    ;
                    root_0 = (CommonTree)adaptor.becomeRoot(WILDCARD7_tree, root_0);


                    }
                    break;
                case 2 :
                    // antlr/UIQuery.g:41:30: NAME ^
                    {
                    NAME8=(Token)match(input,NAME,FOLLOW_NAME_in_className108); 
                    NAME8_tree = 
                    (CommonTree)adaptor.create(NAME8)
                    ;
                    root_0 = (CommonTree)adaptor.becomeRoot(NAME8_tree, root_0);


                    }
                    break;
                case 3 :
                    // antlr/UIQuery.g:41:38: QUALIFIED_NAME ^
                    {
                    QUALIFIED_NAME9=(Token)match(input,QUALIFIED_NAME,FOLLOW_QUALIFIED_NAME_in_className113); 
                    QUALIFIED_NAME9_tree = 
                    (CommonTree)adaptor.create(QUALIFIED_NAME9)
                    ;
                    root_0 = (CommonTree)adaptor.becomeRoot(QUALIFIED_NAME9_tree, root_0);


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
    // antlr/UIQuery.g:47:1: visibility : ( ALL ^| VISIBLE ^) ;
    public final UIQueryParser.visibility_return visibility() throws RecognitionException {
        UIQueryParser.visibility_return retval = new UIQueryParser.visibility_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token ALL10=null;
        Token VISIBLE11=null;

        CommonTree ALL10_tree=null;
        CommonTree VISIBLE11_tree=null;

        try {
            // antlr/UIQuery.g:47:14: ( ( ALL ^| VISIBLE ^) )
            // antlr/UIQuery.g:47:18: ( ALL ^| VISIBLE ^)
            {
            root_0 = (CommonTree)adaptor.nil();


            // antlr/UIQuery.g:47:18: ( ALL ^| VISIBLE ^)
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
                    // antlr/UIQuery.g:47:19: ALL ^
                    {
                    ALL10=(Token)match(input,ALL,FOLLOW_ALL_in_visibility151); 
                    ALL10_tree = 
                    (CommonTree)adaptor.create(ALL10)
                    ;
                    root_0 = (CommonTree)adaptor.becomeRoot(ALL10_tree, root_0);


                    }
                    break;
                case 2 :
                    // antlr/UIQuery.g:47:26: VISIBLE ^
                    {
                    VISIBLE11=(Token)match(input,VISIBLE,FOLLOW_VISIBLE_in_visibility156); 
                    VISIBLE11_tree = 
                    (CommonTree)adaptor.create(VISIBLE11)
                    ;
                    root_0 = (CommonTree)adaptor.becomeRoot(VISIBLE11_tree, root_0);


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
    // antlr/UIQuery.g:54:1: filter : NAME FILTER_COLON ^ ( INT | STRING | BOOL | NIL ) ;
    public final UIQueryParser.filter_return filter() throws RecognitionException {
        UIQueryParser.filter_return retval = new UIQueryParser.filter_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token NAME12=null;
        Token FILTER_COLON13=null;
        Token set14=null;

        CommonTree NAME12_tree=null;
        CommonTree FILTER_COLON13_tree=null;
        CommonTree set14_tree=null;

        try {
            // antlr/UIQuery.g:54:8: ( NAME FILTER_COLON ^ ( INT | STRING | BOOL | NIL ) )
            // antlr/UIQuery.g:54:10: NAME FILTER_COLON ^ ( INT | STRING | BOOL | NIL )
            {
            root_0 = (CommonTree)adaptor.nil();


            NAME12=(Token)match(input,NAME,FOLLOW_NAME_in_filter183); 
            NAME12_tree = 
            (CommonTree)adaptor.create(NAME12)
            ;
            adaptor.addChild(root_0, NAME12_tree);


            FILTER_COLON13=(Token)match(input,FILTER_COLON,FOLLOW_FILTER_COLON_in_filter185); 
            FILTER_COLON13_tree = 
            (CommonTree)adaptor.create(FILTER_COLON13)
            ;
            root_0 = (CommonTree)adaptor.becomeRoot(FILTER_COLON13_tree, root_0);


            set14=(Token)input.LT(1);

            if ( input.LA(1)==BOOL||input.LA(1)==INT||input.LA(1)==NIL||input.LA(1)==STRING ) {
                input.consume();
                adaptor.addChild(root_0, 
                (CommonTree)adaptor.create(set14)
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


 

    public static final BitSet FOLLOW_expr_in_query53 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_WHITE_in_query56 = new BitSet(new long[]{0x0000000000052410L});
    public static final BitSet FOLLOW_expr_in_query59 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_className_in_expr77 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_filter_in_expr81 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_visibility_in_expr85 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WILDCARD_in_className103 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_className108 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUALIFIED_NAME_in_className113 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ALL_in_visibility151 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VISIBLE_in_visibility156 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_filter183 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_FILTER_COLON_in_filter185 = new BitSet(new long[]{0x0000000000004A20L});
    public static final BitSet FOLLOW_set_in_filter188 = new BitSet(new long[]{0x0000000000000002L});

}