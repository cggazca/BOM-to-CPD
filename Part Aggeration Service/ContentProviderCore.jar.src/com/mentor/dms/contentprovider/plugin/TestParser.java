package com.mentor.dms.contentprovider.plugin;

import com.mentor.dms.contentprovider.ContentProviderException;
import java.io.ByteArrayInputStream;
import java.util.List;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.TokenSource;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTree;

public abstract class TestParser {
  public static void main(String[] paramArrayOfString) {
    String str = "*8055*";
    DMSStringExprLexer dMSStringExprLexer = new DMSStringExprLexer();
    CommonTokenStream commonTokenStream1 = new CommonTokenStream((TokenSource)dMSStringExprLexer);
    DMSStringExprParser dMSStringExprParser = new DMSStringExprParser((TokenStream)commonTokenStream1);
    DMSDoubleExprLexer dMSDoubleExprLexer = new DMSDoubleExprLexer();
    CommonTokenStream commonTokenStream2 = new CommonTokenStream((TokenSource)dMSDoubleExprLexer);
    DMSDoubleExprParser dMSDoubleExprParser = new DMSDoubleExprParser((TokenStream)commonTokenStream2);
    try {
      ANTLRInputStream aNTLRInputStream = new ANTLRInputStream(new ByteArrayInputStream(str.getBytes()));
      dMSStringExprLexer.setCharStream((CharStream)aNTLRInputStream);
      commonTokenStream1.setTokenSource((TokenSource)dMSStringExprLexer);
      dMSStringExprParser.setTokenStream((TokenStream)commonTokenStream1);
      DMSStringExprParser.prog_return prog_return = dMSStringExprParser.prog();
      if (dMSStringExprParser.getNumberOfSyntaxErrors() > 0)
        throw new ContentProviderException("Error in query syntax."); 
      CommonTree commonTree = prog_return.getTree();
      printTree(commonTree, "");
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  static void printTree(CommonTree paramCommonTree, String paramString) {
    System.out.println(paramString + paramString);
    List list = paramCommonTree.getChildren();
    if (list != null)
      for (CommonTree commonTree : list)
        printTree(commonTree, paramString + "  ");  
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\plugin\TestParser.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */