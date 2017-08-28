package main;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
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
		int x = 1;
		driver.get("https://warthunder.com/en/media/screenshots/");
		WebElement next = driver.findElement(By.className("next"));

		downloader(driver, obj, x, list);
		next.click();
		downloader(driver, obj, x, list);
		while (true) {
			try {
				next.click();
				break;
			} catch (StaleElementReferenceException e) {
				next = driver.findElement(By.className("next"));
			}
		}
		downloader(driver, obj, x, list);

		try {
			obj.put("src", list);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		FileWriter fw = new FileWriter(new File("C://Users//Admin//Desktop//Documents//JSON File", "image"));
		fw.write(obj.toString());
		fw.close();

	}

	static void downloader(WebDriver driver, JSONObject obj, int x, JSONArray list) throws IOException {
		List<WebElement> screens = driver.findElements(By.tagName("pic"));
		Iterator<WebElement> iter = screens.iterator();
		while (iter.hasNext()) {
			WebElement pic = iter.next();
			String src = pic.getAttribute("src");
			list.put(src);
			URL url = new URL(src);
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
			FileOutputStream fos = new FileOutputStream(
					"C://Users//Admin//Desktop//Documents//Selenium Images//image " + x + ".jpg");
			fos.write(response);
			fos.close();
			x++;
		}

	}
}
