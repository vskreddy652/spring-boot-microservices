package com.eg.mod.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MessageService {

	public static void main(String[] args) {
		MessageService ms = new MessageService();
		ms.sendSms("Hi Arnab how are you", "919434580584", "919434580584");
	}

	public String sendSms(String message, String sender, String numbers) {
		try {
			// Construct data
			String apiKey = "apikey=" + "A10QG2Yc57I-ntFL1cT4zSEOmIhZYdCiFDvgiGyCC0";
			message = "&message=" + message;
			sender = "&sender=" + "TXTLCL";
			numbers = "&numbers=" + numbers; // "918123456789"

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
			System.out.println(stringBuffer.toString());
			return stringBuffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error SMS " + e);
			return "Error " + e;
		}
	}
}
