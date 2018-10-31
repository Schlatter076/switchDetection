package loyer.dataBase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * 不良记录工具类
 * @author hw076
 *
 */
public class NGRecordTools {

  private NGRecordTools() {}  //不允许其他类创建本类实例
  
  /**
   * 获取不良记录表中所有数据
   * @return
   */
  public static ArrayList<NGRecordData> getAllByDB() {
    ArrayList<NGRecordData> list = new ArrayList<>();
    String sql = "select * from testdatas";
    ResultSet rs = DBHelper.search(sql, null);
    try {
      while(rs.next()) {
        int serialNum = rs.getInt(1);
        String steps = rs.getString(2);
        String testItems = rs.getString(3);
        String upperLimit = rs.getString(4);
        String lowerLimit = rs.getString(5);
        String testValue = rs.getString(6);
        String unit = rs.getString(7);
        String date = rs.getString(8);
        String mark = rs.getString(9);
        
        list.add(new NGRecordData(serialNum, steps, testItems, upperLimit, lowerLimit, testValue, unit, date, mark));
      }
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(null, "不良记录表获取失败：" + e.getLocalizedMessage());
    } finally {
      DBHelper.close();
    }
    return list;
  }
  /**
   * 根据指定日期获取不良记录表数据
   * @param date
   * @return
   */
  public static ArrayList<NGRecordData> getAllByDate(String date) {
    ArrayList<NGRecordData> list = new ArrayList<>();
    String sql = "select * from testdatas where date='"+date+"'";
    ResultSet rs = DBHelper.search(sql, null);
    try {
      while(rs.next()) {
        int serialNum = rs.getInt(1);
        String steps = rs.getString(2);
        String testItems = rs.getString(3);
        String upperLimit = rs.getString(4);
        String lowerLimit = rs.getString(5);
        String testValue = rs.getString(6);
        String unit = rs.getString(7);
        //String date = rs.getString(8);
        String mark = rs.getString(9);
        
        list.add(new NGRecordData(serialNum, steps, testItems, upperLimit, lowerLimit, testValue, unit, date, mark));
      }
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(null, "不良记录表获取失败：" + e.getLocalizedMessage());
    } finally {
      DBHelper.close();
    }
    return list;
  }
  /**
   * 向不良记录表中插入数据
   * @param str 长度为8的字符数组
   * @return
   */
  public static int insert(String[] str) {
    int back = -1;
    if(str == null || str.length != 8) {
      JOptionPane.showMessageDialog(null, "数据格式不对，请检查后重试！");
      return back;
    }
    String sql = "insert into testdatas(steps, testItems, upperLimit, lowerLimit, testValue, unit, date, mark) values(?, ?, ?, ?, ?, ?, ?, ?)";
    back = DBHelper.AddU(sql, str);
    DBHelper.close();
    return back;
  }
}
