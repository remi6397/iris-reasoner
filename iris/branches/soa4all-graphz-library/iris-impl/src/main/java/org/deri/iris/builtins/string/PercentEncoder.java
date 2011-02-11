/*
 * Copyright 2011, Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.deri.iris.builtins.string;

import java.util.HashSet;
import java.util.Set;

/**
 * A helper class for percent-encoding. Only those characters are encoded, that
 * are not in the set of unreserved characters. Per default the set of
 * unreserved characters consists of the characters defined in <a
 * href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986</a> section 2.3.
 */
/*
 * This code is partly inspired by the URLEncoder class of the Java API.
 */
public class PercentEncoder {

	private static final int CASE_DIFF = ('a' - 'A');

	private Set<Integer> unreserved;

	private boolean allUnreserved;

	/**
	 * Creates a new instance of PercentEncoder.
	 */
	public PercentEncoder() {
		allUnreserved = false;
		unreserved = new HashSet<Integer>();
		createDefaultUnreserved();
	}

	private void createDefaultUnreserved() {
		for (int i = 'a'; i <= 'z'; i++) {
			unreserve(i);
		}

		for (int i = 'A'; i <= 'Z'; i++) {
			unreserve(i);
		}

		for (int i = '0'; i <= '9'; i++) {
			unreserve(i);
		}

		unreserve('-');
		unreserve('_');
		unreserve('~');
		unreserve('.');
	}

	/**
	 * Puts the given code point in the set of reserved characters.
	 * 
	 * @param codePoint The code point to be put in the set of reserved
	 *            characters.
	 */
	public void reserve(int codePoint) {
		if (unreserved.remove(codePoint)) {
			allUnreserved = false;
		}
	}

	/**
	 * Puts the given character in the set of reserved characters.
	 * 
	 * @param character The character to be put in the set of reserved
	 *            characters.
	 */
	public void reserve(char character) {
		reserve((int) character);
	}

	/**
	 * Puts all character in the given array of characters in the set of
	 * reserved characters.
	 * 
	 * @param characters The array of characters to be put in the set of
	 *            reserved characters.
	 */
	public void reserve(char[] characters) {
		for (char character : characters) {
			reserve(character);
		}
	}

	/**
	 * Puts all characters in the set of reserved character. After this method
	 * has been called, all characters are percent encoded.
	 */
	public void reserveAll() {
		unreserved.clear();
		allUnreserved = false;
	}

	/**
	 * Puts the given code point in the set of unreserved characters.
	 * 
	 * @param codePoint The code point to be put in the set of unreserved
	 *            characters.
	 */
	public void unreserve(int codePoint) {
		unreserved.add(codePoint);
	}

	/**
	 * Puts the given character in the set of unreserved characters.
	 * 
	 * @param codePoint The character to be put in the set of unreserved
	 *            characters.
	 */
	public void unreserve(char character) {
		unreserve((int) character);
	}

	/**
	 * Puts all character in the given array of characters in the set of
	 * unreserved characters.
	 * 
	 * @param characters The array of characters to be put in the set of
	 *            unreserved characters.
	 */
	public void unreserve(char[] characters) {
		for (char character : characters) {
			unreserve(character);
		}
	}

	/**
	 * Puts all characters in the set of unreserved character. After this method
	 * has been called, no characters are percent encoded.
	 */
	public void unreserveAll() {
		allUnreserved = true;
	}

	/**
	 * Percent-encodes the given string according to the sets of reserved and
	 * unreserved characters.
	 * 
	 * @param string The String to be percent-encoded.
	 * @return The percent-encoded String.
	 */
	public String encode(String string) {
		if (allUnreserved) {
			return string;
		}

		StringBuffer buffer = new StringBuffer();

		for (int i = 0; i < string.length(); i++) {
			int codePoint = string.codePointAt(i);
			char[] chars = Character.toChars(codePoint);
			String temp = new String(chars);

			if (unreserved.contains(codePoint)) {
				buffer.append(temp);
				continue;
			}

			byte[] bytes = temp.getBytes();

			for (byte value : bytes) {
				buffer.append("%");

				char character = Character.forDigit((value >> 4) & 0xF, 16);
				if (Character.isLetter(character)) {
					character -= CASE_DIFF;
				}

				buffer.append(character);

				character = Character.forDigit(value & 0xF, 16);
				if (Character.isLetter(character)) {
					character -= CASE_DIFF;
				}

				buffer.append(character);
			}
		}

		return buffer.toString();
	}
}
