package loyer.dataBase;

/**
 * 不良记录表testdatas的实体
 * @author hw076
 *
 */
public class NGRecordData {

  private int serialNum;
  private String steps;
  private String testItems;
  private String upperLimit;
  private String lowerLimit;
  private String testValue;
  private String unit;
  private String date;
  private String mark;
  public NGRecordData() {
    super();
  }
  public NGRecordData(int serialNum, String steps, String testItems, String upperLimit, String lowerLimit,
      String testValue, String unit, String date, String mark) {
    super();
    this.serialNum = serialNum;
    this.steps = steps;
    this.testItems = testItems;
    this.upperLimit = upperLimit;
    this.lowerLimit = lowerLimit;
    this.testValue = testValue;
    this.unit = unit;
    this.date = date;
    this.mark = mark;
  }
  public int getSerialNum() {
    return serialNum;
  }
  public void setSerialNum(int serialNum) {
    this.serialNum = serialNum;
  }
  public String getSteps() {
    return steps;
  }
  public void setSteps(String steps) {
    this.steps = steps;
  }
  public String getTestItems() {
    return testItems;
  }
  public void setTestItems(String testItems) {
    this.testItems = testItems;
  }
  public String getUpperLimit() {
    return upperLimit;
  }
  public void setUpperLimit(String upperLimit) {
    this.upperLimit = upperLimit;
  }
  public String getLowerLimit() {
    return lowerLimit;
  }
  public void setLowerLimit(String lowerLimit) {
    this.lowerLimit = lowerLimit;
  }
  public String getTestValue() {
    return testValue;
  }
  public void setTestValue(String testValue) {
    this.testValue = testValue;
  }
  public String getUnit() {
    return unit;
  }
  public void setUnit(String unit) {
    this.unit = unit;
  }
  public String getDate() {
    return date;
  }
  public void setDate(String date) {
    this.date = date;
  }
  public String getMark() {
    return mark;
  }
  public void setMark(String mark) {
    this.mark = mark;
  }
  
}
