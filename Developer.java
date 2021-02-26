
public class Developer extends Employee {
	
	//constructors
	public Developer(String firstName, String lastName, String passcode) {
		super(firstName, lastName, passcode);
	}
	public Developer(String firstName, String lastName, String title, String passcode) {
		super(firstName, lastName, title, passcode);
	}
	
	public Developer() {
		super();
	}
	@Override
	public void userWindow() {
        String pageTitle = "DEVELOPER USER WINDOW:";
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
	
    public String typeOfObject(){
        return "Developer";
    }
	//use @override for overriding parent methods
}
