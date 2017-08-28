package fb;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class App {

	public static void main(String[] args) throws IOException {

		System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriver\\chromedriver.exe");

		Scanner scanner = new Scanner(System.in);
		System.out.println("Email: ");
		String emailInput = scanner.next();
		System.out.println("Passcode: ");
		String passInput = scanner.next();
		scanner.close();

		WebDriver driver = new ChromeDriver();
		driver.get("https://www.facebook.com/");
		WebElement email = driver.findElement(By.name("email"));
		WebElement pass = driver.findElement(By.name("pass"));
		email.sendKeys(emailInput);
		pass.sendKeys(passInput);
		WebElement login = driver.findElement(By.id("loginbutton"));
		login.click();
		WebElement overlay = driver.findElement(By.className("_3ixn"));
		overlay.click();
		WebElement profile = driver.findElement(By.className("_2s25"));
		profile.click();
		WebElement name = (new WebDriverWait(driver, 10))
				.until(ExpectedConditions.presenceOfElementLocated(By.id("fb-timeline-cover-name")));
		System.out.println(name.getText());
		WebElement thumb = driver.findElement(By.className("profilePicThumb"));
		thumb.click();
		try {
			Thread.sleep(3000);
			// After clicking on the profile pic thumbnail, a gif plays, then
			// the image is displayed. If src = pic.getAttribute("src");
			// executes too quickly, the src of the gif will be used instead of
			// the image src, resulting in a blank icon.
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		WebElement pic = (new WebDriverWait(driver, 10))
				.until(ExpectedConditions.presenceOfElementLocated(By.className("spotlight")));
		String src = null;

		while (true) {
			try {
				src = pic.getAttribute("src");
				break;
			} catch (StaleElementReferenceException e) {
				pic = driver.findElement(By.className("spotlight"));

			}
		}
		URL srcF = new URL(src);
		// System.out.println(srcF);
		BufferedImage image = new BufferedImage(403, 403, BufferedImage.TYPE_4BYTE_ABGR);
		image = ImageIO.read(srcF);
		ImageIcon icon = new ImageIcon();
		icon.setImage(image);
		JOptionPane.showMessageDialog(null, icon);

	}

}
