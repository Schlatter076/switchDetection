package loyer.dataBase;

/**
 * 不良记录表recorddata的实体
 * @author hw076
 *
 */
public class RecordData {

  private String name;
  private int sum;
  private int ok;
  private int ng;
  private String times;
  private String date;
  
  public RecordData() {}
  
  public RecordData(String name, int sum, int ok, int ng, String times, String date) {
    super();
    this.name = name;
    this.sum = sum;
    this.ok = ok;
    this.ng = ng;
    this.times = times;
    this.date = date;
  }

  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public int getSum() {
    return sum;
  }
  public void setSum(int sum) {
    this.sum = sum;
  }
  public int getOk() {
    return ok;
  }
  public void setOk(int ok) {
    this.ok = ok;
  }
  public int getNg() {
    return ng;
  }
  public void setNg(int ng) {
    this.ng = ng;
  }
  public String getTimes() {
    return times;
  }
  public void setTimes(String times) {
    this.times = times;
  }
  public String getDate() {
    return date;
  }
  public void setDate(String date) {
    this.date = date;
  } 
}
