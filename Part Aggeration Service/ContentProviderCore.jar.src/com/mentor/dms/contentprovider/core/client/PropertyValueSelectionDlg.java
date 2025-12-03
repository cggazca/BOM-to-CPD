package com.mentor.dms.contentprovider.core.client;

import com.mentor.datafusion.oi.OIHelper;
import com.mentor.datafusion.utils.logger.MGLogger;
import com.mentor.dms.contentprovider.core.AbstractContentProvider;
import com.mentor.dms.contentprovider.core.ContentProviderGlobal;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
  
  private Map<String, String> valueMap = new LinkedHashMap<>();
  
  public PropertyValueSelectionDlg(Frame paramFrame, AbstractContentProvider paramAbstractContentProvider, String paramString, List<Map.Entry<String, String>> paramList) {
    super(paramFrame, paramString + " - Select " + paramString + " Valid Value(s)");
    for (byte b = 0; paramList.size() > b; b++)
      this.valueMap.put((String)((Map.Entry)paramList.get(b)).getKey() + " (" + (String)((Map.Entry)paramList.get(b)).getKey() + ")", (String)((Map.Entry)paramList.get(b)).getKey()); 
    setModal(true);
    Dimension dimension = new Dimension(500, 300);
    setMinimumSize(dimension);
    setSize(dimension);
    setLocationRelativeTo(paramFrame);
    if (paramAbstractContentProvider.getIcon() == null) {
      setIconImage(ContentProviderGlobal.getAppIconImage());
    } else {
      setIconImage(iconToImage(paramAbstractContentProvider.getIcon()));
    } 
    final FilteredJList list = new FilteredJList(this.valueMap);
    if (this.valueMap.size() > 0)
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
    StringBuilder stringBuilder = new StringBuilder();
    boolean bool = true;
    for (String str : paramFilteredJList.getSelectedValuesList()) {
      if (bool) {
        bool = false;
      } else {
        stringBuilder.append("|");
      } 
      stringBuilder.append(OIHelper.escapeQueryRestriction(this.valueMap.get(str)));
    } 
    this.restriction = stringBuilder.toString();
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


/* Location:              C:\Users\z004ut2y\OneDrive - Siemens AG\Documents\01_Projects\Customers\Var Industries\varindustries_edm-eles-sample-dataset_2025-09-18_1349 (1)\Part Aggeration Service\ContentProviderCore.jar!\com\mentor\dms\contentprovider\core\client\PropertyValueSelectionDlg.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */