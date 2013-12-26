package com.matejdro.pebblenotificationcenter.util;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.support.v4.text.BidiFormatter;
import android.util.Log;

import com.matejdro.pebblenotificationcenter.PebbleNotificationCenter;
import com.matejdro.pebblenotificationcenter.notifications.NotificationHandler;


public class TextUtil {	
	public static String prepareString(String text)
	{
		return prepareString(text, 20);
	}

	public static String prepareString(String text, int length)
	{		
		text = fixInternationalAndTrim(text, length);
		text = reverseRtlStrings(text);
		return trimString(text, length, true);
	}

	public static String reverseRtlStrings(String text){
        Pattern heb_re = Pattern.compile("([א-ת][^a-zA-Z0-9]+)");
        Matcher matcher = heb_re.matcher(text);
        String str = text;
        
        while (matcher.find()) {
        	str = reverseSubString(str, matcher.start(), matcher.end());
        }
        return str;
	}

	public static String reverseSubString(String text, int start, int end){
		String str = new StringBuilder(text.substring(start, end)).reverse().toString();
		if (str.charAt(0) == ' ') {
			str = str.substring(1) + " ";
		}
		return text.substring(0,start) + str + text.substring(end);
	}
	
	public static String fixInternationalAndTrim(String text, int length)
	{
		StringBuilder builder = new StringBuilder(length);

		length = Math.min(length, text.length());

		HashMap<Character, String> replacementTable = PebbleNotificationCenter.getInMemorySettings().getReplacingStrings();

		for (int i = 0; i < length; i++)
		{
			char ch = text.charAt(i);

			String replacement = replacementTable.get(ch);
			if (replacement != null)
			{
				builder.append(replacement);
			}
			else
			{
				builder.append(ch);
			}
		}

		return builder.toString();		
	}

	public static String trimString(String text)
	{
		return trimString(text, 20, true);
	}

	public static String trimString(String text, int length, boolean trailingElipsis)
	{
		if (text == null)
			return null;

		int targetLength = length;
		if (trailingElipsis)
		{
			targetLength -= 3;
		}

		if (text.getBytes().length > length)
		{
			if (text.length() > targetLength)
				text = text.substring(0, targetLength);

			while (text.getBytes().length > targetLength)
			{
				text = text.substring(0, text.length() - 1);
			}

			if (trailingElipsis)
				text = text + "...";

		}

		return text;

	}
} 