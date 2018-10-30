package loyer.client;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * 自定义表格渲染类
 * @author hw076
 *
 */
public class MyTableCellRenderer extends DefaultTableCellRenderer {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
      int row, int column) {

    super.setHorizontalAlignment(JLabel.CENTER); // 该列居中显示
    Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

    if ("PASS".equals(value + "")) {
      comp.setBackground(new Color(0, 204, 51));
    } else if ("NG".equals(value + "")) {
      comp.setBackground(Color.RED);
    } else {
      comp.setBackground(new Color(220, 220, 220));// 这一行保证其他单元格颜色不变
    }
    return comp;
  }
}
