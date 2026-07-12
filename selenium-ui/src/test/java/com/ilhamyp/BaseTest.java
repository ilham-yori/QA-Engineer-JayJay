package com.ilhamyp;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class BaseTest {
    protected static WebDriver driver;

    protected void getDriver() {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--headless");
        WebDriverManager.edgedriver().setup();
        driver = new EdgeDriver(options);
    }
}

