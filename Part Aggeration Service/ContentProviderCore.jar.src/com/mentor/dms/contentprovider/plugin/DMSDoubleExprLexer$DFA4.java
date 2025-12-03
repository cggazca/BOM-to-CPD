package com.mentor.dms.contentprovider.plugin;

import org.antlr.runtime.BaseRecognizer;
import org.antlr.runtime.DFA;

public class DFA4 extends DFA {
  public DFA4(BaseRecognizer paramBaseRecognizer) {
    this.recognizer = paramBaseRecognizer;
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


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\DMSDoubleExprLexer$DFA4.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */