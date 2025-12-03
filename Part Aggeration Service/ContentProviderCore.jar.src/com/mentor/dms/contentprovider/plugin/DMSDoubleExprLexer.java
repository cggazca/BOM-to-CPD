package com.mentor.dms.contentprovider.plugin;

import org.antlr.runtime.BaseRecognizer;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.DFA;
import org.antlr.runtime.EarlyExitException;
import org.antlr.runtime.IntStream;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;

public class DMSDoubleExprLexer extends Lexer {
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
  
  protected DFA4 dfa4 = new DFA4((BaseRecognizer)this);
  
  static final String DFA4_eotS = "\001￿\001\003\002￿";
  
  static final String DFA4_eofS = "\004￿";
  
  static final String DFA4_minS = "\002.\002￿";
  
  static final String DFA4_maxS = "\0029\002￿";
  
  static final String DFA4_acceptS = "\002￿\001\002\001\001";
  
  static final String DFA4_specialS = "\004￿}>";
  
  static final String[] DFA4_transitionS = new String[] { "\001\002\001￿\n\001", "\001\002\001￿\n\001", "", "" };
  
  static final short[] DFA4_eot = DFA.unpackEncodedString("\001￿\001\003\002￿");
  
  static final short[] DFA4_eof = DFA.unpackEncodedString("\004￿");
  
  static final char[] DFA4_min = DFA.unpackEncodedStringToUnsignedChars("\002.\002￿");
  
  static final char[] DFA4_max = DFA.unpackEncodedStringToUnsignedChars("\0029\002￿");
  
  static final short[] DFA4_accept = DFA.unpackEncodedString("\002￿\001\002\001\001");
  
  static final short[] DFA4_special = DFA.unpackEncodedString("\004￿}>");
  
  static final short[][] DFA4_transition;
  
  public Lexer[] getDelegates() {
    return new Lexer[0];
  }
  
  public DMSDoubleExprLexer() {}
  
  public DMSDoubleExprLexer(CharStream paramCharStream) {
    this(paramCharStream, new RecognizerSharedState());
  }
  
  public DMSDoubleExprLexer(CharStream paramCharStream, RecognizerSharedState paramRecognizerSharedState) {
    super(paramCharStream, paramRecognizerSharedState);
  }
  
  public String getGrammarFileName() {
    return "C:\\Consulting\\Customers\\Raytheon\\ANTLR\\DMSDoubleExpr.g";
  }
  
  public final void mT__15() throws RecognitionException {
    byte b = 15;
    boolean bool = false;
    match(38);
    this.state.type = b;
    this.state.channel = bool;
  }
  
  public final void mT__16() throws RecognitionException {
    byte b = 16;
    boolean bool = false;
    match(45);
    this.state.type = b;
    this.state.channel = bool;
  }
  
  public final void mT__17() throws RecognitionException {
    byte b = 17;
    boolean bool = false;
    match(60);
    this.state.type = b;
    this.state.channel = bool;
  }
  
  public final void mT__18() throws RecognitionException {
    byte b = 18;
    boolean bool = false;
    match("<=");
    this.state.type = b;
    this.state.channel = bool;
  }
  
  public final void mT__19() throws RecognitionException {
    byte b = 19;
    boolean bool = false;
    match(62);
    this.state.type = b;
    this.state.channel = bool;
  }
  
  public final void mT__20() throws RecognitionException {
    byte b = 20;
    boolean bool = false;
    match(">=");
    this.state.type = b;
    this.state.channel = bool;
  }
  
  public final void mT__21() throws RecognitionException {
    byte b = 21;
    boolean bool = false;
    match("NULL");
    this.state.type = b;
    this.state.channel = bool;
  }
  
  public final void mT__22() throws RecognitionException {
    byte b = 22;
    boolean bool = false;
    match(124);
    this.state.type = b;
    this.state.channel = bool;
  }
  
  public final void mT__23() throws RecognitionException {
    byte b = 23;
    boolean bool = false;
    match(126);
    this.state.type = b;
    this.state.channel = bool;
  }
  
  public final void mT__24() throws RecognitionException {
    byte b = 24;
    boolean bool = false;
    match("~NULL");
    this.state.type = b;
    this.state.channel = bool;
  }
  
  public final void mDOUBLE() throws RecognitionException {
    byte b2;
    byte b1 = 4;
    boolean bool = false;
    int i = 2;
    i = this.dfa4.predict((IntStream)this.input);
    switch (i) {
      case 1:
        for (b2 = 0;; b2++) {
          MismatchedSetException mismatchedSetException;
          EarlyExitException earlyExitException;
          byte b = 2;
          int j = this.input.LA(1);
          if (j >= 48 && j <= 57)
            b = 1; 
          switch (b) {
            case 1:
              if (this.input.LA(1) >= 48 && this.input.LA(1) <= 57) {
                this.input.consume();
                break;
              } 
              mismatchedSetException = new MismatchedSetException(null, (IntStream)this.input);
              recover((RecognitionException)mismatchedSetException);
              throw mismatchedSetException;
            default:
              if (b2 >= 1)
                break; 
              earlyExitException = new EarlyExitException(1, (IntStream)this.input);
              throw earlyExitException;
          } 
        } 
        break;
      case 2:
        while (true) {
          MismatchedSetException mismatchedSetException;
          b2 = 2;
          int j = this.input.LA(1);
          if (j >= 48 && j <= 57)
            b2 = 1; 
          switch (b2) {
            case 1:
              if (this.input.LA(1) >= 48 && this.input.LA(1) <= 57) {
                this.input.consume();
                continue;
              } 
              mismatchedSetException = new MismatchedSetException(null, (IntStream)this.input);
              recover((RecognitionException)mismatchedSetException);
              throw mismatchedSetException;
          } 
          break;
        } 
        match(46);
        while (true) {
          MismatchedSetException mismatchedSetException;
          b2 = 2;
          int j = this.input.LA(1);
          if (j >= 48 && j <= 57)
            b2 = 1; 
          switch (b2) {
            case 1:
              if (this.input.LA(1) >= 48 && this.input.LA(1) <= 57) {
                this.input.consume();
                continue;
              } 
              mismatchedSetException = new MismatchedSetException(null, (IntStream)this.input);
              recover((RecognitionException)mismatchedSetException);
              throw mismatchedSetException;
          } 
          break;
        } 
        break;
    } 
    this.state.type = b1;
    this.state.channel = bool;
  }
  
  public final void mUNITS() throws RecognitionException {
    byte b1 = 14;
    boolean bool = false;
    for (byte b2 = 0;; b2++) {
      MismatchedSetException mismatchedSetException;
      EarlyExitException earlyExitException;
      byte b = 2;
      int i = this.input.LA(1);
      if ((i >= 0 && i <= 37) || (i >= 39 && i <= 44) || i == 47 || (i >= 58 && i <= 123) || (i >= 125 && i <= 65535))
        b = 1; 
      switch (b) {
        case 1:
          if ((this.input.LA(1) >= 0 && this.input.LA(1) <= 37) || (this.input.LA(1) >= 39 && this.input.LA(1) <= 44) || this.input.LA(1) == 47 || (this.input.LA(1) >= 58 && this.input.LA(1) <= 123) || (this.input.LA(1) >= 125 && this.input.LA(1) <= 65535)) {
            this.input.consume();
            break;
          } 
          mismatchedSetException = new MismatchedSetException(null, (IntStream)this.input);
          recover((RecognitionException)mismatchedSetException);
          throw mismatchedSetException;
        default:
          if (b2 >= 1) {
            this.state.type = b1;
            this.state.channel = bool;
            return;
          } 
          earlyExitException = new EarlyExitException(5, (IntStream)this.input);
          throw earlyExitException;
      } 
    } 
  }
  
  public void mTokens() throws RecognitionException {
    byte b = 12;
    int i = this.input.LA(1);
    if (i == 38) {
      b = 1;
    } else if (i == 45) {
      b = 2;
    } else if (i == 60) {
      int j = this.input.LA(2);
      if (j == 61) {
        int k = this.input.LA(3);
        if ((k >= 0 && k <= 37) || (k >= 39 && k <= 44) || k == 47 || (k >= 58 && k <= 123) || (k >= 125 && k <= 65535)) {
          b = 12;
        } else {
          b = 4;
        } 
      } else if ((j >= 0 && j <= 37) || (j >= 39 && j <= 44) || j == 47 || (j >= 58 && j <= 60) || (j >= 62 && j <= 123) || (j >= 125 && j <= 65535)) {
        b = 12;
      } else {
        b = 3;
      } 
    } else if (i == 62) {
      int j = this.input.LA(2);
      if (j == 61) {
        int k = this.input.LA(3);
        if ((k >= 0 && k <= 37) || (k >= 39 && k <= 44) || k == 47 || (k >= 58 && k <= 123) || (k >= 125 && k <= 65535)) {
          b = 12;
        } else {
          b = 6;
        } 
      } else if ((j >= 0 && j <= 37) || (j >= 39 && j <= 44) || j == 47 || (j >= 58 && j <= 60) || (j >= 62 && j <= 123) || (j >= 125 && j <= 65535)) {
        b = 12;
      } else {
        b = 5;
      } 
    } else if (i == 78) {
      int j = this.input.LA(2);
      if (j == 85) {
        int k = this.input.LA(3);
        if (k == 76) {
          int m = this.input.LA(4);
          if (m == 76) {
            int n = this.input.LA(5);
            if ((n >= 0 && n <= 37) || (n >= 39 && n <= 44) || n == 47 || (n >= 58 && n <= 123) || (n >= 125 && n <= 65535)) {
              b = 12;
            } else {
              b = 7;
            } 
          } else {
            b = 12;
          } 
        } else {
          b = 12;
        } 
      } else {
        b = 12;
      } 
    } else if (i == 124) {
      b = 8;
    } else if (i == 126) {
      int j = this.input.LA(2);
      if (j == 78) {
        int k = this.input.LA(3);
        if (k == 85) {
          int m = this.input.LA(4);
          if (m == 76) {
            int n = this.input.LA(5);
            if (n == 76) {
              int i1 = this.input.LA(6);
              if ((i1 >= 0 && i1 <= 37) || (i1 >= 39 && i1 <= 44) || i1 == 47 || (i1 >= 58 && i1 <= 123) || (i1 >= 125 && i1 <= 65535)) {
                b = 12;
              } else {
                b = 10;
              } 
            } else {
              b = 12;
            } 
          } else {
            b = 12;
          } 
        } else {
          b = 12;
        } 
      } else if ((j >= 0 && j <= 37) || (j >= 39 && j <= 44) || j == 47 || (j >= 58 && j <= 77) || (j >= 79 && j <= 123) || (j >= 125 && j <= 65535)) {
        b = 12;
      } else {
        b = 9;
      } 
    } else if (i == 46 || (i >= 48 && i <= 57)) {
      b = 11;
    } else if ((i >= 0 && i <= 37) || (i >= 39 && i <= 44) || i == 47 || (i >= 58 && i <= 59) || i == 61 || (i >= 63 && i <= 77) || (i >= 79 && i <= 123) || i == 125 || (i >= 127 && i <= 65535)) {
      b = 12;
    } else {
      NoViableAltException noViableAltException = new NoViableAltException("", 6, 0, (IntStream)this.input);
      throw noViableAltException;
    } 
    switch (b) {
      case 1:
        mT__15();
        break;
      case 2:
        mT__16();
        break;
      case 3:
        mT__17();
        break;
      case 4:
        mT__18();
        break;
      case 5:
        mT__19();
        break;
      case 6:
        mT__20();
        break;
      case 7:
        mT__21();
        break;
      case 8:
        mT__22();
        break;
      case 9:
        mT__23();
        break;
      case 10:
        mT__24();
        break;
      case 11:
        mDOUBLE();
        break;
      case 12:
        mUNITS();
        break;
    } 
  }
  
  static {
    int i = DFA4_transitionS.length;
    DFA4_transition = new short[i][];
    for (byte b = 0; b < i; b++)
      DFA4_transition[b] = DFA.unpackEncodedString(DFA4_transitionS[b]); 
  }
  
  protected class DFA4 extends DFA {
    public DFA4(BaseRecognizer param1BaseRecognizer) {
      this.recognizer = param1BaseRecognizer;
      this.decisionNumber = 4;
      this.eot = DMSDoubleExprLexer.DFA4_eot;
      this.eof = DMSDoubleExprLexer.DFA4_eof;
      this.min = DMSDoubleExprLexer.DFA4_min;
      this.max = DMSDoubleExprLexer.DFA4_max;
      this.accept = DMSDoubleExprLexer.DFA4_accept;
      this.special = DMSDoubleExprLexer.DFA4_special;
      this.transition = DMSDoubleExprLexer.DFA4_transition;
    }
    
    public String getDescription() {
      return "101:1: DOUBLE : ( ( '0' .. '9' )+ | ( '0' .. '9' )* ( '.' ) ( '0' .. '9' )* );";
    }
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\DMSDoubleExprLexer.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */