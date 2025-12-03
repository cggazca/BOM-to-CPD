package com.mentor.dms.contentprovider.plugin;

import org.antlr.runtime.BitSet;
import org.antlr.runtime.EarlyExitException;
import org.antlr.runtime.IntStream;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.Parser;
import org.antlr.runtime.ParserRuleReturnScope;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.RewriteRuleSubtreeStream;
import org.antlr.runtime.tree.TreeAdaptor;

public class DMSStringExprParser extends Parser {
  public static final String[] tokenNames = new String[] { 
      "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ESC", "LOGICAL_AND", "LOGICAL_NOT", "LOGICAL_NOT_NULL", "LOGICAL_NULL", "LOGICAL_OR", 
      "WILDCARD", "WILDSTRING", "WSTRING", "'&'", "'NULL'", "'|'", "'~'", "'~NULL'" };
  
  public static final int EOF = -1;
  
  public static final int T__13 = 13;
  
  public static final int T__14 = 14;
  
  public static final int T__15 = 15;
  
  public static final int T__16 = 16;
  
  public static final int T__17 = 17;
  
  public static final int ESC = 4;
  
  public static final int LOGICAL_AND = 5;
  
  public static final int LOGICAL_NOT = 6;
  
  public static final int LOGICAL_NOT_NULL = 7;
  
  public static final int LOGICAL_NULL = 8;
  
  public static final int LOGICAL_OR = 9;
  
  public static final int WILDCARD = 10;
  
  public static final int WILDSTRING = 11;
  
  public static final int WSTRING = 12;
  
  protected TreeAdaptor adaptor = (TreeAdaptor)new CommonTreeAdaptor();
  
  public static final BitSet FOLLOW_expr_in_prog74 = new BitSet(new long[] { 2L });
  
  public static final BitSet FOLLOW_logicalExpr_in_expr84 = new BitSet(new long[] { 2L });
  
  public static final BitSet FOLLOW_andExpr_in_logicalExpr117 = new BitSet(new long[] { 32770L });
  
  public static final BitSet FOLLOW_orOperator_in_logicalExpr126 = new BitSet(new long[] { 218112L });
  
  public static final BitSet FOLLOW_andExpr_in_logicalExpr128 = new BitSet(new long[] { 32770L });
  
  public static final BitSet FOLLOW_unaryExpr_in_andExpr161 = new BitSet(new long[] { 8194L });
  
  public static final BitSet FOLLOW_andOperator_in_andExpr165 = new BitSet(new long[] { 218112L });
  
  public static final BitSet FOLLOW_unaryExpr_in_andExpr167 = new BitSet(new long[] { 8194L });
  
  public static final BitSet FOLLOW_notNullMatch_in_unaryExpr199 = new BitSet(new long[] { 2L });
  
  public static final BitSet FOLLOW_nullMatch_in_unaryExpr210 = new BitSet(new long[] { 2L });
  
  public static final BitSet FOLLOW_logicalNotExpr_in_unaryExpr221 = new BitSet(new long[] { 2L });
  
  public static final BitSet FOLLOW_wildString_in_unaryExpr234 = new BitSet(new long[] { 2L });
  
  public static final BitSet FOLLOW_notOperator_in_logicalNotExpr253 = new BitSet(new long[] { 5120L });
  
  public static final BitSet FOLLOW_wildString_in_logicalNotExpr255 = new BitSet(new long[] { 2L });
  
  public static final BitSet FOLLOW_wildSubString_in_wildString278 = new BitSet(new long[] { 2L });
  
  public static final BitSet FOLLOW_wildSubString_in_wildString286 = new BitSet(new long[] { 1024L });
  
  public static final BitSet FOLLOW_wildCard_in_wildString294 = new BitSet(new long[] { 5122L });
  
  public static final BitSet FOLLOW_wildSubString_in_wildString296 = new BitSet(new long[] { 5122L });
  
  public static final BitSet FOLLOW_WSTRING_in_wildSubString325 = new BitSet(new long[] { 2L });
  
  public static final BitSet FOLLOW_WILDCARD_in_wildCard337 = new BitSet(new long[] { 2L });
  
  public static final BitSet FOLLOW_15_in_orOperator350 = new BitSet(new long[] { 2L });
  
  public static final BitSet FOLLOW_13_in_andOperator362 = new BitSet(new long[] { 2L });
  
  public static final BitSet FOLLOW_16_in_notOperator374 = new BitSet(new long[] { 2L });
  
  public static final BitSet FOLLOW_17_in_notNullMatch386 = new BitSet(new long[] { 2L });
  
  public static final BitSet FOLLOW_14_in_nullMatch398 = new BitSet(new long[] { 2L });
  
  public Parser[] getDelegates() {
    return new Parser[0];
  }
  
  public DMSStringExprParser(TokenStream paramTokenStream) {
    this(paramTokenStream, new RecognizerSharedState());
  }
  
  public DMSStringExprParser(TokenStream paramTokenStream, RecognizerSharedState paramRecognizerSharedState) {
    super(paramTokenStream, paramRecognizerSharedState);
  }
  
  public void setTreeAdaptor(TreeAdaptor paramTreeAdaptor) {
    this.adaptor = paramTreeAdaptor;
  }
  
  public TreeAdaptor getTreeAdaptor() {
    return this.adaptor;
  }
  
  public String[] getTokenNames() {
    return tokenNames;
  }
  
  public String getGrammarFileName() {
    return "C:\\Consulting\\Customers\\Raytheon\\ANTLR\\DMSStringExpr.g";
  }
  
  public final prog_return prog() throws RecognitionException {
    prog_return prog_return = new prog_return();
    prog_return.start = this.input.LT(1);
    CommonTree commonTree = null;
    expr_return expr_return = null;
    try {
      commonTree = (CommonTree)this.adaptor.nil();
      pushFollow(FOLLOW_expr_in_prog74);
      expr_return = expr();
      this.state._fsp--;
      this.adaptor.addChild(commonTree, expr_return.getTree());
      prog_return.stop = this.input.LT(-1);
      prog_return.tree = (CommonTree)this.adaptor.rulePostProcessing(commonTree);
      this.adaptor.setTokenBoundaries(prog_return.tree, prog_return.start, prog_return.stop);
    } catch (RecognitionException recognitionException) {
      reportError(recognitionException);
      recover((IntStream)this.input, recognitionException);
      prog_return.tree = (CommonTree)this.adaptor.errorNode(this.input, prog_return.start, this.input.LT(-1), recognitionException);
    } finally {}
    return prog_return;
  }
  
  public final expr_return expr() throws RecognitionException {
    expr_return expr_return = new expr_return();
    expr_return.start = this.input.LT(1);
    CommonTree commonTree = null;
    logicalExpr_return logicalExpr_return = null;
    try {
      commonTree = (CommonTree)this.adaptor.nil();
      pushFollow(FOLLOW_logicalExpr_in_expr84);
      logicalExpr_return = logicalExpr();
      this.state._fsp--;
      this.adaptor.addChild(commonTree, logicalExpr_return.getTree());
      expr_return.stop = this.input.LT(-1);
      expr_return.tree = (CommonTree)this.adaptor.rulePostProcessing(commonTree);
      this.adaptor.setTokenBoundaries(expr_return.tree, expr_return.start, expr_return.stop);
    } catch (RecognitionException recognitionException) {
      reportError(recognitionException);
      recover((IntStream)this.input, recognitionException);
      expr_return.tree = (CommonTree)this.adaptor.errorNode(this.input, expr_return.start, this.input.LT(-1), recognitionException);
    } finally {}
    return expr_return;
  }
  
  public final logicalExpr_return logicalExpr() throws RecognitionException {
    logicalExpr_return logicalExpr_return = new logicalExpr_return();
    logicalExpr_return.start = this.input.LT(1);
    CommonTree commonTree = null;
    andExpr_return andExpr_return1 = null;
    orOperator_return orOperator_return = null;
    andExpr_return andExpr_return2 = null;
    RewriteRuleSubtreeStream rewriteRuleSubtreeStream1 = new RewriteRuleSubtreeStream(this.adaptor, "rule andExpr");
    RewriteRuleSubtreeStream rewriteRuleSubtreeStream2 = new RewriteRuleSubtreeStream(this.adaptor, "rule orOperator");
    try {
      pushFollow(FOLLOW_andExpr_in_logicalExpr117);
      andExpr_return1 = andExpr();
      this.state._fsp--;
      rewriteRuleSubtreeStream1.add(andExpr_return1.getTree());
      while (true) {
        byte b = 2;
        int i = this.input.LA(1);
        if (i == 15)
          b = 1; 
        switch (b) {
          case 1:
            pushFollow(FOLLOW_orOperator_in_logicalExpr126);
            orOperator_return = orOperator();
            this.state._fsp--;
            rewriteRuleSubtreeStream2.add(orOperator_return.getTree());
            pushFollow(FOLLOW_andExpr_in_logicalExpr128);
            andExpr_return2 = andExpr();
            this.state._fsp--;
            rewriteRuleSubtreeStream1.add(andExpr_return2.getTree());
            continue;
        } 
        logicalExpr_return.tree = commonTree;
        RewriteRuleSubtreeStream rewriteRuleSubtreeStream = new RewriteRuleSubtreeStream(this.adaptor, "rule retval", (logicalExpr_return != null) ? logicalExpr_return.getTree() : null);
        commonTree = (CommonTree)this.adaptor.nil();
        CommonTree commonTree1 = (CommonTree)this.adaptor.nil();
        commonTree1 = (CommonTree)this.adaptor.becomeRoot(this.adaptor.create(9, "LOGICAL_OR"), commonTree1);
        this.adaptor.addChild(commonTree1, rewriteRuleSubtreeStream1.nextTree());
        while (rewriteRuleSubtreeStream1.hasNext())
          this.adaptor.addChild(commonTree1, rewriteRuleSubtreeStream1.nextTree()); 
        rewriteRuleSubtreeStream1.reset();
        this.adaptor.addChild(commonTree, commonTree1);
        logicalExpr_return.tree = commonTree;
        logicalExpr_return.stop = this.input.LT(-1);
        logicalExpr_return.tree = (CommonTree)this.adaptor.rulePostProcessing(commonTree);
        this.adaptor.setTokenBoundaries(logicalExpr_return.tree, logicalExpr_return.start, logicalExpr_return.stop);
        return logicalExpr_return;
      } 
    } catch (RecognitionException recognitionException) {
      reportError(recognitionException);
      recover((IntStream)this.input, recognitionException);
      logicalExpr_return.tree = (CommonTree)this.adaptor.errorNode(this.input, logicalExpr_return.start, this.input.LT(-1), recognitionException);
    } finally {}
    return logicalExpr_return;
  }
  
  public final andExpr_return andExpr() throws RecognitionException {
    andExpr_return andExpr_return = new andExpr_return();
    andExpr_return.start = this.input.LT(1);
    CommonTree commonTree = null;
    unaryExpr_return unaryExpr_return1 = null;
    andOperator_return andOperator_return = null;
    unaryExpr_return unaryExpr_return2 = null;
    RewriteRuleSubtreeStream rewriteRuleSubtreeStream1 = new RewriteRuleSubtreeStream(this.adaptor, "rule unaryExpr");
    RewriteRuleSubtreeStream rewriteRuleSubtreeStream2 = new RewriteRuleSubtreeStream(this.adaptor, "rule andOperator");
    try {
      pushFollow(FOLLOW_unaryExpr_in_andExpr161);
      unaryExpr_return1 = unaryExpr();
      this.state._fsp--;
      rewriteRuleSubtreeStream1.add(unaryExpr_return1.getTree());
      while (true) {
        byte b = 2;
        int i = this.input.LA(1);
        if (i == 13)
          b = 1; 
        switch (b) {
          case 1:
            pushFollow(FOLLOW_andOperator_in_andExpr165);
            andOperator_return = andOperator();
            this.state._fsp--;
            rewriteRuleSubtreeStream2.add(andOperator_return.getTree());
            pushFollow(FOLLOW_unaryExpr_in_andExpr167);
            unaryExpr_return2 = unaryExpr();
            this.state._fsp--;
            rewriteRuleSubtreeStream1.add(unaryExpr_return2.getTree());
            continue;
        } 
        andExpr_return.tree = commonTree;
        RewriteRuleSubtreeStream rewriteRuleSubtreeStream = new RewriteRuleSubtreeStream(this.adaptor, "rule retval", (andExpr_return != null) ? andExpr_return.getTree() : null);
        commonTree = (CommonTree)this.adaptor.nil();
        CommonTree commonTree1 = (CommonTree)this.adaptor.nil();
        commonTree1 = (CommonTree)this.adaptor.becomeRoot(this.adaptor.create(5, "LOGICAL_AND"), commonTree1);
        this.adaptor.addChild(commonTree1, rewriteRuleSubtreeStream1.nextTree());
        while (rewriteRuleSubtreeStream1.hasNext())
          this.adaptor.addChild(commonTree1, rewriteRuleSubtreeStream1.nextTree()); 
        rewriteRuleSubtreeStream1.reset();
        this.adaptor.addChild(commonTree, commonTree1);
        andExpr_return.tree = commonTree;
        andExpr_return.stop = this.input.LT(-1);
        andExpr_return.tree = (CommonTree)this.adaptor.rulePostProcessing(commonTree);
        this.adaptor.setTokenBoundaries(andExpr_return.tree, andExpr_return.start, andExpr_return.stop);
        return andExpr_return;
      } 
    } catch (RecognitionException recognitionException) {
      reportError(recognitionException);
      recover((IntStream)this.input, recognitionException);
      andExpr_return.tree = (CommonTree)this.adaptor.errorNode(this.input, andExpr_return.start, this.input.LT(-1), recognitionException);
    } finally {}
    return andExpr_return;
  }
  
  public final unaryExpr_return unaryExpr() throws RecognitionException {
    unaryExpr_return unaryExpr_return = new unaryExpr_return();
    unaryExpr_return.start = this.input.LT(1);
    CommonTree commonTree = null;
    notNullMatch_return notNullMatch_return = null;
    nullMatch_return nullMatch_return = null;
    logicalNotExpr_return logicalNotExpr_return = null;
    wildString_return wildString_return = null;
    RewriteRuleSubtreeStream rewriteRuleSubtreeStream1 = new RewriteRuleSubtreeStream(this.adaptor, "rule wildString");
    RewriteRuleSubtreeStream rewriteRuleSubtreeStream2 = new RewriteRuleSubtreeStream(this.adaptor, "rule nullMatch");
    RewriteRuleSubtreeStream rewriteRuleSubtreeStream3 = new RewriteRuleSubtreeStream(this.adaptor, "rule notNullMatch");
    RewriteRuleSubtreeStream rewriteRuleSubtreeStream4 = new RewriteRuleSubtreeStream(this.adaptor, "rule logicalNotExpr");
    try {
      NoViableAltException noViableAltException;
      RewriteRuleSubtreeStream rewriteRuleSubtreeStream;
      CommonTree commonTree1;
      byte b = 4;
      switch (this.input.LA(1)) {
        case 17:
          b = 1;
          break;
        case 14:
          b = 2;
          break;
        case 16:
          b = 3;
          break;
        case 10:
        case 12:
          b = 4;
          break;
        default:
          noViableAltException = new NoViableAltException("", 3, 0, (IntStream)this.input);
          throw noViableAltException;
      } 
      switch (b) {
        case 1:
          pushFollow(FOLLOW_notNullMatch_in_unaryExpr199);
          notNullMatch_return = notNullMatch();
          this.state._fsp--;
          rewriteRuleSubtreeStream3.add(notNullMatch_return.getTree());
          unaryExpr_return.tree = commonTree;
          rewriteRuleSubtreeStream = new RewriteRuleSubtreeStream(this.adaptor, "rule retval", (unaryExpr_return != null) ? unaryExpr_return.getTree() : null);
          commonTree = (CommonTree)this.adaptor.nil();
          commonTree1 = (CommonTree)this.adaptor.nil();
          commonTree1 = (CommonTree)this.adaptor.becomeRoot(this.adaptor.create(7, "LOGICAL_NOT_NULL"), commonTree1);
          this.adaptor.addChild(commonTree, commonTree1);
          unaryExpr_return.tree = commonTree;
          break;
        case 2:
          pushFollow(FOLLOW_nullMatch_in_unaryExpr210);
          nullMatch_return = nullMatch();
          this.state._fsp--;
          rewriteRuleSubtreeStream2.add(nullMatch_return.getTree());
          unaryExpr_return.tree = commonTree;
          rewriteRuleSubtreeStream = new RewriteRuleSubtreeStream(this.adaptor, "rule retval", (unaryExpr_return != null) ? unaryExpr_return.getTree() : null);
          commonTree = (CommonTree)this.adaptor.nil();
          commonTree1 = (CommonTree)this.adaptor.nil();
          commonTree1 = (CommonTree)this.adaptor.becomeRoot(this.adaptor.create(8, "LOGICAL_NULL"), commonTree1);
          this.adaptor.addChild(commonTree, commonTree1);
          unaryExpr_return.tree = commonTree;
          break;
        case 3:
          pushFollow(FOLLOW_logicalNotExpr_in_unaryExpr221);
          logicalNotExpr_return = logicalNotExpr();
          this.state._fsp--;
          rewriteRuleSubtreeStream4.add(logicalNotExpr_return.getTree());
          unaryExpr_return.tree = commonTree;
          rewriteRuleSubtreeStream = new RewriteRuleSubtreeStream(this.adaptor, "rule retval", (unaryExpr_return != null) ? unaryExpr_return.getTree() : null);
          commonTree = (CommonTree)this.adaptor.nil();
          commonTree1 = (CommonTree)this.adaptor.nil();
          commonTree1 = (CommonTree)this.adaptor.becomeRoot(this.adaptor.create(6, "LOGICAL_NOT"), commonTree1);
          this.adaptor.addChild(commonTree1, rewriteRuleSubtreeStream4.nextTree());
          this.adaptor.addChild(commonTree, commonTree1);
          unaryExpr_return.tree = commonTree;
          break;
        case 4:
          pushFollow(FOLLOW_wildString_in_unaryExpr234);
          wildString_return = wildString();
          this.state._fsp--;
          rewriteRuleSubtreeStream1.add(wildString_return.getTree());
          unaryExpr_return.tree = commonTree;
          rewriteRuleSubtreeStream = new RewriteRuleSubtreeStream(this.adaptor, "rule retval", (unaryExpr_return != null) ? unaryExpr_return.getTree() : null);
          commonTree = (CommonTree)this.adaptor.nil();
          commonTree1 = (CommonTree)this.adaptor.nil();
          commonTree1 = (CommonTree)this.adaptor.becomeRoot(this.adaptor.create(11, "WILDSTRING"), commonTree1);
          this.adaptor.addChild(commonTree1, rewriteRuleSubtreeStream1.nextTree());
          this.adaptor.addChild(commonTree, commonTree1);
          unaryExpr_return.tree = commonTree;
          break;
      } 
      unaryExpr_return.stop = this.input.LT(-1);
      unaryExpr_return.tree = (CommonTree)this.adaptor.rulePostProcessing(commonTree);
      this.adaptor.setTokenBoundaries(unaryExpr_return.tree, unaryExpr_return.start, unaryExpr_return.stop);
    } catch (RecognitionException recognitionException) {
      reportError(recognitionException);
      recover((IntStream)this.input, recognitionException);
      unaryExpr_return.tree = (CommonTree)this.adaptor.errorNode(this.input, unaryExpr_return.start, this.input.LT(-1), recognitionException);
    } finally {}
    return unaryExpr_return;
  }
  
  public final logicalNotExpr_return logicalNotExpr() throws RecognitionException {
    logicalNotExpr_return logicalNotExpr_return = new logicalNotExpr_return();
    logicalNotExpr_return.start = this.input.LT(1);
    CommonTree commonTree = null;
    notOperator_return notOperator_return = null;
    wildString_return wildString_return = null;
    RewriteRuleSubtreeStream rewriteRuleSubtreeStream1 = new RewriteRuleSubtreeStream(this.adaptor, "rule wildString");
    RewriteRuleSubtreeStream rewriteRuleSubtreeStream2 = new RewriteRuleSubtreeStream(this.adaptor, "rule notOperator");
    try {
      pushFollow(FOLLOW_notOperator_in_logicalNotExpr253);
      notOperator_return = notOperator();
      this.state._fsp--;
      rewriteRuleSubtreeStream2.add(notOperator_return.getTree());
      pushFollow(FOLLOW_wildString_in_logicalNotExpr255);
      wildString_return = wildString();
      this.state._fsp--;
      rewriteRuleSubtreeStream1.add(wildString_return.getTree());
      logicalNotExpr_return.tree = commonTree;
      RewriteRuleSubtreeStream rewriteRuleSubtreeStream = new RewriteRuleSubtreeStream(this.adaptor, "rule retval", (logicalNotExpr_return != null) ? logicalNotExpr_return.getTree() : null);
      commonTree = (CommonTree)this.adaptor.nil();
      CommonTree commonTree1 = (CommonTree)this.adaptor.nil();
      commonTree1 = (CommonTree)this.adaptor.becomeRoot(this.adaptor.create(11, "WILDSTRING"), commonTree1);
      this.adaptor.addChild(commonTree1, rewriteRuleSubtreeStream1.nextTree());
      this.adaptor.addChild(commonTree, commonTree1);
      logicalNotExpr_return.tree = commonTree;
      logicalNotExpr_return.stop = this.input.LT(-1);
      logicalNotExpr_return.tree = (CommonTree)this.adaptor.rulePostProcessing(commonTree);
      this.adaptor.setTokenBoundaries(logicalNotExpr_return.tree, logicalNotExpr_return.start, logicalNotExpr_return.stop);
    } catch (RecognitionException recognitionException) {
      reportError(recognitionException);
      recover((IntStream)this.input, recognitionException);
      logicalNotExpr_return.tree = (CommonTree)this.adaptor.errorNode(this.input, logicalNotExpr_return.start, this.input.LT(-1), recognitionException);
    } finally {}
    return logicalNotExpr_return;
  }
  
  public final wildString_return wildString() throws RecognitionException {
    wildString_return wildString_return = new wildString_return();
    wildString_return.start = this.input.LT(1);
    CommonTree commonTree = null;
    wildSubString_return wildSubString_return1 = null;
    wildSubString_return wildSubString_return2 = null;
    wildCard_return wildCard_return = null;
    wildSubString_return wildSubString_return3 = null;
    try {
      byte b2;
      int j;
      byte b3;
      commonTree = (CommonTree)this.adaptor.nil();
      byte b1 = 2;
      int i = this.input.LA(1);
      if (i == 12) {
        int k = this.input.LA(2);
        if (k == -1 || k == 13 || k == 15) {
          b1 = 1;
        } else if (k == 10) {
          b1 = 2;
        } else {
          int m = this.input.mark();
          try {
            this.input.consume();
            NoViableAltException noViableAltException = new NoViableAltException("", 7, 1, (IntStream)this.input);
            throw noViableAltException;
          } finally {
            this.input.rewind(m);
          } 
        } 
      } else if (i == 10) {
        b1 = 2;
      } else {
        NoViableAltException noViableAltException = new NoViableAltException("", 7, 0, (IntStream)this.input);
        throw noViableAltException;
      } 
      switch (b1) {
        case 1:
          pushFollow(FOLLOW_wildSubString_in_wildString278);
          wildSubString_return1 = wildSubString();
          this.state._fsp--;
          this.adaptor.addChild(commonTree, wildSubString_return1.getTree());
          break;
        case 2:
          b2 = 2;
          j = this.input.LA(1);
          if (j == 12)
            b2 = 1; 
          switch (b2) {
            case 1:
              pushFollow(FOLLOW_wildSubString_in_wildString286);
              wildSubString_return2 = wildSubString();
              this.state._fsp--;
              this.adaptor.addChild(commonTree, wildSubString_return2.getTree());
              break;
          } 
          b3 = 0;
          while (true) {
            byte b = 2;
            int k = this.input.LA(1);
            if (k == 10)
              b = 1; 
            switch (b) {
              case 1:
                pushFollow(FOLLOW_wildCard_in_wildString294);
                wildCard_return = wildCard();
                this.state._fsp--;
                this.adaptor.addChild(commonTree, wildCard_return.getTree());
                while (true) {
                  byte b4 = 2;
                  int m = this.input.LA(1);
                  if (m == 12)
                    b4 = 1; 
                  switch (b4) {
                    case 1:
                      pushFollow(FOLLOW_wildSubString_in_wildString296);
                      wildSubString_return3 = wildSubString();
                      this.state._fsp--;
                      this.adaptor.addChild(commonTree, wildSubString_return3.getTree());
                      continue;
                  } 
                  b3++;
                } 
            } 
            if (b3 >= 1)
              break; 
            EarlyExitException earlyExitException = new EarlyExitException(6, (IntStream)this.input);
            throw earlyExitException;
          } 
          break;
      } 
      wildString_return.stop = this.input.LT(-1);
      wildString_return.tree = (CommonTree)this.adaptor.rulePostProcessing(commonTree);
      this.adaptor.setTokenBoundaries(wildString_return.tree, wildString_return.start, wildString_return.stop);
    } catch (RecognitionException recognitionException) {
      reportError(recognitionException);
      recover((IntStream)this.input, recognitionException);
      wildString_return.tree = (CommonTree)this.adaptor.errorNode(this.input, wildString_return.start, this.input.LT(-1), recognitionException);
    } finally {}
    return wildString_return;
  }
  
  public final wildSubString_return wildSubString() throws RecognitionException {
    wildSubString_return wildSubString_return = new wildSubString_return();
    wildSubString_return.start = this.input.LT(1);
    CommonTree commonTree1 = null;
    Token token = null;
    CommonTree commonTree2 = null;
    try {
      commonTree1 = (CommonTree)this.adaptor.nil();
      token = (Token)match((IntStream)this.input, 12, FOLLOW_WSTRING_in_wildSubString325);
      commonTree2 = (CommonTree)this.adaptor.create(token);
      this.adaptor.addChild(commonTree1, commonTree2);
      wildSubString_return.stop = this.input.LT(-1);
      wildSubString_return.tree = (CommonTree)this.adaptor.rulePostProcessing(commonTree1);
      this.adaptor.setTokenBoundaries(wildSubString_return.tree, wildSubString_return.start, wildSubString_return.stop);
    } catch (RecognitionException recognitionException) {
      reportError(recognitionException);
      recover((IntStream)this.input, recognitionException);
      wildSubString_return.tree = (CommonTree)this.adaptor.errorNode(this.input, wildSubString_return.start, this.input.LT(-1), recognitionException);
    } finally {}
    return wildSubString_return;
  }
  
  public final wildCard_return wildCard() throws RecognitionException {
    wildCard_return wildCard_return = new wildCard_return();
    wildCard_return.start = this.input.LT(1);
    CommonTree commonTree1 = null;
    Token token = null;
    CommonTree commonTree2 = null;
    try {
      commonTree1 = (CommonTree)this.adaptor.nil();
      token = (Token)match((IntStream)this.input, 10, FOLLOW_WILDCARD_in_wildCard337);
      commonTree2 = (CommonTree)this.adaptor.create(token);
      commonTree1 = (CommonTree)this.adaptor.becomeRoot(commonTree2, commonTree1);
      wildCard_return.stop = this.input.LT(-1);
      wildCard_return.tree = (CommonTree)this.adaptor.rulePostProcessing(commonTree1);
      this.adaptor.setTokenBoundaries(wildCard_return.tree, wildCard_return.start, wildCard_return.stop);
    } catch (RecognitionException recognitionException) {
      reportError(recognitionException);
      recover((IntStream)this.input, recognitionException);
      wildCard_return.tree = (CommonTree)this.adaptor.errorNode(this.input, wildCard_return.start, this.input.LT(-1), recognitionException);
    } finally {}
    return wildCard_return;
  }
  
  public final orOperator_return orOperator() throws RecognitionException {
    orOperator_return orOperator_return = new orOperator_return();
    orOperator_return.start = this.input.LT(1);
    CommonTree commonTree1 = null;
    Token token = null;
    CommonTree commonTree2 = null;
    try {
      commonTree1 = (CommonTree)this.adaptor.nil();
      token = (Token)match((IntStream)this.input, 15, FOLLOW_15_in_orOperator350);
      commonTree2 = (CommonTree)this.adaptor.create(token);
      this.adaptor.addChild(commonTree1, commonTree2);
      orOperator_return.stop = this.input.LT(-1);
      orOperator_return.tree = (CommonTree)this.adaptor.rulePostProcessing(commonTree1);
      this.adaptor.setTokenBoundaries(orOperator_return.tree, orOperator_return.start, orOperator_return.stop);
    } catch (RecognitionException recognitionException) {
      reportError(recognitionException);
      recover((IntStream)this.input, recognitionException);
      orOperator_return.tree = (CommonTree)this.adaptor.errorNode(this.input, orOperator_return.start, this.input.LT(-1), recognitionException);
    } finally {}
    return orOperator_return;
  }
  
  public final andOperator_return andOperator() throws RecognitionException {
    andOperator_return andOperator_return = new andOperator_return();
    andOperator_return.start = this.input.LT(1);
    CommonTree commonTree1 = null;
    Token token = null;
    CommonTree commonTree2 = null;
    try {
      commonTree1 = (CommonTree)this.adaptor.nil();
      token = (Token)match((IntStream)this.input, 13, FOLLOW_13_in_andOperator362);
      commonTree2 = (CommonTree)this.adaptor.create(token);
      this.adaptor.addChild(commonTree1, commonTree2);
      andOperator_return.stop = this.input.LT(-1);
      andOperator_return.tree = (CommonTree)this.adaptor.rulePostProcessing(commonTree1);
      this.adaptor.setTokenBoundaries(andOperator_return.tree, andOperator_return.start, andOperator_return.stop);
    } catch (RecognitionException recognitionException) {
      reportError(recognitionException);
      recover((IntStream)this.input, recognitionException);
      andOperator_return.tree = (CommonTree)this.adaptor.errorNode(this.input, andOperator_return.start, this.input.LT(-1), recognitionException);
    } finally {}
    return andOperator_return;
  }
  
  public final notOperator_return notOperator() throws RecognitionException {
    notOperator_return notOperator_return = new notOperator_return();
    notOperator_return.start = this.input.LT(1);
    CommonTree commonTree1 = null;
    Token token = null;
    CommonTree commonTree2 = null;
    try {
      commonTree1 = (CommonTree)this.adaptor.nil();
      token = (Token)match((IntStream)this.input, 16, FOLLOW_16_in_notOperator374);
      commonTree2 = (CommonTree)this.adaptor.create(token);
      this.adaptor.addChild(commonTree1, commonTree2);
      notOperator_return.stop = this.input.LT(-1);
      notOperator_return.tree = (CommonTree)this.adaptor.rulePostProcessing(commonTree1);
      this.adaptor.setTokenBoundaries(notOperator_return.tree, notOperator_return.start, notOperator_return.stop);
    } catch (RecognitionException recognitionException) {
      reportError(recognitionException);
      recover((IntStream)this.input, recognitionException);
      notOperator_return.tree = (CommonTree)this.adaptor.errorNode(this.input, notOperator_return.start, this.input.LT(-1), recognitionException);
    } finally {}
    return notOperator_return;
  }
  
  public final notNullMatch_return notNullMatch() throws RecognitionException {
    notNullMatch_return notNullMatch_return = new notNullMatch_return();
    notNullMatch_return.start = this.input.LT(1);
    CommonTree commonTree1 = null;
    Token token = null;
    CommonTree commonTree2 = null;
    try {
      commonTree1 = (CommonTree)this.adaptor.nil();
      token = (Token)match((IntStream)this.input, 17, FOLLOW_17_in_notNullMatch386);
      commonTree2 = (CommonTree)this.adaptor.create(token);
      this.adaptor.addChild(commonTree1, commonTree2);
      notNullMatch_return.stop = this.input.LT(-1);
      notNullMatch_return.tree = (CommonTree)this.adaptor.rulePostProcessing(commonTree1);
      this.adaptor.setTokenBoundaries(notNullMatch_return.tree, notNullMatch_return.start, notNullMatch_return.stop);
    } catch (RecognitionException recognitionException) {
      reportError(recognitionException);
      recover((IntStream)this.input, recognitionException);
      notNullMatch_return.tree = (CommonTree)this.adaptor.errorNode(this.input, notNullMatch_return.start, this.input.LT(-1), recognitionException);
    } finally {}
    return notNullMatch_return;
  }
  
  public final nullMatch_return nullMatch() throws RecognitionException {
    nullMatch_return nullMatch_return = new nullMatch_return();
    nullMatch_return.start = this.input.LT(1);
    CommonTree commonTree1 = null;
    Token token = null;
    CommonTree commonTree2 = null;
    try {
      commonTree1 = (CommonTree)this.adaptor.nil();
      token = (Token)match((IntStream)this.input, 14, FOLLOW_14_in_nullMatch398);
      commonTree2 = (CommonTree)this.adaptor.create(token);
      this.adaptor.addChild(commonTree1, commonTree2);
      nullMatch_return.stop = this.input.LT(-1);
      nullMatch_return.tree = (CommonTree)this.adaptor.rulePostProcessing(commonTree1);
      this.adaptor.setTokenBoundaries(nullMatch_return.tree, nullMatch_return.start, nullMatch_return.stop);
    } catch (RecognitionException recognitionException) {
      reportError(recognitionException);
      recover((IntStream)this.input, recognitionException);
      nullMatch_return.tree = (CommonTree)this.adaptor.errorNode(this.input, nullMatch_return.start, this.input.LT(-1), recognitionException);
    } finally {}
    return nullMatch_return;
  }
  
  public static class prog_return extends ParserRuleReturnScope {
    CommonTree tree;
    
    public CommonTree getTree() {
      return this.tree;
    }
  }
  
  public static class expr_return extends ParserRuleReturnScope {
    CommonTree tree;
    
    public CommonTree getTree() {
      return this.tree;
    }
  }
  
  public static class logicalExpr_return extends ParserRuleReturnScope {
    CommonTree tree;
    
    public CommonTree getTree() {
      return this.tree;
    }
  }
  
  public static class andExpr_return extends ParserRuleReturnScope {
    CommonTree tree;
    
    public CommonTree getTree() {
      return this.tree;
    }
  }
  
  public static class orOperator_return extends ParserRuleReturnScope {
    CommonTree tree;
    
    public CommonTree getTree() {
      return this.tree;
    }
  }
  
  public static class unaryExpr_return extends ParserRuleReturnScope {
    CommonTree tree;
    
    public CommonTree getTree() {
      return this.tree;
    }
  }
  
  public static class andOperator_return extends ParserRuleReturnScope {
    CommonTree tree;
    
    public CommonTree getTree() {
      return this.tree;
    }
  }
  
  public static class notNullMatch_return extends ParserRuleReturnScope {
    CommonTree tree;
    
    public CommonTree getTree() {
      return this.tree;
    }
  }
  
  public static class nullMatch_return extends ParserRuleReturnScope {
    CommonTree tree;
    
    public CommonTree getTree() {
      return this.tree;
    }
  }
  
  public static class logicalNotExpr_return extends ParserRuleReturnScope {
    CommonTree tree;
    
    public CommonTree getTree() {
      return this.tree;
    }
  }
  
  public static class wildString_return extends ParserRuleReturnScope {
    CommonTree tree;
    
    public CommonTree getTree() {
      return this.tree;
    }
  }
  
  public static class notOperator_return extends ParserRuleReturnScope {
    CommonTree tree;
    
    public CommonTree getTree() {
      return this.tree;
    }
  }
  
  public static class wildSubString_return extends ParserRuleReturnScope {
    CommonTree tree;
    
    public CommonTree getTree() {
      return this.tree;
    }
  }
  
  public static class wildCard_return extends ParserRuleReturnScope {
    CommonTree tree;
    
    public CommonTree getTree() {
      return this.tree;
    }
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\DMSStringExprParser.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */