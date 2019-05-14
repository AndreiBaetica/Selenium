package main;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class App {

	public static void main(String[] args) throws IOException {
		// save pic to desktop, create JSON file with img src
		System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriver\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		JSONObject obj = new JSONObject();
		JSONArray list = new JSONArray();
		driver.get("https://warthunder.com/en/media/screenshots/");
		WebElement next = driver.findElement(By.className("next"));
		downloader(driver, obj, 1, list, 0);
		next.click();
		downloader(driver, obj, 33, list, 0);
		while (true) {
			try {
				next.click();
				break;
			} catch (StaleElementReferenceException e) {
				next = driver.findElement(By.className("next"));
			}
		}
		// stale element exception only thrown here, first click always works
		// without issue.
		downloader(driver, obj, 66, list, 3);

		try {
			obj.put("src", list);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println("Writing JSON...");
		FileWriter fw = new FileWriter(new File("C://Users//Admin//Desktop//Documents//JSON File", "image"));
		fw.write(obj.toString());
		fw.close();
		driver.close();
		System.out.println("Process completed.");
	}

	static void downloader(WebDriver driver, JSONObject obj, int imageNumber, JSONArray list, int missingLink)
			throws IOException {
		List<WebElement> screens = driver.findElements(By.className("info"));
		int localNum = 0;
		for (int i = 0; i < screens.size() - missingLink; i += 3) {
			// missing link variable in place because the last image throws a
			// 404 error. This way the loop doesn't include that image.
			WebElement pictureWebElement = screens.get(i);
			String imageAddress = pictureWebElement.getAttribute("href");
			list.put(imageAddress);
			URL url = new URL(imageAddress);
			InputStream in = new BufferedInputStream(url.openStream());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int n = 0;
			while (-1 != (n = in.read(buf))) {
				out.write(buf, 0, n);
			}
			out.close();
			in.close();
			byte[] response = out.toByteArray();
			FileOutputStream fos = new FileOutputStream("C://Users//Admin//Desktop//Documents//Selenium Images//image "
					+ (imageNumber + localNum) + ".jpg");
			fos.write(response);
			fos.close();
			localNum++;
			System.out.println("Saving photo "+(imageNumber + localNum)+"...");
		}
		imageNumber = imageNumber + localNum;
	}
}
