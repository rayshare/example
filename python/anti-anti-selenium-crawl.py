from selenium import webdriver
import os
import json
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager
import time


def get_chromedriver_path():
    path_file = "chromedriver_path.json"
    if os.path.exists(path_file):
        with open(path_file, "r") as file:
            data = json.load(file)
            return data.get("path")
    chrome_driver_path = ChromeDriverManager().install()
    with open(path_file, "w") as file:
        json.dump({"path": chrome_driver_path}, file)
    return chrome_driver_path

print("auto config")
driver_path = get_chromedriver_path()
print(driver_path)
service = Service(driver_path)

options = webdriver.ChromeOptions()
options.add_experimental_option('useAutomationExtension', False)
options.add_experimental_option('excludeSwitches', ['enable-automation'])
options.add_argument('disable-blink-features')
options.add_argument('start-maximized')
options.add_argument('disable-blink-features=AutomationControlled')

driver = webdriver.Chrome(service=service, options=options)
# driver.execute_cdp_cmd(
#     "Page.addScriptToEvaluateOnNewDocument",
#     {"source": "Object.defineProperty(navigator, 'webdriver', {get: () => undefined})"},
# )

driver.get("https://bot.sannysoft.com/")
time.sleep(600)
driver.quit()
