package com.example.spiderjobfull.script;

import com.alibaba.fastjson.JSON;
import com.egoist.parent.common.annotation.EgoistTimeRecorder;
import com.egoist.parent.common.utils.string.EgoistStringUtil;
import com.example.spiderjobfull.config.Config;
import com.example.spiderjobfull.pojo.FmsData;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class Script16880908com {
    @Autowired
    private Config CONFIG;

    private static final String url_template = "http://16880908.com/openhsitory.aspx?p=%s&date=%s&id=10&open_key=pk10kai";

    private static final String startDateStr = "2020-02-28";

    private static final String type = "幸運飛艇";

    private static WebDriver driver;

    @EgoistTimeRecorder
    public void run() {
        try {
            // 注意chromedriver的版本要和chrome浏览器的版本一致
//            System.setProperty("webdriver.chrome.driver", "E:\\yjs\\application\\chromedriver.exe");
//            driver = new ChromeDriver();
//            driver.get("http://16880908.com/openhsitory.aspx?date=2020-02-28&id=10&open_key=pk10kai");

            // 注意geckodriver的版本要和火狐浏览器的版本一致
            System.setProperty("webdriver.gecko.driver", CONFIG.getWEB_DRIVER_PATH());
            driver = new FirefoxDriver();
            String startDate = CONFIG.getSTART_DATE();
            String endDate = CONFIG.getEND_DATE();
            if (EgoistStringUtil.isBlank(startDate)) {
                startDate = startDateStr;
            }
            if (EgoistStringUtil.isBlank(endDate)) {
                endDate = LocalDate.now().toString();
            }
            LocalDate endLocalDate = LocalDate.parse(endDate);
            LocalDate localDate = LocalDate.parse(startDate);
            while (localDate.compareTo(endLocalDate) <= 0) {
                try {
                    int page = 1;
                    do {
                        driver.get(String.format(url_template, page, localDate.toString()));
                        WebDriverWait wait = new WebDriverWait(driver, 3);
                        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("jrsmhmtj")));
                        List<WebElement> trList = driver.findElement(By.id("jrsmhmtj")).findElement(By.tagName("table")).findElements(By.tagName("tr"));
                        // 如果没有记录就跳过这个日期
                        if (trList.size() == 1) {
                            break;
                        }
                        List<FmsData> fmsDataList = new ArrayList<>();
                        for (int i = 1; i < trList.size(); i++) {
                            WebElement tr = trList.get(i);
                            List<WebElement> tdList = tr.findElements(By.tagName("td"));
                            FmsData fmsData = new FmsData();
                            fmsData.setType(type);
                            fmsData.setDrawDate(tdList.get(0).getText());
                            fmsData.setTermNo(tdList.get(1).getText());
                            List<WebElement> liList = tdList.get(2).findElement(By.tagName("ul")).findElements(By.tagName("li"));
                            StringBuilder drawNumber = new StringBuilder();
                            for (int j = 0; j < liList.size(); j++) {
                                WebElement li = liList.get(j);
                                drawNumber.append(li.findElement(By.tagName("i")).getText());
                                if (j != liList.size() - 1) {
                                    drawNumber.append(",");
                                }
                            }
                            fmsData.setDrawNumber(drawNumber.toString());
                            StringBuilder sumSb = new StringBuilder();
                            sumSb.append(tdList.get(3).getText()).append(",").append(tdList.get(4).getText()).append(",").append(tdList.get(5).getText());
                            fmsData.setSum(sumSb.toString());
                            StringBuilder dragonTigerSb = new StringBuilder();
                            dragonTigerSb.append(tdList.get(6).getText()).append(",").append(tdList.get(7).getText()).append(",")
                                    .append(tdList.get(8).getText()).append(",").append(tdList.get(9).getText()).append(",")
                                    .append(tdList.get(10).getText());
                            fmsData.setDragonTiger(dragonTigerSb.toString());
                            fmsDataList.add(fmsData);
                        }
                        this.save(fmsDataList);
//                        System.out.println(localDate.toString()+" "+ page);
                        page++;
                        try {
                            driver.findElement(By.xpath("//a[@title='下一頁']"));
                        } catch (Exception exx) {
                            break;
                        }
                    } while (true);
                    localDate = localDate.plusDays(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            log.error("报错", e);
        }
    }

    public void save(List<FmsData> fmsDataList) {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(CONFIG.getJDBC_DRIVER());
            conn = DriverManager.getConnection(CONFIG.getDB_URL(), CONFIG.getUSER(), CONFIG.getPASS());
            stmt = conn.createStatement();
            for (FmsData fmsData : fmsDataList) {
                try {
                    String sql = String.format("insert into tb_fms_data_temp (type,term_no,draw_date,draw_number,sum,dragon_tiger) values ('%s','%s','%s','%s','%s','%s')"
                            , fmsData.getType(), fmsData.getTermNo(), fmsData.getDrawDate(), fmsData.getDrawNumber(), fmsData.getSum(), fmsData.getDragonTiger());
                    int result = stmt.executeUpdate(sql);
                    if (result > 0) {
                        log.info("插入数据" + JSON.toJSONString(fmsData));
                    }
                } catch (Exception ex) {

                }
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se) {
            }// do nothing
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }
    }
}
