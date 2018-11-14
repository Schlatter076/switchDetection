package loyer.client;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import loyer.exception.InputStreamCloseFail;
import loyer.exception.NoSuchPort;
import loyer.exception.NotASerialPort;
import loyer.exception.OutputStreamCloseFail;
import loyer.exception.PortInUse;
import loyer.exception.ReadDataFromSerialFail;
import loyer.exception.SendToPortFail;
import loyer.exception.SerialPortParamFail;
import loyer.exception.TooManyListeners;
import loyer.serial.SerialPortTools;
import javax.swing.JTextField;

public class UsartTools {

  private JFrame frame;
  private JTextArea rxBufferField;
  private JScrollPane txScrollPane;
  private JScrollPane rxScrollPane;
  private JTextArea txBufferField;
  private JLabel rxBuffer;
  private JButton clrRxBuffer;
  private JRadioButton rxHexMode;
  private JRadioButton rxTextMode;
  private JRadioButton txHexMode;
  private JRadioButton txTextMode;
  private JLabel txBuffer;
  private JButton clrTxBuffer;
  private JLabel PINA;
  private JLabel PINB;
  private JLabel PINC;
  private JLabel PIND;
  private JLabel PINE;
  private JLabel PINF;
  private JLabel PING;
  private JLabel comLabel;
  private JLabel label;
  private JButton ioControl;
  private JButton allSetBit;
  private JButton allClrBit;
  private JButton clrButt;
  private JLabel transLabel;
  private JLabel recieveLabel;
  private JComboBox<String> portList;
  private JComboBox<Integer> baudList;
  private JLabel baudLabel;
  private JComboBox<String> parityList;
  private JLabel parityLabel;
  private JComboBox<Integer> stopList;
  private JLabel stopLabel;
  private JButton openPort;
  private JButton transData;
  private byte[] datas = new byte[] { (byte) 0xf3, (byte) 0xf4, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x0a };
  private JToggleButton[] PA = new JToggleButton[8];
  private JToggleButton[] PB = new JToggleButton[8];
  private JToggleButton[] PC = new JToggleButton[8];
  private JToggleButton[] PD = new JToggleButton[4];
  private JToggleButton[] PE = new JToggleButton[8];
  private JToggleButton[] PF = new JToggleButton[8];
  private JToggleButton[] PG = new JToggleButton[8];
  private SerialPort serialPort;
  private final byte FIRST_TEXT = (byte) 0xf3;
  private final byte SECOND_TEXT = (byte) 0xf4;
  private final byte END_TEXT = 0x0a;
  private final int BUFFER_SIZE = 11;
  private byte[] bytes = new byte[BUFFER_SIZE];
  private byte data = 0;
  private int rxCounter = 0;
  private boolean hasData = false;
  private Timer timer1;
  private JTextField transField;
  private JTextField recieveField;
  private int transCount = 0;
  private int recieveCount = 0;
  

  /**
   * 串口调试工具
   */
  public static void getUsartTools() {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          UsartTools window = new UsartTools();
          window.frame.setVisible(true);
          window.addListener();
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
          UsartTools window = new UsartTools();
          window.frame.setVisible(true);
          window.addListener();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }
  //*/
  /**
   * Create the application.
   */
  public UsartTools() {
    initialize();
  }

  /**
   * Initialize the contents of the frame.
   */
  private void initialize() {

    frame = new JFrame();
    frame.setResizable(false); // 窗口大小不可改变
    frame.setTitle("串口调试助手");
    frame.getContentPane().setBackground(new Color(245, 245, 245));
    frame.getContentPane().setLayout(null);
    // 替换窗口的咖啡图标
    Image img = Toolkit.getDefaultToolkit().getImage(frame.getClass().getResource("/Kyokuto.png"));
    frame.setIconImage(img);

    rxScrollPane = new JScrollPane();
    rxScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    rxScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    rxScrollPane.setBounds(107, 30, 311, 100);
    frame.getContentPane().add(rxScrollPane);

    rxBufferField = new JTextArea();
    rxBufferField.setFont(new Font("宋体", Font.PLAIN, 14));
    rxScrollPane.setViewportView(rxBufferField);
    rxBufferField.setToolTipText("");

    txScrollPane = new JScrollPane();
    txScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    txScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    txScrollPane.setBounds(107, 151, 311, 100);
    frame.getContentPane().add(txScrollPane);

    txBufferField = new JTextArea();
    txBufferField.setForeground(new Color(0, 0, 0));
    txBufferField.setFont(new Font("宋体", Font.PLAIN, 14));
    txBufferField.setToolTipText("");
    txScrollPane.setViewportView(txBufferField);

    rxBuffer = new JLabel("接收缓冲区");
    rxBuffer.setFont(new Font("宋体", Font.PLAIN, 14));
    rxBuffer.setBounds(10, 35, 98, 15);
    frame.getContentPane().add(rxBuffer);

    clrRxBuffer = new JButton("清空接收区");
    clrRxBuffer.setForeground(new Color(0, 0, 0));
    clrRxBuffer.setFont(new Font("宋体", Font.PLAIN, 12));
    clrRxBuffer.setBackground(new Color(245, 245, 245));
    clrRxBuffer.setBounds(5, 111, 98, 23);
    frame.getContentPane().add(clrRxBuffer);

    rxHexMode = new JRadioButton("HEX模式");
    rxHexMode.setSelected(true);
    rxHexMode.setFont(new Font("宋体", Font.PLAIN, 12));
    rxHexMode.setBackground(new Color(245, 245, 245));
    rxHexMode.setBounds(6, 82, 95, 23);
    frame.getContentPane().add(rxHexMode);

    rxTextMode = new JRadioButton("文本模式");
    rxTextMode.setFont(new Font("宋体", Font.PLAIN, 12));
    rxTextMode.setBackground(new Color(245, 245, 245));
    rxTextMode.setBounds(6, 56, 95, 23);
    frame.getContentPane().add(rxTextMode);

    txBuffer = new JLabel("发送缓冲区");
    txBuffer.setFont(new Font("宋体", Font.PLAIN, 14));
    txBuffer.setBounds(10, 151, 98, 15);
    frame.getContentPane().add(txBuffer);

    clrTxBuffer = new JButton("清空发送区");
    clrTxBuffer.setForeground(Color.BLACK);
    clrTxBuffer.setFont(new Font("宋体", Font.PLAIN, 12));
    clrTxBuffer.setBackground(new Color(245, 245, 245));
    clrTxBuffer.setBounds(5, 227, 98, 23);
    frame.getContentPane().add(clrTxBuffer);

    txHexMode = new JRadioButton("HEX模式");
    txHexMode.setSelected(true);
    txHexMode.setFont(new Font("宋体", Font.PLAIN, 12));
    txHexMode.setBackground(new Color(245, 245, 245));
    txHexMode.setBounds(6, 198, 95, 23);
    frame.getContentPane().add(txHexMode);

    txTextMode = new JRadioButton("文本模式");
    txTextMode.setFont(new Font("宋体", Font.PLAIN, 12));
    txTextMode.setBackground(new Color(245, 245, 245));
    txTextMode.setBounds(6, 172, 95, 23);
    frame.getContentPane().add(txTextMode);

    PINA = new JLabel("PINA:");
    PINA.setForeground(new Color(128, 0, 128));
    PINA.setBackground(new Color(220, 220, 220));
    PINA.setFont(new Font("宋体", Font.BOLD, 14));
    PINA.setBounds(437, 35, 49, 15);
    frame.getContentPane().add(PINA);

    PA[7] = new JToggleButton("7");
    PA[7].setFont(new Font("宋体", Font.PLAIN, 12));
    PA[7].setBackground(new Color(245, 245, 245));
    PA[7].setBounds(484, 30, 50, 25);
    frame.getContentPane().add(PA[7]);

    PA[6] = new JToggleButton("6");
    PA[6].setFont(new Font("宋体", Font.PLAIN, 12));
    PA[6].setBackground(new Color(245, 245, 245));
    PA[6].setBounds(534, 30, 50, 25);
    frame.getContentPane().add(PA[6]);

    PA[5] = new JToggleButton("5");
    PA[5].setFont(new Font("宋体", Font.PLAIN, 12));
    PA[5].setBackground(new Color(245, 245, 245));
    PA[5].setBounds(584, 30, 50, 25);
    frame.getContentPane().add(PA[5]);

    PA[4] = new JToggleButton("4");
    PA[4].setFont(new Font("宋体", Font.PLAIN, 12));
    PA[4].setBackground(new Color(245, 245, 245));
    PA[4].setBounds(634, 30, 50, 25);
    frame.getContentPane().add(PA[4]);

    PA[3] = new JToggleButton("3");
    PA[3].setFont(new Font("宋体", Font.PLAIN, 12));
    PA[3].setBackground(new Color(245, 245, 245));
    PA[3].setBounds(684, 30, 50, 25);
    frame.getContentPane().add(PA[3]);

    PA[2] = new JToggleButton("2");
    PA[2].setFont(new Font("宋体", Font.PLAIN, 12));
    PA[2].setBackground(new Color(245, 245, 245));
    PA[2].setBounds(734, 30, 50, 25);
    frame.getContentPane().add(PA[2]);

    PA[1] = new JToggleButton("1");
    PA[1].setFont(new Font("宋体", Font.PLAIN, 12));
    PA[1].setBackground(new Color(245, 245, 245));
    PA[1].setBounds(784, 30, 50, 25);
    frame.getContentPane().add(PA[1]);

    PA[0] = new JToggleButton("0");
    PA[0].setFont(new Font("宋体", Font.PLAIN, 12));
    PA[0].setBackground(new Color(245, 245, 245));
    PA[0].setBounds(834, 30, 50, 25);
    frame.getContentPane().add(PA[0]);

    PINB = new JLabel("PINB:");
    PINB.setForeground(new Color(128, 0, 128));
    PINB.setFont(new Font("宋体", Font.BOLD, 14));
    PINB.setBackground(new Color(220, 220, 220));
    PINB.setBounds(437, 65, 49, 15);
    frame.getContentPane().add(PINB);

    PB[7] = new JToggleButton("7");
    PB[7].setFont(new Font("宋体", Font.PLAIN, 12));
    PB[7].setBackground(new Color(245, 245, 245));
    PB[7].setBounds(484, 60, 50, 25);
    frame.getContentPane().add(PB[7]);

    PB[6] = new JToggleButton("6");
    PB[6].setFont(new Font("宋体", Font.PLAIN, 12));
    PB[6].setBackground(new Color(245, 245, 245));
    PB[6].setBounds(534, 60, 50, 25);
    frame.getContentPane().add(PB[6]);

    PB[5] = new JToggleButton("5");
    PB[5].setFont(new Font("宋体", Font.PLAIN, 12));
    PB[5].setBackground(new Color(245, 245, 245));
    PB[5].setBounds(584, 60, 50, 25);
    frame.getContentPane().add(PB[5]);

    PB[4] = new JToggleButton("4");
    PB[4].setFont(new Font("宋体", Font.PLAIN, 12));
    PB[4].setBackground(new Color(245, 245, 245));
    PB[4].setBounds(634, 60, 50, 25);
    frame.getContentPane().add(PB[4]);

    PB[3] = new JToggleButton("3");
    PB[3].setFont(new Font("宋体", Font.PLAIN, 12));
    PB[3].setBackground(new Color(245, 245, 245));
    PB[3].setBounds(684, 60, 50, 25);
    frame.getContentPane().add(PB[3]);

    PB[2] = new JToggleButton("2");
    PB[2].setFont(new Font("宋体", Font.PLAIN, 12));
    PB[2].setBackground(new Color(245, 245, 245));
    PB[2].setBounds(734, 60, 50, 25);
    frame.getContentPane().add(PB[2]);

    PB[1] = new JToggleButton("1");
    PB[1].setFont(new Font("宋体", Font.PLAIN, 12));
    PB[1].setBackground(new Color(245, 245, 245));
    PB[1].setBounds(784, 60, 50, 25);
    frame.getContentPane().add(PB[1]);

    PB[0] = new JToggleButton("0");
    PB[0].setFont(new Font("宋体", Font.PLAIN, 12));
    PB[0].setBackground(new Color(245, 245, 245));
    PB[0].setBounds(834, 60, 50, 25);
    frame.getContentPane().add(PB[0]);

    PINC = new JLabel("PINC:");
    PINC.setForeground(new Color(128, 0, 128));
    PINC.setFont(new Font("宋体", Font.BOLD, 14));
    PINC.setBackground(new Color(220, 220, 220));
    PINC.setBounds(437, 97, 49, 15);
    frame.getContentPane().add(PINC);

    PC[7] = new JToggleButton("7");
    PC[7].setFont(new Font("宋体", Font.PLAIN, 12));
    PC[7].setBackground(new Color(245, 245, 245));
    PC[7].setBounds(484, 92, 50, 25);
    frame.getContentPane().add(PC[7]);

    PC[6] = new JToggleButton("6");
    PC[6].setFont(new Font("宋体", Font.PLAIN, 12));
    PC[6].setBackground(new Color(245, 245, 245));
    PC[6].setBounds(534, 92, 50, 25);
    frame.getContentPane().add(PC[6]);

    PC[5] = new JToggleButton("5");
    PC[5].setFont(new Font("宋体", Font.PLAIN, 12));
    PC[5].setBackground(new Color(245, 245, 245));
    PC[5].setBounds(584, 92, 50, 25);
    frame.getContentPane().add(PC[5]);

    PC[4] = new JToggleButton("4");
    PC[4].setFont(new Font("宋体", Font.PLAIN, 12));
    PC[4].setBackground(new Color(245, 245, 245));
    PC[4].setBounds(634, 92, 50, 25);
    frame.getContentPane().add(PC[4]);

    PC[3] = new JToggleButton("3");
    PC[3].setFont(new Font("宋体", Font.PLAIN, 12));
    PC[3].setBackground(new Color(245, 245, 245));
    PC[3].setBounds(684, 92, 50, 25);
    frame.getContentPane().add(PC[3]);

    PC[2] = new JToggleButton("2");
    PC[2].setFont(new Font("宋体", Font.PLAIN, 12));
    PC[2].setBackground(new Color(245, 245, 245));
    PC[2].setBounds(734, 92, 50, 25);
    frame.getContentPane().add(PC[2]);

    PC[1] = new JToggleButton("1");
    PC[1].setFont(new Font("宋体", Font.PLAIN, 12));
    PC[1].setBackground(new Color(245, 245, 245));
    PC[1].setBounds(784, 92, 50, 25);
    frame.getContentPane().add(PC[1]);

    PC[0] = new JToggleButton("0");
    PC[0].setFont(new Font("宋体", Font.PLAIN, 12));
    PC[0].setBackground(new Color(245, 245, 245));
    PC[0].setBounds(834, 92, 50, 25);
    frame.getContentPane().add(PC[0]);

    PIND = new JLabel("PIND:");
    PIND.setForeground(new Color(128, 0, 128));
    PIND.setFont(new Font("宋体", Font.BOLD, 14));
    PIND.setBackground(new Color(220, 220, 220));
    PIND.setBounds(437, 127, 49, 15);
    frame.getContentPane().add(PIND);

    PD[3] = new JToggleButton("3");
    PD[3].setFont(new Font("宋体", Font.PLAIN, 12));
    PD[3].setBackground(new Color(245, 245, 245));
    PD[3].setBounds(684, 122, 50, 25);
    frame.getContentPane().add(PD[3]);

    PD[2] = new JToggleButton("2");
    PD[2].setFont(new Font("宋体", Font.PLAIN, 12));
    PD[2].setBackground(new Color(245, 245, 245));
    PD[2].setBounds(734, 122, 50, 25);
    frame.getContentPane().add(PD[2]);

    PINE = new JLabel("PINE:");
    PINE.setForeground(new Color(128, 0, 128));
    PINE.setFont(new Font("宋体", Font.BOLD, 14));
    PINE.setBackground(new Color(220, 220, 220));
    PINE.setBounds(437, 157, 49, 15);
    frame.getContentPane().add(PINE);

    PE[7] = new JToggleButton("7");
    PE[7].setFont(new Font("宋体", Font.PLAIN, 12));
    PE[7].setBackground(new Color(245, 245, 245));
    PE[7].setBounds(484, 152, 50, 25);
    frame.getContentPane().add(PE[7]);

    PE[6] = new JToggleButton("6");
    PE[6].setFont(new Font("宋体", Font.PLAIN, 12));
    PE[6].setBackground(new Color(245, 245, 245));
    PE[6].setBounds(534, 152, 50, 25);
    frame.getContentPane().add(PE[6]);

    PE[5] = new JToggleButton("5");
    PE[5].setFont(new Font("宋体", Font.PLAIN, 12));
    PE[5].setBackground(new Color(245, 245, 245));
    PE[5].setBounds(584, 152, 50, 25);
    frame.getContentPane().add(PE[5]);

    PE[4] = new JToggleButton("4");
    PE[4].setFont(new Font("宋体", Font.PLAIN, 12));
    PE[4].setBackground(new Color(245, 245, 245));
    PE[4].setBounds(634, 152, 50, 25);
    frame.getContentPane().add(PE[4]);

    PE[3] = new JToggleButton("3");
    PE[3].setFont(new Font("宋体", Font.PLAIN, 12));
    PE[3].setBackground(new Color(245, 245, 245));
    PE[3].setBounds(684, 152, 50, 25);
    frame.getContentPane().add(PE[3]);

    PE[2] = new JToggleButton("2");
    PE[2].setFont(new Font("宋体", Font.PLAIN, 12));
    PE[2].setBackground(new Color(245, 245, 245));
    PE[2].setBounds(734, 152, 50, 25);
    frame.getContentPane().add(PE[2]);

    PINF = new JLabel("PINF:");
    PINF.setForeground(new Color(128, 0, 128));
    PINF.setFont(new Font("宋体", Font.BOLD, 14));
    PINF.setBackground(new Color(220, 220, 220));
    PINF.setBounds(437, 187, 49, 15);
    frame.getContentPane().add(PINF);

    PF[7] = new JToggleButton("7");
    PF[7].setFont(new Font("宋体", Font.PLAIN, 12));
    PF[7].setBackground(new Color(245, 245, 245));
    PF[7].setBounds(484, 182, 50, 25);
    frame.getContentPane().add(PF[7]);

    PF[6] = new JToggleButton("6");
    PF[6].setFont(new Font("宋体", Font.PLAIN, 12));
    PF[6].setBackground(new Color(245, 245, 245));
    PF[6].setBounds(534, 182, 50, 25);
    frame.getContentPane().add(PF[6]);

    PF[5] = new JToggleButton("5");
    PF[5].setFont(new Font("宋体", Font.PLAIN, 12));
    PF[5].setBackground(new Color(245, 245, 245));
    PF[5].setBounds(584, 182, 50, 25);
    frame.getContentPane().add(PF[5]);

    PF[4] = new JToggleButton("4");
    PF[4].setFont(new Font("宋体", Font.PLAIN, 12));
    PF[4].setBackground(new Color(245, 245, 245));
    PF[4].setBounds(634, 182, 50, 25);
    frame.getContentPane().add(PF[4]);

    PF[3] = new JToggleButton("3");
    PF[3].setFont(new Font("宋体", Font.PLAIN, 12));
    PF[3].setBackground(new Color(245, 245, 245));
    PF[3].setBounds(684, 182, 50, 25);
    frame.getContentPane().add(PF[3]);

    PF[2] = new JToggleButton("2");
    PF[2].setFont(new Font("宋体", Font.PLAIN, 12));
    PF[2].setBackground(new Color(245, 245, 245));
    PF[2].setBounds(734, 182, 50, 25);
    frame.getContentPane().add(PF[2]);

    PF[1] = new JToggleButton("1");
    PF[1].setFont(new Font("宋体", Font.PLAIN, 12));
    PF[1].setBackground(new Color(245, 245, 245));
    PF[1].setBounds(784, 182, 50, 25);
    frame.getContentPane().add(PF[1]);

    PF[0] = new JToggleButton("0");
    PF[0].setFont(new Font("宋体", Font.PLAIN, 12));
    PF[0].setBackground(new Color(245, 245, 245));
    PF[0].setBounds(834, 182, 50, 25);
    frame.getContentPane().add(PF[0]);

    PING = new JLabel("PING:");
    PING.setForeground(new Color(128, 0, 128));
    PING.setFont(new Font("宋体", Font.BOLD, 14));
    PING.setBackground(new Color(220, 220, 220));
    PING.setBounds(437, 216, 49, 15);
    frame.getContentPane().add(PING);

    PG[4] = new JToggleButton("4");
    PG[4].setFont(new Font("宋体", Font.PLAIN, 12));
    PG[4].setBackground(new Color(245, 245, 245));
    PG[4].setBounds(634, 211, 50, 25);
    frame.getContentPane().add(PG[4]);

    PG[3] = new JToggleButton("3");
    PG[3].setFont(new Font("宋体", Font.PLAIN, 12));
    PG[3].setBackground(new Color(245, 245, 245));
    PG[3].setBounds(684, 211, 50, 25);
    frame.getContentPane().add(PG[3]);

    PG[2] = new JToggleButton("2");
    PG[2].setFont(new Font("宋体", Font.PLAIN, 12));
    PG[2].setBackground(new Color(245, 245, 245));
    PG[2].setBounds(734, 211, 50, 25);
    frame.getContentPane().add(PG[2]);

    PG[1] = new JToggleButton("1");
    PG[1].setFont(new Font("宋体", Font.PLAIN, 12));
    PG[1].setBackground(new Color(245, 245, 245));
    PG[1].setBounds(784, 211, 50, 25);
    frame.getContentPane().add(PG[1]);

    PG[0] = new JToggleButton("0");
    PG[0].setFont(new Font("宋体", Font.PLAIN, 12));
    PG[0].setBackground(new Color(245, 245, 245));
    PG[0].setBounds(834, 211, 50, 25);
    frame.getContentPane().add(PG[0]);

    comLabel = new JLabel("串口");
    comLabel.setFont(new Font("宋体", Font.PLAIN, 14));
    comLabel.setBounds(10, 274, 36, 15);
    frame.getContentPane().add(comLabel);

    portList = new JComboBox<String>();
    ArrayList<String> list = SerialPortTools.findPort();
    for (String s : list) {
      portList.addItem(s);
    }
    portList.setFont(new Font("宋体", Font.PLAIN, 14));
    portList.setBackground(new Color(245, 245, 245));
    portList.setBounds(43, 271, 60, 21);
    frame.getContentPane().add(portList);

    baudLabel = new JLabel("波特率");
    baudLabel.setFont(new Font("宋体", Font.PLAIN, 14));
    baudLabel.setBounds(113, 274, 46, 15);
    frame.getContentPane().add(baudLabel);

    baudList = new JComboBox<Integer>();
    baudList.addItem(9600);
    baudList.addItem(115200);
    baudList.setFont(new Font("宋体", Font.PLAIN, 14));
    baudList.setBackground(new Color(245, 245, 245));
    baudList.setBounds(155, 271, 68, 21);
    frame.getContentPane().add(baudList);

    parityLabel = new JLabel("校验位");
    parityLabel.setFont(new Font("宋体", Font.PLAIN, 14));
    parityLabel.setBounds(233, 274, 46, 15);
    frame.getContentPane().add(parityLabel);

    parityList = new JComboBox<String>();
    parityList.addItem("无校验");
    parityList.addItem("奇校验");
    parityList.addItem("偶校验");
    parityList.setFont(new Font("宋体", Font.PLAIN, 14));
    parityList.setBackground(new Color(245, 245, 245));
    parityList.setBounds(282, 271, 68, 21);
    frame.getContentPane().add(parityList);

    stopLabel = new JLabel("停止位");
    stopLabel.setFont(new Font("宋体", Font.PLAIN, 14));
    stopLabel.setBounds(360, 274, 46, 15);
    frame.getContentPane().add(stopLabel);

    stopList = new JComboBox<Integer>();
    stopList.addItem(1);
    stopList.addItem(2);
    stopList.setFont(new Font("宋体", Font.PLAIN, 14));
    stopList.setBackground(new Color(245, 245, 245));
    stopList.setBounds(405, 271, 68, 21);
    frame.getContentPane().add(stopList);

    openPort = new JButton("打开串口");
    openPort.setFont(new Font("宋体", Font.PLAIN, 16));
    openPort.setBackground(new Color(245, 245, 245));
    openPort.setBounds(10, 314, 98, 37);
    frame.getContentPane().add(openPort);

    transData = new JButton("发送数据");
    transData.setFont(new Font("宋体", Font.PLAIN, 14));
    transData.setBackground(new Color(245, 245, 245));
    transData.setBounds(118, 322, 98, 23);
    frame.getContentPane().add(transData);

    label = new JLabel("*版权所有：广州番禺旭东阪田电子有限公司(PKS)");
    label.setHorizontalAlignment(SwingConstants.LEFT);
    label.setForeground(Color.GRAY);
    label.setFont(new Font("楷体", Font.PLAIN, 12));
    label.setBounds(5, 395, 800, 15);
    frame.getContentPane().add(label);

    ioControl = new JButton("I/O控制");
    ioControl.setFont(new Font("宋体", Font.PLAIN, 14));
    ioControl.setBackground(new Color(245, 245, 245));
    ioControl.setBounds(484, 270, 98, 23);
    frame.getContentPane().add(ioControl);

    allSetBit = new JButton("全部置位");
    allSetBit.setFont(new Font("宋体", Font.PLAIN, 14));
    allSetBit.setBackground(new Color(245, 245, 245));
    allSetBit.setBounds(592, 270, 98, 23);
    frame.getContentPane().add(allSetBit);

    allClrBit = new JButton("全部清零");
    allClrBit.setFont(new Font("宋体", Font.PLAIN, 14));
    allClrBit.setBackground(new Color(245, 245, 245));
    allClrBit.setBounds(700, 270, 98, 23);
    frame.getContentPane().add(allClrBit);
    
    transField = new JTextField();
    transField.setHorizontalAlignment(SwingConstants.RIGHT);
    transField.setText("0");
    transField.setFont(new Font("宋体", Font.PLAIN, 14));
    transField.setBounds(352, 309, 66, 21);
    frame.getContentPane().add(transField);
    transField.setColumns(10);
    
    recieveField = new JTextField();
    recieveField.setText("0");
    recieveField.setHorizontalAlignment(SwingConstants.RIGHT);
    recieveField.setFont(new Font("宋体", Font.PLAIN, 14));
    recieveField.setColumns(10);
    recieveField.setBounds(352, 341, 66, 21);
    frame.getContentPane().add(recieveField);
    
    clrButt = new JButton("清零");
    clrButt.setBackground(new Color(245, 245, 245));
    clrButt.setFont(new Font("宋体", Font.PLAIN, 12));
    clrButt.setBounds(434, 340, 75, 23);
    frame.getContentPane().add(clrButt);
    
    transLabel = new JLabel("发送：");
    transLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    transLabel.setFont(new Font("宋体", Font.PLAIN, 12));
    transLabel.setBounds(288, 312, 54, 15);
    frame.getContentPane().add(transLabel);
    
    recieveLabel = new JLabel("接收：");
    recieveLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    recieveLabel.setFont(new Font("宋体", Font.PLAIN, 12));
    recieveLabel.setBounds(288, 344, 54, 15);
    frame.getContentPane().add(recieveLabel);
    frame.setBounds(100, 100, 939, 459);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
    timer1 = new Timer(500, (event) -> {
      if(hasData) {
        hasData = false;
        rxBufferField.append(SerialPortTools.bytesToHex(bytes) + "\r\n");
        recieveCount += 11;
        recieveField.setText(recieveCount + "");
      }
    });
    timer1.start();
    
  }

  /**
   * 给各个部件添加事件
   */
  public void addListener() {

    //清空接收缓冲区事件
    clrRxBuffer.addActionListener(e -> {
      rxBufferField.setText("");
      
    });
    //清空发送缓冲区事件
    clrTxBuffer.addActionListener(e -> txBufferField.setText(""));
    //打开串口事件
    openPort.addActionListener(e -> {
      if(openPort.getText().equals("打开串口")) {
        openPort.setText("关闭串口");
        String name = (String) portList.getSelectedItem();
        int baud = (int) baudList.getSelectedItem();
        int parity = -1;
        if(parityList.getSelectedItem().equals("无校验"))  parity = 0;
        else if(parityList.getSelectedItem().equals("奇校验"))  parity = 1;
        else if(parityList.getSelectedItem().equals("偶校验"))  parity = 2;
        int stop = (int) stopList.getSelectedItem();
        try {
          serialPort = SerialPortTools.getPort(name, baud, 8, stop, parity);
        } catch (SerialPortParamFail | NotASerialPort  | NoSuchPort | PortInUse e1) {
          JOptionPane.showMessageDialog(null, serialPort.getName() + "::" + e1.toString());
        }
        try {
          SerialPortTools.add(serialPort, arg0 -> {
            switch (arg0.getEventType()) {
            case SerialPortEvent.BI:  //10 通讯中断
            case SerialPortEvent.OE:  // 7 溢位（溢出）错误
            case SerialPortEvent.FE:  // 9 帧错误
            case SerialPortEvent.PE:  // 8 奇偶校验错误
            case SerialPortEvent.CD:  // 6 载波检测
            case SerialPortEvent.CTS:  // 3 清除待发送数据
            case SerialPortEvent.DSR:  // 4 待发送数据准备好了
            case SerialPortEvent.RI:  // 5 振铃指示
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:  // 2 输出缓冲区已清空
              JOptionPane.showMessageDialog(null, serialPort.getName() + "::" + arg0.toString());
              break;
            case SerialPortEvent.DATA_AVAILABLE: {
              try {
                data = SerialPortTools.read_byte(serialPort);
              } catch (ReadDataFromSerialFail e2) {
                JOptionPane.showMessageDialog(null, serialPort.getName() + "::" + e2.toString());
              } catch (InputStreamCloseFail e2) {
                JOptionPane.showMessageDialog(null, serialPort.getName() + "::" + e2.toString());
              }
              bytes[rxCounter] = data;
              rxCounter++;
              switch(rxCounter) {
              case 1:
                if(data != FIRST_TEXT)  rxCounter = 0;
                break;
              case 2:
                if(data != SECOND_TEXT) rxCounter = 0;
                break;
              case 11:
                rxCounter = 0;
                if(data == END_TEXT)  hasData = true;
                break;
              default:break;
              }
            }
              break;
            }
          });
        } catch (TooManyListeners e1) {
          JOptionPane.showMessageDialog(null, serialPort.getName() + "::" + e1.toString());
        }
      }
      else if(openPort.getText().equals("关闭串口")) {
        openPort.setText("打开串口");
        SerialPortTools.closePort(serialPort);
      }
    });
    // 发送数据事件
    transData.addActionListener(e -> {
      
      if(serialPort != null) {
        try {
          String str = txBufferField.getText();
          if(str != null) {
            SerialPortTools.write(serialPort, SerialPortTools.toByteArray(str));
            transCount += 11;
            transField.setText(transCount + "");
          }          
          else
            JOptionPane.showMessageDialog(null, serialPort.getName() + "::发送数据不能为空！");
        } catch (SendToPortFail | OutputStreamCloseFail e1) {
          JOptionPane.showMessageDialog(null, serialPort.getName() + "::" + e1.toString());
        }
      }
      else
        JOptionPane.showMessageDialog(null, "请打开串口后再操作！");
    });
    // I/O控制事件
    ioControl.addActionListener(e -> {
      // 根据按钮有无按下赋值,按下则置位，否则清零
      for (int i = 0; i < 8; i++) {
        datas[2] = PA[i].isSelected() ? (datas[2] |= (1 << i)) : (datas[2] &= ~(1 << i));
        datas[3] = PB[i].isSelected() ? (datas[3] |= (1 << i)) : (datas[3] &= ~(1 << i));
        datas[4] = PC[i].isSelected() ? (datas[4] |= (1 << i)) : (datas[4] &= ~(1 << i));
        if (i == 2 || i == 3) {
          datas[5] = PD[i].isSelected() ? (datas[5] |= (1 << i)) : (datas[5] &= ~(1 << i));
        }
        if (i >= 2) {
          datas[6] = PE[i].isSelected() ? (datas[6] |= (1 << i)) : (datas[6] &= ~(1 << i));
        }
        datas[7] = PF[i].isSelected() ? (datas[7] |= (1 << i)) : (datas[7] &= ~(1 << i));
        if (i <= 4) {
          datas[8] = PG[i].isSelected() ? (datas[8] |= (1 << i)) : (datas[8] &= ~(1 << i));
        }
      }
      // 发送I/O控制指令
      txBufferField.setText(SerialPortTools.bytesToHex(datas));
    });
    // 全部置位事件
    allSetBit.addActionListener(e -> {
      for (int i = 0; i < 8; i++) {
        
        PA[i].setSelected(true);
        PB[i].setSelected(true);
        PC[i].setSelected(true);
        if (i == 2 || i == 3)
          PD[i].setSelected(true);
        if (i >= 2)
          PE[i].setSelected(true);
        PF[i].setSelected(true);
        if (i <= 4)
          PG[i].setSelected(true);
      }
      for (int i = 2; i < 9; i++) {
        datas[i] = (byte) 0xff;
      }
      // 发送全部置位指令
      txBufferField.setText(SerialPortTools.bytesToHex(datas));
    });
    // 全部清零事件
    allClrBit.addActionListener(e -> {
      for (int i = 0; i < 8; i++) {
        PA[i].setSelected(false);
        PB[i].setSelected(false);
        PC[i].setSelected(false);
        if (i == 2 || i == 3)
          PD[i].setSelected(false);
        if (i >= 2)
          PE[i].setSelected(false);
        PF[i].setSelected(false);
        if (i <= 4)
          PG[i].setSelected(false);
      }
      for (int i = 2; i < 9; i++) {
        datas[i] = 0x00;
      }
      // 发送全部清零指令
      txBufferField.setText(SerialPortTools.bytesToHex(datas));
    });
    //清零按键事件
    clrButt.addActionListener(e -> {
      transField.setText("");
      recieveField.setText("");
      transCount = 0;
      recieveCount = 0;
      transField.setText("0");
      recieveField.setText("0");
    });
  }
}
