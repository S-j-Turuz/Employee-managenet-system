public class Advisor extends Employee {

	//constructors
	public Advisor (String firstName, String lastName, String passcode) {
		super(firstName, lastName, passcode);
	}
	public Advisor(String firstName, String lastName, String title, String passcode) {
		super(firstName, lastName, title, passcode );
	}
	
	public Advisor () {
		super();
	}


    public String typeOfObject(){
        return "Advisor";
    }

    public void userWindow(){
    
    	String pageTitle = "ADVISOR USER WINDOW:";
    	String longestLine = "- For security reasons: please exit your account once finished.\n";
        ManagementSystem.printPageHeaderNOBAR(pageTitle);
        ManagementSystem.matchBarToString(longestLine);
 		System.out.println("\n- Welcome " + this.getFullName()+ " \n");
 		System.out.println(longestLine);
        ManagementSystem.matchBarToString(longestLine);
 		System.out.println("\n1. Print Monthly Attendance\n");//add an option to choose a month
 		System.out.println("2. Exit Your Account\n");
 		System.out.println("3. End Work Day\n");
        ManagementSystem.matchBarToString(longestLine);

        Integer choice = ManagementSystem.validateUserMenuChoice(3);
        
 		switch (choice) {
 			case 1: {this.printMyMonthlyTime();}break;
 			case 2: {ManagementSystem.startLoginSystem();}break;
 			case 3: {this.endWorkDay();}break;
 		}
    	
		
    }

}
