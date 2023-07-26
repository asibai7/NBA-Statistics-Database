import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;
import java.util.ListIterator;

public class P1 {
	
	/* Define data structures for holding the data here */
    public class coach{
        String CoachID = "";
        String Team = "";
        String FirstName = "";
        String LastName = "";
        int Season = 0;
        int seasonWin = 0;
        int seasonLoss = 0;
        int playoffWin = 0;
        int playoffLost = 0;

        public coach(String coachID, int szn, String firstName, String lastName, int sznWin, int sznLoss, int pfWin, int pfLost, String team){
            this.CoachID = coachID;
            this.Team = team;
            this.FirstName = firstName;
            this.LastName = lastName;
            this.Season = szn;
            this.seasonWin = sznWin;
            this.seasonLoss = sznLoss;
            this.playoffWin = pfWin;
            this.playoffLost = pfLost;
        }

    }

    public class team{
        String TeamID = "";
        String Location = "";
        String Name = "";
        Character League = '\0';

        public team(String teamID, String location, String name, Character league){
            this.TeamID = teamID;
            this.Location = location;
            this.Name = name;
            this.League = league;
        }
    }

    ArrayList<coach> CoachList = new ArrayList<coach>();
    ArrayList<team> TeamsList = new ArrayList<team>();

    public P1() {
        CoachList.clear();
        TeamsList.clear();
    }
    
    public void run(){
        CommandParser parser = new CommandParser();

        System.out.println("The mini-DB of NBA coaches and teams");
        System.out.println("Please enter a command.  Enter 'help' for a list of commands.");
        System.out.println();
        System.out.print("> "); 
        
        Command cmd = null;
        while ((cmd = parser.fetchCommand()) != null) {
            
            boolean result=false;
            
            if (cmd.getCommand().equals("help")) {
                result = doHelp();
            } 

            else if (cmd.getCommand().equals("add_coach")) {
                coach newCoach = new coach(cmd.getParameters()[0], Integer.parseInt(cmd.getParameters()[1]), cmd.getParameters()[2], cmd.getParameters()[3], Integer.parseInt(cmd.getParameters()[4]), Integer.parseInt(cmd.getParameters()[5]), Integer.parseInt(cmd.getParameters()[6]), Integer.parseInt(cmd.getParameters()[7]), cmd.getParameters()[8]);
                CoachList.add(newCoach);           
            }

            else if (cmd.getCommand().equals("add_team")) {
                if (cmd.getParameters().length > 4){
                    cmd.getParameters()[1] += " " + cmd.getParameters()[2]; 
                    team newTeam = new team(cmd.getParameters()[0], cmd.getParameters()[1], cmd.getParameters()[3], cmd.getParameters()[4].charAt(0));
                    TeamsList.add(newTeam);
                }
                else{
                    team newTeam = new team(cmd.getParameters()[0], cmd.getParameters()[1], cmd.getParameters()[2], cmd.getParameters()[4].charAt(0));
                    TeamsList.add(newTeam);
                }
            }

            else if (cmd.getCommand().equals("print_coaches")) {
                for(coach i : CoachList){
                    System.out.format("%.9S, %4s, %s, %s, %d, %d, %d, %d, %S\n", i.CoachID, i.Season, i.FirstName, i.LastName, i.seasonWin, i.seasonLoss, i.playoffWin, i.playoffLost, i.Team);
                }
            }
            else if (cmd.getCommand().equals("print_teams")) {
                for(team i : TeamsList){
                    System.out.format("%S, %s, %s, %s\n", i.TeamID, i.Location, i.Name, i.League);
                }
            }
            else if (cmd.getCommand().equals("coaches_by_name")) {
                for (coach i : CoachList){
                    if (i.LastName.toLowerCase().equals(cmd.getParameters()[0].toLowerCase())){
                        System.out.format("%.9S, %4s, %s, %s, %d, %d, %d, %d, %S\n", i.CoachID, i.Season, i.FirstName, i.LastName, i.seasonWin, i.seasonLoss, i.playoffWin, i.playoffLost, i.Team);
                        for (team j : TeamsList){
                            if (i.Team.equals(j.Name)){
                                System.out.format("%S", j.TeamID);   
                            }
                        }
                    }
                }
            }

            else if (cmd.getCommand().equals("teams_by_city")) {
                for(team i : TeamsList){
                    if (i.Location.toLowerCase().equals(cmd.getParameters()[0].toLowerCase())){
                        System.out.format("%S, %s, %s, %s\n", i.TeamID, i.Location, i.Name, i.League);
                        for (coach j : CoachList){
                            if(j.Team.equals(i.TeamID)){
                                System.out.format("Coach: %s %s Year:\n", j.FirstName, j.LastName, j.Season);
                            }
                        }
                    }
                }
            }

            else if (cmd.getCommand().equals("load_coaches")) {

                try{
                    File inputFile = new File(cmd.getParameters()[0]);
                    Scanner scanner = new Scanner(inputFile);

                    scanner.nextLine();
                    
                    while (scanner.hasNextLine()){
                        String[] input = scanner.nextLine().split(",");

                        coach newCoach = new coach(input[0], Integer.parseInt(input[1]), input[2], input[3], Integer.parseInt(input[4]), Integer.parseInt(input[5]), Integer.parseInt(input[6]), Integer.parseInt(input[7]), input[8]);
                        CoachList.add(newCoach); 
                    }
                    scanner.close();
                }
                catch(Exception e){
                    System.out.println(e);
                    
                }
            }

            else if (cmd.getCommand().equals("load_teams")) {
                try{
                    File inputFile = new File(cmd.getParameters()[0]);
                    Scanner scanner = new Scanner(inputFile);

                    scanner.nextLine();
                    
                    while (scanner.hasNextLine()){
                        String[] input = scanner.nextLine().split(",");
                        if (input.length > 4){
                            input[1] += " " + input[2]; 
                            team newTeam = new team(input[0], input[1], input[3], input[4].charAt(0));
                            TeamsList.add(newTeam);
                        }
                        else{
                            team newTeam = new team(input[0],input[1], input[2], input[3].charAt(0));
                            TeamsList.add(newTeam);
                        }
                    }
                    scanner.close();
                }
                catch(Exception e){
                    System.out.println(e);
                    
                }
                
            }
            else if (cmd.getCommand().equals("best_coach")) {
                int max = 0;
                coach biggest = new coach("", 0, "", "", 0, 0, 0 ,0 , "");

                for (coach i : CoachList){
                    int total = (i.seasonWin-i.seasonLoss) + (i.playoffWin-i.playoffLost);
                    if (max < total){
                        max = total;
                        biggest = i;
                    }
                }
                System.out.format("%.9S, %4s, %s, %s, %d, %d, %d, %d, %S\n", biggest.CoachID, biggest.Season, biggest.FirstName, biggest.LastName, biggest.seasonWin, biggest.seasonLoss, biggest.playoffWin, biggest.playoffLost, biggest.Team);
            }
            else if (cmd.getCommand().equals("search_coaches")) {
                int index = 0;

                ArrayList<coach> currentQuery = new ArrayList<coach>(CoachList);

                ListIterator<coach> iterator = currentQuery.listIterator();

                for (String i : cmd.getParameters()){

                    String[] option = i.split("=");

                    if (option[0].equals("coach_id")){
                        String value = cmd.getParameters()[index].substring(cmd.getParameters()[index].lastIndexOf("=")+1);
                        while(iterator.hasNext()){
                            try{
                                coach x = iterator.next();
                                if (!x.CoachID.equals(value)){
                                    iterator.remove();
                                }
                            }
                            catch(Exception e){}
                        }                        
                    } 
                    else if (option[0].equals("season")){
                        int value = Integer.parseInt(cmd.getParameters()[index].replaceAll("[^0-9]", ""));
                        while(iterator.hasNext()){
                            try{    
                                coach x = iterator.next();
                                if (x.Season != value){
                                    iterator.remove();
                                }
                            }
                            catch(Exception e){}
                        }
                    }
                    else if (option[0].equals("first_name")){
                        String value = cmd.getParameters()[index].substring(cmd.getParameters()[index].lastIndexOf("=")+1);
                        while(iterator.hasNext()){
                            try{
                                coach x = iterator.next();
                                if (!x.FirstName.toLowerCase().equals(value.toLowerCase())){
                                    iterator.remove();
                                }
                            }
                            catch(Exception e){}
                        }
                    }                    
            
                    else if (option[0].equals("last_name")){
                        String stringInc = cmd.getParameters()[index].substring(cmd.getParameters()[index].lastIndexOf("=")+1);
                        String value = stringInc;
                        
                        String[] string = stringInc.split("\\+");
                        if (string.length > 1){
                            value = string[0] + " " + string[1];
                        }
                        
                        while(iterator.hasNext()){
                            try{
                                coach x = iterator.next();
                                if (!x.LastName.toLowerCase().equals(value.toLowerCase())){
                                    iterator.remove();
                                }
                            }
                            catch(Exception e){}
                        }
                    }
            
                    else if (option[0].equals("season_wins")){
                        int value = Integer.parseInt(cmd.getParameters()[index].replaceAll("[^0-9]", ""));
                        while(iterator.hasNext()){
                            try{
                                coach x = iterator.next();
                                if (x.seasonWin != value){
                                    iterator.remove();
                                }
                            }
                            catch(Exception e){}
                        }
                    }
            
                    else if (option[0].equals("season_losses")){
                        int value = Integer.parseInt(cmd.getParameters()[index].replaceAll("[^0-9]", ""));
                        while(iterator.hasNext()){
                            try{
                                coach x = iterator.next();
                                if (x.seasonLoss != value){
                                    iterator.remove();
                                }
                            }
                            catch(Exception e){}
                        }
                    }

                    else if (option[0].equals("playoff_wins")){
                        int value = Integer.parseInt(cmd.getParameters()[index].replaceAll("[^0-9]", ""));
                        while(iterator.hasNext()){
                            try{
                                coach x = iterator.next();    
                                if (x.playoffWin != value){
                                    iterator.remove();
                                }
                            }
                            catch(Exception e){}
                        }
                    }
            
                    else if (option[0].equals("playoff_losses")){
                        int value = Integer.parseInt(cmd.getParameters()[index].replaceAll("[^0-9]", ""));
                        while(iterator.hasNext()){
                            try{
                                coach x = iterator.next();
                                if (x.playoffLost != value){
                                    iterator.remove();
                                }
                            }
                            catch(Exception e){}
                        }
                    }
            
                    else if (option[0].equals("team")){
                        String value = cmd.getParameters()[index].substring(cmd.getParameters()[index].lastIndexOf("=")+1);
                        while(iterator.hasNext()){
                            try{
                                coach x = iterator.next();
                                if (!x.Team.equals(value)){
                                    iterator.remove();
                                }
                            }
                            catch(Exception e){}
                        }
                    }
                    else{
                        System.out.printf("Invalid field, %s", cmd.getParameters()[index]);
                        continue;
                    }
                    iterator = currentQuery.listIterator(0);
                    index += 1;
                }
                for(coach i : currentQuery){
                    System.out.format("%.9S, %4s, %s, %s, %d, %d, %d, %d, %S\n", i.CoachID, i.Season, i.FirstName, i.LastName, i.seasonWin, i.seasonLoss, i.playoffWin, i.playoffLost, i.Team);
                }
            }
            else if (cmd.getCommand().equals("exit")) {
                System.out.println("Leaving the database, goodbye!");
                break;
            }
            else if (cmd.getCommand().equals("")) {
            }
            else {
                System.out.println("Invalid Command, try again!");
            } 
                
            if (result) {
                    // ...
            }

            System.out.print("> "); 
        }        
    }
    
    private boolean doHelp() {
        System.out.println("add_coach ID SEASON FIRST_NAME LAST_NAME SEASON_WIN SEASON_LOSS PLAYOFF_WIN PLAYOFF_LOSS TEAM - add new coach data"); 
        System.out.println("add_team ID LOCATION NAME LEAGUE - add a new team");
        System.out.println("print_coaches - print a listing of all coaches");
        System.out.println("print_teams - print a listing of all teams");
        System.out.println("coaches_by_name NAME - list info of coaches with the specified name");
        System.out.println("teams_by_city CITY - list the teams in the specified city");
	    System.out.println("load_coach FILENAME - bulk load of coach info from a file");
        System.out.println("load_team FILENAME - bulk load of team info from a file");
        System.out.println("best_coach SEASON - print the name of the coach with the most netwins in a specified season");
        System.out.println("search_coaches field=VALUE - print the name of the coach satisfying the specified conditions");
		System.out.println("exit - quit the program");        
        return true;
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        new P1().run();
    }
    
    

}
