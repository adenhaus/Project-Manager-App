import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Poise {

	public static void main(String[] args) {
		
		try (
				// Connect to database
				Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/PoisePMS?useSSL=false","myuser","HelloTiggs10@");
				Statement stmt = conn.createStatement();
			) {
				
				// Create scanner
				Scanner s = new Scanner(System.in);
				
				// Call menu function
				menu(stmt, s);
				
			} catch (SQLException ex) {
				ex.printStackTrace();
			}

	}
	
	// Function for the main menu
	public static void menu(Statement stmt, Scanner s) {
		
		while (true) {
			// Print menu
			System.out.println("----------------------------\n"
					+ "n - Add a new project\n"
					+ "e - Edit a project\n"
					+ "v - View all projects\n"
					+ "i - View incomplete projects\n"
					+ "o - View overdue projects\n"
					+ "q - Quit\n"
					+ "----------------------------\n");
			// Take user input
			String input = s.nextLine();
			
			// Call functions
			if (input.equals("n")) {
				newProject(stmt, s);
			} else if (input.equals("v")) {
				viewAll(stmt, s);
			} else if (input.equals("i")) {
				viewIncomplete(stmt, s);
			} else if (input.equals("o")) {
				viewOverdue(stmt, s);
			} else if (input.equals("e")) {
				editProj(stmt, s);
			} else if (input.equals("q")) {
				break;
			} else {
				System.out.println("That is not a valid input");
			}
		}
		
	}
	
	// Function to add a new project
	public static void newProject(Statement stmt, Scanner s) {
		
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
		int totalFee;
		while (true) {
			System.out.println("Total Fee: ");
			try {
				totalFee = Integer.parseInt(s.nextLine());
				break;
			} catch (NumberFormatException e) {
				System.out.println("Must be a number");
			}
		}
		
		// Set the amount paid
		int amountPaid;
		while (true) {
			System.out.println("Amount Paid: ");
			try {
				amountPaid = Integer.parseInt(s.nextLine());
				break;
			} catch (NumberFormatException e) {
				System.out.println("Must be a number");
			}
		}
		
		// Set the deadline
		String deadline = "";
		while (true) {
			System.out.println("Deadline (format must be like this: yyyy-MM-dd): ");
			deadline = s.nextLine();
			boolean dateValid = isThisDateValid(deadline, "yyyy-MM-dd");
			
			if (dateValid) {
				break;
			} else {
				System.out.println("Not a valid date.");
			}
		}
		
		// Set customer and employees
		int custID = addCustomer(stmt, s);
		int archID = assignPerson(stmt, s, "employees", "architect");
		int manID = assignPerson(stmt, s, "employees", "manager");
		int engID = assignPerson(stmt, s, "employees", "engineer");
		
		// Set project name if none is given
		if (projectName.equals("")) {
			try {
				String name = "";
				ResultSet nam = stmt.executeQuery("select name from customers where cust_id = " + custID);
				while (nam.next()) {
					name = nam.getString("name");
				}
				String[] names = name.split(" ");
				projectName = buildingType + " " + names[1];
				
			} catch (SQLException e) {
				System.out.println(e.getErrorCode());
			}
		}
		
		// Insert project into table
		String sqlInsert = "insert into projects (proj_name, building_type, address, erf, total_fee, amt_paid, deadline, architect_id, manager_id, engineer_id, customer_id, finalised)"
				+ "values ('" + projectName + "', '" + buildingType + "', '" + physicalAddress + "', " + erfNumber + ", " + totalFee + ", " + amountPaid + ", '" + deadline + "', " + archID + ", " +  manID + ", " + engID + ", " + custID + ", 'false')";
		try {
			stmt.executeUpdate(sqlInsert);
			System.out.println("Project added");
		} catch (SQLException e) {
			System.out.println(e.getErrorCode());
		}
		
	}
	
	// Function to add a new customer
	public static int addCustomer(Statement stmt, Scanner s) {
		
		String customerFirstName = "";
		String customerSecondName = "";
		
		// Give choice to create new customer or add an existing one
		while (true) {
			System.out.println("e - Existing customer\nn - New customer");
			String customerChoice = s.nextLine();
			
			if (customerChoice.equals("n")) {
				
				// Create a new customer
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
				
				try {
					// Assign a new customer ID
					// Make sure that there will never be a duplicate cust_id
					// I could not use SQL's AUTO_INCREMENT like I did for the proj_id
					// because it if a foreign key in projects
					ArrayList<Integer> custIDs = new ArrayList<Integer>();
					String strSelect = "select cust_id from customers";
					ResultSet rset = stmt.executeQuery(strSelect);
					while (rset.next()) {
						custIDs.add(rset.getInt("cust_id"));
					}
					int custID = Collections.max(custIDs) + 1;
					
					// Add customer to database
					String sqlInsert = "insert into customers "
							+ "values (" + custID + ", '" + customerFirstName + " " + customerSecondName + "', '" + customerPhone + "', '" + customerEmail + "', '" + customerAddress + "')";
					stmt.executeUpdate(sqlInsert);
					System.out.println("Customer added. Customer ID is " + custID);
					return custID;
				} catch (SQLException e) {
					System.out.println("Database error.");
					return 0;
				}
				
			} else if (customerChoice.equals("e")) {
				
				// Choose an existing customer
				return assignPerson(stmt, s, "customers", "");
				
			} else {
				System.out.println("That is not a valid input");
			}
			
		}
	}
	
	// Function to assign people to a project
	public static int assignPerson(Statement stmt, Scanner s, String personType, String employeeType) {
		
		if (personType.equals("customers")) {
			
			try {
				// Print customer names
				System.out.println("These are the customers on the system:");
				String strSelect = "select name from customers";
				ResultSet rset = stmt.executeQuery(strSelect);
				while (rset.next()) {
					String name = rset.getString("name");
					System.out.println(name);
				}
				
				int custID = 0;
				
				// Select customer
				while (true) {
					System.out.println("Type the name of the customer to select them:");
					// Variable to hold the name of each person
					String tempCust = "";
					// The name the user enters
					String chosenCust = s.nextLine();
					// See if the user's entered name matches a person
					String strCust = "select name, cust_id from customers";
					ResultSet res = stmt.executeQuery(strCust);
					while (res.next()) {
						// Set the name
						tempCust = res.getString("name");
						// Check if equal
						if (tempCust.equals(chosenCust)) {
							custID = res.getInt("cust_id");
							break;
						}
					}
					
					if (tempCust.equals(chosenCust)) {
						return custID;
					} else {
						System.out.println("That is not a valid customer");
					}
				}
			} catch (SQLException e) {
				System.out.println(e.getStackTrace() + "\n\n" + e.getErrorCode());
				return 0;
			}
			
			
		} else {
			
			try {
				// Print employee names
				System.out.println("These are the " + employeeType + "s on the system:");
				String strSelect = "select name from employees where role = '" + employeeType + "'";
				ResultSet rset = stmt.executeQuery(strSelect);
				while (rset.next()) {
					String name = rset.getString("name");
					System.out.println(name);
				}
				
				int custID = 0;
				
				// Select employee
				while (true) {
					System.out.println("Type the name of the " + employeeType + " to select them:");
					// Variable to hold the name of each person
					String tempCust = "";
					// The name the user enters
					String chosenCust = s.nextLine();
					// See if the user's entered name matches a person
					String strCust = "select name, emp_id from employees";
					ResultSet res = stmt.executeQuery(strCust);
					while (res.next()) {
						// Set the name
						tempCust = res.getString("name");
						// Check if equal
						if (tempCust.equals(chosenCust)) {
							custID = res.getInt("emp_id");
							break;
						}
					}
					
					if (tempCust.equals(chosenCust)) {
						return custID;
					} else {
						System.out.println("That is not a valid employee");
					}
				}
			} catch (SQLException e) {
				System.out.println("Database error");
				return 0;
			}
			
		}
		
	}
	
	// Function to check validity of an entered date
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
	
	// Function to get customer names
	public static HashMap<Integer, String> getCustomers(Statement stmt) {
		// Create hash map of customer IDs and names
		HashMap<Integer, String> customers = new HashMap<Integer, String>();
		String nameSelect = "select cust_id, name from customers";
		// Get customer names
		try {
			ResultSet set = stmt.executeQuery(nameSelect);
			while (set.next()) {
				int id = set.getInt("cust_id");
				String name = set.getString("name");
				customers.put(id, name);
			}
			return customers;
		} catch (SQLException e) {
			System.out.println(e.getErrorCode());
			return customers;
		}
	}
	
	// Function to get employee names
	public static HashMap<Integer, String> getEmployees(Statement stmt) {
		// Create hash map of employee IDs and names
		HashMap<Integer, String> employees = new HashMap<Integer, String>();
		String empSelect = "select emp_id, name from employees";
		// Get employee names
		try {
			ResultSet set = stmt.executeQuery(empSelect);
			while (set.next()) {
				int id = set.getInt("emp_id");
				String name = set.getString("name");
				employees.put(id, name);
			}
			return employees;
		} catch (SQLException e) {
			System.out.println(e.getErrorCode());
			return employees;
		}
	}
	
	// Function to display all projects
	public static void viewAll(Statement stmt, Scanner s) {
		
		// Make hashmaps of IDs and names
		HashMap<Integer, String> customers = getCustomers(stmt);
		HashMap<Integer, String> employees = getEmployees(stmt);
		
		// Retrieve info about projects
		String strSelect = "select * from projects";
		try {
			ResultSet rset = stmt.executeQuery(strSelect);
			int rowCount = 0;
			
			while (rset.next()) {
				int id = rset.getInt("proj_id");
				String name = rset.getString("proj_name");
				String building = rset.getString("building_type");
				String address = rset.getString("address");
				int erf = rset.getInt("erf");
				int totalFee = rset.getInt("total_fee");
				int amtPaid = rset.getInt("amt_paid");
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String deadline = df.format(rset.getDate("deadline"));
				String customer = "";
				for (Map.Entry<Integer, String> entry : customers.entrySet()) {
					if (rset.getInt("customer_id") == entry.getKey()) {
						customer = entry.getValue();
						break;
					}
				}
				String architect = "";
				for (Map.Entry<Integer, String> entry : employees.entrySet()) {
					if (rset.getInt("architect_id") == entry.getKey()) {
						architect = entry.getValue();
						break;
					}
				}
				String manager = "";
				for (Map.Entry<Integer, String> entry : employees.entrySet()) {
					if (rset.getInt("manager_id") == entry.getKey()) {
						manager = entry.getValue();
						break;
					}
				}
				String engineer = "";
				for (Map.Entry<Integer, String> entry : employees.entrySet()) {
					if (rset.getInt("engineer_id") == entry.getKey()) {
						engineer = entry.getValue();
						break;
					}
				}
				String finalised = rset.getString("finalised");
				
				// Print info
				String output = "Project ID: " + id
						+ "\nProject Name: " + name
						+ "\nBuilding Type: " + building
						+ "\nPhysical Address: " + address
						+ "\nERF Number: " + erf
						+ "\nTotal Fee: " + totalFee
						+ "\nAmount Paid: " + amtPaid
						+ "\nDeadline: " + deadline
						+ "\nArchitect: " + architect
						+ "\nManager: " + manager
						+ "\nEngineer: " + engineer
						+ "\nCustomer: " + customer
						+ "\nFinalised: " + finalised + "\n";
				
				System.out.println(output);
				++rowCount;
			}
			// Print number of projects
			System.out.println("Total number of projects: " + rowCount + "\n");
		} catch (SQLException e) {
			System.out.println(e.getErrorCode());
		}
	}
	
	// Function to display incomplete projects
	public static void viewIncomplete(Statement stmt, Scanner s) {
		
		// Make hashmaps of IDs and names
		HashMap<Integer, String> customers = getCustomers(stmt);
		HashMap<Integer, String> employees = getEmployees(stmt);
		
		String strSelect = "select * from projects";
		
		try {
			ResultSet rset = stmt.executeQuery(strSelect);
			int rowCount = 0;
			
			while (rset.next()) {
				int id = rset.getInt("proj_id");
				String name = rset.getString("proj_name");
				String building = rset.getString("building_type");
				String address = rset.getString("address");
				int erf = rset.getInt("erf");
				int totalFee = rset.getInt("total_fee");
				int amtPaid = rset.getInt("amt_paid");
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String deadline = df.format(rset.getDate("deadline"));
				String customer = "";
				for (Map.Entry<Integer, String> entry : customers.entrySet()) {
					if (rset.getInt("customer_id") == entry.getKey()) {
						customer = entry.getValue();
						break;
					}
				}
				String architect = "";
				for (Map.Entry<Integer, String> entry : employees.entrySet()) {
					if (rset.getInt("architect_id") == entry.getKey()) {
						architect = entry.getValue();
						break;
					}
				}
				String manager = "";
				for (Map.Entry<Integer, String> entry : employees.entrySet()) {
					if (rset.getInt("manager_id") == entry.getKey()) {
						manager = entry.getValue();
						break;
					}
				}
				String engineer = "";
				for (Map.Entry<Integer, String> entry : employees.entrySet()) {
					if (rset.getInt("engineer_id") == entry.getKey()) {
						engineer = entry.getValue();
						break;
					}
				}
				String finalised = rset.getString("finalised");
				
				String output = "Project ID: " + id
						+ "\nProject Name: " + name
						+ "\nBuilding Type: " + building
						+ "\nPhysical Address: " + address
						+ "\nERF Number: " + erf
						+ "\nTotal Fee: " + totalFee
						+ "\nAmount Paid: " + amtPaid
						+ "\nDeadline: " + deadline
						+ "\nArchitect: " + architect
						+ "\nManager: " + manager
						+ "\nEngineer: " + engineer
						+ "\nCustomer: " + customer
						+ "\nFinalised: " + finalised + "\n";
				
				if (rset.getString("finalised").equals("false")) {
					System.out.println(output);
					++rowCount;
				}
				
			}
			System.out.println("Total number of incomplete projects: " + rowCount + "\n");
		} catch (SQLException e) {
			System.out.println(e.getErrorCode());
		}
	}
	
	// Function to display overdue projects
	public static void viewOverdue(Statement stmt, Scanner s) {
		
		// Make hashmaps of IDs and names
		HashMap<Integer, String> customers = getCustomers(stmt);
		HashMap<Integer, String> employees = getEmployees(stmt);
		
		String strSelect = "select * from projects";
		
		try {
			ResultSet rset = stmt.executeQuery(strSelect);
			int rowCount = 0;
			
			while (rset.next()) {
				int id = rset.getInt("proj_id");
				String name = rset.getString("proj_name");
				String building = rset.getString("building_type");
				String address = rset.getString("address");
				int erf = rset.getInt("erf");
				int totalFee = rset.getInt("total_fee");
				int amtPaid = rset.getInt("amt_paid");
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String deadline = df.format(rset.getDate("deadline"));
				String customer = "";
				for (Map.Entry<Integer, String> entry : customers.entrySet()) {
					if (rset.getInt("customer_id") == entry.getKey()) {
						customer = entry.getValue();
						break;
					}
				}
				String architect = "";
				for (Map.Entry<Integer, String> entry : employees.entrySet()) {
					if (rset.getInt("architect_id") == entry.getKey()) {
						architect = entry.getValue();
						break;
					}
				}
				String manager = "";
				for (Map.Entry<Integer, String> entry : employees.entrySet()) {
					if (rset.getInt("manager_id") == entry.getKey()) {
						manager = entry.getValue();
						break;
					}
				}
				String engineer = "";
				for (Map.Entry<Integer, String> entry : employees.entrySet()) {
					if (rset.getInt("engineer_id") == entry.getKey()) {
						engineer = entry.getValue();
						break;
					}
				}
				String finalised = rset.getString("finalised");
				
				String output = "Project ID: " + id
						+ "\nProject Name: " + name
						+ "\nBuilding Type: " + building
						+ "\nPhysical Address: " + address
						+ "\nERF Number: " + erf
						+ "\nTotal Fee: " + totalFee
						+ "\nAmount Paid: " + amtPaid
						+ "\nDeadline: " + deadline
						+ "\nArchitect: " + architect
						+ "\nManager: " + manager
						+ "\nEngineer: " + engineer
						+ "\nCustomer: " + customer
						+ "\nFinalised: " + finalised + "\n";
				
				Date todayDate = Calendar.getInstance().getTime();
				if (todayDate.compareTo(rset.getDate("deadline")) < 0 && finalised.equals("false")) {
					System.out.println(output);
					++rowCount;
				}
			}
			System.out.println("Total number of overdue projects: " + rowCount + "\n");
		} catch (SQLException e) {
			System.out.println(e.getErrorCode());
		}
	}
	
	// Function to edit projects
	public static void editProj(Statement stmt, Scanner s) {
		
		try {
			String projName = "";
			
			// Give option to select by name or ID
			while (true) {
				System.out.println("i - Select by ID\n"
						+ "n - Select by name");
				
				String projSelect = s.nextLine();
				
				if (projSelect.equals("i")) {
					
					// Print project names and IDs
					System.out.println("Projects in the system:");
					ResultSet set = stmt.executeQuery("select * from projects");
					while (set.next()) {
						System.out.println("Name: " + set.getString("proj_name") + " - ID: " + set.getInt("proj_id"));
					}
					while (true) {
						System.out.println("Type the number of the project to select it:");
						// Variable to hold the number of each project
						int tempNum = 0;
						// The project number the user enters
						int projNum = Integer.parseInt(s.nextLine());
						// See if the user's number matches a project number
						ResultSet aset = stmt.executeQuery("select * from projects");
						while (aset.next()) {
							tempNum = aset.getInt("proj_id");
							if (tempNum == projNum) {
								projName = aset.getString("proj_name");
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
				} else if (projSelect.equals("n")) {
					
					// Print project names and IDs
					System.out.println("Projects in the system:");
					ResultSet set = stmt.executeQuery("select * from projects");
					while (set.next()) {
						System.out.println("Name: " + set.getString("proj_name") + " - ID: " + set.getInt("proj_id"));
					}
					while (true) {
						System.out.println("Type the name of the project to select it:");
						// Variable to hold the number of each project
						String tempName = "";
						// The project number the user enters
						String projNam = (s.nextLine());
						// See if the user's number matches a project number
						ResultSet aset = stmt.executeQuery("select * from projects");
						while (aset.next()) {
							tempName = aset.getString("proj_name");
							if (tempName.equals(projNam)) {
								projName = aset.getString("proj_name");
								break;
							}
						}
						if (tempName.equals(projNam)) {
							break;
						} else {
							System.out.println("That is not a valid project.");
						}
					}
					break;
				}
			}
			
			// Edit menu
			while (true) {
				System.out.println("\nChoose what to edit\n"
						+ "d - Deadline\n"
						+ "a - Aount Paid\n"
						+ "c - Somebody's Contact Details\n"
						+ "t - Total fee\n"
						+ "b - Building type\n"
						+ "p - Physical Address\n"
						+ "f - Finalise\n"
						+ "e - ERF number\n"
						+ "q - Exit to main menu");
				
				String edit = s.nextLine();
				
				if (edit.equals("d")) {
					ResultSet set = stmt.executeQuery("select * from projects where proj_name = '" + projName + "'");
					while (set.next()) {
						System.out.println("Current deadline is " + set.getDate("deadline"));
					}
					
					// Set the deadline
					String deadline = "";
					while (true) {
						System.out.println("Deadline (format must be: yyyy-MM-dd): ");
						deadline = s.nextLine();
						boolean dateValid = isThisDateValid(deadline, "yyyy-MM-dd");
						
						if (dateValid) {
							break;
						} else {
							System.out.println("Not a valid date.");
						}
					}
					stmt.executeUpdate("update projects set deadline = '" + deadline + "' where proj_name = '" + projName + "'");
					
				} else if (edit.equals("a")) {
					ResultSet set = stmt.executeQuery("select * from projects where proj_name = '" + projName + "'");
					while (set.next()) {
						System.out.println("Current amount is " + set.getInt("amt_paid"));
					}
					
					// Set the amount
					int amt = 0;
					while (true) {
						try {
							System.out.println("New amount: ");
							amt = Integer.parseInt(s.nextLine());
							break;
						} catch (NumberFormatException e) {
							System.out.println("Must be an integer");
						}
					}
					stmt.executeUpdate("update projects set amt_paid = " + amt + " where proj_name = '" + projName + "'");
					
				} else if (edit.equals("t")) {
					ResultSet set = stmt.executeQuery("select * from projects where proj_name = '" + projName + "'");
					while (set.next()) {
						System.out.println("Current fee is " + set.getInt("total_fee"));
					}
					
					// Set the fee
					int fee = 0;
					while (true) {
						try {
							System.out.println("New amount: ");
							fee = Integer.parseInt(s.nextLine());
							break;
						} catch (NumberFormatException e) {
							System.out.println("Must be an integer");
						}
					}
					stmt.executeUpdate("update projects set total_fee = " + fee + " where proj_name = '" + projName + "'");
					
				} else if (edit.equals("b")) {
					ResultSet set = stmt.executeQuery("select * from projects where proj_name = '" + projName + "'");
					while (set.next()) {
						System.out.println("Current building type is " + set.getString("building_type"));
					}
					
					// Set the type
					String type = "";
					System.out.println("New building type: ");
					type = (s.nextLine());
					stmt.executeUpdate("update projects set building_type = '" + type + "' where proj_name = '" + projName + "'");
					
				} else if (edit.equals("p")) {
					ResultSet set = stmt.executeQuery("select * from projects where proj_name = '" + projName + "'");
					while (set.next()) {
						System.out.println("Current physical address is " + set.getString("address"));
					}
					
					// Set the address
					String address = "";
					System.out.println("New address: ");
					address = (s.nextLine());
					stmt.executeUpdate("update projects set address = '" + address + "' where proj_name = '" + projName + "'");
					
				} else if (edit.equals("e")) {
					ResultSet set = stmt.executeQuery("select * from projects where proj_name = '" + projName + "'");
					while (set.next()) {
						System.out.println("Current ERF number is " + set.getInt("erf"));
					}
					
					// Set the ERF
					int erf = 0;
					while (true) {
						try {
							System.out.println("New ERF: ");
							erf = Integer.parseInt(s.nextLine());
							break;
						} catch (NumberFormatException e) {
							System.out.println("Must be an integer");
						}
					}
					stmt.executeUpdate("update projects set erf = " + erf + " where proj_name = '" + projName + "'");
					
				} else if (edit.equals("c")) {
					// Edit contact details
					while (true) {
						System.out.println("m - Manager\na - Architect\nc - Customer\ne - Engineer");
						String personChoice = s.nextLine();
						if (personChoice.equals("m")) {
							changeDetails(stmt, s, "manager_id");
							break;
						} else if (personChoice.equals("a")) {
							changeDetails(stmt, s, "architect_id");
							break;
						} else if (personChoice.equals("e")) {
							changeDetails(stmt, s, "engineer_id");
							break;
						} else if (personChoice.equals("c")) {
							changeDetails(stmt, s, "customer_id");
							break;
						} else {
							System.out.println("That is not a valid person.");
						}
					}
				} else if (edit.equals("f")) {
					
					// Finalise the project
					ResultSet fset = stmt.executeQuery("select * from projects where proj_name = '" + projName + "'");
					int amt = 0;
					int fee = 0;
					int id = 0;
					String finalised = "";
					while (fset.next()) {
						amt = fset.getInt("amt_paid");
						fee = fset.getInt("total_fee");
						finalised = fset.getString("finalised");
						id = fset.getInt("customer_id");
					}
					if (finalised.equals("false")) {
						if (amt == fee) {
							System.out.println("Date of completion: ");
							String completionDate = s.nextLine();
							stmt.executeUpdate("update projects set finalised = 'Finalised on " + completionDate + "'");
							System.out.println("Project finalised. The full amount has already been paid so no invoice will be generated.");
						} else {
							System.out.println("Date of completion: ");
							String completionDate = s.nextLine();
							stmt.executeUpdate("update projects set finalised = 'Finalised on " + completionDate + "' where proj_name = '" + projName + "'");
							
							String name = "";
							String phone = "";
							String email = "";
							String address = "";
							ResultSet cust = stmt.executeQuery("select * from customers where cust_id = " + id);
							while (cust.next()) {
								name = cust.getString("name");
								phone = cust.getString("phone");
								email = cust.getString("email");
								address = cust.getString("address");
							}
							System.out.println(fee + " " + amt);
							int due = fee - amt;
							String invoice = "Invoice\n" + name + "\n" + 
									email + "\n" 
									+ phone + "\n" + 
									address + "\nOutstanding balance: R" 
									+ due;
							System.out.println(invoice);
						}
					} else {
						System.out.println("This project is already completed.");
					}
					
				} else if (edit.equals("q")) {
					break;
				} else {
					System.out.println("Not a valid input.");
				}
			}
			
		} catch (SQLException e) {
			System.out.println(e.getErrorCode());
		}
		
	}
	
	// Function to change somebody's contact details
	public static void changeDetails(Statement stmt, Scanner s, String personType) {
		try {
			ResultSet set = stmt.executeQuery("select * from projects");
			int id = 0;
			while (set.next()) {
				id = set.getInt(personType);
			}
			
			String name = "";
			String phone = "";
			String email = "";
			String address = "";
			if (personType.equals("customer_id")) {
				ResultSet cust = stmt.executeQuery("select * from customers where cust_id = " + id);
				while (cust.next()) {
					name = cust.getString("name");
					phone = cust.getString("phone");
					email = cust.getString("email");
					address = cust.getString("address");
				}
				System.out.println("Customer: " + name);
				
				// Set the phone
				System.out.println("Current phone number is " + phone);
				System.out.println("New phone number: ");
				String newPhone = (s.nextLine());
				stmt.executeUpdate("update customers set phone = '" + newPhone + "' where cust_id = '" + id + "'");
				
				// Set the email
				System.out.println("Current email address is " + email);
				System.out.println("New email: ");
				String newEmail = (s.nextLine());
				stmt.executeUpdate("update customers set email = '" + newEmail + "' where cust_id = '" + id + "'");
				
				// Set the address
				System.out.println("Current address is " + address);
				System.out.println("New address: ");
				String newAddress = (s.nextLine());
				stmt.executeUpdate("update customers set address = '" + newAddress + "' where cust_id = '" + id + "'");
				
			} else {
				ResultSet emp = stmt.executeQuery("select * from employees where emp_id = " + id);
				while (emp.next()) {
					name = emp.getString("name");
					phone = emp.getString("phone");
					email = emp.getString("email");
					address = emp.getString("address");
				}
				System.out.println("Employee: " + name);
				
				// Set the phone
				System.out.println("Current phone number is " + phone);
				System.out.println("New phone number: ");
				String newPhone = (s.nextLine());
				stmt.executeUpdate("update employees set phone = '" + newPhone + "' where emp_id = '" + id + "'");
				
				// Set the email
				System.out.println("Current email address is " + email);
				System.out.println("New email: ");
				String newEmail = (s.nextLine());
				stmt.executeUpdate("update employees set email = '" + newEmail + "' where emp_id = '" + id + "'");
				
				// Set the address
				System.out.println("Current address is " + address);
				System.out.println("New address: ");
				String newAddress = (s.nextLine());
				stmt.executeUpdate("update employees set address = '" + newAddress + "' where emp_id = '" + id + "'");
			}
			
		} catch (SQLException e) {
			System.out.println(e.getErrorCode());
		}
		
	}

}
