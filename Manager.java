
import java.io.Console;
import java.util.ArrayList;
//import java.util.Map.Entry;


public class Manager extends Employee {

	
	//constructors
	public Manager(String firstName, String lastName, String passcode) {
		super(firstName, lastName, passcode);
	}
	public Manager(String firstName, String lastName, String title, String passcode) {
		super(firstName, lastName, title, passcode);
	}
	
	public Manager() {
		super();
	}

    public String typeOfObject(){
        return "Manager";
    }

	//methods
	public void addEmployee() {
        String pageTitle = "Add New Employee Window:";
        String longestLine = "4. Go Back To Manager Window\n";
        ManagementSystem.printPageHeaderNOBAR(pageTitle);
        ManagementSystem.matchBarToString(longestLine);
		System.out.println("\n1. New Manager\n");
		System.out.println("2. New Developer\n");
		System.out.println("3. New Advisor\n");
		System.out.println(longestLine);
        ManagementSystem.matchBarToString(longestLine);

        int choice = ManagementSystem.validateUserMenuChoice(4);  
        
		switch (choice) {
			case 1: {this.addNewManager();}break;
			case 2: {this.addNewDeveloper();}break;
			case 3: {this.addNewAdvisor();}break;
			case 4: {this.userWindow();}break;
		}//end of switch case
	}//end of method: addEmployee

	public void addNewManager() {
        String pageTitle = "Add New Manager Window:";
        ManagementSystem.printPageHeaderWithBar(pageTitle);
		Manager mang = ManagementSystem.createManager();
        postAddingEmployee(mang);
	}
	
	public void addNewAdvisor() {

        String pageTitle = "Add New Advisor Window:";
        ManagementSystem.printPageHeaderWithBar(pageTitle);
		Advisor adv = ManagementSystem.createAdvisor();
        postAddingEmployee(adv);
		
	}
	
	public void addNewDeveloper() {
		//create new developer object
		//add it to the hashmaps
        String pageTitle = "Add New Developer Window:";
        ManagementSystem.printPageHeaderWithBar(pageTitle);
		Developer dev = ManagementSystem.createDeveloper();
		
        postAddingEmployee(dev);
        
	}//end of method: addNewDeveloper
	
    public void postAddingEmployee(Employee emp){
        //handles all the post envets after creating a new employee
        String pageTitle = "You Successfully Added " + emp.getFullName() +  " as a New " + emp.getTitle() + " " + emp.typeOfObject() +"!";
        ManagementSystem.printPageHeaderWithBar(pageTitle);
		ManagementSystem.matchBarToString(emp.getFullName()+ "'s Employee Number is: " + emp.get_workerId());
		System.out.println(emp.getFullName()+ "'s Employee Number is: " + emp.get_workerId());
		ManagementSystem.matchBarToString(emp.getFullName()+ "'s Employee Number is: " + emp.get_workerId());

		System.out.println();
		ManagementSystem.pressEnterToContinue();

        //clears the console and list further options
		System.out.print("\033[H\033[2J");  
	    System.out.flush(); 
	    
		System.out.println("1. Add a New Employee\n");
		System.out.println("2. Assign " + emp.getFullName() + " to a Project\n");
		System.out.println("3. Create New Project\n");
		System.out.println("4. Go Back To Manager Window\n");
		
        int choice = ManagementSystem.validateUserMenuChoice(4);
		switch (choice) {
			case 1: {this.addEmployee();}break;
			case 2: {this.addEmployeeToProject(emp);}break;
			case 3: {createNewProject(false);}break;
			case 4: {this.userWindow();}break;
		}

    }//end method: postAddingEmployee

	public boolean checkProjectName(String projName) {
		//return true if name is valid
		//the method works as long as the name is spelled correctly, regardless to case
		
		if(ManagementSystem.getProjectToEmployees().containsKey(projName)) {
			return true;
		}
        String errorMsg = "- Project Name Not Found!\n" + "- The Names Are Case Sensitive!\n" + "- Please Try Again!";
        System.out.println();
        ManagementSystem.matchBarToString(errorMsg);
        System.out.println(errorMsg);
        ManagementSystem.matchBarToString(errorMsg);
        System.out.println();
        return false;	
	}//end of method: check project name
	

	
	public void addEmployeeToProject(Employee emp) {
		
		Console console = System.console();
		
		if(ManagementSystem.getProjectToEmployees().isEmpty()) {
			System.out.print("\033[H\033[2J");  
		    System.out.flush(); 

            String pageTitle = "- You Must Create a Project Before You Can Assign Employees To It!";
            ManagementSystem.printPageHeaderNOBAR(pageTitle);
		    System.out.println("- You Will Be Redirected Back Here After You've Created A Project.");
            ManagementSystem.matchBarToString(pageTitle);
		    System.out.println("\n1. Create New Project\n");
		    System.out.println("2. Go Back To Manager Window\n");
		    
	        int choice = ManagementSystem.validateUserMenuChoice(2); 
			switch (choice) {
				case 1: {this.createNewProject(true);}break;
				case 2: {this.userWindow();}break;//need to add return; if theres code after this choice because of data flow
			}
		    if(choice == 2) {//if user chose to go back to main menu this function will DIE here tan tan tannnnn
		    	return;
		    }
		}//end if checking if map is empty
		
        boolean repeat = false;
        String projectName = null;

        do{
            String midPageTitle = "Add " + emp.getFullName() + " To a Project:";
            String longestLine = "\n1. Add " + emp.getFullName() + " To Existing Project\n";
            ManagementSystem.printPageHeaderNOBAR(midPageTitle);
            ManagementSystem.matchBarToString(longestLine);

            System.out.println("\n- All Current Projects:\n");
            ManagementSystem.matchBarToString(longestLine);
            ManagementSystem.getProjectToEmployees().forEach((k, v) -> System.out.println("- "+k));	
            ManagementSystem.matchBarToString(longestLine);
            
            System.out.println(longestLine);
            System.out.println("2. Create a New Project\n");
            ManagementSystem.matchBarToString(longestLine);

            Integer choice = ManagementSystem.validateUserMenuChoice(2);

            if(choice.equals(1)){
                do {
                    projectName = new String(console.readLine("\nEnter Project Name to Assign " + emp.getFullName() + " to: "));
                    }while(!checkProjectName(projectName));//if the name isn't in the db, retry
                repeat = false;
            }else{
                createNewProject(true);
                repeat = true;
            }
        }while(repeat);
	    
	    
	    
		ArrayList<Employee> employeeList = ManagementSystem.getProjectToEmployees().get(projectName);//get arraylist of project employees
		emp.setCurrentProject(projectName);//updating the employee object
		employeeList.add(emp);//adds developer to list of employees who work on the project
		ManagementSystem.getProjectToEmployees().put(projectName, employeeList);//updates the arraylist in the hashmap
		
        System.out.println();
        ManagementSystem.matchBarToString(emp.getFullName() + " Was Assigned to " + projectName);
		System.out.println(emp.getFullName() + " Was Assigned to " + projectName);	
        ManagementSystem.matchBarToString(emp.getFullName() + " Was Assigned to " + projectName);
        System.out.println();

        ManagementSystem.pressEnterToContinue();
	

        //clears the console
		System.out.print("\033[H\033[2J");  
	    System.out.flush(); 
	    
		System.out.println("1. Assign Existing Employee to Project\n");
		System.out.println("2. Create New Employee\n");
		System.out.println("3. Go Back To Manager Window\n");
		
        int choice = ManagementSystem.validateUserMenuChoice(3);
		switch (choice) {
			case 1: {assignEmployeeToProject();}break;
			case 2: {this.addEmployee();}break;
			case 3: {this.userWindow();}break;
		}
        return;//KILL IT! KILL IT WITH RETURN!
	}//end of method: addEmployeeToProject

    public void assignEmployeeToProject(){
        //will get the developer from the user and then call addEmployeeToProject
        String pageTitle = "ASSIGN Employee to Project Window:";
        ManagementSystem.printPageHeaderNOBAR(pageTitle);
        ManagementSystem.matchBarToString(pageTitle);

        ManagementSystem.printPageHeaderNOBAR(pageTitle);
        ManagementSystem.matchBarToString(pageTitle);
        System.out.println("");
        System.out.println("1. ASSIGN Employee to Project\n");
	    System.out.println("2. Cancel\n");
        ManagementSystem.matchBarToString(pageTitle);
        Integer choice = ManagementSystem.validateUserMenuChoice(2);

        if(choice == 2){
            this.userWindow();
            return;//if user canceled, this call will be terminated
        }

        ManagementSystem.printPageHeaderNOBAR(pageTitle);
        ManagementSystem.matchBarToString(pageTitle);
        System.out.println("");
        
        listEmployeeNameToNumber();//prints a list of employees name and number
        System.out.println("");
        ManagementSystem.matchBarToString(pageTitle);
        System.out.println("");
        
        Integer employeeNumber;
        employeeNumber = ManagementSystem.getEmployeeNumber();//takes care of all validation process
        Employee emp = ManagementSystem.getNumberToNameMap().get(employeeNumber);//getting the correct employee object
        addEmployeeToProject(emp);//sending the employee object to  addEmployeeToProject
    }//end method: assignEmployeeToProject

    public static void listEmployeeNameToNumber(){
    //lists out a name to employee number list	
		ManagementSystem.getNumberToNameMap().forEach((k, v) -> 
		System.out.println("Name: " + v.getFullName() + " | " + "Number: " + k ));
    }//end method: listEmployeeNameToNumber

	public void removeEmployee() {
		//find employee object
		//remove from hashmaps
		
		if(ManagementSystem.getNumberToNameMap().size() == 1) {
			ManagementSystem.printPageHeaderWithBar("No Employees To Remove!");
			ManagementSystem.pressEnterToContinue();
			this.userWindow();
			return;
		}
		
        String pageTitle = "REMOVE Employee Window:";
        String longestLine = "\n- To Remove an Employee You Must Enter His Employee Number\n";

        ManagementSystem.printPageHeaderNOBAR(pageTitle);
        ManagementSystem.matchBarToString(longestLine);
        System.out.println("");
        System.out.println("1. Remove an Employee\n");
	    System.out.println("2. Cancel\n");
        ManagementSystem.matchBarToString(longestLine);
        Integer choice = ManagementSystem.validateUserMenuChoice(2);

        if(choice == 2){
            this.userWindow();
            return;//if user canceled, this call will be terminated
        }

        ManagementSystem.printPageHeaderNOBAR(pageTitle);
        ManagementSystem.matchBarToString(longestLine);
	    System.out.println(longestLine);
	    System.out.println("- Then Enter Your Employee Number and Passcode To Confirm\n");

        ManagementSystem.matchBarToString(longestLine);
        listEmployeeNameToNumber();//prints list of names and corelating numbers
        ManagementSystem.matchBarToString(longestLine);
	    System.out.println();


        Integer employeeNumber;
        boolean invalidChoice = true;
        
        do{
		    employeeNumber = ManagementSystem.getEmployeeNumber();
            //an admin can't remove himself	
            if(employeeNumber.equals(this.get_workerId())){
            	
            	System.out.println();
            	ManagementSystem.matchBarToString(longestLine);
                System.out.println("You Can't Remove Yourself!");
                ManagementSystem.matchBarToString(longestLine);
                System.out.println();
                
                invalidChoice = true;
            }else{
                invalidChoice = false;
            }
        }while(invalidChoice);

        
   
        System.out.println();
    	ManagementSystem.matchBarToString(longestLine);
       // System.out.println("Enter Your Employee Number and Passcode To Confirm");
        System.out.println("ENTER YOUR EMPLOYEE NUMBER AND PASSCODE TO CONFIRM");
        ManagementSystem.matchBarToString(longestLine);
        System.out.println();
        
        //asks the manager to verify that it's him by entering his employee number and passcode
        if(this.get_workerId() != ManagementSystem.loginProcess()) {
        	System.out.println();
        	ManagementSystem.matchBarToString(longestLine);
            System.out.println("Not The Credantials Of " + this.getFullName() + "!");    
            ManagementSystem.matchBarToString(longestLine);
            System.out.println();
            
            ManagementSystem.pressEnterToContinue();
            ManagementSystem.startLoginSystem();
            return;//killing the function here if by any change the stack comes back here
        }
        
        System.out.println();
        
        String fullName = ManagementSystem.getNumberToNameMap().get(employeeNumber).getFullName();//getting the full name of the employee to remove
       
        ManagementSystem.matchBarToString(longestLine);
        System.out.println("Employee: " + fullName + " Was Removed.");
        ManagementSystem.matchBarToString(longestLine);
        System.out.println();
        //ACTUALLY REMOVING THE EMPLOYEE OBJECT FROM THE HASHMAPS
        ManagementSystem.getNumberToNameMap().remove(employeeNumber);
        ManagementSystem.getNumberToPasscodeMap().remove(employeeNumber);
	
        ManagementSystem.pressEnterToContinue();
        this.userWindow();
	}
	

	
	public void taskToDifferentProject() {
		//task employee to a different project
        //this function works like assignEmployeeToProject,
        //with the only difference being that this function removes the employee
        //from his current project
        String pageTitle = "ASSIGN EMPLOYEE TO A DIFFERENT PROJECT:";
        

        ManagementSystem.printPageHeaderNOBAR(pageTitle);
        ManagementSystem.matchBarToString(pageTitle);
        System.out.println("");
        System.out.println("1. Task Employee To a Different Project\n");
	    System.out.println("2. Cancel\n");
        ManagementSystem.matchBarToString(pageTitle);
        Integer choice = ManagementSystem.validateUserMenuChoice(2);

        if(choice == 2){
            this.userWindow();
            return;//if user canceled, this call will be terminated
        }
        
        
        ManagementSystem.printPageHeaderWithBar(pageTitle);
        
        listEmployeeNameToNumber();//prints a list of employees name and number
        System.out.println("");
        ManagementSystem.matchBarToString(pageTitle);
        System.out.println("");
        
        Integer employeeNumber;
        employeeNumber = ManagementSystem.getEmployeeNumber();//takes care of all validation process
        
        Employee emp = ManagementSystem.getNumberToNameMap().get(employeeNumber);//getting the correct employee object

        
        ArrayList<Employee> empList = ManagementSystem.getProjectToEmployees().get(emp.getCurrentProject());//employees arraylist

        empList.remove(emp);//removing employee from arraylist of its current project
        emp.setCurrentProject("");
        
        addEmployeeToProject(emp);//sending the employee object to  addEmployeeToProject
	}
	
	
	public void createNewProject(boolean fromActiveFunc) {
        //the parameter is used to detrmine if the call came from an active function
        //if the call came from an active function--we must either return to it or create another project, and then return to it.
        
		//creates a new project by giving it a name and an empty list of employees who work on the project.
        String pageTitle = "CREATE NEW PROJECT WINDOW:";
        

        ManagementSystem.printPageHeaderNOBAR(pageTitle);
        ManagementSystem.matchBarToString(pageTitle);
        System.out.println("");
        System.out.println("1. Create New Project\n");
	    System.out.println("2. Cancel\n");
        ManagementSystem.matchBarToString(pageTitle);
        Integer choice = ManagementSystem.validateUserMenuChoice(2);

        if(choice == 2){
            this.userWindow();
            return;//if user canceled, this call will be terminated
        }
        
        
        ManagementSystem.printPageHeaderWithBar(pageTitle);

	    Console console = System.console();
		String projectName = null;
	    String areYouSure = null; //can only be a "Y" or "N" string
	    boolean notSure = true; //used for outer loop
	    boolean notValidInput = true; //used for inner loop
	    
	    //nested loops that manage input of project name
	    //asks user to confirm his entry
	    //and handles input exceptions
	    String longestLine;
	    do {
	    	projectName = new String(console.readLine("Choose a Project Name: "));
	    	longestLine = "Choose a Project Name: " + projectName;
	    	
	    	do {
	    	
		    	try {
		    		console.format("\nYou Chose The Name: " + projectName + "\n\nARE YOU SURE?\n");
		    		console.format("Y - YES\nN - NO ");
		    		
		    		areYouSure = new String(console.readLine("\nYour Choice: "));
		  
		    		//stupid double negative bullshit. Couldn't think of something better, though...
		    		if( !(areYouSure.equals("Y")) && !(areYouSure.equals("N")) ) {
		    			throw new Exception("YOU CAN ONLY PRESS \"Y\" OR \"N\" \n");
		    		}//end exception if
		    		
		    		notValidInput = false; //if no exception was thrown--the answer input is valid
		    		
		    		if(areYouSure.equals("Y")) {
		    			notSure = false;
		    		}else {
		    			ManagementSystem.matchBarToString(longestLine);
		    			notSure = true;
		    		}//end if
		    		
		    	}//end try block
		    	catch(Exception ex) {
		    		//System.out.println("--------------------------\n");
		    		ManagementSystem.matchBarToString(longestLine);
		    		System.out.println(ex.getMessage());
		    		ManagementSystem.matchBarToString(longestLine);
		    		notValidInput = true;
		    	}//end catch block
		    	
	    	}while(notValidInput);//asks for confirmation, and check input validity
	    	
	    }while(notSure);//if user regret project name he can choose a different name
	    
		
		ManagementSystem.getProjectToEmployees().put(projectName, new ArrayList<Employee>());//adds new project to the hashmap that maps who works on what
        pageTitle = "You Succesfully Created Project: " + projectName;
        ManagementSystem.printPageHeaderWithBar(pageTitle);
	    System.out.println("1. Create Another Project\n");

        

        if(fromActiveFunc){
            System.out.println("2. Done Creating Projects\n");//only works well if the call to the function came from "Assign dev to project"
            choice = ManagementSystem.validateUserMenuChoice(2);
            switch (choice) {
                case 1: {this.createNewProject(true);}break;
                case 2: {}break;
            }
            return;//kills the function 
        }else{
            System.out.println("2. Go Back To Manager Window\n");
            choice = ManagementSystem.validateUserMenuChoice(2);
            switch (choice) {
                case 1: {this.createNewProject(false);}break;
                case 2: {this.userWindow();}break;
            }
            return;//kills the function 
        }
		
	   
		
	}
	
	public void changeEmployeeTitle() {
		
		
		String pageTitle = "Change Employee's Title:";
        String longestLine = "1. Change Employee's Title\n";

        ManagementSystem.printPageHeaderNOBAR(pageTitle);
        ManagementSystem.matchBarToString(longestLine);
        System.out.println("");
        System.out.println(longestLine);
	    System.out.println("2. Cancel\n");
        ManagementSystem.matchBarToString(longestLine);
        Integer choice = ManagementSystem.validateUserMenuChoice(2);

        if(choice == 2){
            this.userWindow();
            return;//if user canceled, this call will be terminated
        }
        
        
        ManagementSystem.printPageHeaderNOBAR(pageTitle);
        ManagementSystem.matchBarToString(longestLine);
        System.out.println("");
        listEmployeeNameToNumber();//prints a list of employees name and number
        System.out.println("");
        ManagementSystem.matchBarToString(longestLine);
        System.out.println("");
        
        Integer employeeNumber;
        employeeNumber = ManagementSystem.getEmployeeNumber();//takes care of all validation process
        System.out.println("");
        
        Employee emp = ManagementSystem.getNumberToNameMap().get(employeeNumber);//getting the correct employee object
        
        ManagementSystem.checkJobTitle("");//using this to print out the list of valid job titles. kind of sideways, but it's already there so why not...
        String title = ManagementSystem.scanForJobTitle();
        System.out.println("");
        
        emp.setTitle(title);
		
        
        
        System.out.println(emp.getFullName() + " Is A " + title + " Now!");
        ManagementSystem.matchBarToString(emp.getFullName() + " Is A " + title + " Now!");
        System.out.println("");
        ManagementSystem.pressEnterToContinue();
        this.userWindow();
        
	}
	
	
	public void userWindow() {
		
        String pageTitle = "MANAGER USER WINDOW:";
        String longestLine = "- For security reasons: please exit your account once finished.\n";
        ManagementSystem.printPageHeaderNOBAR(pageTitle);
        ManagementSystem.matchBarToString(longestLine);
		System.out.println("\n- Welcome " + this.getFullName()+ " \n");
		System.out.println(longestLine);
        ManagementSystem.matchBarToString(longestLine);
		
		
		System.out.println("\n1. Add New Employee\n");
		System.out.println("2. Remove Employee\n");
		System.out.println("3. Assign Employee To Project\n");
		System.out.println("4. Move Employee To Different Project\n");
		System.out.println("5. Create New Project\n");
		System.out.println("6. Print Monthly Attendance\n");
		System.out.println("7. Change Employee's Job Title\n");
		System.out.println("8. Exit Your Account\n");
		System.out.println("9. End Work Day\n");
        ManagementSystem.matchBarToString(longestLine);

        Integer choice = ManagementSystem.validateUserMenuChoice(9);
       
		switch (choice) {
			
			case 1: {this.addEmployee();}break;
			case 2: {this.removeEmployee();}break;
			case 3: {this.assignEmployeeToProject();}break;
			case 4: {this.taskToDifferentProject();}break;
			case 5: {this.createNewProject(false);}break;
			case 6: {this.printMyMonthlyTime();}break;
			case 7: {this.changeEmployeeTitle();}break;
			case 8: { ManagementSystem.startLoginSystem();}break;
			case 9: {this.endWorkDay();}break;
		}
		
	}//end method: userWindow
	
	
}



