package ru.kalmykov.whatsappsender.service;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.SessionStorage;
import org.openqa.selenium.html5.WebStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@ParametersAreNonnullByDefault
public class ChromeWebDriver implements WebDriver {

    private static final String CHROME_DRIVER = "webdriver.chrome.driver";

    private final WebDriver webDriver;
    private final JavascriptExecutor jse;

    public ChromeWebDriver(
            @Value("${chrome-web-driver.driver-path}") String driverPath
    ) {
        System.setProperty(CHROME_DRIVER, driverPath);
        this.webDriver = new ChromeDriver(new ChromeOptions());
        this.webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        this.jse = (JavascriptExecutor) this.webDriver;
    }

    @Override
    public void get(String url) {
        webDriver.get(url);
    }

    @Override
    public String getCurrentUrl() {
        return webDriver.getCurrentUrl();
    }

    @Override
    public String getTitle() {
        return webDriver.getTitle();
    }

    @Override
    public List<WebElement> findElements(By by) {
        return webDriver.findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return webDriver.findElement(by);
    }

    @Override
    public String getPageSource() {
        return webDriver.getPageSource();
    }

    @Override
    public void close() {
        webDriver.close();
    }

    @Override
    public void quit() {
        webDriver.quit();
    }

    @Override
    public Set<String> getWindowHandles() {
        return webDriver.getWindowHandles();
    }

    @Override
    public String getWindowHandle() {
        return webDriver.getWindowHandle();
    }

    @Override
    public TargetLocator switchTo() {
        return webDriver.switchTo();
    }

    @Override
    public Navigation navigate() {
        return webDriver.navigate();
    }

    @Override
    public Options manage() {
        return webDriver.manage();
    }

    public LocalStorage getLocalStorage() {
        return ((WebStorage) this.webDriver).getLocalStorage();
    }

    public SessionStorage getSessionStorage() {
        return ((ChromeDriver) this.webDriver).getSessionStorage();
    }

    public void executeScript(String jsScript) {
        jse.executeScript(jsScript);
    }
}
