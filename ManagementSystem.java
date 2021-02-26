import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public abstract class ManagementSystem {
	
	private static Integer _numOfEmployees = 0;
	private static HashMap<Integer , String> _employeesNumberToPasscode = new HashMap<Integer , String>();
	private static HashMap<Integer , Employee> _employeesNumberToName = new HashMap<Integer , Employee>();
	private static HashMap<String , ArrayList<Employee>> _projectToEmployees = new HashMap<String , ArrayList<Employee>>();
	
	
	//getters and setters
	public static Integer getnumOfEmployees() {
		return _numOfEmployees;
	}
	
	public static void setnumOfEmployees(Integer numOfEmployees) {
		_numOfEmployees = numOfEmployees;
	}
	
	public static HashMap<Integer , String> getNumberToPasscodeMap(){
		return _employeesNumberToPasscode;
	}
	
	public static HashMap<Integer , Employee> getNumberToNameMap(){
		return _employeesNumberToName;
	}
	
	public static HashMap<String , ArrayList<Employee>> getProjectToEmployees() {
		return _projectToEmployees;
	}

	public static void setProjectToEmployees(HashMap<String , ArrayList<Employee>> _projectToEmployees) {
		ManagementSystem._projectToEmployees = _projectToEmployees;
	}

	

	//methods
	



	//system methods
	public static void installSystem() {
		//assuming first time lunched
        String pageTitle = "Welcome To Manage My Start Up:";
        printPageHeaderWithBar(pageTitle); 
        pressEnterToContinue();

        pageTitle = "Setting Up The System:";
        
        //the longest line sets the bar length to make everything look more unified 
        String longestLine = "- Choose a 4-digit Passcode\n";
        printPageHeaderNOBAR(pageTitle); 
        matchBarToString(longestLine);
        
		System.out.println("\n- Create a Manager User\n");
		System.out.println("- Enter Your Name\n");
		System.out.println(longestLine);
		System.out.println("- Get an Employee Number\n");
        matchBarToString(longestLine);

		System.out.println("");
        pressEnterToContinue();

		createFirstManager();
		
	}
	
	public static void startLoginSystem() {
		//assuming users have already been created
        String pageTitle = "Welcome To The Login Window:";
        String longestLine = "3. Sign In And Go To User Window\n";

        printPageHeaderNOBAR(pageTitle);
        matchBarToString(longestLine);
        System.out.println("");

        System.out.println("1. Sign In\n");
        System.out.println("2. Sign Out\n");
        System.out.println(longestLine);
        System.out.println("4. Exit\n");
        matchBarToString(longestLine);
        System.out.println("");

        Integer choice = validateUserMenuChoice(4);

        switch (choice){
            case 1: {signIntoWork(false);}break;
            case 2: {signOutOfWork();}break;
            case 3: {signIntoWork(true);}break;
            case 4: {exitTheProgram();}break;
        }
        //move to appropriate user window for the user
	}//end of method: startLoginSystem

    public static Integer loginProcess(){
        //makes sure that employee number and passcode match
    	int tries = 3;
        Integer employeeNumber;
        String enteredPassword;
        
        //login process
        do {
        	tries--;
        	
        	if(tries == 0) {
        		startLoginSystem();   
        		return -1;//minus 1 because it probably won't break stuff
        	}
        	
        	employeeNumber = getEmployeeNumber();      
            System.out.println();
        	enteredPassword = getUserPasscode();
        }while( (!verifyLogin(employeeNumber, enteredPassword)));

        return employeeNumber;

    }

    public static void signIntoWork(boolean goToUserWindow){
        String pageTitle = "Sign In Window:";
        printPageHeaderWithBar(pageTitle);

        Integer employeeNumber = loginProcess();
        Employee loggedEmployee = _employeesNumberToName.get(employeeNumber);
        
       	loggedEmployee.startWorkDay(goToUserWindow);//true means to go to the user window after login
      
    }//end method: signIntoWork

    public static void signOutOfWork(){

        String pageTitle = "Sign Out Window:";

        printPageHeaderWithBar(pageTitle);
        Integer employeeNumber = loginProcess();
        Employee loggedEmployee = _employeesNumberToName.get(employeeNumber);
        loggedEmployee.endWorkDay();
    }
    
    
    public static Integer getEmployeeNumber(){
        Console console = System.console();
        Integer empl = null;//when exception is thrown--null is send to verifyId
        do{
            try{
                empl = Integer.parseInt(console.readLine("Enter Employee Number: "));
            }catch(Exception NumberFormatException){
                String errorMsg = "YOU MUST ENTER AN INTEGER!";
                System.out.println();
                matchBarToString(errorMsg);
                System.out.println(errorMsg);
                matchBarToString(errorMsg);
                System.out.println();
                
            }
        }while(!verifyEmployeeNumber(empl));

        return empl;
    }
    private static boolean verifyEmployeeNumber(Integer employeeNumber){
    //returns true if id is valid and exists
        if(employeeNumber == null){
            //used to avoid duplicate messages
            //this will catch if the user inputs anything that's NaN
            return false;
        }

        boolean contains;
        if(_employeesNumberToPasscode.containsKey(employeeNumber)){
            contains = true;
        }else{
            String errorMsg = "Employee Number Doesn't Exist!";
            System.out.println();
            matchBarToString(errorMsg);
            System.out.println(errorMsg);
            matchBarToString(errorMsg);
            System.out.println();
            contains = false;
         }
        return contains;
    }//end method: verifyEmployeeNumber

    public static String getUserPasscode(){
        Console console = System.console();
        String passcode = null;//when exception is thrown--null is send to verifyId
        do{
                passcode = new String(console.readPassword("Enter Passcode: "));
            
        }while(!verifyPasscode(passcode));

        return passcode;
    }//end of method: getUserPasscode
        private static boolean verifyPasscode(String pass){
        //returns true if id is valid and exists
            boolean contains;
             if(_employeesNumberToPasscode.containsValue(pass)){
                 contains = true;
             }else{
                String errorMsg = "Wrong Passcode!";
                System.out.println();
                matchBarToString(errorMsg);
                System.out.println(errorMsg);
                matchBarToString(errorMsg);
                System.out.println();
                contains = false;
             }
            return contains;
        }
    
        
	public static boolean verifyLogin(Integer employeeNumber, String pass) {
		//return true if employee number and passcode match match
		boolean loginPass;
		
		if(_employeesNumberToPasscode.get(employeeNumber).equals(pass)){
        
			loginPass = true;
        }else{
			loginPass = false;
            String errorMsg = "Wrong Passcode!";
            System.out.println();
            matchBarToString(errorMsg);
            System.out.println(errorMsg);
            matchBarToString(errorMsg);
            System.out.println();
        }
		
		return loginPass;
	}
	
	
	//user methods
	public static Quaplet createEmployee() {
		
		String firstName;
		String lastName;
		String jobTitle; //CEO, CFO, Junior, Senior
		String passcode;
		
		firstName = scanForName("First");
		
		lastName = scanForName("Last");
	
        checkJobTitle("");//using this to print out the list of valid job titles. kind of sideways, but it's already there so why not...
		jobTitle = scanForJobTitle();
		
		passcode = scanForPasscode();
		
		Quaplet quaple = new Quaplet(firstName, lastName, jobTitle, passcode);
		return quaple;
	}//end method: createEmployee

	public static Manager createManager() {
		Quaplet quaple = createEmployee();
		Manager manager_1 = new Manager(quaple._fName, quaple._lName, quaple._jobTitle, quaple._passcode);
		
		_employeesNumberToPasscode.put(manager_1.get_workerId() ,manager_1.getPasscode());
		_employeesNumberToName.put(manager_1.get_workerId() , manager_1);
		
		return manager_1;
	}
	
	public static Developer createDeveloper() {
		Quaplet quaple = createEmployee();
		Developer dev = new Developer(quaple._fName, quaple._lName, quaple._jobTitle, quaple._passcode);
		
		_employeesNumberToPasscode.put(dev.get_workerId() ,dev.getPasscode());
		_employeesNumberToName.put(dev.get_workerId() , dev);
		
		return dev;
	}
	
	public static Advisor createAdvisor() {
		Quaplet quaple = createEmployee();
		Advisor adv = new Advisor(quaple._fName, quaple._lName, quaple._jobTitle, quaple._passcode);
		
		_employeesNumberToPasscode.put(adv.get_workerId() ,adv.getPasscode());
		_employeesNumberToName.put(adv.get_workerId() , adv);
		
		return adv;
	}
	public static void createFirstManager() {
		//it's...in the name
        String pageTitle = "Create The First Manager Account:";
        printPageHeaderWithBar(pageTitle);
		
		Manager firstManager = createManager();//calling the method that actually creates the Manager object

        howToLogInWindow(firstManager);
        startLoginSystem();
	}//end method: createFirstManager
	
    public static void howToLogInWindow(Employee emp){
        //print a user guide on how to log into the system
        String msg = "How To Log Into The System: ";
        String longestLine = "- The Employee Number Is Assigned Automatically By The System.";
        printPageHeaderNOBAR(msg);
        matchBarToString(longestLine);//this is done for UI--makes things look better

        System.out.println("\n");
		System.out.println("- Enter Your Employee Number");
		System.out.println("- Enter Your Passcode\n\n");
        System.out.println(longestLine);
        matchBarToString(longestLine);//this is done for UI--makes things look better
        System.out.println("\n");
        matchBarToString(longestLine);
		System.out.println(emp.getFullName() + " - Your Employee Number Is: " + emp.get_workerId());
        matchBarToString(longestLine);
        System.out.println("\n");

        pressEnterToContinue();
    }

	public static String scanForPasscode() {
		//scans and return a single word name
		//name can be first or last name
		
		String firstPass="";
		String secondPass="";
        Console LaunchConsole = System.console();
        String errorMsg = "Passcodes Don't Match!\n" + "Please Try Again!\n";
		do
		{
            //will prompt an error message if passcode don't match.
            //i thought it would be redundent to do that check 
            //through an outer method and then print the message from there
            //so i did it here
            //if they don't match: the result is false, and false of false is true
            if(!firstPass.equals(secondPass)){
                matchBarToString(errorMsg);
                System.out.print(errorMsg);
                matchBarToString(errorMsg);
                System.out.println();
            }
            do
            {
            firstPass  = new String(LaunchConsole.readPassword("Enter a 4-digit passcode: "));
			System.out.println("");
            }while(!checkPasscode(firstPass));//if passcode is invalid: user will need to re-enter
		
            secondPass  = new String(LaunchConsole.readPassword("Please Confirm Your Passcode: "));
			System.out.println("");

		}while(!firstPass.equals(secondPass));//making the user confirm the passcode he chose
		
		return firstPass;
	}
	
	
	public static String scanForName(String nameTitle) {
		//scans and return a single word name
		//name can be first or last name
		
        Console LaunchConsole = System.console();
		String name="";
		do
		{
            name = new String(LaunchConsole.readLine("Enter " + nameTitle  + " Name: ") );

			//name = String.valueOf(scan.next());	
			System.out.println("");
		}while(!checkName(name));//if name is invalid: user will be prompt to re-enter a name
		
		//capitalizing first letter
		String capitalizedName = name.substring(0,1).toUpperCase() + name.substring(1);
		
		return capitalizedName;
	}
	
	public static String scanForJobTitle() {
		//scans and return a single word name
		//name can be first or last name
		
        Console LaunchConsole = System.console();
		String title="";
		do
		{
            title = new String(LaunchConsole.readLine("Job Title: ") );	
			System.out.println("");
		}while(!checkJobTitle(title));//if name is invalid: user will be prompt to re-enter a name
		
		//capitalizing first letter
		String properCasedTitle; 
		
        //this if will take care of the casing on the string
        if(title.equals("ceo") || title.equals("cto") || title.equals("cfo")){
            properCasedTitle = title.toUpperCase();//titles like these are all upper caps
        }else{
            properCasedTitle = properCaseWords(title);//usually a word is cased with a capital first letter
        }
		return properCasedTitle;
	}
	

	public static int addEmployee() {
		//increments the employee count by 1
		return ++_numOfEmployees;	
	}
	
	
	public static boolean checkJobTitle(String title) {
		//valid job titles return true
		
        String allLowerCase = title.toLowerCase();
        String properCasedTitle; 
        String[] jobTitles = {"CEO", "CTO", "CFO", "Team Leader", "Senior", "Junior"};

        //this if will take care of the casing on the string
        if(allLowerCase.contains("ceo") || allLowerCase.contains("cto") || allLowerCase.contains("cfo")){
            properCasedTitle = allLowerCase.toUpperCase();//titles like these are all upper caps
        }else{
            properCasedTitle = properCaseWords(title);//usually a word is cased with a capital first letter
        }


		//Message for the user
        String msgHeader ="Valid Job Titles Are:"; 
		String msg = 
             "- CEO\n"
            + "- CTO\n"
           + "- CFO\n"
           + "- Team Leader\n"
           + "- Senior\n"
           + "- Junior";

        for( int i=0; i<jobTitles.length;i++){
            if(properCasedTitle.equals(jobTitles[i])){
                return true;
            }
        }
            matchBarToString(msgHeader);
			System.out.println(msgHeader);
            matchBarToString(msgHeader);
			System.out.println(msg);
            matchBarToString(msgHeader);
            System.out.println("\n");
			return false;
		
	}//end checkJobTitle function
	
	public static boolean checkName(String name) {
		//valid name return true
		
		String lowerName = name.toLowerCase();//converting string to lower case
		char[] nameChars = lowerName.toCharArray();//converting lower case string to char array
        String[] profanityArray = {"fuck", "cunt","tits", "shit", "stupid", 
            "dumb", "ass",  "dick", "ball", "fart", 
            "poop", "urine", "piss", "wtf","niger",
            "kike", "retard", "gay", "fag", "homo",
        "sex"};

		//Message for the user
		String msg = "- Valid Names Can Only Be Actual Names:\n"
            + "- Valid Names Cannot Contain Any Symbols But Letters\n"
            + "- Valid Names Cannot Be a Single Character\n"
           + "- Valid Names Cannot Be a Profanity";
				
        for(int i=0; i<profanityArray .length;i++){
		//a name can't be a single character
        //and a name can't be a prophenity
		if(lowerName.length()<2 || lowerName.contains(profanityArray [i])) {
            matchBarToString(msg);
			System.out.println(msg);
            matchBarToString(msg);
            System.out.println("\n");
			return false;
		}
        }
		//iterate over the name to check if contains only english letters
		for(int i=0; i<nameChars.length;i++ ){
			if(!(nameChars[i] >= 'a' && nameChars[i] <= 'z')) {
            matchBarToString(msg);
			System.out.println(msg);
            matchBarToString(msg);
            System.out.println("\n");
				return false;
			}//end it
		}//end for loop
		
		return true;
	}//end checkName function
	
	
	public static boolean checkPasscode(String passcode) {
		//valid passcode return true
		
		//String passcode = code.toLowerCase();//converting string to lower case
		char[] passcodeCharArray = passcode.toCharArray();//converting lower case string to char array
		
		//Message for the user
		String msg = "- Invalid Passcode!\n"
				+ "- A Valid Passcode Must Be a 4-Digit Number.\n"
				+ "- Please Try Again: ";
				
		
		//a pass code must be 4 digits long
		if(passcode.length() !=4) {
            matchBarToString(msg);
			System.out.println(msg);
            matchBarToString(msg);
            System.out.println("\n");  
			return false;
		}
		
		//iterate over the pass code to check that it contains only digits
		for(int i=0; i<passcodeCharArray.length;i++ ){
			if(!(passcodeCharArray[i] >= '0' && passcodeCharArray[i] <= '9')) {
                    matchBarToString(msg);
                    System.out.println(msg);
                    matchBarToString(msg);
                    System.out.println("\n");  
				return false;
			}//end it
		}//end for loop
		
		return true;
	}//end checkPasscode function

    public static void pressEnterToContinue(){
        //prints to the console and waits till the user presses enter
    	
        //the enter key returns an empty string
        //so the exit condition for the loop is: 
        //only move on when enter is pressed
        Console console = System.console();
        String msg = "Press Enter To Continue...";
        String input = null;
	do{
         try {
           input = new String(console.readLine(msg)); 
		}
		catch(Exception ex){
			{}
		}

    }while(!input.equals(""));

   }//end methos: pressEnterToContinue



    public static void printPageHeaderNOBAR(String pageTitle){
        System.out.print("\033[H\033[2J");  //ANSI escape characters that clear the console--same as clear function in bash
	    System.out.flush(); //guarantees that all buffer content is flushed to the console
        System.out.println(pageTitle);  //ANSI escape characters that clear the console--same as clear function in bash
    }

    public static void printPageHeaderWithBar(String pageTitle){
        //every function will tell this method what header to print
        //and the method will do it in a way that's both saves us from dublicating code
        //and with a title bar with the exact length

        System.out.print("\033[H\033[2J");  //ANSI escape characters that clear the console--same as Ctrl l
	    System.out.flush(); //guarantees that all buffer content is flushed to the console
        System.out.println(pageTitle);  //ANSI escape characters that clear the console--same as clear function in bash
        matchBarToString(pageTitle);
        System.out.println("");  //ANSI escape characters that clear the console--same as clear function in bash
    }


    public static void matchBarToString(String str){
        //prints the exact amount of dashes to fit its string
        String strToMatch;
        String longestLine="";
        
        //if the string is multiple line: that bar will be match to the longest one
        if(str.contains("\n")){
            String[] lines = str.split("\n");
            for(int i=0; i<lines.length; i++){
                if(lines[i].length() > longestLine.length()){
                    longestLine = lines[i];
                }
            }//end of for loop
            strToMatch = longestLine;

        }else{
        strToMatch = str;
        }

        int barSize = strToMatch.length();
        String bar = "";
        for(int i = 0; i<barSize; i++){
            bar += "-";
        }   
        System.out.println(bar);  //ANSI escape characters that clear the console--same as clear function in bash
    }


	
    public static int validateUserMenuChoice(int NumberOfChoices){
   //validates user menu choice. Makes sure he can only enter valid inputs 
		Console console = System.console();
        Integer entry = null;
	    boolean notANumber;


        do{
            try{
            	entry = Integer.parseInt(console.readLine("\nYour Choice: "));
            	
            	if(entry < 1 || entry > NumberOfChoices) {
            		throw new Exception("YOU MUST CHOOSE A NUMBER FROM THE OPTIONS MENU!");
            	}
            	notANumber = false;
            	
            }
            catch(NumberFormatException ex){
				notANumber = true;
                String msg = "YOU MUST CHOOSE A NUMBER FROM THE OPTIONS MENU!";

				System.out.println();
                ManagementSystem.matchBarToString(msg);
				System.out.println(msg);
                ManagementSystem.matchBarToString(msg);
			}
            catch(Exception e){
            	String msg = e.getMessage();
				System.out.println();
                ManagementSystem.matchBarToString(msg);
            	System.out.println(msg);
                ManagementSystem.matchBarToString(msg);
				notANumber = true;
			}
		}while(notANumber);
		
        return entry;

    }//end method: validateUserMenuChoice

	
	public static String properCaseWords(String str) {
		//checks if single or multiple words in the string
		//capitalizes every first letter of every words
		String projectName = "";

        //if the string is empty: it has no case. so return an empty string
        if(str.isEmpty()){
        return "";
        }

		if(str.contains(" ")) {
			String[] words = str.split(" ");
			
			for(int i=0; i< words.length; i++) {
				projectName += words[i].substring(0,1).toUpperCase() + words[i].substring(1).toLowerCase();
				
				if(i < words.length-1) {//adding space after every words, except the last word
					projectName += " ";
				}
			}//end of for loop
			
		}else {
			projectName = str.substring(0,1).toUpperCase() + str.substring(1).toLowerCase();
		}
		return projectName;
	}//end of method: properCaseWords

	public static void exitTheProgram() {
		//only a Manager can close the system
		
		Integer employeeNumber = loginProcess();
		Employee employee = _employeesNumberToName.get(employeeNumber);
		String employeeType = employee.typeOfObject();
		
		if(employeeType.equals("Manager")) {
			printPageHeaderWithBar("GoodBye!");
			
			//sleep for 2 seconds
		}else {
			printPageHeaderWithBar("Only Managers Can Close The System!");
			pressEnterToContinue();
			startLoginSystem();
		}
		
		
	}//end method: exitTheProgram
	
}
