package com.eg.mod.service;

import static com.eg.mod.model.Constants.SMS_APK_KEY;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

@Component
public class SMSService {

	protected final Log logger = LogFactory.getLog(getClass());

	
	public static void main(String[] args) {
		SMSService ms = new SMSService();
		ms.sendSms("Hi Arnab how are you", "9434580584", "9434580584");
	}
	 

	public void sendSms(String message, String sender, String numbers) {

		try {
			// Construct data
			String apiKey = "apikey=" + SMS_APK_KEY;
			message = "&message=" + message;
			sender = "&sender=" + "TXTLCL";
			numbers = "&numbers=" + "91" + numbers;

			// Send data
			HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
			String data = apiKey + numbers + message + sender;
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
			conn.getOutputStream().write(data.getBytes("UTF-8"));
			final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			final StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				stringBuffer.append(line);
			}
			rd.close();

			logger.info(stringBuffer.toString());
			if (stringBuffer.indexOf("errors") == -1)
				logger.info("SMS Sent Successfully");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
