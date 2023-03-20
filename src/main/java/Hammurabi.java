package hamurabi.src.main.java;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Random;


public class Hammurabi {
    Scanner in = new Scanner(System.in);
    Random rand = new Random();
    StringBuilder sb = new StringBuilder();
    String currentStats;

    private int currentPop, currentYear, currentAcres, currentGrain, currentPlantedAcres, currentAcreValue;
    private int citizensArrived, plagueDeaths, starvationDeaths;
    private int totalDeaths, highestPop, highestGrain, totalStarved, totalNewCitizens, totalGrownGrain, happinessTracker,
            totalRatEatGrain, totalPlagueDeaths;
    private double ratFeast;
    boolean isStarving = false;
    boolean isPlayable = true;
    boolean isBuyTime = false;
    boolean isSellTime = false;
    boolean popUprose = false;


    public static void main (String[] args){
        Hammurabi hamurabiGame = new Hammurabi();
        hamurabiGame.playGame();
    }

    public String setUpThings(){
        currentGrain = 2800;
        currentPop = 100;
        currentAcres = 1000;
        currentAcreValue = 19;
        citizensArrived = 0;
        starvationDeaths = 0;
        currentYear = 1;
        happinessTracker = 0;
        highestGrain = currentGrain;
        highestPop = currentPop;

        String startingString = "\n\nThe year is 1780 B.C. The people of the great city Babylon have entrusted you, Hamurabi, as their ruler.\n" +
                "Entrusted to you are the fields of grain, and their harvest.  The people sustain themselves by grain, but the grain is also a central " +
                "point in our economy.\nYou will have to spend grain to make grain, but in doing so, our city will thrive. " +
                "Our city has had known issues with both plague and rats. Be careful to watch our reserves.\n" +
                "Never let our people go hungry. O great Hamurabi, we have put our trust in you. Do not let our people starve.";
        sb.append(startingString);

        return sb.toString();
    }
    public void playGame(){
        System.out.println(setUpThings());
        while(isPlayable) {
            sb.setLength(0);
            currentStats = "\n\nToday marks the beginning of year " + currentYear + " of your ten year rule of Babylon. Currently you have: " + currentPop + " citizens." +
                    "\n"+ currentGrain + " grain left in storage. \nWe own "+ currentAcres + " acres of grain fields, valued at " + currentAcreValue + " bushels of grains per acre." +
                    "\nIn the previous year, " + starvationDeaths + " citizens starved.\n" + citizensArrived +" citizens immigrated to the city.";
            sb.append(currentStats);
            System.out.println(currentStats);
            getPlayerInput();
            yearEnd();
            currentYear++;
            if(currentYear > 10){
                isPlayable = false;
            }
        }
        System.out.println(finalSummary());
        System.exit(1);
    }

    public int getPlayerInput(){
        buyLand();
        currentGrain -= feedPeople();
        currentGrain += (currentAcreValue * sellAcres());
        currentGrain -= (plantGrain()*2);
        return currentGrain;
    }

    public int buyLand(){
        sb.setLength(0);
        sb.append("\n\nCurrently you have " + currentGrain + " bushels of grain. Each acre of land is valued at " + currentAcreValue +
                ". How many acres of land would you like to buy.");
        System.out.println(sb);
        isBuyTime = true;
        int acresBought;
        while(isBuyTime){
            try {
                acresBought = in.nextInt();
                if((acresBought*currentAcreValue) >currentGrain){
                    System.out.println("Surely you jest. You do not have the grain to do that!");
                }else{
                    isBuyTime = false;
                    currentGrain -= (acresBought * currentAcreValue);
                    currentAcres += acresBought;
                    return acresBought;
                }
            }catch(InputMismatchException e){
                System.out.println("You silly old fool, enter a viable number!");
            }
        }
        return currentGrain;
    }

    public int feedPeople(){
        sb.setLength(0);
        sb.append("\nCurrently you have " + currentGrain + " bushels of grain. To feed your people completely it would cost " +
                (currentPop *20) + ". You can feed them more than that if you'd like. \nHow much would you like to feed your people?");
        System.out.println(sb);
        int grainToFeed = spendingGrain();
        if(grainToFeed == (currentPop*20)){
            isStarving = false;
            System.out.println("Your city was completely fed. The citizens are content with your rule.");
            return grainToFeed;
        }else if(grainToFeed> (currentPop*20)){
            isStarving = false;
            System.out.println("You are very generous, O gracious leader. The people are very happy.");
            happinessTracker ++;
            return grainToFeed;
        }else{
            isStarving = true;
            currentPop -= starvationTime(grainToFeed);
        }
        return grainToFeed;
    }

    public int spendingGrain(){
        isBuyTime = true;
        int grainSpent;
        while(isBuyTime){
            try {
                grainSpent = in.nextInt();
                if(grainSpent>currentGrain){
                    System.out.println("Surely you jest. You do not have the grain to do that!");
                }else{
                    isBuyTime = false;
                    return grainSpent;
                }
            }catch(InputMismatchException e){
                System.out.println("You silly old fool, enter a viable number!");
            }
        }
        return currentGrain;
    }

    public int sellAcres(){
        sb.setLength(0);
        sb.append("\nCurrently we have " + currentGrain + " bushels of grain. We have " + currentAcres +
                " acres of land. Each acre of land is valued at " + currentAcreValue + " bushels of grain per acre.\nHow many acres of land would you like to sell.");
        System.out.println(sb);
        isSellTime = true;
        int grainToAcquire;
        while(isSellTime){
            try {
                grainToAcquire = in.nextInt();
                if(grainToAcquire>currentAcres){
                    System.out.println("O great leader, your age must be getting to you. You do not have the acres to do that!");
                }else{
                    isSellTime = false;
                    currentAcres -= grainToAcquire;
                    return grainToAcquire;
                }
            }catch(InputMismatchException e){
                System.out.println("You silly old fool, enter a viable number!");
            }
        }
        return 0;
    }

    public int plantGrain(){
        int maxAcresPlant = currentPop *10;
        sb.setLength(0);
        sb.append("\nCurrently we have " + currentGrain + " bushels of grain. The population is " + currentPop + " citizens. We have " + currentAcres + " acres of land.\n" +
                "Each citizen can plant 10 acres of land, at the cost of 2 bushels of grains per acre.\nHow many acres would you like to plant?");
        System.out.println(sb);
        int acresDesired = 0;
        boolean isPlantTime = true;
        while(isPlantTime){
            try {
                acresDesired = in.nextInt();
                if(acresDesired > currentAcres || acresDesired > maxAcresPlant){
                    System.out.println("You Great Fool. We do not have that many acres to plant!");
                }else if (acresDesired > (currentPop*10)) {
                    System.out.println("We do not have the people to support such a plant.");
                }else if((acresDesired*2) > currentGrain) {
                    System.out.println("We do not have the grain to support such a plant.");
                }else{
                    currentPlantedAcres = acresDesired;
                    isPlantTime = false;
                    return acresDesired;
                }
            }catch(InputMismatchException e){
                System.out.println("You silly old fool, enter a viable number!");
            }
        }
        return 0;
    }

    public int plagueTime(){
        //15%
        if(rand.nextInt(100)< 15){
            plagueDeaths = currentPop/2;
            currentPop /=2;
            totalPlagueDeaths+= plagueDeaths;
            totalDeaths += plagueDeaths;
            System.out.println("\nOur city has been hit with a terrible plague, we have lost " + plagueDeaths + " citizens.");
        }

        return plagueDeaths;
    }

    public int ratTime(){
        //40%
        if(rand.nextInt(100) < 40){
            double percentDecrease = rand.nextInt(10)+21;
            percentDecrease /= 100;
            ratFeast = currentGrain * percentDecrease;
            totalRatEatGrain += ratFeast;
            currentGrain -= (int) ratFeast;
            System.out.println("\nOur grain reserves have been infested with rats!! We have lost a total of " + (int) ratFeast
            + " bushels of grain! Where is that dang rat poison!?");
        }
        return currentGrain;
    }

    public int starvationTime(int grainsFed){
        int grainsNeeded = currentPop *20;
        int citizensDead = 0;
        double upRisingLimit = currentPop * 0.45;
        //determine the number of deaths
        for(int i = grainsFed; i < grainsNeeded; i+=20){
            citizensDead++;
        }
        //adjust total values of deaths
        totalDeaths += citizensDead;
        starvationDeaths += citizensDead;
        totalStarved += starvationDeaths;
        System.out.println("\nYour city is starving!! As a result " + citizensDead + " citizens have died!");
        //if enough citizens died, cause uprising
        if(citizensDead > upRisingLimit){
            upRising();
        }
        return citizensDead;
    }

    public int immigrationTime(){
        //determine how many people immigrate to the city
        if(!isStarving){
            citizensArrived = (20 * currentAcres + currentGrain) / (100 * currentPop) + 1;
            totalNewCitizens += citizensArrived;
            currentPop += citizensArrived;
            //adjust high score of population
            if(currentPop>highestPop){
                highestPop = currentPop;
            }
        }
        return currentPop;
    }

    public int landCost(){
        //change the value of land to a random number between 17 and 23
        currentAcreValue = rand.nextInt(7)+17;
        return currentAcreValue;
    }

    public void upRising(){
        //close out the game
        sb.setLength(0);
        sb.append("We have lost majority of our population. You have disgraced this city. The citizens have formulated a plot against you.\n" +
                "They demand your seat of power... and your head. \n\nGame Over.");
        System.out.println(sb);
        popUprose = true;
        isPlayable = false;
        finalSummary();
    }

    public int harvestTime(){
        int harvestRate = rand.nextInt(6)+1;
        int grownGrain = harvestRate * currentPlantedAcres;
        totalGrownGrain += grownGrain;
        currentGrain += grownGrain;
        //set high score of grain
        if(currentGrain>highestGrain){
            highestGrain = currentGrain;
        }
        //display messages to the player depending on how much grain they harvested
        if(harvestRate == 1){
            System.out.println("\nThis year's harvest has been pitiful, we have lost more grain than we gained." +
                    " We harvested " + grownGrain + " bushels of grain, bringing our current stores to " + currentGrain + " bushels of grain."
                    + " Hopefully next year is better.");
        }else if(harvestRate == 2){
            System.out.println("\nThis year's harvest has been adequate, at least we did not lose any grain. We harvested "
                    + grownGrain + " bushels of grain, bringing our current stores to " + currentGrain +" bushels of grain.");
        }else if(harvestRate < 6){
            System.out.println("\nThis year's harvest has been plentiful." + " We harvested " + grownGrain + " bushels of grain, bringing our current stores to "
                    + currentGrain + " bushels of grain. What a joy it is to see our crops thrive.");
        }else{
            System.out.println("\nThis year's harvest has been overwhelmingly good. We harvested " + grownGrain
                    + " bushels of grain, bringing our current stores to " + currentGrain +
                    " bushels of grain. The grain is plentiful, and the people respect your leadership.");
        }
        return currentGrain;
    }

    public void yearEnd(){
        plagueTime();
        ratTime();
        immigrationTime();
        landCost();
        harvestTime();
    }

    public String finalSummary(){
        sb.setLength(0);
        sb.append("\nAt the end of your rule you had a total death count of " + totalDeaths + ".\nYou grew a total of " + totalGrownGrain + " bushels of grain."
                + "\nYou enticed " + totalNewCitizens + " new citizens to join your city.\nThe rats ate a total of " +totalRatEatGrain + " bushels of grain."
                + "\nThe plague claimed a total of " + totalPlagueDeaths +" people.\nYou starved a total of "+ totalStarved +" citizens."
                +"\n\nYour highest population was " + highestPop + " citizens.\nThe highest amount of grain you had stored was " +highestGrain +".");
        if(popUprose){
            sb.append("\n\nFinal rating:\n\nThe city is most likely better off without you. Maybe the rats and the plagues are to blame, but the people do not care.\n" +
                    "You chose to lead, so you have to deal the the consequences. It doesn't seem as though the angry mob will listen to reason though.");
            System.out.println(sb);
            System.exit(1);
        }
        if(happinessTracker == 0){
            sb.append("\n\nFinal rating:\n\nYou ruled the people hard. You may have given your populace enough food to live, but that was all you did. " +
                    "You ruled all ten years, but the people will not remember you for your kindness. None will sing songs of you in the future.");
        }else if(happinessTracker > 0 && happinessTracker <= 3){
            sb.append("\n\nFinal rating:\n\nThe long ten years have come to a close and the people are grateful for the small amount of kindness you have shown them." +
            "You did not have to share more with the people of Babylon, but you did. It wasn't much, but the people noticed and praise you for it.");
        }else if(happinessTracker >=4 && happinessTracker<=7){
            sb.append("\n\nFinal rating:\n\nThe people noticed your generosity, however small, and praise you generously for your rule." +
                    " So much so, they have decided to re-elect you immediately.");
        }else if(happinessTracker>=8 && happinessTracker<=9){
            sb.append("\n\nFinal rating:\n\nYou have kept the city well fed, so well fed that they have named a holiday after you." +
                    " The Hammurabi Hammy Time. A full week off of work to celebrate the love they have for you.");
        }else if(happinessTracker == 10){
            //not done
            sb.append("\n\nFinal rating:\n\nThe people of Babylon have determined you as God reincarnated." +
                    " As a result they have erected statues of your likeness throughout the city. " +
                    "There are constant parades in your honor, and your every whim and desire is always met by a smattering of servants.");
        }
        return sb.toString();
    }


}
