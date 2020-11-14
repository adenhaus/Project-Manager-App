/**
 * Person class
 * 
 * This class manages the fields and methods regarding people
 *
 * @author Aden Haussmann
 * @version 2.00
 * @since 15 Jan 2020
 */
public class Person {
	
	// Attributes
	private String personType;
	private String name;
	private String phoneNumber;
	private String emailAddress;
	private String physicalAddress;
	
	// Methods
	public Person(String personType, String name, String phoneNumber, String emailAddress, String physicalAddress) {
		this.personType = personType;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
		this.physicalAddress = physicalAddress;
	}
	
	public Person() {};
	
	public String toString() {
		String output = "Role: " + personType;
		output += "\nName: " + name;
		output += "\nPhone Number: " + phoneNumber;
		output += "\nEmail Address: " + emailAddress;
		output += "\nPhysical Address: " + physicalAddress;
				
		return output;
	}

	public String getPersonType() {
		return personType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPhysicalAddress() {
		return physicalAddress;
	}

	public void setPhysicalAddress(String physicalAddress) {
		this.physicalAddress = physicalAddress;
	}
	
	

}
