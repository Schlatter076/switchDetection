package loyer.client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.JButton;

public class HelpPanel {

  private JFrame frame;

  public static void getHelpPanel() {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          HelpPanel window = new HelpPanel();
          window.frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }
  public HelpPanel() {
    initialize();
  }
  private void initialize() {
    frame = new JFrame();
    frame.setTitle("联系电话：15850203026");
    frame.getContentPane().setBackground(new Color(245, 222, 179));
    frame.setResizable(false);  //窗口大小不可改
    //frame.setUndecorated(true); //去掉边框
    //替换窗口的咖啡图标
    Image img = Toolkit.getDefaultToolkit().getImage(frame.getClass().getResource("/Kyokuto.png"));
    frame.setIconImage(img);
    ((JPanel) frame.getContentPane()).setOpaque(false);  //设置窗口透明
    frame.setBounds(500, 200, 348, 205);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.getContentPane().setLayout(null);
    
    JLabel pks = new JLabel("*版权所有：广州番禺旭东阪田电子有限公司(PKS)");
    pks.setHorizontalAlignment(SwingConstants.LEFT);
    pks.setForeground(Color.GRAY);
    pks.setFont(new Font("楷体", Font.PLAIN, 12));
    pks.setBounds(12, 116, 306, 22);
    frame.getContentPane().add(pks);
    
    JTextArea txtrNj = new JTextArea();
    txtrNj.setOpaque(false);
    txtrNj.setEditable(false);
    txtrNj.setFont(new Font("楷体", Font.PLAIN, 16));
    txtrNj.setText("如遇设备出现以下情况，请及时联系生技解决！\r\n1.设备测试不稳定\r\n2.软件启动不了\r\n3.其他问题\r\n给您造成的不便，深表抱歉！");
    txtrNj.setForeground(Color.BLACK);
    txtrNj.setBackground(new Color(245, 222, 179));
    txtrNj.setBounds(10, 10, 328, 103);
    frame.getContentPane().add(txtrNj);
    
    JButton button = new JButton("确定");
    button.setFont(new Font("楷体", Font.PLAIN, 16));
    button.setOpaque(false);
    button.setBackground(new Color(220, 220, 220));
    button.addActionListener(event -> frame.dispose());
    button.setBounds(110, 144, 93, 23);
    frame.getContentPane().add(button);
  }
}
