import java.math.BigInteger;
import java.util.Scanner;

public class BruteForceCrackTimeCalculator {
        public static void main(String[] args) {
                Scanner scanner = new Scanner(System.in);

                BigInteger machines = new BigInteger("1000");
                BigInteger base = new BigInteger("2");
                int exponent = 42;
                BigInteger keysPerSecond = base.pow(exponent);
                BigInteger secondsPerYear = BigInteger.valueOf(60 * 60 * 24 * 365);

                System.out.print("\nMachines = " + (machines)
                                + "\nKeys per second = " + (base) + "^" + (exponent) + " = " + (keysPerSecond)
                                + "\nSeconds per year = 60 * 60 * 24 * 365 = " + (secondsPerYear) + "\n");

                System.out.print("\n[integer] bit encryption? ");
                int bits = scanner.nextInt();
                scanner.close();

                System.out.println("\nTo calculate total possibilities: " + (base) + "^" + bits);
                BigInteger totalPossibilities = base.pow(bits);
                System.out.println("Total possibilities: " + totalPossibilities);

                System.out.println("\nTo calculate total keys per second, per machine: " + (machines) + "*"
                                + (keysPerSecond));
                BigInteger keysPerSecondPerMachine = machines.multiply(keysPerSecond);
                System.out.println("Total keys per second, per machine: " + keysPerSecondPerMachine);

                System.out.println("\nTo calculate total seconds: " + (totalPossibilities) + "/"
                                + (keysPerSecondPerMachine));
                BigInteger totalSeconds = totalPossibilities.divide(keysPerSecondPerMachine);
                System.out.println("Total seconds: " + totalSeconds + "\n");
                BigInteger totalYears = totalSeconds.divide(secondsPerYear);
                System.out.println(
                                "How many years would it take to try all possibilities for " + bits
                                                + " bit encryption? " + totalYears
                                                + "\n");
        }
}