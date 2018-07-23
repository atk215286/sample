public String takeScreenShot(ITestResult testResult) throws IOException {
		File file = null;
		String filePath = null;
		String testMethodName = testResult.getMethod().getMethodName();
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(Calendar.getInstance().getTime());
		if (testResult.getStatus() == ITestResult.FAILURE) {
			File scrFile = ((TakesScreenshot) driver)
					.getScreenshotAs(OutputType.FILE);
			String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar
					.getInstance().getTime());
			file = new File("test-output/Failure_Screenshot/" + date);
			if (!file.exists()) {
				file.mkdir();
			}
			filePath = file + "/" + testMethodName + "_ " + timeStamp + ".jpg";
			FileUtils.copyFile(scrFile, new File(filePath));
		}
		else if (testResult.getStatus() == ITestResult.SUCCESS) {
			File scrFile = ((TakesScreenshot) driver)
					.getScreenshotAs(OutputType.FILE);
			String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar
					.getInstance().getTime());
			file = new File("test-output/Success_Screenshot/" + date);
			if (!file.exists()) {
				file.mkdir();
			}
			filePath = file + "/" + testMethodName + "_ " + timeStamp + ".jpg";
			FileUtils.copyFile(scrFile, new File(filePath));
		}
		return filePath;
	}

	@AfterMethod(alwaysRun = true)
	public void closeBrowser(ITestResult testResult, Method method)
			throws Exception {
		String filePath = takeScreenShot(testResult);
		System.out.println("filePath: " + filePath);
