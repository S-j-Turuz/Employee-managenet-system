import java.util.ArrayList;
import java.util.HashMap;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import java.time.format.*;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Employee {

	private String _firstName; //"humanizing the system"
	private String _lastName; //"humanizing the system"
	private String _fullName;
	private String _passcode; 
	private  int _workerId; //is set automatically to uniquely identify a worker
	private String _title;
	private String _currentProject;
    private HashMap<LocalDate, ArrayList<Tuple<LocalTime>>> _attendance;//each tuple is pair of sign-in/sign-out
    private HashMap<LocalDate, ArrayList<String>> _dailyHoursAtWork;
    private HashMap<LocalDate, String> _totalDailyHoursAtWork;
    private int totalDaysWorked = 0;
	private long totalMonthlyTime = 0;
	//constructors
	
	public Employee() {
		//Empty Constructor
	}
	
	public Employee(String firstName, String lastName, String passcode) {
		
		_firstName = firstName;
		_lastName = lastName;
		setFullName(firstName + " " + lastName);
		_passcode = passcode;
		_workerId = ManagementSystem.addEmployee();
        _attendance = new HashMap<LocalDate, ArrayList<Tuple<LocalTime>>>();
        _dailyHoursAtWork = new HashMap<LocalDate, ArrayList<String>>();
        _totalDailyHoursAtWork = new HashMap<LocalDate, String>();
	}
	
	public Employee(String firstName, String lastName, String title, String passcode) {
		
		_firstName = firstName;
		_lastName = lastName;
		setFullName(firstName + " " + lastName);
		_passcode = passcode;
		_workerId = ManagementSystem.addEmployee();
		_title = title;
		
        _attendance = new HashMap<LocalDate, ArrayList<Tuple<LocalTime>>>();
        _dailyHoursAtWork = new HashMap<LocalDate, ArrayList<String>>();
        _totalDailyHoursAtWork = new HashMap<LocalDate, String>();
		
	}
	
	//ToString
	public String ToString() {
		
		return "Name: " + _firstName + " " +_lastName + "\nTitle: " + _title + "\nCurrent Project: " + _currentProject + "\n";
	}
	
	
	//getters and setters
	public String getFirstName() {
		return _firstName;
	}
	
	public void setFirstName(String firstName) {
		//exceptions:
		//name must be more than one character long
		//name can only contain Latin(English) characters
		_firstName = firstName;
	}
	
	public String getLastName() {
		return _lastName;
	}
	
	public void setLastName(String lastName) {
		//exceptions:
		//name must be more than one character long
		//name can only contain Latin(English) characters
		_lastName = lastName;
	}
	
	public String getFullName() {
		return _fullName;
	}

	public void setFullName(String _fullName) {
		this._fullName = _fullName;
	}

	
	public int get_workerId() {
		return _workerId;
	}

	public void set_workerId(int workerId) {
		_workerId = workerId;
	}

	public String getPasscode() {
		return _passcode;
	}
	
	public void setPasscode(String passcode) {
		//exceptions:
		//can only be an existing job title
		//if new title: create the title first in the list of titles 
		_passcode = passcode;
	}
	
	public String getTitle() {
		return _title;
	}
	
	public void setTitle(String title) {
		//exceptions:
		//can only be an existing job title
		//if new title: create the title first in the list of titles 
		_title = title;
	}
	public String getCurrentProject() {
		return _currentProject;
	}
	
	public void setCurrentProject(String currentProject) {
		//exceptions:
		
		_currentProject = currentProject;
	}
	
	
	//methods
	public void startWorkDay(boolean goToUserWindow) {
		//takes current time and puts in database
        LocalDate thisDate = LocalDate.now();
        LocalTime thisTime = LocalTime.now().withNano(0);
        
        //checking if user already signed in and wants to go to his user window
        
        if(goToUserWindow && !(hasSignedOut())) {
        	this.userWindow();
        }

        //if first sign in of the day--create a list for today
        if(!this._attendance.containsKey(thisDate)){
        	
            //no date key: means first sign-in of the day
            //if it's the first sign-in: we need to create the arraylist for today
            ArrayList<Tuple<LocalTime>> arrayListOfTheDay = new ArrayList<Tuple<LocalTime>>();
            this._attendance.put(thisDate, arrayListOfTheDay);
            arrayListOfTheDay.add(new Tuple<LocalTime>(null));//creates a new empty tuple so user can log again today

            //creating the duration list of the day
            ArrayList<String> listOfTodaysDuration = new ArrayList<String>();
            this._dailyHoursAtWork.put(thisDate, listOfTodaysDuration);
            
        }else{
        	
            //IF ALREADY CHECKED-IN WITHOUT CHECKING OUT: DON'T ALLOW TO CHECK-IN AGAIN
            ArrayList<Tuple<LocalTime>> arrList =  this._attendance.get(thisDate);
            int indexOfLastTuple = arrList.size() - 1;
            Tuple<LocalTime> currentLastTupleInArrayList = arrList.get(indexOfLastTuple);
          
            if(currentLastTupleInArrayList._open != null){
                //check if the last (current) tuple in the list
                //has an sign-in value
            
                String msg = "- YOU ALREADY SIGNED INTO WORK TODAY AT " + currentLastTupleInArrayList._open + "!";
                String longestLine = "- IF THERE WAS A MISTAKE WITH YOUR TIME: PLEASE INFORM YOUR MANAGER.";
                ManagementSystem.printPageHeaderNOBAR("");
                ManagementSystem.matchBarToString(longestLine);
                System.out.println(msg + "\n\n" + longestLine);
                ManagementSystem.matchBarToString(longestLine);

                System.out.println("");
                ManagementSystem.pressEnterToContinue();
                ManagementSystem.startLoginSystem();
                return;
            }

        }

        //printing page title and what not
        String pageTitle = "Start Your Work Day: " + this.getFullName();
        String pageTitleInfo = "Current Date: " + thisDate +"\n" + "Current Time: " + thisTime.withSecond(0) +"\n";
        ManagementSystem.printPageHeaderWithBar(pageTitle);
        System.out.println(pageTitleInfo);
        ManagementSystem.matchBarToString(pageTitle);
        String msg ="Have a Nice Productive Day!";
        ManagementSystem.printPageHeaderWithBar(pageTitle);
        System.out.println(pageTitleInfo);
        System.out.println(msg);
        ManagementSystem.matchBarToString(pageTitle);
        System.out.println();

        //updating the the list in the hashmap that stores the time stamps
        Tuple<LocalTime> open = new Tuple<LocalTime>(thisTime);
        ArrayList<Tuple<LocalTime>> listOfTodaysAttendance = this._attendance.get(thisDate);
        int indexOfLastTuple = listOfTodaysAttendance.size() - 1;
        listOfTodaysAttendance.set(indexOfLastTuple, open);
        this._attendance.put(thisDate, listOfTodaysAttendance);
        
        //go back to login window after signing in
        ManagementSystem.pressEnterToContinue();
        
        //true: go to user window; else go back to main login window
        if(goToUserWindow) {
        	this.userWindow();
        }else {
        	ManagementSystem.startLoginSystem();
        }
        
	}//end method: startWorkDay
	
	
	public void endWorkDay() {
		//takes current time and puts in database
        LocalDate thisDate = LocalDate.now();
        LocalTime thisTime = LocalTime.now().withNano(0);
         
        //you can't set an end time without a start time
        if(!this._attendance.containsKey(thisDate)){

            String pageTitle = "YOU HAVEN'T SINGED IN TODAY!";
            String longestLine ="\n- If You Forgot To Sign In: Please Inform Your Manager.\n";
            ManagementSystem.printPageHeaderNOBAR(pageTitle);
            ManagementSystem.matchBarToString(longestLine);
            System.out.println(longestLine);
            ManagementSystem.matchBarToString(longestLine);
            System.out.println();
            ManagementSystem.pressEnterToContinue();
            ManagementSystem.startLoginSystem();
            return;

        }
        
        //IF ALREADY LEFT: DON'T ALLOW TO CHANGE LEAVE TIME
        ArrayList<Tuple<LocalTime>> arLst =  this._attendance.get(thisDate);
        int indexOfLastTuple = arLst.size() - 1;
        Tuple<LocalTime> currentLastTupleInArrayList = arLst.get(indexOfLastTuple);
        
        if(currentLastTupleInArrayList._open == null){
            
            String pageTitle = "YOU ALREADY SIGNED OUT TODAY!";
            String longestLine ="\n- If There Was a Mistake With Your Time: Please Inform Your Manager.\n";
            ManagementSystem.printPageHeaderNOBAR(pageTitle);
            ManagementSystem.matchBarToString(longestLine);
            System.out.println(longestLine);
            ManagementSystem.matchBarToString(longestLine);
            System.out.println();
            ManagementSystem.pressEnterToContinue();
            ManagementSystem.startLoginSystem();

            return;
        }

        //this will execute if haven't left, and did sign in
        
       

        //adding the end time to the employee's attendance
        ArrayList<Tuple<LocalTime>> listOfTodaysAttendance = this._attendance.get(thisDate);
        indexOfLastTuple = listOfTodaysAttendance.size() - 1;
        LocalTime startTime = listOfTodaysAttendance.get(indexOfLastTuple)._open;//takes start time from start tuple
        Tuple<LocalTime> fullDay = new Tuple<LocalTime>(startTime, thisTime);//creats new tuple with full time--start and end

        listOfTodaysAttendance.set(indexOfLastTuple, fullDay);//updates the last tuple in the list
        this._attendance.put(thisDate, listOfTodaysAttendance);


        //preparing time string for saving and printing
        Duration currentDuration = Duration.between(startTime, thisTime);
        Long currentDurationInSeconds = currentDuration.getSeconds();

        //the total duration is the sum of all the durations
        Long totalDurationInSeconds = 0L;// = totalDailyDuration.getSeconds();
        Tuple<LocalTime> tpl;
        Duration tplDuration;
        for(int i=0; i<listOfTodaysAttendance.size();i++){
                tpl  = listOfTodaysAttendance.get(i);
                tplDuration = Duration.between(tpl._open, tpl._close);
                totalDurationInSeconds += tplDuration.getSeconds();
        }
        
        //updating the total number of seconds the worker worked this month
        this.totalMonthlyTime += totalDurationInSeconds;

        listOfTodaysAttendance.add(new Tuple<LocalTime>(null));//creates a new empty tuple so user can log again today

        //currentDuration shoes start time and end time and total time
        String topString ="- Date:  " + thisDate  + " | Day: " + thisDate.getDayOfWeek();
        String duration = (currentDurationInSeconds/3600)%24 + "h " + (currentDurationInSeconds/60)%60 + "m " + currentDurationInSeconds%60 + "s "; 
        String stringOfCurrentDuration = "- In:  " + startTime + " | Out: " + thisTime + " | Duration: " + duration + "";
        String totalDuration = (totalDurationInSeconds/3600)%24 + "h " + (totalDurationInSeconds/60)%60 + "m " + totalDurationInSeconds%60 + "s "; 
        String bottomString = "Total Time Today: " + totalDuration;

        ArrayList<String> durations = this._dailyHoursAtWork.get(thisDate);
        durations.add(stringOfCurrentDuration);
        
        //actual printing 
        String pageTitle = "GoodBye: " + this.getFullName();
        ManagementSystem.printPageHeaderNOBAR(pageTitle); 
        ManagementSystem.matchBarToString(stringOfCurrentDuration);
        System.out.println();
        System.out.println(topString);
        ManagementSystem.matchBarToString(stringOfCurrentDuration);

        for(int i=0; i<durations.size();i++){
            System.out.println(durations.get(i));
            ManagementSystem.matchBarToString(stringOfCurrentDuration);
        }
        //System.out.println(stringOfCurrentDuration);

        System.out.println();
        System.out.println(bottomString);
        ManagementSystem.matchBarToString(stringOfCurrentDuration);
        System.out.println();

        //saving the amount of hours the employe worked today 
        //for making the paycheck in the end of the month
        this._dailyHoursAtWork.put(thisDate, durations);
        this._totalDailyHoursAtWork.put(thisDate, totalDuration);
        
        this.totalDaysWorked++;
        
        ManagementSystem.pressEnterToContinue();
        ManagementSystem.startLoginSystem();



    	}//end method: endWorkDay

        public boolean hasSignedOut(){
            //returns true if the user is trying to exit his account during an active session
            //an active session is a session with a start time, but no end time
            LocalDate thisDate = LocalDate.now();
            ArrayList<Tuple<LocalTime>> todaysList =  this._attendance.get(thisDate);
            Tuple<LocalTime> lastSession;

            try{
                //if the user hasn't signed in at all today--the list doesn't exist!
                lastSession = todaysList.get(todaysList.size()-1);
            }catch(NullPointerException ex){
                return true;
            }

           if(lastSession._open != null && lastSession._close == null){
                return false;
            }
            return true;
        }


        
	    public void printMyMonthlyTime(){
	    	//prints the hour and date to the console
	    	
	    	if(this._totalDailyHoursAtWork.isEmpty()) {
	    		
	    		ManagementSystem.printPageHeaderWithBar("No Data Yet");
	    		   ManagementSystem.pressEnterToContinue();
	    		   this.userWindow();
	    		   return;
	    		
	    	}
	    	
		    ArrayList<String> datesAndTime = new ArrayList<String>();
	        String longestLine = "";
	        String totalMonthTime = (this.totalMonthlyTime/3600) + "h " + (this.totalMonthlyTime/60)%60 + "m " + this.totalMonthlyTime%60 + "s ";
	        //k is the key in the hashmap. It's a LocalTime object
	        //v is the value of k in the hashmap. It's a String object.
	        this._totalDailyHoursAtWork.forEach((k, v) -> datesAndTime.add("- Date: " + k + " | Day: " + k.getDayOfWeek()+ " | Total Time: " + v ));
	         

	        //will find the longest string 
        	//so all the bars match it
	        for(int i=0; i< datesAndTime.size();i++) {
	        	
	        	if(datesAndTime.get(i).length() > longestLine.length()) {
	        		longestLine = datesAndTime.get(i);
	        	}
	        }
	        
	        
            String pageTitle = this.getFullName() + "'s MONTHLY ATTENDANCE TABLE:";
	        ManagementSystem.printPageHeaderNOBAR(pageTitle);
	        ManagementSystem.matchBarToString(longestLine);
	        System.out.println();
	        
	        for(int i=0; i< datesAndTime.size();i++) {
	        	ManagementSystem.matchBarToString(longestLine);
	        	System.out.println(datesAndTime.get(i));
	        	
	        }//end printing for loop
	        
	        ManagementSystem.matchBarToString(longestLine);
        	System.out.println();
        	
        	System.out.println("Number Of Days Worked: " + this.totalDaysWorked); 
        	ManagementSystem.matchBarToString(longestLine);
        	System.out.println("Total Monthly Time: " + totalMonthTime);
        	ManagementSystem.matchBarToString(longestLine);
          	System.out.println();
	        	
	        ManagementSystem.pressEnterToContinue();
	        this.userWindow();

    }//end method: printMyMonthlyTime

	public abstract void userWindow();

    public abstract String typeOfObject();
}
