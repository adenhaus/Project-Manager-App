/**
 * Project class
 * 
 * This class manages the fields and methods regarding projects
 *
 * @author Aden Haussmann
 * @version 2.00
 * @since 15 Jan 2020
 */
public class Project {
	
	// Attributes
	private int projectNumber;
	private String projectName;
	private String buildingType;
	private String physicalAddress;
	private int erfNumber;
	private double totalFee;
	private double amountPaid;
	private String deadline;
	private Person architect;
	private Person contractor;
	private Person customer;
	private String finalised;
	
	// Methods
	public Project(int projectNumber, String projectName, 
			String buildingType, String physicalAddress, int erfNumber, 
			double totalFee, double amountPaid, String deadline, Person architect, 
			Person contractor, Person customer, String finalised) {
		this.projectNumber = projectNumber;
		this.projectName = projectName;
		this.buildingType = buildingType;
		this.physicalAddress = physicalAddress;
		this.erfNumber = erfNumber;
		this.totalFee = totalFee;
		this.amountPaid = amountPaid;
		this.deadline = deadline;
		this.architect = architect;
		this.contractor = contractor;
		this.customer = customer;
		this.finalised = finalised;
	}
	
	public Project() {};
	
	public String toString() {
		String output = "Project Number: " + projectNumber;
		output += "\nProject Name: " + projectName;
		output += "\nBuilding Type: " + buildingType;
		output += "\nPhysical Address: " + physicalAddress;
		output += "\nERF Number: " + erfNumber;
		output += "\nTotal Fee: " + totalFee;
		output += "\nAmount Paid: " + amountPaid;
		output += "\nDeadline: " + deadline;
		output += "\nArchitect: " + architect.getName();
		output += "\nContractor: " + contractor.getName();
		output += "\nCustomer: " + customer.getName();
		output += "\nStatus: " + finalised;
		
		return output;
	}

	public Person getPerson(String personType) {
		if (personType.equals("architect")) {
			return architect;
		} else if (personType.equals("customer")) {
			return customer;
		} else {
			return contractor;
		}
		
	}
	
	public int getProjectNumber() {
		return projectNumber;
	}

	public String getProjectName() {
		return projectName;
	}

	public String getBuildingType() {
		return buildingType;
	}

	public void setBuildingType(String buildingType) {
		this.buildingType = buildingType;
	}

	public String getPhysicalAddress() {
		return physicalAddress;
	}

	public void setPhysicalAddress(String physicalAddress) {
		this.physicalAddress = physicalAddress;
	}

	public int getErfNumber() {
		return erfNumber;
	}

	public void setErfNumber(int erfNumber) {
		this.erfNumber = erfNumber;
	}

	public double getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(double totalFee) {
		this.totalFee = totalFee;
	}

	public double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public String getFinalised() {
		return finalised;
	}

	public void setFinalised(String finalised) {
		this.finalised = finalised;
	}
	
}
