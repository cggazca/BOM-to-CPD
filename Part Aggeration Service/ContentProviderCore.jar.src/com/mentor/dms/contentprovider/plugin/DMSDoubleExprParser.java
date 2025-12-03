package com.mentor.dms.contentprovider.plugin;

import org.antlr.runtime.BitSet;
import org.antlr.runtime.IntStream;
import org.antlr.runtime.MismatchedSetException;
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
import org.antlr.runtime.tree.RewriteRuleTokenStream;
import org.antlr.runtime.tree.TreeAdaptor;

public class DMSDoubleExprParser extends Parser {
  public static final String[] tokenNames = new String[] { 
      "<invalid>", "<EOR>", "<DOWN>", "<UP>", "DOUBLE", "DOUBLE_VALUE", "LOGICAL_AND", "LOGICAL_NOT", "LOGICAL_NOT_NULL", "LOGICAL_NULL", 
      "LOGICAL_OR", "NEGATIVE", "RANGE_EXPRESSION", "RELATIONAL_EXPRESSION", "UNITS", "'&'", "'-'", "'<'", "'<='", "'>'", 
      "'>='", "'NULL'", "'|'", "'~'", "'~NULL'" };
  
  public static final int EOF = -1;
  
  public static final int T__15 = 15;
  
  public static final int T__16 = 16;
  
  public static final int T__17 = 17;
  
  public static final int T__18 = 18;
  
  public static final int T__19 = 19;
  
  public static final int T__20 = 20;
  
  public static final int T__21 = 21;
  
  public static final int T__22 = 22;
  
  public static final int T__23 = 23;
  
  public static final int T__24 = 24;
  
  public static final int DOUBLE = 4;
  
  public static final int DOUBLE_VALUE = 5;
  
  public static final int LOGICAL_AND = 6;
  
  public static final int LOGICAL_NOT = 7;
  
  public static final int LOGICAL_NOT_NULL = 8;
  
  public static final int LOGICAL_NULL = 9;
  
  public static final int LOGICAL_OR = 10;
  
  public static final int NEGATIVE = 11;
  
  public static final int RANGE_EXPRESSION = 12;
  
  public static final int RELATIONAL_EXPRESSION = 13;
  
  public static final int UNITS = 14;
  
  protected TreeAdaptor adaptor = (TreeAdaptor)new CommonTreeAdaptor();
  
  public static final BitSet FOLLOW_expr_in_prog86 = new BitSet(new long[] { 2L });
  
  public static final BitSet FOLLOW_logicalExpr_in_expr96 = new BitSet(new long[] { 2L });
  
  public static final BitSet FOLLOW_andExpr_in_logicalExpr129 = new BitSet(new long[] { 4194306L });
  
  public static final BitSet FOLLOW_orOperator_in_logicalExpr138 = new BitSet(new long[] { 29294608L });
  
  public static final BitSet FOLLOW_andExpr_in_logicalExpr140 = new BitSet(new long[] { 4194306L });
  
  public static final BitSet FOLLOW_unaryExpr_in_andExpr173 = new BitSet(new long[] { 32770L });
  
  public static final BitSet FOLLOW_andOperator_in_andExpr183 = new BitSet(new long[] { 29294608L });
  
  public static final BitSet FOLLOW_unaryExpr_in_andExpr185 = new BitSet(new long[] { 32770L });
  
  public static final BitSet FOLLOW_notNullMatch_in_unaryExpr216 = new BitSet(new long[] { 2L });
  
  public static final BitSet FOLLOW_nullMatch_in_unaryExpr227 = new BitSet(new long[] { 2L });
  
  public static final BitSet FOLLOW_notOperator_in_unaryExpr238 = new BitSet(new long[] { 2031632L });
  
  public static final BitSet FOLLOW_floatExpr_in_unaryExpr240 = new BitSet(new long[] { 2L });
  
  public static final BitSet FOLLOW_floatExpr_in_unaryExpr253 = new BitSet(new long[] { 2L });
  
  public static final BitSet FOLLOW_rangeExpr_in_floatExpr264 = new BitSet(new long[] { 2L });
  
  public static final BitSet FOLLOW_relationalExpr_in_floatExpr277 = new BitSet(new long[] { 2L });
  
  public static final BitSet FOLLOW_dblValue_in_floatExpr290 = new BitSet(new long[] { 2L });
  
  public static final BitSet FOLLOW_dblValue_in_rangeExpr301 = new BitSet(new long[] { 65536L });
  
  public static final BitSet FOLLOW_rangeOperator_in_rangeExpr303 = new BitSet(new long[] { 65552L });
  
  public static final BitSet FOLLOW_dblValue_in_rangeExpr306 = new BitSet(new long[] { 2L });
  
  public static final BitSet FOLLOW_relationalOperator_in_relationalExpr318 = new BitSet(new long[] { 65552L });
  
  public static final BitSet FOLLOW_dblValue_in_relationalExpr320 = new BitSet(new long[] { 2L });
  
  public static final BitSet FOLLOW_16_in_dblValue333 = new BitSet(new long[] { 16L });
  
  public static final BitSet FOLLOW_dblValueUnits_in_dblValue335 = new BitSet(new long[] { 2L });
  
  public static final BitSet FOLLOW_dblValueUnits_in_dblValue350 = new BitSet(new long[] { 2L });
  
  public static final BitSet FOLLOW_DOUBLE_in_dblValueUnits370 = new BitSet(new long[] { 2L });
  
  public static final BitSet FOLLOW_DOUBLE_in_dblValueUnits375 = new BitSet(new long[] { 16384L });
  
  public static final BitSet FOLLOW_UNITS_in_dblValueUnits377 = new BitSet(new long[] { 2L });
  
  public static final BitSet FOLLOW_22_in_orOperator389 = new BitSet(new long[] { 2L });
  
  public static final BitSet FOLLOW_15_in_andOperator401 = new BitSet(new long[] { 2L });
  
  public static final BitSet FOLLOW_23_in_notOperator413 = new BitSet(new long[] { 2L });
  
  public static final BitSet FOLLOW_24_in_notNullMatch452 = new BitSet(new long[] { 2L });
  
  public static final BitSet FOLLOW_21_in_nullMatch464 = new BitSet(new long[] { 2L });
  
  public static final BitSet FOLLOW_16_in_rangeOperator476 = new BitSet(new long[] { 2L });
  
  public Parser[] getDelegates() {
    return new Parser[0];
  }
  
  public DMSDoubleExprParser(TokenStream paramTokenStream) {
    this(paramTokenStream, new RecognizerSharedState());
  }
  
  public DMSDoubleExprParser(TokenStream paramTokenStream, RecognizerSharedState paramRecognizerSharedState) {
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
    return "C:\\Consulting\\Customers\\Raytheon\\ANTLR\\DMSDoubleExpr.g";
  }
  
  public final prog_return prog() throws RecognitionException {
    prog_return prog_return = new prog_return();
    prog_return.start = this.input.LT(1);
    CommonTree commonTree = null;
    expr_return expr_return = null;
    try {
      commonTree = (CommonTree)this.adaptor.nil();
      pushFollow(FOLLOW_expr_in_prog86);
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
      pushFollow(FOLLOW_logicalExpr_in_expr96);
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
      pushFollow(FOLLOW_andExpr_in_logicalExpr129);
      andExpr_return1 = andExpr();
      this.state._fsp--;
      rewriteRuleSubtreeStream1.add(andExpr_return1.getTree());
      while (true) {
        byte b = 2;
        int i = this.input.LA(1);
        if (i == 22)
          b = 1; 
        switch (b) {
          case 1:
            pushFollow(FOLLOW_orOperator_in_logicalExpr138);
            orOperator_return = orOperator();
            this.state._fsp--;
            rewriteRuleSubtreeStream2.add(orOperator_return.getTree());
            pushFollow(FOLLOW_andExpr_in_logicalExpr140);
            andExpr_return2 = andExpr();
            this.state._fsp--;
            rewriteRuleSubtreeStream1.add(andExpr_return2.getTree());
            continue;
        } 
        logicalExpr_return.tree = commonTree;
        RewriteRuleSubtreeStream rewriteRuleSubtreeStream = new RewriteRuleSubtreeStream(this.adaptor, "rule retval", (logicalExpr_return != null) ? logicalExpr_return.getTree() : null);
        commonTree = (CommonTree)this.adaptor.nil();
        CommonTree commonTree1 = (CommonTree)this.adaptor.nil();
        commonTree1 = (CommonTree)this.adaptor.becomeRoot(this.adaptor.create(10, "LOGICAL_OR"), commonTree1);
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
      pushFollow(FOLLOW_unaryExpr_in_andExpr173);
      unaryExpr_return1 = unaryExpr();
      this.state._fsp--;
      rewriteRuleSubtreeStream1.add(unaryExpr_return1.getTree());
      while (true) {
        byte b = 2;
        int i = this.input.LA(1);
        if (i == 15)
          b = 1; 
        switch (b) {
          case 1:
            pushFollow(FOLLOW_andOperator_in_andExpr183);
            andOperator_return = andOperator();
            this.state._fsp--;
            rewriteRuleSubtreeStream2.add(andOperator_return.getTree());
            pushFollow(FOLLOW_unaryExpr_in_andExpr185);
            unaryExpr_return2 = unaryExpr();
            this.state._fsp--;
            rewriteRuleSubtreeStream1.add(unaryExpr_return2.getTree());
            continue;
        } 
        andExpr_return.tree = commonTree;
        RewriteRuleSubtreeStream rewriteRuleSubtreeStream = new RewriteRuleSubtreeStream(this.adaptor, "rule retval", (andExpr_return != null) ? andExpr_return.getTree() : null);
        commonTree = (CommonTree)this.adaptor.nil();
        CommonTree commonTree1 = (CommonTree)this.adaptor.nil();
        commonTree1 = (CommonTree)this.adaptor.becomeRoot(this.adaptor.create(6, "LOGICAL_AND"), commonTree1);
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
    notOperator_return notOperator_return = null;
    floatExpr_return floatExpr_return1 = null;
    floatExpr_return floatExpr_return2 = null;
    RewriteRuleSubtreeStream rewriteRuleSubtreeStream1 = new RewriteRuleSubtreeStream(this.adaptor, "rule nullMatch");
    RewriteRuleSubtreeStream rewriteRuleSubtreeStream2 = new RewriteRuleSubtreeStream(this.adaptor, "rule notNullMatch");
    RewriteRuleSubtreeStream rewriteRuleSubtreeStream3 = new RewriteRuleSubtreeStream(this.adaptor, "rule floatExpr");
    RewriteRuleSubtreeStream rewriteRuleSubtreeStream4 = new RewriteRuleSubtreeStream(this.adaptor, "rule notOperator");
    try {
      NoViableAltException noViableAltException;
      RewriteRuleSubtreeStream rewriteRuleSubtreeStream;
      CommonTree commonTree1;
      byte b = 4;
      switch (this.input.LA(1)) {
        case 24:
          b = 1;
          break;
        case 21:
          b = 2;
          break;
        case 23:
          b = 3;
          break;
        case 4:
        case 16:
        case 17:
        case 18:
        case 19:
        case 20:
          b = 4;
          break;
        default:
          noViableAltException = new NoViableAltException("", 3, 0, (IntStream)this.input);
          throw noViableAltException;
      } 
      switch (b) {
        case 1:
          pushFollow(FOLLOW_notNullMatch_in_unaryExpr216);
          notNullMatch_return = notNullMatch();
          this.state._fsp--;
          rewriteRuleSubtreeStream2.add(notNullMatch_return.getTree());
          unaryExpr_return.tree = commonTree;
          rewriteRuleSubtreeStream = new RewriteRuleSubtreeStream(this.adaptor, "rule retval", (unaryExpr_return != null) ? unaryExpr_return.getTree() : null);
          commonTree = (CommonTree)this.adaptor.nil();
          commonTree1 = (CommonTree)this.adaptor.nil();
          commonTree1 = (CommonTree)this.adaptor.becomeRoot(this.adaptor.create(8, "LOGICAL_NOT_NULL"), commonTree1);
          this.adaptor.addChild(commonTree, commonTree1);
          unaryExpr_return.tree = commonTree;
          break;
        case 2:
          pushFollow(FOLLOW_nullMatch_in_unaryExpr227);
          nullMatch_return = nullMatch();
          this.state._fsp--;
          rewriteRuleSubtreeStream1.add(nullMatch_return.getTree());
          unaryExpr_return.tree = commonTree;
          rewriteRuleSubtreeStream = new RewriteRuleSubtreeStream(this.adaptor, "rule retval", (unaryExpr_return != null) ? unaryExpr_return.getTree() : null);
          commonTree = (CommonTree)this.adaptor.nil();
          commonTree1 = (CommonTree)this.adaptor.nil();
          commonTree1 = (CommonTree)this.adaptor.becomeRoot(this.adaptor.create(9, "LOGICAL_NULL"), commonTree1);
          this.adaptor.addChild(commonTree, commonTree1);
          unaryExpr_return.tree = commonTree;
          break;
        case 3:
          pushFollow(FOLLOW_notOperator_in_unaryExpr238);
          notOperator_return = notOperator();
          this.state._fsp--;
          rewriteRuleSubtreeStream4.add(notOperator_return.getTree());
          pushFollow(FOLLOW_floatExpr_in_unaryExpr240);
          floatExpr_return1 = floatExpr();
          this.state._fsp--;
          rewriteRuleSubtreeStream3.add(floatExpr_return1.getTree());
          unaryExpr_return.tree = commonTree;
          rewriteRuleSubtreeStream = new RewriteRuleSubtreeStream(this.adaptor, "rule retval", (unaryExpr_return != null) ? unaryExpr_return.getTree() : null);
          commonTree = (CommonTree)this.adaptor.nil();
          commonTree1 = (CommonTree)this.adaptor.nil();
          commonTree1 = (CommonTree)this.adaptor.becomeRoot(this.adaptor.create(7, "LOGICAL_NOT"), commonTree1);
          this.adaptor.addChild(commonTree1, rewriteRuleSubtreeStream3.nextTree());
          this.adaptor.addChild(commonTree, commonTree1);
          unaryExpr_return.tree = commonTree;
          break;
        case 4:
          commonTree = (CommonTree)this.adaptor.nil();
          pushFollow(FOLLOW_floatExpr_in_unaryExpr253);
          floatExpr_return2 = floatExpr();
          this.state._fsp--;
          this.adaptor.addChild(commonTree, floatExpr_return2.getTree());
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
  
  public final floatExpr_return floatExpr() throws RecognitionException {
    floatExpr_return floatExpr_return = new floatExpr_return();
    floatExpr_return.start = this.input.LT(1);
    CommonTree commonTree = null;
    rangeExpr_return rangeExpr_return = null;
    relationalExpr_return relationalExpr_return = null;
    dblValue_return dblValue_return = null;
    RewriteRuleSubtreeStream rewriteRuleSubtreeStream1 = new RewriteRuleSubtreeStream(this.adaptor, "rule rangeExpr");
    RewriteRuleSubtreeStream rewriteRuleSubtreeStream2 = new RewriteRuleSubtreeStream(this.adaptor, "rule relationalExpr");
    try {
      int i;
      NoViableAltException noViableAltException;
      RewriteRuleSubtreeStream rewriteRuleSubtreeStream;
      int j;
      CommonTree commonTree1;
      byte b = 3;
      switch (this.input.LA(1)) {
        case 16:
          i = this.input.LA(2);
          if (i == 4) {
            int m;
            switch (this.input.LA(3)) {
              case 14:
                k = this.input.LA(4);
                if (k == 16) {
                  b = 1;
                  break;
                } 
                if (k == -1 || k == 15 || k == 22) {
                  b = 3;
                  break;
                } 
                m = this.input.mark();
                try {
                  for (byte b1 = 0; b1 < 3; b1++)
                    this.input.consume(); 
                  NoViableAltException noViableAltException1 = new NoViableAltException("", 4, 8, (IntStream)this.input);
                  throw noViableAltException1;
                } finally {
                  this.input.rewind(m);
                } 
              case 16:
                b = 1;
                break;
              case -1:
              case 15:
              case 22:
                b = 3;
                break;
            } 
            int k = this.input.mark();
            try {
              for (m = 0; m < 2; m++)
                this.input.consume(); 
              NoViableAltException noViableAltException1 = new NoViableAltException("", 4, 4, (IntStream)this.input);
              throw noViableAltException1;
            } finally {
              this.input.rewind(k);
            } 
          } 
          j = this.input.mark();
          try {
            this.input.consume();
            NoViableAltException noViableAltException1 = new NoViableAltException("", 4, 1, (IntStream)this.input);
            throw noViableAltException1;
          } finally {
            this.input.rewind(j);
          } 
        case 4:
          switch (this.input.LA(2)) {
            case 14:
              i = this.input.LA(3);
              if (i == 16) {
                b = 1;
                break;
              } 
              if (i == -1 || i == 15 || i == 22) {
                b = 3;
                break;
              } 
              j = this.input.mark();
              try {
                for (byte b1 = 0; b1 < 2; b1++)
                  this.input.consume(); 
                NoViableAltException noViableAltException1 = new NoViableAltException("", 4, 5, (IntStream)this.input);
                throw noViableAltException1;
              } finally {
                this.input.rewind(j);
              } 
            case 16:
              b = 1;
              break;
            case -1:
            case 15:
            case 22:
              b = 3;
              break;
          } 
          i = this.input.mark();
          try {
            this.input.consume();
            NoViableAltException noViableAltException1 = new NoViableAltException("", 4, 2, (IntStream)this.input);
            throw noViableAltException1;
          } finally {
            this.input.rewind(i);
          } 
        case 17:
        case 18:
        case 19:
        case 20:
          b = 2;
          break;
        default:
          noViableAltException = new NoViableAltException("", 4, 0, (IntStream)this.input);
          throw noViableAltException;
      } 
      switch (b) {
        case 1:
          pushFollow(FOLLOW_rangeExpr_in_floatExpr264);
          rangeExpr_return = rangeExpr();
          this.state._fsp--;
          rewriteRuleSubtreeStream1.add(rangeExpr_return.getTree());
          floatExpr_return.tree = commonTree;
          rewriteRuleSubtreeStream = new RewriteRuleSubtreeStream(this.adaptor, "rule retval", (floatExpr_return != null) ? floatExpr_return.getTree() : null);
          commonTree = (CommonTree)this.adaptor.nil();
          commonTree1 = (CommonTree)this.adaptor.nil();
          commonTree1 = (CommonTree)this.adaptor.becomeRoot(this.adaptor.create(12, "RANGE_EXPRESSION"), commonTree1);
          this.adaptor.addChild(commonTree1, rewriteRuleSubtreeStream1.nextTree());
          this.adaptor.addChild(commonTree, commonTree1);
          floatExpr_return.tree = commonTree;
          break;
        case 2:
          pushFollow(FOLLOW_relationalExpr_in_floatExpr277);
          relationalExpr_return = relationalExpr();
          this.state._fsp--;
          rewriteRuleSubtreeStream2.add(relationalExpr_return.getTree());
          floatExpr_return.tree = commonTree;
          rewriteRuleSubtreeStream = new RewriteRuleSubtreeStream(this.adaptor, "rule retval", (floatExpr_return != null) ? floatExpr_return.getTree() : null);
          commonTree = (CommonTree)this.adaptor.nil();
          commonTree1 = (CommonTree)this.adaptor.nil();
          commonTree1 = (CommonTree)this.adaptor.becomeRoot(this.adaptor.create(13, "RELATIONAL_EXPRESSION"), commonTree1);
          this.adaptor.addChild(commonTree1, rewriteRuleSubtreeStream2.nextTree());
          this.adaptor.addChild(commonTree, commonTree1);
          floatExpr_return.tree = commonTree;
          break;
        case 3:
          commonTree = (CommonTree)this.adaptor.nil();
          pushFollow(FOLLOW_dblValue_in_floatExpr290);
          dblValue_return = dblValue();
          this.state._fsp--;
          this.adaptor.addChild(commonTree, dblValue_return.getTree());
          break;
      } 
      floatExpr_return.stop = this.input.LT(-1);
      floatExpr_return.tree = (CommonTree)this.adaptor.rulePostProcessing(commonTree);
      this.adaptor.setTokenBoundaries(floatExpr_return.tree, floatExpr_return.start, floatExpr_return.stop);
    } catch (RecognitionException recognitionException) {
      reportError(recognitionException);
      recover((IntStream)this.input, recognitionException);
      floatExpr_return.tree = (CommonTree)this.adaptor.errorNode(this.input, floatExpr_return.start, this.input.LT(-1), recognitionException);
    } finally {}
    return floatExpr_return;
  }
  
  public final rangeExpr_return rangeExpr() throws RecognitionException {
    rangeExpr_return rangeExpr_return = new rangeExpr_return();
    rangeExpr_return.start = this.input.LT(1);
    CommonTree commonTree = null;
    dblValue_return dblValue_return1 = null;
    rangeOperator_return rangeOperator_return = null;
    dblValue_return dblValue_return2 = null;
    try {
      commonTree = (CommonTree)this.adaptor.nil();
      pushFollow(FOLLOW_dblValue_in_rangeExpr301);
      dblValue_return1 = dblValue();
      this.state._fsp--;
      this.adaptor.addChild(commonTree, dblValue_return1.getTree());
      pushFollow(FOLLOW_rangeOperator_in_rangeExpr303);
      rangeOperator_return = rangeOperator();
      this.state._fsp--;
      pushFollow(FOLLOW_dblValue_in_rangeExpr306);
      dblValue_return2 = dblValue();
      this.state._fsp--;
      this.adaptor.addChild(commonTree, dblValue_return2.getTree());
      rangeExpr_return.stop = this.input.LT(-1);
      rangeExpr_return.tree = (CommonTree)this.adaptor.rulePostProcessing(commonTree);
      this.adaptor.setTokenBoundaries(rangeExpr_return.tree, rangeExpr_return.start, rangeExpr_return.stop);
    } catch (RecognitionException recognitionException) {
      reportError(recognitionException);
      recover((IntStream)this.input, recognitionException);
      rangeExpr_return.tree = (CommonTree)this.adaptor.errorNode(this.input, rangeExpr_return.start, this.input.LT(-1), recognitionException);
    } finally {}
    return rangeExpr_return;
  }
  
  public final relationalExpr_return relationalExpr() throws RecognitionException {
    relationalExpr_return relationalExpr_return = new relationalExpr_return();
    relationalExpr_return.start = this.input.LT(1);
    CommonTree commonTree = null;
    relationalOperator_return relationalOperator_return = null;
    dblValue_return dblValue_return = null;
    try {
      commonTree = (CommonTree)this.adaptor.nil();
      pushFollow(FOLLOW_relationalOperator_in_relationalExpr318);
      relationalOperator_return = relationalOperator();
      this.state._fsp--;
      this.adaptor.addChild(commonTree, relationalOperator_return.getTree());
      pushFollow(FOLLOW_dblValue_in_relationalExpr320);
      dblValue_return = dblValue();
      this.state._fsp--;
      this.adaptor.addChild(commonTree, dblValue_return.getTree());
      relationalExpr_return.stop = this.input.LT(-1);
      relationalExpr_return.tree = (CommonTree)this.adaptor.rulePostProcessing(commonTree);
      this.adaptor.setTokenBoundaries(relationalExpr_return.tree, relationalExpr_return.start, relationalExpr_return.stop);
    } catch (RecognitionException recognitionException) {
      reportError(recognitionException);
      recover((IntStream)this.input, recognitionException);
      relationalExpr_return.tree = (CommonTree)this.adaptor.errorNode(this.input, relationalExpr_return.start, this.input.LT(-1), recognitionException);
    } finally {}
    return relationalExpr_return;
  }
  
  public final dblValue_return dblValue() throws RecognitionException {
    dblValue_return dblValue_return = new dblValue_return();
    dblValue_return.start = this.input.LT(1);
    CommonTree commonTree = null;
    Token token = null;
    dblValueUnits_return dblValueUnits_return1 = null;
    dblValueUnits_return dblValueUnits_return2 = null;
    Object object = null;
    RewriteRuleTokenStream rewriteRuleTokenStream = new RewriteRuleTokenStream(this.adaptor, "token 16");
    RewriteRuleSubtreeStream rewriteRuleSubtreeStream = new RewriteRuleSubtreeStream(this.adaptor, "rule dblValueUnits");
    try {
      RewriteRuleSubtreeStream rewriteRuleSubtreeStream1;
      CommonTree commonTree1;
      byte b = 2;
      int i = this.input.LA(1);
      if (i == 16) {
        b = 1;
      } else if (i == 4) {
        b = 2;
      } else {
        NoViableAltException noViableAltException = new NoViableAltException("", 5, 0, (IntStream)this.input);
        throw noViableAltException;
      } 
      switch (b) {
        case 1:
          token = (Token)match((IntStream)this.input, 16, FOLLOW_16_in_dblValue333);
          rewriteRuleTokenStream.add(token);
          pushFollow(FOLLOW_dblValueUnits_in_dblValue335);
          dblValueUnits_return1 = dblValueUnits();
          this.state._fsp--;
          rewriteRuleSubtreeStream.add(dblValueUnits_return1.getTree());
          dblValue_return.tree = commonTree;
          rewriteRuleSubtreeStream1 = new RewriteRuleSubtreeStream(this.adaptor, "rule retval", (dblValue_return != null) ? dblValue_return.getTree() : null);
          commonTree = (CommonTree)this.adaptor.nil();
          commonTree1 = (CommonTree)this.adaptor.nil();
          commonTree1 = (CommonTree)this.adaptor.becomeRoot(this.adaptor.create(5, "DOUBLE_VALUE"), commonTree1);
          this.adaptor.addChild(commonTree1, this.adaptor.create(11, "NEGATIVE"));
          this.adaptor.addChild(commonTree1, rewriteRuleSubtreeStream.nextTree());
          this.adaptor.addChild(commonTree, commonTree1);
          dblValue_return.tree = commonTree;
          break;
        case 2:
          pushFollow(FOLLOW_dblValueUnits_in_dblValue350);
          dblValueUnits_return2 = dblValueUnits();
          this.state._fsp--;
          rewriteRuleSubtreeStream.add(dblValueUnits_return2.getTree());
          dblValue_return.tree = commonTree;
          rewriteRuleSubtreeStream1 = new RewriteRuleSubtreeStream(this.adaptor, "rule retval", (dblValue_return != null) ? dblValue_return.getTree() : null);
          commonTree = (CommonTree)this.adaptor.nil();
          commonTree1 = (CommonTree)this.adaptor.nil();
          commonTree1 = (CommonTree)this.adaptor.becomeRoot(this.adaptor.create(5, "DOUBLE_VALUE"), commonTree1);
          this.adaptor.addChild(commonTree1, rewriteRuleSubtreeStream.nextTree());
          this.adaptor.addChild(commonTree, commonTree1);
          dblValue_return.tree = commonTree;
          break;
      } 
      dblValue_return.stop = this.input.LT(-1);
      dblValue_return.tree = (CommonTree)this.adaptor.rulePostProcessing(commonTree);
      this.adaptor.setTokenBoundaries(dblValue_return.tree, dblValue_return.start, dblValue_return.stop);
    } catch (RecognitionException recognitionException) {
      reportError(recognitionException);
      recover((IntStream)this.input, recognitionException);
      dblValue_return.tree = (CommonTree)this.adaptor.errorNode(this.input, dblValue_return.start, this.input.LT(-1), recognitionException);
    } finally {}
    return dblValue_return;
  }
  
  public final dblValueUnits_return dblValueUnits() throws RecognitionException {
    dblValueUnits_return dblValueUnits_return = new dblValueUnits_return();
    dblValueUnits_return.start = this.input.LT(1);
    CommonTree commonTree1 = null;
    Token token1 = null;
    Token token2 = null;
    Token token3 = null;
    CommonTree commonTree2 = null;
    CommonTree commonTree3 = null;
    CommonTree commonTree4 = null;
    try {
      byte b = 2;
      int i = this.input.LA(1);
      if (i == 4) {
        int j = this.input.LA(2);
        if (j == 14) {
          b = 2;
        } else if (j == -1 || (j >= 15 && j <= 16) || j == 22) {
          b = 1;
        } else {
          int k = this.input.mark();
          try {
            this.input.consume();
            NoViableAltException noViableAltException = new NoViableAltException("", 6, 1, (IntStream)this.input);
            throw noViableAltException;
          } finally {
            this.input.rewind(k);
          } 
        } 
      } else {
        NoViableAltException noViableAltException = new NoViableAltException("", 6, 0, (IntStream)this.input);
        throw noViableAltException;
      } 
      switch (b) {
        case 1:
          commonTree1 = (CommonTree)this.adaptor.nil();
          token1 = (Token)match((IntStream)this.input, 4, FOLLOW_DOUBLE_in_dblValueUnits370);
          commonTree2 = (CommonTree)this.adaptor.create(token1);
          this.adaptor.addChild(commonTree1, commonTree2);
          break;
        case 2:
          commonTree1 = (CommonTree)this.adaptor.nil();
          token2 = (Token)match((IntStream)this.input, 4, FOLLOW_DOUBLE_in_dblValueUnits375);
          commonTree3 = (CommonTree)this.adaptor.create(token2);
          this.adaptor.addChild(commonTree1, commonTree3);
          token3 = (Token)match((IntStream)this.input, 14, FOLLOW_UNITS_in_dblValueUnits377);
          commonTree4 = (CommonTree)this.adaptor.create(token3);
          this.adaptor.addChild(commonTree1, commonTree4);
          break;
      } 
      dblValueUnits_return.stop = this.input.LT(-1);
      dblValueUnits_return.tree = (CommonTree)this.adaptor.rulePostProcessing(commonTree1);
      this.adaptor.setTokenBoundaries(dblValueUnits_return.tree, dblValueUnits_return.start, dblValueUnits_return.stop);
    } catch (RecognitionException recognitionException) {
      reportError(recognitionException);
      recover((IntStream)this.input, recognitionException);
      dblValueUnits_return.tree = (CommonTree)this.adaptor.errorNode(this.input, dblValueUnits_return.start, this.input.LT(-1), recognitionException);
    } finally {}
    return dblValueUnits_return;
  }
  
  public final orOperator_return orOperator() throws RecognitionException {
    orOperator_return orOperator_return = new orOperator_return();
    orOperator_return.start = this.input.LT(1);
    CommonTree commonTree1 = null;
    Token token = null;
    CommonTree commonTree2 = null;
    try {
      commonTree1 = (CommonTree)this.adaptor.nil();
      token = (Token)match((IntStream)this.input, 22, FOLLOW_22_in_orOperator389);
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
      token = (Token)match((IntStream)this.input, 15, FOLLOW_15_in_andOperator401);
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
      token = (Token)match((IntStream)this.input, 23, FOLLOW_23_in_notOperator413);
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
  
  public final relationalOperator_return relationalOperator() throws RecognitionException {
    relationalOperator_return relationalOperator_return = new relationalOperator_return();
    relationalOperator_return.start = this.input.LT(1);
    CommonTree commonTree = null;
    Token token = null;
    Object object = null;
    try {
      commonTree = (CommonTree)this.adaptor.nil();
      token = this.input.LT(1);
      if (this.input.LA(1) >= 17 && this.input.LA(1) <= 20) {
        this.input.consume();
        this.adaptor.addChild(commonTree, this.adaptor.create(token));
        this.state.errorRecovery = false;
      } else {
        MismatchedSetException mismatchedSetException = new MismatchedSetException(null, (IntStream)this.input);
        throw mismatchedSetException;
      } 
      relationalOperator_return.stop = this.input.LT(-1);
      relationalOperator_return.tree = (CommonTree)this.adaptor.rulePostProcessing(commonTree);
      this.adaptor.setTokenBoundaries(relationalOperator_return.tree, relationalOperator_return.start, relationalOperator_return.stop);
    } catch (RecognitionException recognitionException) {
      reportError(recognitionException);
      recover((IntStream)this.input, recognitionException);
      relationalOperator_return.tree = (CommonTree)this.adaptor.errorNode(this.input, relationalOperator_return.start, this.input.LT(-1), recognitionException);
    } finally {}
    return relationalOperator_return;
  }
  
  public final notNullMatch_return notNullMatch() throws RecognitionException {
    notNullMatch_return notNullMatch_return = new notNullMatch_return();
    notNullMatch_return.start = this.input.LT(1);
    CommonTree commonTree1 = null;
    Token token = null;
    CommonTree commonTree2 = null;
    try {
      commonTree1 = (CommonTree)this.adaptor.nil();
      token = (Token)match((IntStream)this.input, 24, FOLLOW_24_in_notNullMatch452);
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
      token = (Token)match((IntStream)this.input, 21, FOLLOW_21_in_nullMatch464);
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
  
  public final rangeOperator_return rangeOperator() throws RecognitionException {
    rangeOperator_return rangeOperator_return = new rangeOperator_return();
    rangeOperator_return.start = this.input.LT(1);
    CommonTree commonTree1 = null;
    Token token = null;
    CommonTree commonTree2 = null;
    try {
      commonTree1 = (CommonTree)this.adaptor.nil();
      token = (Token)match((IntStream)this.input, 16, FOLLOW_16_in_rangeOperator476);
      commonTree2 = (CommonTree)this.adaptor.create(token);
      this.adaptor.addChild(commonTree1, commonTree2);
      rangeOperator_return.stop = this.input.LT(-1);
      rangeOperator_return.tree = (CommonTree)this.adaptor.rulePostProcessing(commonTree1);
      this.adaptor.setTokenBoundaries(rangeOperator_return.tree, rangeOperator_return.start, rangeOperator_return.stop);
    } catch (RecognitionException recognitionException) {
      reportError(recognitionException);
      recover((IntStream)this.input, recognitionException);
      rangeOperator_return.tree = (CommonTree)this.adaptor.errorNode(this.input, rangeOperator_return.start, this.input.LT(-1), recognitionException);
    } finally {}
    return rangeOperator_return;
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
  
  public static class notOperator_return extends ParserRuleReturnScope {
    CommonTree tree;
    
    public CommonTree getTree() {
      return this.tree;
    }
  }
  
  public static class floatExpr_return extends ParserRuleReturnScope {
    CommonTree tree;
    
    public CommonTree getTree() {
      return this.tree;
    }
  }
  
  public static class rangeExpr_return extends ParserRuleReturnScope {
    CommonTree tree;
    
    public CommonTree getTree() {
      return this.tree;
    }
  }
  
  public static class relationalExpr_return extends ParserRuleReturnScope {
    CommonTree tree;
    
    public CommonTree getTree() {
      return this.tree;
    }
  }
  
  public static class dblValue_return extends ParserRuleReturnScope {
    CommonTree tree;
    
    public CommonTree getTree() {
      return this.tree;
    }
  }
  
  public static class rangeOperator_return extends ParserRuleReturnScope {
    CommonTree tree;
    
    public CommonTree getTree() {
      return this.tree;
    }
  }
  
  public static class relationalOperator_return extends ParserRuleReturnScope {
    CommonTree tree;
    
    public CommonTree getTree() {
      return this.tree;
    }
  }
  
  public static class dblValueUnits_return extends ParserRuleReturnScope {
    CommonTree tree;
    
    public CommonTree getTree() {
      return this.tree;
    }
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\DMSDoubleExprParser.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */