package loyer.dataBase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class RecordTools {

  private RecordTools() {} //不允许其他类创建本类的实例
  
  /**
   * 读取recorddata表中所有的数据
   * @return
   */
  public static ArrayList<RecordData> getAllByDB() {
    
    ArrayList<RecordData> list = new ArrayList<>();
    String sql = "select * from recorddata";
    ResultSet rs = DBHelper.search(sql, null);
    try {
      while(rs.next()) {
        String name = rs.getString(1);
        int sum = rs.getInt(2);
        int ok = rs.getInt(3);
        int ng = rs.getInt(4);
        String times = rs.getString(5);
        String date = rs.getString(6);
        
        list.add(new RecordData(name, sum, ok, ng, times, date));
      }
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(null, "不良对照表读取失败：" + e.getLocalizedMessage());
    } finally {
      DBHelper.close();
    }
    return list;
  }
  /**
   * 获取指定日期的recorddata表的数据
   * @param date  指定的日期
   * @return
   */
  public static RecordData getDataByDate(String date) {
    
    RecordData data = new RecordData();
    String sql = "select * from recorddata where date='"+date+"'";
    ResultSet rs = DBHelper.search(sql, null);
    try {
      if(rs.next()) {
        String name = rs.getString(1);
        int sum = rs.getInt(2);
        int ok = rs.getInt(3);
        int ng = rs.getInt(4);
        String times = rs.getString(5);
        String dates = rs.getString(6);
        data = new RecordData(name, sum, ok, ng, times, dates);
      }
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(null, "不良对照表读取失败：" + e.getLocalizedMessage());
    } finally {
      DBHelper.close();
    }
    return data;
  }
  /**
   * 更新recorddata表中原有的数据
   * @param str
   * @return
   */
  public static int updateData(String[] str) {
    int back = 0;
    if(str == null || str.length != 6) {
      JOptionPane.showMessageDialog(null, "数据格式不对，请检查后重试！");
      return -1;
    }
    String sql = "update recorddata set name='"+str[0]+"',sum='"+str[1]+"',ok='"+str[2]+"',ng='"+str[3]+"',times='"+str[4]+"',date='"+str[5]+"' where date='"+str[5]+"'";
    back = DBHelper.AddU(sql, null);
    DBHelper.close();
    return back;
  }
  /**
   * 向recorddata表插入数据
   * @param str  待插入的数据
   * @return
   */
  public static int insertData(String[] str) {
    int back = 0; 
    if(str == null || str.length != 6) {
      JOptionPane.showMessageDialog(null, "数据格式不对，请检查后重试！");
      return -1;
    }
    String sql = "insert into recorddata(name, sum, ok, ng, times, date) values(?, ?, ?, ?, ?, ?)";
    back = DBHelper.AddU(sql, str);
    DBHelper.close();
    return back;
  }
  
}
