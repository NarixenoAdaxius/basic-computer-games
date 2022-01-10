import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;

public class Roulette {

    private PrintStream out;
    private Scanner scanner;

    private int houseBalance, playerBalance;

    private Random random;

    private static Set<Integer> RED_NUMBERS;

    static {
        RED_NUMBERS = Set.of(1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36);
    }

    public static void main(String[] args) {
        new Roulette(System.out, System.in).play();
    }

    public Roulette(PrintStream out, InputStream in) {
        this.out = out;
        this.scanner = new Scanner(in);
        houseBalance = 100000;
        playerBalance = 1000;
        random = new Random();
    }

    public void play() {
        out.println("                                ROULETTE");
        out.println("               CREATIVE COMPUTING  MORRISTOWN, NEW JERSEY");
        out.println("WELCOME TO THE ROULETTE TABLE\n");
        out.println("DO YOU WANT INSTRUCTIONS");
        if(scanner.nextLine().toLowerCase().charAt(0) != 'n') {
            printInstructions();
        }

        while(houseBalance > 0 && playerBalance > 0) {

            Bet[] bets = queryBets();

            out.println("SPINNING...\n\n\n");
            int result = random.nextInt(1,39);

            /*
            Equivalent to following line
            if(RED_NUMBERS.contains(result)) {
                out.println(result + " RED");
            } else {
                out.println(result + " BLACK");
            }
             */
           switch(result) {
               case 37 -> out.print("00");
               case 38 -> out.print("0");
               default ->  out.println(result + (RED_NUMBERS.contains(result) ? " RED\n" : " BLACK\n"));
           }


        }
    }

    private Bet[] queryBets() {
        int numBets = -1;
        while(numBets < 1) {
            out.println("HOW MANY BETS");
            try {
                numBets = Integer.parseInt(scanner.nextLine());
            } catch(NumberFormatException exception) {
                out.println("THAT IS NOT A NUMBER");
            }
        }

        Bet[] bets = new Bet[numBets];

        for(int i = 0; i < numBets; i++) {
            try {
                out.println("BET NUMBER " + (i + 1) + ":");
                String[] values = scanner.nextLine().split(",");
                int betNumber = Integer.parseInt(values[0]);
                int betValue = Integer.parseInt(values[1]);

                for(int j = 0; j < i; j++) {
                    if(bets[j].num == betNumber) {
                        out.println("YOU MADE THAT BET ONCE ALREADY,DUM-DUM");
                        throw new Exception();
                    }
                }

                if(betNumber < 1 || betNumber > 50 || betValue < 5 || betValue > 500) {
                    out.println("INVALID VALUE, TRY AGAIN");
                    i--;
                    continue;
                }

                bets[i] = new Bet(betNumber,betValue);

            } catch(Exception exception) {
                if(exception instanceof NumberFormatException) {
                    out.println("SYNTAX ERROR, TRY AGAIN");
                }
                i--;
            }
        }
        return bets;
    }

    private void betResults(Bet[] bets, int num) {
        for(int i = 0; i < bets.length; i++) {
            Bet bet = bets[i];
            /*
            Using a switch statement of ternary operators that check if a certain condition is met based on the bet value
            Returns the coefficient that the bet amount should be multiplied by to get the resulting value
             */
            int coefficient = switch(bet.num) {
                case 37 -> (num <= 12) ? 2 : -1;
                case 38 -> (num > 12 && num <= 24) ? 2 : -1;
                case 39 -> (num > 24 && num < 37) ? 2 : -1;
                case 40 -> (num < 37 && num % 3 == 1) ? 2 : -1;
                case 41 -> (num < 37 && num % 3 == 2) ? 2 : -1;
                case 42 -> (num < 37 && num % 3 == 0) ? 2 : -1;
                case 43 -> (num <= 18) ? 1 : -1;
                case 44 -> (num > 18 && num <= 36) ? 1 : -1;
                case 45 -> (num % 2 == 0) ? 1 : -1;
                case 46 -> (num % 2 == 1) ? 1 : -1;
                case 47 -> RED_NUMBERS.contains(num) ? 1 : -1;
                case 48 -> !RED_NUMBERS.contains(num) ? 1 : -1;
                case 49 -> (num == 37) ? 35 : -1;
                case 50 -> (num == 38) ? 35 : -1;
                default -> (bet.num < 49 && bet.num == num) ? 35 : -1;
            };

            int betResult = bet.amount * coefficient;
        }
    }

    public void printInstructions() {
        out.println();
        out.println( "THIS IS THE BETTING LAYOUT");
        out.println( "  (*=RED)");
        out.println();
        out.println( " 1*    2     3*");
        out.println( " 4     5*    6 ");
        out.println( " 7*    8     9*");
        out.println( "10    11    12*");
        out.println( "---------------");
        out.println( "13    14*   15 ");
        out.println( "16*   17    18*");
        out.println( "19*   20    21*");
        out.println( "22    23*   24 ");
        out.println( "---------------");
        out.println( "25*   26    27*");
        out.println( "28    29    30*");
        out.println( "31    32*   33 ");
        out.println( "34*   35    36*");
        out.println( "---------------");
        out.println( "    00    0    ");
        out.println();
        out.println( "TYPES OF BETS");
        out.println();
        out.println( "THE NUMBERS 1 TO 36 SIGNIFY A STRAIGHT BET");
        out.println( "ON THAT NUMBER.");
        out.println( "THESE PAY OFF 35:1");
        out.println();
        out.println( "THE 2:1 BETS ARE:");
        out.println( " 37) 1-12     40) FIRST COLUMN");
        out.println( " 38) 13-24    41) SECOND COLUMN");
        out.println( " 39) 25-36    42) THIRD COLUMN");
        out.println();
        out.println( "THE EVEN MONEY BETS ARE:");
        out.println( " 43) 1-18     46) ODD");
        out.println( " 44) 19-36    47) RED");
        out.println( " 45) EVEN     48) BLACK");
        out.println();
        out.println( " 49)0 AND 50)00 PAY OFF 35:1");
        out.println( " NOTE: 0 AND 00 DO NOT COUNT UNDER ANY");
        out.println( "       BETS EXCEPT THEIR OWN.");
        out.println();
        out.println( "WHEN I ASK FOR EACH BET, TYPE THE NUMBER");
        out.println( "AND THE AMOUNT, SEPARATED BY A COMMA.");
        out.println( "FOR EXAMPLE: TO BET $500 ON BLACK, TYPE 48,500");
        out.println( "WHEN I ASK FOR A BET.");
        out.println();
        out.println( "THE MINIMUM BET IS $5, THE MAXIMUM IS $500.");
    }

    public class Bet {
        final int num, amount;

        public Bet(int num, int amount) {
            this.num = num;
            this.amount = amount;
        }
    }
}
