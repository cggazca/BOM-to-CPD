package com.mentor.dms.contentprovider.client;

import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.AbstractContentProvider;
import com.mentor.dms.contentprovider.ContentProviderGlobal;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

public class PropertyValueSelectionDlg extends JDialog {
  private static MGLogger log = MGLogger.getLogger(PropertyValueSelectionDlg.class);
  
  private boolean bCancelled = false;
  
  private String restriction = "";
  
  public PropertyValueSelectionDlg(Frame paramFrame, AbstractContentProvider paramAbstractContentProvider, String paramString, Set<String> paramSet) {
    super(paramFrame, paramString + " - Select " + paramString + " Valid Value(s)");
    setModal(true);
    Point point = MouseInfo.getPointerInfo().getLocation();
    point.y -= 150;
    if (point.y < 0)
      point.y = 0; 
    setLocation(point);
    setSize(500, 300);
    if (paramAbstractContentProvider.getIcon() == null) {
      setIconImage(ContentProviderGlobal.getAppIconImage());
    } else {
      setIconImage(iconToImage(paramAbstractContentProvider.getIcon()));
    } 
    final FilteredJList list = new FilteredJList(paramSet.toArray());
    if (paramSet.size() > 0)
      filteredJList.setSelectedIndex(0); 
    JPanel jPanel1 = new JPanel(new BorderLayout());
    jPanel1.setBorder(new EmptyBorder(5, 5, 0, 5));
    jPanel1.add(new JLabel("Filter: "), "North");
    jPanel1.add(filteredJList.getFilterField(), "Center");
    JScrollPane jScrollPane = new JScrollPane(filteredJList, 20, 30);
    jScrollPane.setBorder(new CompoundBorder(new EmptyBorder(0, 5, 0, 5), BorderFactory.createEtchedBorder(1)));
    JPanel jPanel2 = new JPanel();
    jPanel2.setBorder(new EmptyBorder(5, 5, 5, 5));
    JButton jButton1 = new JButton("Set");
    JButton jButton2 = new JButton("Cancel");
    getRootPane().setDefaultButton(jButton1);
    MouseAdapter mouseAdapter = new MouseAdapter() {
        public void mouseClicked(MouseEvent param1MouseEvent) {
          JList jList = (JList)param1MouseEvent.getSource();
          if (param1MouseEvent.getClickCount() == 2) {
            int i = jList.locationToIndex(param1MouseEvent.getPoint());
            if (i >= 0) {
              PropertyValueSelectionDlg.this.processSelection(list);
              PropertyValueSelectionDlg.this.dispose();
            } 
          } 
        }
      };
    filteredJList.addMouseListener(mouseAdapter);
    jButton1.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent param1ActionEvent) {
            PropertyValueSelectionDlg.this.processSelection(list);
            PropertyValueSelectionDlg.this.dispose();
          }
        });
    jButton2.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent param1ActionEvent) {
            PropertyValueSelectionDlg.this.bCancelled = true;
            PropertyValueSelectionDlg.this.dispose();
          }
        });
    jPanel2.add(jButton1);
    jPanel2.add(jButton2);
    add(jPanel1, "North");
    add(jScrollPane, "Center");
    add(jPanel2, "South");
    setVisible(true);
  }
  
  private void processSelection(FilteredJList paramFilteredJList) {
    // Byte code:
    //   0: new java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore_2
    //   8: iconst_1
    //   9: istore_3
    //   10: aload_1
    //   11: invokevirtual getSelectedValuesList : ()Ljava/util/List;
    //   14: invokeinterface iterator : ()Ljava/util/Iterator;
    //   19: astore #4
    //   21: aload #4
    //   23: invokeinterface hasNext : ()Z
    //   28: ifeq -> 72
    //   31: aload #4
    //   33: invokeinterface next : ()Ljava/lang/Object;
    //   38: astore #5
    //   40: iload_3
    //   41: ifeq -> 49
    //   44: iconst_0
    //   45: istore_3
    //   46: goto -> 56
    //   49: aload_2
    //   50: ldc '|'
    //   52: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   55: pop
    //   56: aload_2
    //   57: aload #5
    //   59: checkcast java/lang/String
    //   62: invokestatic escapeQueryRestriction : (Ljava/lang/String;)Ljava/lang/String;
    //   65: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   68: pop
    //   69: goto -> 21
    //   72: aload_0
    //   73: aload_2
    //   74: invokevirtual toString : ()Ljava/lang/String;
    //   77: putfield restriction : Ljava/lang/String;
    //   80: return
  }
  
  public boolean isCancelled() {
    return this.bCancelled;
  }
  
  public String getRestriction() {
    return this.restriction;
  }
  
  private Image iconToImage(Icon paramIcon) {
    if (paramIcon instanceof ImageIcon)
      return ((ImageIcon)paramIcon).getImage(); 
    int i = paramIcon.getIconWidth();
    int j = paramIcon.getIconHeight();
    GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
    GraphicsConfiguration graphicsConfiguration = graphicsDevice.getDefaultConfiguration();
    BufferedImage bufferedImage = graphicsConfiguration.createCompatibleImage(i, j);
    Graphics2D graphics2D = bufferedImage.createGraphics();
    paramIcon.paintIcon(null, graphics2D, 0, 0);
    graphics2D.dispose();
    return bufferedImage;
  }
}


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\client\PropertyValueSelectionDlg.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */