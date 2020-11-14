import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Main class
 * 
 * This is the main class for the Poised program. It facilitates project management for a
 * structural engineering company, by allowing the creation of new projects, editing of projects,
 * managing customers and employees, viewing incomplete projects and generating invoices.
 *
 * @author Aden Haussmann
 * @version 2.00
 * @since 15 Jan 2020
 */
public class Poised {

	public static void main(String[] args) {
		
		// Create scanner
		// I never use nextInt or nextDouble in the program, as they cause the scanner
		// to skip lines. I always use nextLine and convert to an integer or double.
		Scanner s = new Scanner(System.in);
		
		// Create empty Project objects that will hold project information later
		Project newProject = new Project();
		Project oldProject = new Project();
		
		// Create project and people hash maps
		HashMap<String, Project> projects = new HashMap<String, Project>();
		HashMap<String, Project> completedProjects = new HashMap<String, Project>();
		HashMap<String, Person> people = new HashMap<String, Person>();
		
		// Get Person information from file
		String peopleInfo = "";
		List<String> tempPeople = new ArrayList<String>();
		// Open and read people.txt file
		try {
			File inputFile = new File("people.txt");
			Scanner sc = new Scanner(inputFile);
			while (sc.hasNext()) {
				peopleInfo += sc.nextLine() + "\n";
			}
			sc.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("Error: File not found");
		}
		try {
			// Create array of person strings
			tempPeople = Arrays.asList(peopleInfo.split("\n\n"));
			String[] peopleArr = tempPeople.toArray(new String[tempPeople.size()]);
			// Create Person objects
			for (int i = 0; i < peopleArr.length; i++) {
				// Make a string array of each Person
				String[] lines = peopleArr[i].split("\n");
				// Set variables
				String oldPersonType = lines[0];
				String oldName = lines[1];
				String oldPhoneNumber = lines[2];
				String oldEmailAddress = lines[3];
				String oldPhysicalAddress = lines[4];
				// Construct Person object
				Person oldPerson = new Person(oldPersonType, oldName, 
						oldPhoneNumber, oldEmailAddress, oldPhysicalAddress);
				// Add person to hash map
				people.put(oldName, oldPerson);
			}
		} catch (IndexOutOfBoundsException e) {
			System.out.println("The file people.txt is corrupted.");
		}
		
		// Get Project information from file
		String projectInfo = "";
		List<String> tempProjects = new ArrayList<String>();
		// Open and read projects.txt file
		try {
			File inputFile = new File("projects.txt");
			Scanner sc = new Scanner(inputFile);
			while (sc.hasNext()) {
				projectInfo += sc.nextLine() + "\n";
			}
			sc.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("Error: File not found");
		}
		try {
			// Create array of project strings
			tempProjects = Arrays.asList(projectInfo.split("\n\n"));
			String[] projectsArr = tempProjects.toArray(new String[tempProjects.size()]);
			// Create Project objects
			for (int i = 0; i < projectsArr.length; i++) {
				// Make a string array of each Project
				String[] lines = projectsArr[i].split("\n");
				// Set variables
				int oldProjectNumber = Integer.parseInt(lines[0]);
				String oldProjectName = lines[1];
				String oldBuildingType = lines[2];
				String oldPhysicalAddress = lines[3];
				int oldErfNumber = Integer.parseInt(lines[4]);
				double oldTotalFee = Double.parseDouble(lines[5]);
				double oldAmountPaid = Double.parseDouble(lines[6]);
				String oldDeadline = lines[7];
				Person oldArchitect = (Person)people.get(lines[8]);
				Person oldContractor = (Person)people.get(lines[9]);
				Person oldCustomer = (Person)people.get(lines[10]);
				String oldFinalised = lines[11];
				// Construct Project object
				oldProject = new Project(oldProjectNumber, oldProjectName, oldBuildingType, oldPhysicalAddress, oldErfNumber, 
						oldTotalFee, oldAmountPaid, oldDeadline, oldArchitect, oldContractor, oldCustomer, oldFinalised);
				// Add project to hash map
				projects.put(oldProjectName, oldProject);
			}
		} catch (IndexOutOfBoundsException e) {
			System.out.println("The file projects.txt is corrupted.");
		} catch (NumberFormatException n) {
			System.out.println("The file projects.txt is corrupted.");
		}
			
		// Get Completed Project information from file
		String completedProjectInfo = "";
		List<String> tempCompletedProjects = new ArrayList<String>();
		// Open and read completedProjects.txt file
		try {
			File inputFile = new File("completedProjects.txt");
			Scanner sc = new Scanner(inputFile);
			while (sc.hasNext()) {
				completedProjectInfo += sc.nextLine() + "\n";
			}
			sc.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("Error: File not found");
		}
		try {
			// Create array of project strings
			tempCompletedProjects = Arrays.asList(completedProjectInfo.split("\n\n"));
			String[] completedProjectsArr = tempCompletedProjects.toArray(new String[tempCompletedProjects.size()]);
			// Create Project objects
			for (int x = 0; x < completedProjectsArr.length; x++) {
				// Make a string array of each Project
				String[] completedLines = completedProjectsArr[x].split("\n");
				// Set variables
				int oldProjectNumber = Integer.parseInt(completedLines[0]);
				String oldProjectName = completedLines[1];
				String oldBuildingType = completedLines[2];
				String oldPhysicalAddress = completedLines[3];
				int oldErfNumber = Integer.parseInt(completedLines[4]);
				double oldTotalFee = Double.parseDouble(completedLines[5]);
				double oldAmountPaid = Double.parseDouble(completedLines[6]);
				String oldDeadline = completedLines[7];
				Person oldArchitect = (Person)people.get(completedLines[8]);
				Person oldContractor = (Person)people.get(completedLines[9]);
				Person oldCustomer = (Person)people.get(completedLines[10]);
				String oldFinalised = completedLines[11];
				// Construct Project object
				oldProject = new Project(oldProjectNumber, oldProjectName, oldBuildingType, oldPhysicalAddress, oldErfNumber, 
						oldTotalFee, oldAmountPaid, oldDeadline, oldArchitect, oldContractor, oldCustomer, oldFinalised);
				// Add completed project to hash map
				completedProjects.put(oldProjectName, oldProject);
			}
		} catch (IndexOutOfBoundsException e) {
			System.out.println("The file projects.txt is corrupted.");
		} catch (NumberFormatException n) {
			System.out.println("The file projects.txt is corrupted.");
		}
		
		// Print software title
		System.out.println("Poised Engineering - Project Manager\n");
		
		// Main loop
		while (true) {
			System.out.println("----------------------------\n"
					+ "n - Add a new project\n"
					+ "e - Edit a project\n"
					+ "v - View all projects\n"
					+ "i - View incomplete projects\n"
					+ "o - View overdue projects\n"
					+ "q - Quit\n"
					+ "----------------------------\n");
			String input = s.nextLine();
			
			if (input.contentEquals("n")) {
				
				// Set project number to one larger than the largest project number already in the system
				// This ensures there will never be duplicate project numbers
				ArrayList<Integer> projNums = new ArrayList<Integer>();
				for (Map.Entry<String, Project> entry : projects.entrySet()) {
					projNums.add(entry.getValue().getProjectNumber());
				}
				int projectNumber = Collections.max(projNums) + 1;
				
				// Set project name
				System.out.println("Project Name: ");
				String projectName = s.nextLine();
				
				// Set building type
				System.out.println("Building Type: ");
				String buildingType = s.nextLine();
				
				// Set physical address
				System.out.println("Physical Address: ");
				String physicalAddress = s.nextLine();
				
				// Set ERF number
				int erfNumber = 0;
				while (true) {
					System.out.println("ERF Number: ");
					try {
						erfNumber = Integer.parseInt(s.nextLine());
						break;
					} catch (NumberFormatException e) {
						System.out.println("Must be an integer");
					}
				}
				
				// Set the total fee
				double totalFee;
				while (true) {
					System.out.println("Total Fee: ");
					try {
						totalFee = Double.parseDouble(s.nextLine());
						break;
					} catch (NumberFormatException e) {
						System.out.println("Must be a number");
					}
				}
				
				// Set the amount paid
				double amountPaid;
				while (true) {
					System.out.println("Amount Paid: ");
					try {
						amountPaid = Double.parseDouble(s.nextLine());
						break;
					} catch (NumberFormatException e) {
						System.out.println("Must be a number");
					}
				}
				
				// Set the date
				String deadline = "";
				while (true) {
					System.out.println("Deadline (format must be dd/mm/yyyy): ");
					deadline = s.nextLine();
					boolean dateValid = isThisDateValid(deadline, "dd/MM/yyyy");
					
					if (dateValid) {
						break;
					} else {
						System.out.println("Not a valid date.");
					}
				}
				
				String customerFirstName = "";
				String customerSecondName = "";
				
				// Create empty people objects
				Person customer = new Person();
				Person architect = new Person();
				Person contractor = new Person();
				
				// Assign a customer to the project
				while (true) {
					System.out.println("e - Existing customer\nn - New customer");
					String customerChoice = s.nextLine();
					
					if (customerChoice.equals("n")) {
						
						// Create a new customer Person object
						System.out.println("Customer First Name: ");
						customerFirstName = s.nextLine();
						
						System.out.println("Customer Second Name: ");
						customerSecondName = s.nextLine();
						
						System.out.println("Customer Phone Number: ");
						String customerPhone = s.nextLine();
						
						System.out.println("Customer Email Address: ");
						String customerEmail = s.nextLine();
						
						System.out.println("Customer Physical Address: ");
						String customerAddress = s.nextLine();
						
						String personType = "customer";
						
						// Construct the customer Person object
						customer = new Person(personType, customerFirstName + " " + customerSecondName, 
								customerPhone, customerEmail, customerAddress);
						
						// Add person to People hash map
						people.put(customer.getName(), customer);
						break;
						
					} else if (customerChoice.equals("e")) {
						
						// Choose an existing customer
						customer = assignPerson("customer", people);
						break;
						
					} else {
						System.out.println("That is not a valid input");
					}
					
				}
				
				// Choose an architect
				architect = assignPerson("architect", people);
				
				// Choose a contractor
				contractor = assignPerson("contractor", people);
				
				// Set finalised to Uncompleted by default.
				String finalised = "Uncompleted";
				
				// Set project name if one is not entered
				if (projectName.equals("")) {
					projectName = buildingType + " " + customerSecondName;
				}
				
				// Ensure there is never a duplicate project name
				ArrayList<String> projNames = new ArrayList<String>();
				for (Map.Entry<String, Project> entry : projects.entrySet()) {
					projNames.add(entry.getValue().getProjectName());
				}
				boolean alreadyExists = projNames.contains(projectName);
				if (alreadyExists) {
					projectName = projectName + " (2)";
				}
				
				// Construct the Project object
				newProject = new Project(projectNumber, projectName, buildingType, physicalAddress, erfNumber, 
						totalFee, amountPaid, deadline, architect, contractor, customer, finalised);
				
				// Print the newly-created project
				System.out.println("\nYou just created the following project:\n");
				String project = newProject.toString();
				System.out.println(project + "\n");
				
				// Add project to hash map
				projects.put(projectName, newProject);
				
			} else if (input.equals("e")) {
				
				// Construct an empty Project object to hold the project the user will choose
				Project editProject = new Project();
				
				// Let user choose to select by number or name
				while (true) {
					System.out.println("\nThe projects in the system are:\n" 
							+ projects.keySet().toString() + "\n\nno - Select by number\n"
							+ "na - Select by name");
					String projectSelect = s.nextLine();
					
					if (projectSelect.equals("no")) {
						
						// Select by number
						while (true) {
							System.out.println("Type the number of the project to select it:");
							// Variable to hold the number of each project
							int tempNum = 0;
							// The project number the user enters
							int projNum = Integer.parseInt(s.nextLine());
							// See if the user's number matches a project number
							for (Map.Entry<String, Project> entry : projects.entrySet()) {
								// Set the project number
								tempNum = entry.getValue().getProjectNumber();
								// Check if equal
								if (tempNum == projNum) {
									editProject = entry.getValue();
									break;
								}
							}
							if (tempNum == projNum) {
								break;
							} else {
								System.out.println("That is not a valid project.");
							}
						}
						break;
						
					} else if (projectSelect.equals("na")) {
						
						// Select by name
						while (true) {
							System.out.println("Type the name of the project to select it:");
							// Variable to hold the name of each project
							String tempName = "";
							// The project name the user enters
							String projName = s.nextLine();
							// See if the user's name matches a project name
							for (Map.Entry<String, Project> entry : projects.entrySet()) {
								// Set the project name
								tempName = entry.getKey();
								// Check if equal
								if (tempName.equals(projName)) {
									editProject = entry.getValue();
									break;
								}
							}
							if (tempName.equals(projName)) {
								break;
							} else {
								System.out.println("That is not a valid project.");
							}
						}
						break;
						
					} else {
						System.out.println("That is not a valid option.");
					}
				}
				
				// Edit information
				while (true) {
					System.out.println("\nChoose what to edit\n"
							+ "d - Deadline\n"
							+ "a - Aount Paid\n"
							+ "c - Somebody's Contact Details\n"
							+ "t - Total fee\nb - Building type\n"
							+ "p - Physical Address\nf - Finalise\n"
							+ "e - ERF number\n"
							+ "q - Exit to main menu");
					String editOption = s.nextLine();
					
					if (editOption.equals("d")) {
						
						// Edit deadline
						if (editProject.getFinalised().equals("Uncompleted")) {
							System.out.println("Current deadline: " + editProject.getDeadline());
							String newDate = "";
							while (true) {
								System.out.println("Enter new deadline (format must be dd/mm/yyyy): ");
								newDate = s.nextLine();
								boolean dateValid = isThisDateValid(newDate, "dd/MM/yyyy");
								if (dateValid) {
									break;
								} else {
									System.out.println("Not a valid date.");
								}
							}
							editProject.setDeadline(newDate);
							System.out.println("New deadline set to: " + editProject.getDeadline());
						} else {
							System.out.println("You cannort edit this field as this project has already been finalised.");
						}
						
					} else if (editOption.equals("a")) {
						
						// Edit amount paid
						if (editProject.getFinalised().equals("Uncompleted")) {
							System.out.println("Current amount paid: R" + editProject.getAmountPaid());
							// Set the new amount paid
							double newAmount;
							while (true) {
								System.out.println("Enter new amount:");
								try {
									newAmount = Double.parseDouble(s.nextLine());
									break;
								} catch (NumberFormatException e) {
									System.out.println("Must be a number");
								}
							}
							editProject.setAmountPaid(newAmount);
							System.out.println("New amount paid set to: " + editProject.getAmountPaid());
						} else {
							System.out.println("You cannort edit this field as this project has already been finalised.");
						}
						
					} else if (editOption.equals("c")) {
						
						// Edit contact details
						while (true) {
							System.out.println("co - Contractor\na - Architect\ncu - customer");
							String personChoice = s.nextLine();
							if (personChoice.equals("co")) {
								changeDetails(editProject, "contractor");
							} else if (personChoice.equals("cu")) {
								changeDetails(editProject, "customer");
							} else if (personChoice.equals("a")) {
								changeDetails(editProject, "architect");
							} else {
								System.out.println("That is not a valid person.");
							}
						}
						
					} else if (editOption.equals("f")) {
						
						// Finalise project
						if (editProject.getFinalised().equals("Uncompleted")) {
							if (editProject.getAmountPaid() == editProject.getTotalFee()) {
								System.out.println("Date of completion: ");
								String completionDate = s.nextLine();
								editProject.setFinalised("Finalised on " + completionDate);
								System.out.println("The full amount has already been paid so no invoice will be generated.");
							} else {
								System.out.println("Date of completion: ");
								String completionDate = s.nextLine();
								editProject.setFinalised("Finalised on " + completionDate);
								String invoice = "Invoice\n" + editProject.getPerson("customer").getName() + "\n" + 
										editProject.getPerson("customer").getEmailAddress() + "\n" 
										+ editProject.getPerson("customer").getPhoneNumber() + "\n" + 
										editProject.getPerson("customer").getPhysicalAddress() + "\nOutstanding balance: R" 
										+ (editProject.getTotalFee() - editProject.getAmountPaid());
								System.out.println(invoice);
							}
							completedProjects.put(editProject.getProjectName(), editProject);
						} else {
							System.out.println("This project is already completed.");
						}
						
					} else if (editOption.equals("t")) {
						
						// Edit total fee
						if (editProject.getFinalised().equals("Uncompleted")) {
							System.out.println("Current total fee: R" + editProject.getTotalFee());
							// Set the new total fee
							double newTotal;
							while (true) {
								System.out.println("Enter new total:");
								try {
									newTotal = Double.parseDouble(s.nextLine());
									break;
								} catch (NumberFormatException e) {
									System.out.println("Must be a number");
								}
							}
							editProject.setTotalFee(newTotal);
							System.out.println("New total fee set to: " + editProject.getTotalFee());
						} else {
							System.out.println("You cannort edit this field as this project has already been finalised.");
						}
						
					} else if (editOption.equals("p")) {
						
						// Edit physical address
						System.out.println("Current physical address: " + editProject.getPhysicalAddress() + "\nEnter new address:");
						String newAddress = s.nextLine();
						editProject.setPhysicalAddress(newAddress);
						System.out.println("New address set to: " + editProject.getPhysicalAddress());
						
					} else if (editOption.equals("b")) {
						
						// Edit building type
						System.out.println("Current building type: " + editProject.getBuildingType() + "\nEnter new type:");
						String newType = s.nextLine();
						editProject.setBuildingType(newType);
						System.out.println("New address set to: " + editProject.getBuildingType());
						
					} else if (editOption.equals("e")) {
						
						// Edit ERF number
						System.out.println("Current ERF:" + editProject.getErfNumber());
						int newErf;
						while (true) {
							System.out.println("Enter new ERF:");
							try {
								newErf = Integer.parseInt(s.nextLine());
								break;
							} catch (NumberFormatException e) {
								System.out.println("Must be a number");
							}
						}
						editProject.setErfNumber(newErf);
						System.out.println("New ERF set to: " + editProject.getErfNumber());
						
					} else if (editOption.equals("q")) {
						// Exit to main menu
						break;
						
					} else {
						System.out.println("That is not a valid input.");
					}
				}
				
			} else if (input.equals("v")) {
				
				// View all projects
				for (Map.Entry<String, Project> entry : projects.entrySet()) {
					System.out.println(entry.getValue().toString() + "\n");
				}
				
			} else if (input.equals("o")) {
				
				// View all overdue projects
				System.out.println("Overdue projects:");
				for (Map.Entry<String, Project> entry : projects.entrySet()) {
					String dueDateStr = entry.getValue().getDeadline();
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					sdf.setLenient(false);
					Date dueDate = Calendar.getInstance().getTime();
					try {
						dueDate = sdf.parse(dueDateStr);
					} catch (ParseException e) {
						System.out.println("No valid date.");
					}
					Date todayDate = Calendar.getInstance().getTime();
					if (todayDate.compareTo(dueDate) < 0 && entry.getValue().getFinalised().equals("Uncompleted")) {
						System.out.println(entry.getKey() + "\n");
					}
				}
				
			} else if (input.equals("i")) {
				
				// View all incomplete projects
				System.out.println("Incomplete projects:");
				for (Map.Entry<String, Project> entry : projects.entrySet()) {
					if (entry.getValue().getFinalised().equals("Uncompleted")) {
						System.out.println(entry.getKey());
					}
				}
				System.out.println("");
				
			} else if (input.equals("q")) {
				// Clear all files
				try {
					String filename = "people.txt";
				    FileWriter fw = new FileWriter(filename);
				    fw.write("");
				    fw.close();
				    
				    String filename2 = "projects.txt";
				    FileWriter fw2 = new FileWriter(filename2);
				    fw2.write("");
				    fw2.close();
				    
				    String filename3 = "completedProjects.txt";
				    FileWriter fw5 = new FileWriter(filename3);
				    fw5.write("");
				    fw5.close();
				} catch (IOException ioe) {
					System.err.println("IOException: " + ioe.getMessage());
				}
				// Write people to file
				for (Map.Entry<String, Person> entry : people.entrySet()) {
					try {
						String filename = "people.txt";
					    FileWriter fw3 = new FileWriter(filename, true);
					    fw3.write(entry.getValue().getPersonType() + "\n"
					    		+ entry.getValue().getName() + "\n"
					    		+ entry.getValue().getPhoneNumber() + "\n"
					    		+ entry.getValue().getEmailAddress() + "\n"
					    		+ entry.getValue().getPhysicalAddress() + "\n\n");
					    fw3.close();
					}
					catch(IOException ioe) {
					    System.err.println("IOException: " + ioe.getMessage());
					}
				}
				// Write projects to file
				for (Map.Entry<String, Project> entry : projects.entrySet()) {
					try {
						String filename2 = "projects.txt";
					    FileWriter fw4 = new FileWriter(filename2, true);
					    fw4.write(entry.getValue().getProjectNumber() + "\n"
					    		+ entry.getValue().getProjectName() + "\n"
					    		+ entry.getValue().getBuildingType() + "\n"
					    		+ entry.getValue().getPhysicalAddress() + "\n"
					    		+ entry.getValue().getErfNumber() + "\n"
					    		+ entry.getValue().getTotalFee() + "\n"
					    		+ entry.getValue().getAmountPaid() + "\n"
					    		+ entry.getValue().getDeadline() + "\n"
					    		+ entry.getValue().getPerson("architect").getName() + "\n"
					    		+ entry.getValue().getPerson("contractor").getName() + "\n"
					    		+ entry.getValue().getPerson("customer").getName() + "\n"
					    		+ entry.getValue().getFinalised() + "\n\n");
					    fw4.close();
					}
					catch(IOException ioe) {
					    System.err.println("IOException: " + ioe.getMessage());
					}
				}
				// Write completed projects to file
				for (Map.Entry<String, Project> entry : completedProjects.entrySet()) {
					try {
						String filename3 = "completedProjects.txt";
					    FileWriter fw6 = new FileWriter(filename3, true);
					    fw6.write(entry.getValue().getProjectNumber() + "\n"
					    		+ entry.getValue().getProjectName() + "\n"
					    		+ entry.getValue().getBuildingType() + "\n"
					    		+ entry.getValue().getPhysicalAddress() + "\n"
					    		+ entry.getValue().getErfNumber() + "\n"
					    		+ entry.getValue().getTotalFee() + "\n"
					    		+ entry.getValue().getAmountPaid() + "\n"
					    		+ entry.getValue().getDeadline() + "\n"
					    		+ entry.getValue().getPerson("architect").getName() + "\n"
					    		+ entry.getValue().getPerson("contractor").getName() + "\n"
					    		+ entry.getValue().getPerson("customer").getName() + "\n"
					    		+ entry.getValue().getFinalised() + "\n\n");
					    fw6.close();
					}
					catch(IOException ioe) {
					    System.err.println("IOException: " + ioe.getMessage());
					}
				}
				// Quit program
				break;
				
			} else {
				System.out.println("That is not a valid option.");
			}
		}
		
	}
	
	/**
    *
    * Function to assign a customer, architect or contractor to a new project
    * <br>
    * The function edits the selected project's assigned contractor's phone number, email and physical address
    *
    * @param personType string
    * @param people Person hash map
    * @return Person
    * @since version 2.00
    */
	public static Person assignPerson(String personType, HashMap<String, Person> people) {
		Scanner sc = new Scanner(System.in);
		System.out.println("These are the " + personType + "s on the system:");
		// Only print contractor names from people hash map
		for (Map.Entry<String, Person> entry : people.entrySet()) {
			if (entry.getValue().getPersonType().equals(personType)) {
				System.out.println(entry.getKey());
			}
		}
		while (true) {
			System.out.println("Type the name of the " + personType + " to select them:");
			Person newPerson = new Person();
			// Variable to hold the name of each person
			String tempContractor = "";
			// The name the user enters
			String chosenContractor = sc.nextLine();
			// See if the user's entered name matches a person
			for (Map.Entry<String, Person> entry : people.entrySet()) {
				// Set the name
				tempContractor = entry.getKey();
				// Check if equal
				if (tempContractor.equals(chosenContractor)) {
					newPerson = entry.getValue();
					break;
				}
			}
			if (tempContractor.equals(chosenContractor)) {
				return newPerson;
			} else {
				System.out.println("That is not a valid " + personType + ".");
			}
		}
	}
	
	/**
    *
    * Function to edit a person's contact details
    * <br>
    * The function edits the selected person's phone number, email and physical address
    *
    * @param editProject the project
    * @return void
    * @since version 2.00
    */
	public static void changeDetails(Project editProject, String personType) {
		Scanner sc = new Scanner(System.in);
		
		String theContact = "Phone Number: " + editProject.getPerson(personType).getPhoneNumber()
		+ "\nEmail Address: " + editProject.getPerson(personType).getEmailAddress()
		+ "\nPhysical Address: " + editProject.getPerson(personType).getPhysicalAddress();
		System.out.println(theContact + "\n");
		
		System.out.println("New Phone Number: ");
		String phone = sc.nextLine();
		editProject.getPerson(personType).setPhoneNumber(phone);
		
		System.out.println("New Email Address: ");
		String email = sc.nextLine();
		editProject.getPerson(personType).setEmailAddress(email);
		
		System.out.println("New Physical Address: ");
		String physical = sc.nextLine();
		editProject.getPerson(personType).setPhysicalAddress(physical);
		
		System.out.println("Updated contact details:\nPhone Number: " + editProject.getPerson(personType).getPhoneNumber() + 
				"\nEmail Address: " + editProject.getPerson(personType).getEmailAddress() + "\nPhysical Address: " 
				+ editProject.getPerson(personType).getPhysicalAddress());
	}
	
	/**
    *
    * Function to check if a string is a valid date
    * <br>
    * The methods uses the dd/MM/yyyy format
    *
    * @param dateToValidate the string
    * @param dateFormat the format
    * @return boolean for whether or not the date is valid
    * @since version 2.00
    */
	public static boolean isThisDateValid(String dateToValidate, String dateFromat) {
		if(dateToValidate == null){
			return false;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
		sdf.setLenient(false);
		try {
			// If not valid, it will throw ParseException
			Date date = sdf.parse(dateToValidate);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

}
