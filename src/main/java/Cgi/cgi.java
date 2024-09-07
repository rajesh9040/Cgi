package Cgi;

import java.io.File;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class cgi {
    WebDriver driver;

    public cgi(WebDriver driver) {
        this.driver = driver;
    }

    public void launchbrowser() throws IOException, InterruptedException {
        // Setup WebDriver
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\91700\\eclipse-workspace\\Cgi\\driver\\chromedriver.exe");
        DesiredCapabilities capable = new DesiredCapabilities();
        capable.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        ChromeOptions options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Open Amazon and take screenshot
        driver.get("https://www.amazon.in/");
        takeScreenshot("D:\\amazon_homepage.png");

        // Open a new tab and navigate to SpiceJet
        driver.switchTo().newWindow(WindowType.TAB);
        driver.navigate().to("https://www.spicejet.com/");
        switchToFirstTab();

        // Interact with the search dropdown on Amazon
        interactWithDropdown();

        // Search for an iPhone
        searchForItem("Iphone");

        // Open a new tab and navigate to Rahul Shetty Academy
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get("https://rahulshettyacademy.com/AutomationPractice/");
        Thread.sleep(5000);

        // Scroll and interact with the table
        scrollAndInteractWithTable();

        // Check for broken links
        checkForBrokenLinks();

        // Upload file
        uploadFile("https://ps.uci.edu/~franklin/doc/file_upload.html", "D:\\amazon_homepage.png");

        // Interact with Flipkart
        interactWithFlipkart();

        // Read Excel file
        readExcelFile("D:\\continentname");
    }

    private void takeScreenshot(String filePath) throws IOException {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(src, new File(filePath));
    }

    private void switchToFirstTab() {
        Set<String> windowHandles = driver.getWindowHandles();
        Iterator<String> iterator = windowHandles.iterator();
        String parentWindow = iterator.next();
        driver.switchTo().window(parentWindow);
    }

    private void interactWithDropdown() {
        WebElement element = driver.findElement(By.cssSelector("div[id='nav-search-dropdown-card']"));
        element.click();

        WebDriverWait wait= new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='nav-search-dropdown-card']")));

        WebElement dropdownElement = driver.findElement(By.id("searchDropdownBox"));
        Select dropdown = new Select(dropdownElement);
        List<WebElement> list = dropdown.getOptions();

        for (WebElement option : list) {
            System.out.println(option.getText());
        }
        dropdown.selectByVisibleText("Apps & Games");
    }

    private void searchForItem(String item) {
        WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
        Actions action = new Actions(driver);
        action.moveToElement(searchBox).click().keyDown(Keys.SHIFT).sendKeys(item).build().perform();
        driver.findElement(By.id("nav-search-submit-button")).click();
    }

    private void scrollAndInteractWithTable() throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,500)");

        WebElement tableHeader = driver.findElement(By.xpath("//div[@class='tableFixHead']/table/thead/tr"));
        System.out.println(tableHeader.getText());

        WebElement firstColumnHeader = driver.findElement(By.xpath("//div[@class='tableFixHead']/table/thead/tr/th[1]"));
        System.out.println(firstColumnHeader.getText());

        List<WebElement> firstColumnCells = driver.findElements(By.cssSelector(".tableFixHead td:nth-child(1)"));
        for (WebElement cell : firstColumnCells) {
            System.out.print(cell.getText());
        }

        List<WebElement> rows = driver.findElements(By.tagName("tr"));
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            for (WebElement cell : cells) {
                System.out.println(cell.getText());
            }
        }
    }

    private void checkForBrokenLinks() throws IOException {
        List<WebElement> links = driver.findElements(By.tagName("a"));
        System.out.println("Total links: " + links.size());

        for (WebElement link : links) {
            String url = link.getAttribute("href");
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.connect();
            int responseCode = connection.getResponseCode(); 
            if (responseCode >= 400) {
                System.out.println("Broken link found: " + url + " (Response code: " + responseCode + ")");
            } else {
                System.out.println("Valid link: " + url);
            }
        }
    }

    private void uploadFile(String url, String filePath) {
        driver.get(url);
        driver.findElement(By.xpath("/html/body/form/input[1]")).sendKeys(filePath);
    }

    private void interactWithFlipkart() {
        driver.get("https://www.flipkart.com/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
        WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div[1]/div[1]/div[2]/div[2]/form/div/div/input")));
        searchBox.click();
        searchBox.sendKeys("mobile");
    }

    private void readExcelFile(String filePath) throws IOException {
        FileInputStream excelFile = new FileInputStream(filePath);
        XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
        int numberOfSheets = workbook.getNumberOfSheets();
        int column = 0;

        for (int i = 0; i < numberOfSheets; i++) {
            if (workbook.getSheetName(i).equalsIgnoreCase("continentname")) {
                XSSFSheet sheet = workbook.getSheetAt(i);
                Iterator<Row> rows = sheet.iterator();
                Row firstRow = rows.next();
                Iterator<Cell> cells = firstRow.iterator();
                int k = 0;
                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    if (cell.getStringCellValue().equalsIgnoreCase("Continent")) {
                        column = k;
                    }
                    k++;
                }
            }
        }
        System.out.println("Continent column index: " + column);
    }
}
