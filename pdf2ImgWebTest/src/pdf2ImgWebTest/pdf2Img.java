package pdf2ImgWebTest;

import org.testng.annotations.Test;

import pdf2ImgWebTest.zObjMap;
import pdf2ImgWebTest.zPdf2ImgUtil;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

@Test
public class pdf2Img {

	WebDriver driver;
	zObjMap objMap = new zObjMap();
	Properties testVars = new Properties();
	String mainSite, driverPath, testPdfPath;
	Integer impWait, expWait;
	
	String testFilePath = "resources/test_files.txt";
	LinkedList<String> testFileList = new LinkedList<String>();
	
	String pageRangeMax = "Demo_Max";
	String pageRangeFirst = "First_Page";
	
	// Webdriver setup
	// This will check the "browser" parameter in the testng.xml file and create the correct webdriver.
	// This will also prep the Properties object with most test variables
	//   and get the list of test books.
	@Parameters({ "browser" }) @BeforeClass(alwaysRun = true)
	public void setUp(String browser) throws Exception{
		zPdf2ImgUtil.propLoader(testVars);
		mainSite = testVars.getProperty("pdf2imgWeb");
		driverPath = testVars.getProperty("driverPath");
		testPdfPath = testVars.getProperty("testPdfPath");
		impWait = Integer.parseInt(testVars.getProperty("impWait"));
		expWait = Integer.parseInt(testVars.getProperty("expWait"));
		
		zPdf2ImgUtil.fetchTestDirectoryPdfs(testFileList, testPdfPath);
		Collections.sort(testFileList);
		
		driver = zPdf2ImgUtil.setDriver(driver, browser, driverPath, impWait);
		//fetchTestList(testFileList, testFilePath);
	}
	
	// Main test
	@Test (groups = { "base" })
	public void pdf2ImgMainTest() throws Exception {
		Iterator<String> it = testFileList.iterator();
		while (it.hasNext()) {
			String testBook = testPdfPath + it.next();
			submitBook(it, testBook, pageRangeMax, "PNG");
		}
	}
	
	@Test (groups = { "png" })
	public void pdf2ImgPngTest() throws Exception {
		Iterator<String> it = testFileList.iterator();
		while (it.hasNext()) {
			String testBook = testPdfPath + it.next();
			submitBook(it, testBook, pageRangeFirst, "PNG");
		}
	}
	
	@Test (groups = { "jpg" })
	public void pdf2ImgJpgTest() throws Exception {
		Iterator<String> it = testFileList.iterator();
		while (it.hasNext()) {
			String testBook = testPdfPath + it.next();
			submitBook(it, testBook, pageRangeFirst, "JPG");
		}
	}
	
	@Test (groups = { "gif" })
	public void pdf2ImgGifTest() throws Exception {
		Iterator<String> it = testFileList.iterator();
		while (it.hasNext()) {
			String testBook = testPdfPath + it.next();
			submitBook(it, testBook, pageRangeFirst, "GIF");
		}
	}
	
	@Test (groups = { "tif" })
	public void pdf2TifJpgTest() throws Exception {
		Iterator<String> it = testFileList.iterator();
		while (it.hasNext()) {
			String testBook = testPdfPath + it.next();
			submitBook(it, testBook, pageRangeFirst, "TIF");
		}
	}
	
	@Test (groups = { "bmp" })
	public void pdf2ImgBmpTest() throws Exception {
		Iterator<String> it = testFileList.iterator();
		while (it.hasNext()) {
			String testBook = testPdfPath + it.next();
			submitBook(it, testBook, pageRangeFirst, "BMP");
		}
	}
	
	// Webdriver shutdown
	// This will run after the test class, and will exit the webdriver
	@AfterClass(alwaysRun = true)
	public void quitDriver() throws InterruptedException{
		zPdf2ImgUtil.exitDriver(driver);
	}
	
	// Uploads the book and retrieves the results
	public void submitBook(Iterator<String> it, String testBook, String pageRange, String imgFormat) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, expWait);
		
		driver.get(mainSite);
		wait.until(ExpectedConditions.visibilityOfElementLocated(objMap.getLoc("pdf2img.panel-widget")));
		
		System.out.println("Processing book " + testBook + " as " + imgFormat + " with page range " + pageRange);
		
		driver.findElement(objMap.getLoc("pdf2img.disclaimer")).click();
		driver.findElement(objMap.getLoc("pdf2img.upload_file")).sendKeys(testBook);
		Select pageRangeDropdown = new Select(driver.findElement(objMap.getLoc("pdf2img.page_range")));
		pageRangeDropdown.selectByValue(pageRange);
		Select imgFormatDropdown = new Select(driver.findElement(objMap.getLoc("pdf2img.img_format")));
		imgFormatDropdown.selectByValue(imgFormat);
		driver.findElement(objMap.getLoc("pdf2img.submit_btn")).click();
		
		wait.until(ExpectedConditions.presenceOfElementLocated(objMap.getLoc("pdf2img.start_over")));
		if (!driver.findElements(objMap.getLoc("pdf2img.download_link")).isEmpty()){
			driver.findElement(objMap.getLoc("pdf2img.download_link")).click();
		}
		else {
			String bodyText = driver.findElement(objMap.getLoc("pdf2img.body_text")).getText();
			System.out.println(bodyText);
			driver.findElement(objMap.getLoc("pdf2img.start_over")).click();
		}
	}
	
//	// Fetches the list of test books
//	public void fetchTestList (LinkedList<String> inc, String testFilePath) throws FileNotFoundException{
//		BufferedReader reader = new BufferedReader (new FileReader (new File(testFilePath)));
//		String line;
//		try{
//			while (( line = reader.readLine()) != null) {
//				testFileList.add(line);
//			}
//		} catch (IOException e) {
//		    e.printStackTrace();
//		} finally {
//		    try {
//		        reader.close();
//		    } catch (IOException e) {
//		        e.printStackTrace();
//		    }
//		}
//	}
}
