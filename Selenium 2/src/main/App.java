package main;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;



public class App {

	public static void main(String[] args) throws IOException {
		// save pic to desktop, create JSON file with username and img src
		System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriver\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		JSONObject obj = new JSONObject();
		int x = 1;
		driver.get("http://writingexercises.co.uk/random-images.php");
		
		for(int i=0; i<10; i++){
			downloader(driver, obj, x);
		}
		
		FileWriter fw = new FileWriter(new File("C://Users//Admin//Desktop//Documents//JSON File", "randomImage"));
		fw.write(obj.toString());
		fw.close();
		

	}

	static void downloader(WebDriver driver, JSONObject obj, int x) throws IOException{
		WebElement pic = driver.findElement(By.cssSelector("#imageout > img"));
		String src = pic.getAttribute("src");
		try {
			obj.put("src", src);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		URL url = new URL(src);
		InputStream in = new BufferedInputStream(url.openStream());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int n = 0;
		while (-1!=(n=in.read(buf)))
		{
		   out.write(buf, 0, n);
		}
		out.close();
		in.close();
		byte[] response = out.toByteArray();
		FileOutputStream fos = new FileOutputStream("C://Users//Admin//Desktop//Documents//Selenium Images//image "+x+".jpg");
		fos.write(response);
		fos.close();
		x++;
		WebElement button = driver.findElement(By.className("button"));
		button.click();
	}
}
