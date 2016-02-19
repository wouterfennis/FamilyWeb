package util.FamilyWeb;

import java.util.Calendar;
import java.util.Date;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class Validation {
	private static final String FORENAME_PATTERN = "[A-Z][a-zA-Z]*";
	private static final String SURNAME_PATTERN = "[a-zA-z]+([ '-][a-zA-Z]+)*";
	private static final String USERNAME_PATTERN = "^[a-z0-9_-]{3,15}$";
	private static final String LETTER_PATTERN = "([a-zA-Z]*(\\s)*)*";
	private static final String NL_POSTCODE_PATTERN = "^[1-9][0-9]{3}[\\s]?[A-Za-z]{2}$";
	private static final String NL_PHONENUMBER = "^(((0)[1-9]{2}[0-9][-]?[1-9][0-9]{5})|((\\\\+31|0|0031)[1-9][0-9][-]?[1-9][0-9]{6}))$";
	private static final String NL_MOBILEPHONENUMBER = "^(((\\+31|0|0031)6){1}[1-9]{1}[0-9]{7})$";
	private static final String LETTER_NUMBER_WHITESPACE = "([ ]*+[0-9A-Za-z]++[ ]*+)+";

	private static Validation vd;

	public Validation(){
		vd = this;
	}

	public static Validation getInstance() {
		if(vd == null){
			vd = new Validation();
		}
		return vd;
	}

	public String validateEmployeeNumber(String input) {
		//check if not null or empty
		if (input != null && !input.trim().equals("")) {
			return input.trim();
		} else {
			return null;
		}
	}

	public String validateUsername(String input) {
		//check if not null or empty
		if (input != null && !input.trim().equals("")) {
			String username = input.trim();
			// check if matches the regex: "^[a-z0-9_-]{3,15}$"
			if (username.matches(USERNAME_PATTERN)) {
				return username;
			} else {
				return null;
			}
		} else {
			return null;
		}

	}

	public String validateForename(String input) {
		// check null or empty
		if (input != null && !input.trim().equals("")) {
			String forename = input.trim().substring(0, 1).toUpperCase()
					+ input.toLowerCase().trim().substring(1);
			// check if matches the regex: "[A-Z][a-zA-Z]*" and max length of 35
			if (forename.matches(FORENAME_PATTERN) && forename.length() <= 35) {
				return forename;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public String validateSurname(String input) {
		// check null or empty
		if (input != null && !input.trim().equals("")) {
			String surname = input.trim();
			// check if matches the regex: "[a-zA-z]+([ '-][a-zA-Z]+)*" and max length of 35
			if (surname.matches(SURNAME_PATTERN) && surname.length() <= 35) {
				return surname;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public Date validateDateOfBirth(String inputDay, String inputMonth, String inputYear) {

		if (inputDay != null && inputMonth != null && inputYear != null) {

			if (!inputDay.trim().equals("") && !inputMonth.trim().equals("") && !inputYear.trim().equals("")) {
				try {
					int day = Integer.valueOf(inputDay);
					int month = Integer.valueOf(inputMonth);
					int year = Integer.valueOf(inputYear);

					if (year > 0 && month > 0 && day > 0) {
						Calendar cal = Calendar.getInstance();
						cal.set(year, month - 1, day, 0, 0, 0);

						if (cal.before(Calendar.getInstance())) {
							return cal.getTime();
						} else {
							return null;
						}
					} else {
						return null;
					}
				} catch (NumberFormatException e) {
					return null;
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public String validatePostcode(String input) {
		if (input != null && !input.trim().equals("")) {
			String postcode = input.trim();
			if (postcode.matches(NL_POSTCODE_PATTERN)) {
				return postcode;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public String validateStreet(String input) {
		if (input != null && !input.trim().equals("")) {
			String street = input.trim();
			if (street.matches(LETTER_PATTERN) && street.length() <= 35) {
				return street;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public String validateCity(String input) {
		if (input != null && !input.trim().equals("")) {
			String city = input.trim();
			if (city.matches(LETTER_PATTERN) && city.length() <= 50) {
				return city;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public String validateNationality(String input) {
		if (input != null && !input.trim().equals("")) {
			String nationality = input.trim();
			if (nationality.matches(LETTER_PATTERN)
					&& nationality.length() <= 50) {
				return nationality;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public String validateMobilePhoneNumber(String input) {
		if (input != null && !input.trim().equals("")) {
			String mobilePhoneNumber = input.trim();
			if (mobilePhoneNumber.matches(NL_MOBILEPHONENUMBER)) {
				return mobilePhoneNumber;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public String validateEmail(String input, String input2) {

		if (input != null && input2 != null & !input.trim().equals("") && !input2.trim().equals("")) {

			String email = input.trim();
			String email_confirmation = input2.trim();

			if (email.equals(email_confirmation)) {
				try {
					InternetAddress emailAddr = new InternetAddress(email);
					emailAddr.validate();
					return email;
				} catch (AddressException ex) {
					return null;
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public String validateHouseNumber(String input) {
		if (input != null && !input.trim().equals("")) {
			String houseNumber = input.trim();
			if (houseNumber.matches(LETTER_NUMBER_WHITESPACE) && houseNumber.length() <= 10) {
				return houseNumber;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public String validateTelephoneNumber(String input) {
		if (input != null && !input.trim().equals("")) {
			String telephoneNumber = input.trim();
			if (telephoneNumber.matches(NL_PHONENUMBER)) {
				return telephoneNumber;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}
