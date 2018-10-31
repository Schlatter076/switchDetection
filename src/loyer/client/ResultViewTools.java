package loyer.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import loyer.dataBase.NGRecordData;
import loyer.dataBase.NGRecordTools;
import loyer.dataBase.RecordData;
import loyer.dataBase.RecordTools;

public class ResultViewTools {

  private JFrame frame;
  private JScrollPane scrollPane;
  private JButton viewAllButt;
  private JButton viewCurrentButt;
  private JComboBox<String> dateBox;
  private JTable table;
  private JButton backHomeButt;

  public static void getResultView() {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          ResultViewTools window = new ResultViewTools();
          window.frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }
  /*
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          ResultViewTools window = new ResultViewTools();
          window.frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }//*/
  
  /**
   * Create the application.
   */
  public ResultViewTools() {
    initialize();
  }

  /**
   * Initialize the contents of the frame.
   */
  private void initialize() {
    frame = new JFrame();
    frame.getContentPane().setFont(new Font("宋体", Font.PLAIN, 14));
    frame.setResizable(false);  //窗口大小不可改
    // frame.setUndecorated(true); //窗口无装饰
    // frame.setOpacity(0.5f); //半透明
    // 替换窗口的咖啡图标
    frame.setIconImage(Toolkit.getDefaultToolkit().getImage(frame.getClass().getResource("/Kyokuto.png")));
    frame.setTitle("查看测试结果");
    frame.getContentPane().setBackground(new Color(245, 245, 245));
    frame.getContentPane().setLayout(null);

    scrollPane = new JScrollPane();
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    scrollPane.setBounds(0, 0, 725, 281);
    frame.getContentPane().add(scrollPane);
    
    table = getRecordTable();
    scrollPane.setViewportView(table);
    
    viewAllButt = new JButton("查看所有");
    viewAllButt.addActionListener(e -> {
      ArrayList<NGRecordData> list = NGRecordTools.getAllByDB();
      table = createNGTable(list);
      scrollPane.setViewportView(table);
    });
    viewAllButt.setBackground(new Color(245, 245, 245));
    viewAllButt.setFont(new Font("宋体", Font.PLAIN, 14));
    viewAllButt.setBounds(344, 303, 93, 23);
    frame.getContentPane().add(viewAllButt);

    viewCurrentButt = new JButton("查看当前日期数据");
    viewCurrentButt.addActionListener(e -> {
      String date = (String) dateBox.getSelectedItem();
      ArrayList<NGRecordData> list = NGRecordTools.getAllByDate(date);
      if(list.isEmpty()) {
        JOptionPane.showMessageDialog(scrollPane, "当前日期无不良记录！");
      }
      table = createNGTable(list);
      scrollPane.setViewportView(table);
    });
    viewCurrentButt.setFont(new Font("宋体", Font.PLAIN, 14));
    viewCurrentButt.setBackground(new Color(245, 245, 245));
    viewCurrentButt.setBounds(172, 303, 152, 23);
    frame.getContentPane().add(viewCurrentButt);

    dateBox = new JComboBox<String>();
    for(RecordData rd : RecordTools.getAllByDB()) {
      dateBox.addItem(rd.getDate());
    }
    dateBox.setBackground(new Color(245, 245, 245));
    dateBox.setFont(new Font("宋体", Font.PLAIN, 14));
    dateBox.setBounds(10, 304, 152, 21);
    frame.getContentPane().add(dateBox);

    JLabel label = new JLabel("*版权所有：广州番禺旭东阪田电子有限公司(PKS)");
    label.setHorizontalAlignment(SwingConstants.LEFT);
    label.setForeground(Color.GRAY);
    label.setFont(new Font("楷体", Font.PLAIN, 12));
    label.setBounds(10, 403, 705, 15);
    frame.getContentPane().add(label);
    
    backHomeButt = new JButton("回到首页");
    backHomeButt.addActionListener(e -> {
      table = getRecordTable();
      scrollPane.setViewportView(table);
    });
    backHomeButt.setFont(new Font("宋体", Font.PLAIN, 14));
    backHomeButt.setBackground(new Color(245, 245, 245));
    backHomeButt.setBounds(454, 303, 93, 23);
    frame.getContentPane().add(backHomeButt);
    frame.setBounds(100, 100, 741, 468);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

  /**
   * 创建不良测试表
   * @param list
   * @return
   */
  public JTable createNGTable(ArrayList<NGRecordData> list) {
    Vector<Object> rowDatas = new Vector<>(), colName = new Vector<>();
    colName.add("*");
    colName.add("序号");
    colName.add("步骤");
    colName.add("测试项目");
    colName.add("上限值");
    colName.add("下限值");
    colName.add("测试值");
    colName.add("单位");
    colName.add("日期");
    colName.add("备注");
    for (NGRecordData data : list) {
      Vector<String> vt = new Vector<>();
      vt.add("");
      vt.add(data.getSerialNum() + "");
      vt.add(data.getSteps());
      vt.add(data.getTestItems());
      vt.add(data.getUpperLimit());
      vt.add(data.getLowerLimit());
      vt.add(data.getTestValue());
      vt.add(data.getUnit());
      vt.add(data.getDate());
      vt.add(data.getMark());

      rowDatas.add(vt);
    }
    JTable table = new JTable(rowDatas, colName);
    table.setSelectionBackground(Color.WHITE);
    table.setBackground(new Color(245, 245, 245));
    JTableHeader jTableHeader = table.getTableHeader(); // 获取表头
    // 设置表头名称字体样式
    jTableHeader.setFont(new Font("宋体", Font.PLAIN, 14));
    // 设置表头背景颜色
    jTableHeader.setBackground(new Color(245, 245, 245));
    // 设置表头名称字体颜色
    jTableHeader.setForeground(Color.BLACK);
    // 表头不可拖动
    jTableHeader.setReorderingAllowed(false);
    // 列大小不可改变
    jTableHeader.setResizingAllowed(false);

    TableColumn colNull = table.getColumnModel().getColumn(0);
    colNull.setPreferredWidth(15); // 设置第一列的列宽
    colNull = table.getColumnModel().getColumn(3);
    colNull.setPreferredWidth(150);  //设置第三列列宽
    colNull = table.getColumnModel().getColumn(8);
    colNull.setPreferredWidth(120);  //设置第八列列宽

    DefaultTableCellRenderer r = new DefaultTableCellRenderer(); // 设置
    r.setHorizontalAlignment(JLabel.CENTER); // 单元格内容
    table.setDefaultRenderer(Object.class, r); // 居中显示
    table.setRowHeight(25); // 设置行高
    table.setForeground(Color.BLACK); // 设置文字颜色
    table.setFont(new Font("宋体", Font.PLAIN, 14));
    return table;
  }
  /**
   * 获取测试数据记录表
   * 
   * @return
   */
  public JTable getNGRecordTable() {
    Vector<Object> rowDatas = new Vector<>(), colName = new Vector<>();
    colName.add("*");
    colName.add("序号");
    colName.add("步骤");
    colName.add("测试项目");
    colName.add("上限值");
    colName.add("下限值");
    colName.add("测试值");
    colName.add("单位");
    colName.add("日期");
    colName.add("备注");
    ArrayList<NGRecordData> list = NGRecordTools.getAllByDB();
    for (NGRecordData data : list) {
      Vector<String> vt = new Vector<>();
      vt.add("");
      vt.add(data.getSerialNum() + "");
      vt.add(data.getSteps());
      vt.add(data.getTestItems());
      vt.add(data.getUpperLimit());
      vt.add(data.getLowerLimit());
      vt.add(data.getTestValue());
      vt.add(data.getUnit());
      vt.add(data.getDate());
      vt.add(data.getMark());

      rowDatas.add(vt);
    }
    JTable table = new JTable(rowDatas, colName);
    table.setSelectionBackground(Color.WHITE);
    table.setBackground(new Color(245, 245, 245));
    JTableHeader jTableHeader = table.getTableHeader(); // 获取表头
    // 设置表头名称字体样式
    jTableHeader.setFont(new Font("宋体", Font.PLAIN, 14));
    // 设置表头背景颜色
    jTableHeader.setBackground(new Color(245, 245, 245));
    // 设置表头名称字体颜色
    jTableHeader.setForeground(Color.BLACK);
    // 表头不可拖动
    jTableHeader.setReorderingAllowed(false);
    // 列大小不可改变
    jTableHeader.setResizingAllowed(false);

    TableColumn colNull = table.getColumnModel().getColumn(0);
    colNull.setPreferredWidth(15); // 设置第一列的列宽
    colNull = table.getColumnModel().getColumn(3);
    colNull.setPreferredWidth(150);  //设置第三列列宽
    colNull = table.getColumnModel().getColumn(8);
    colNull.setPreferredWidth(120);  //设置第八列列宽

    DefaultTableCellRenderer r = new DefaultTableCellRenderer(); // 设置
    r.setHorizontalAlignment(JLabel.CENTER); // 单元格内容
    table.setDefaultRenderer(Object.class, r); // 居中显示
    table.setRowHeight(25); // 设置行高
    table.setForeground(Color.BLACK); // 设置文字颜色
    table.setFont(new Font("宋体", Font.PLAIN, 14));
    return table;
  }
  /**
   * 获取测试数据记录表
   * 
   * @return
   */
  public JTable getRecordTable() {
    Vector<Object> rowDatas = new Vector<>(), colName = new Vector<>();
    colName.add("*");
    colName.add("产品名");
    colName.add("测试总数");
    colName.add("测试OK");
    colName.add("测试NG");
    colName.add("平均测试时间");
    colName.add("日期");
    colName.add("修改");
    ArrayList<RecordData> list = RecordTools.getAllByDB();
    for (RecordData data : list) {
      Vector<String> vt = new Vector<>();
      vt.add("");
      vt.add(data.getName());
      vt.add(data.getSum() + "");
      vt.add(data.getOk() + "");
      vt.add(data.getNg() + "");
      vt.add(data.getTimes());
      vt.add(data.getDate());
      vt.add("修改");

      rowDatas.add(vt);
    }
    JTable table = new JTable(rowDatas, colName);
    table.getColumn("修改").setCellRenderer(new ButtonRenderer()); // 这两句
    table.getColumn("修改").setCellEditor(new ButtonEditor(new JCheckBox())); // 在table中添加JButton
    table.setSelectionBackground(Color.WHITE);
    table.setBackground(new Color(245, 245, 245));
    JTableHeader jTableHeader = table.getTableHeader(); // 获取表头
    // 设置表头名称字体样式
    jTableHeader.setFont(new Font("宋体", Font.PLAIN, 14));
    // 设置表头背景颜色
    jTableHeader.setBackground(new Color(245, 245, 245));
    // 设置表头名称字体颜色
    jTableHeader.setForeground(Color.BLACK);
    // 表头不可拖动
    jTableHeader.setReorderingAllowed(false);
    // 列大小不可改变
    jTableHeader.setResizingAllowed(false);

    TableColumn colNull = table.getColumnModel().getColumn(0);
    colNull.setPreferredWidth(15); // 设置第一列的列宽

    DefaultTableCellRenderer r = new DefaultTableCellRenderer(); // 设置
    r.setHorizontalAlignment(JLabel.CENTER); // 单元格内容
    table.setDefaultRenderer(Object.class, r); // 居中显示
    table.setRowHeight(25); // 设置行高
    table.setForeground(Color.BLACK); // 设置文字颜色
    table.setFont(new Font("宋体", Font.PLAIN, 14));
    return table;
  }
  
  /**
   * 修改数据库数据
   * @param row
   */
  public boolean modifyResult(int row) {
    String[] str = new String[6];
    try {
      for(int i = 0; i < 6; i++) {
        str[i] = table.getValueAt(row, i + 1).toString();
      }
      //如果数据库中没有该数据
      if(!addRowDatas(getTableValueAt(row))) {
        RecordTools.insertData(str);
      }
      else {
        RecordTools.updateData(str);
      }
      return true;
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, e.getMessage());
      return false;
    }
    
  }
  public boolean addRowDatas(String recordDate) {
    
    RecordData rd = RecordTools.getDataByDate(recordDate);
    if(rd != null) 
      return true;
    return false;
  }
  public String getTableValueAt(int row) {
    return table.getValueAt(row, 6).toString();
  }
  /////////////////////////////////////////////////////////////////////////////
  class ButtonRenderer extends JButton implements TableCellRenderer {

    private static final long serialVersionUID = 1L;

    public ButtonRenderer() {
      setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
        int row, int column) {

      super.setHorizontalAlignment(JLabel.CENTER); // 该列居中显示
      if (isSelected) {
        setForeground(Color.BLACK);
        setBackground(Color.WHITE);
      } else {
        setForeground(table.getForeground());
        setBackground(table.getBackground());
      }
      setText((value == null) ? "" : value.toString());
      return this;
    }
  }

  /////////////////////////////////////////////////////////////////////////////
  class ButtonEditor extends DefaultCellEditor {

    private static final long serialVersionUID = 1L;
    protected JButton button;
    private String label;
    private boolean isPushed;

    public ButtonEditor(JCheckBox checkBox) {
      super(checkBox);
      button = new JButton();
      button.setOpaque(true);
      button.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          fireEditingStopped();
        }
      });
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      if (isSelected) {
        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);
      } else {
        button.setForeground(table.getForeground());
        button.setBackground(table.getBackground());
      }
      label = (value == null) ? "" : value.toString();
      button.setText(label);
      isPushed = true;
      return button;
    }

    // 点击按键时触发
    public Object getCellEditorValue() {
      if (isPushed) {
        if (modifyResult(table.getSelectedRow())) {
          JOptionPane.showMessageDialog(button, label + "成功");
        } else
          JOptionPane.showMessageDialog(button, label + "失败");
        // System.out.println(table.getSelectedRow());
        // System.out.println(table.getSelectedColumn());
        // JOptionPane.showMessageDialog(button, label + "成功");
      }
      isPushed = false;
      return new String(label);
    }

    public boolean stopCellEditing() {
      isPushed = false;
      return super.stopCellEditing();
    }

    protected void fireEditingStopped() {
      super.fireEditingStopped();
    }
  }
}
