



import java.util.Arrays;

/**
 * @overview A program that performs the coffee tin game on a 
 *    tin of beans and display result on the standard output.
 *
 */
public class CoffeeTinGame {
    /** constant value for the green bean */
    private static final char GREEN = 'G';
    /** constant value for the blue bean */
    private static final char BLUE = 'B';
    /** constant for removed beans */
    private static final char REMOVED = '-';
    /** the null character */
    private static final char NULL = '\u0000';
    // create constant array BeansBag
    public static char[] BeansBag = {BLUE, BLUE, BLUE, BLUE, BLUE, BLUE, BLUE, BLUE, BLUE, BLUE, BLUE, BLUE, 
        GREEN, GREEN, GREEN, GREEN, GREEN, GREEN, GREEN, GREEN, GREEN, GREEN, GREEN, GREEN, GREEN, GREEN, 
        REMOVED, REMOVED, REMOVED, REMOVED, REMOVED, REMOVED, REMOVED, REMOVED, REMOVED, REMOVED, REMOVED, REMOVED};
    /**
     * the main procedure
     * @effects
     *    initialise a coffee tin
     *    {@link TextIO#putf(String, Object...)}: print the tin content
     *    {@link @tinGame(char[])}: perform the coffee tin game on tin
     *    {@link TextIO#putf(String, Object...)}: print the tin content again
     *    if last bean is correct
     *      {@link TextIO#putf(String, Object...)}: print its colour
     *    else
     *      {@link TextIO#putf(String, Object...)}: print an error message
     */
    public static void main(String[] args) {

        // expected last bean
        // p0 = green parity /\
        // (p0=1 -> last=GREEN) /\ (p0=0 -> last=BLUE)
        // count number of greens
        int greens = 0;
        for (char bean : BeansBag) {
            if (bean == GREEN)
                greens++;
        }
        // expected last bean
        final char last = (greens % 2 == 1) ? GREEN : BLUE;

        // print the content of tin before the game
        System.out.printf("%nTIN (%d Gs): %s %n", greens, Arrays.toString(BeansBag));
        // perform the game
        // get actual last bean
        char lastBean = tinGame(BeansBag);
        // lastBean = last \/ lastBean != last

        // print the content of tin and last bean
        System.out.printf("tin after: %s %n", Arrays.toString(BeansBag));

        // check if last bean as expected and print
        if (lastBean == last) {
            System.out.printf("last bean: %c%n", lastBean);
        } else {
            System.out.printf("Oops, wrong last bean: %c (expected: %c)%n", lastBean, last);
        }
    }
    /**
     * Performs the coffee tin game to determine the colour of the last bean
     *
     * @requires tin is not null /\ tin.length > 0
     * @modifies tin
     * @effects
     *   take out two beans from tin
     *   if same colour
     *     throw both away, put one blue bean back
     *   else
     *     put green bean back
     *   let p0 = initial number of green beans
     *   if p0 = 1
     *     result = `G'
     *   else
     *     result = `B'
     */
    public static char tinGame(char[] tin) {
        while (hasAtLeastTwoBeans(tin)) {
            
            // take two beans from tin
            char[] twoBeans = takeTwo(tin);
            // process beans to update tin
            char b1 = twoBeans[0];
            char b2 = twoBeans[1];
            // process beans to update tin
            if (b1 == b2) {
                updateTin(tin, b1, BLUE); // update tin with blue bean
            } else { // BG, GB
                updateTin(tin, b1, GREEN); // update tin with green bean
            }
        }
        
        return anyBean(tin);
    }

    /**
     * @effects
     *  if tin has at least two beans
     *    return true
     *  else
     *    return false
     */
    private static boolean hasAtLeastTwoBeans(char[] tin) {
        int count = 0;
        for (char bean : tin) {
            if (bean != REMOVED) {
                count++;
            }

            if (count >= 2) // enough beans
                return true;
        }

        // not enough beans
        return false;
    }

    /**
     * @requires tin has at least 2 beans left
     * @modifies tin
     * @effects
     *  remove any two beans from tin and return them
     */
    private static char[] takeTwo(char[] tin) {
        char first = takeOne(tin);
        char second = takeOne(tin);

        return new char[]{first, second};
    }

    /**
     * @requires tin has at least one bean
     * @modifies tin
     * @effects
     *   remove any bean from tin and return it
     */
    public static char takeOne(char[] tin) {
         // Count the number of available beans in the bag

         while(true){
             int index = randInt(tin.length);
             char bean = tin[index];
             if (bean != REMOVED) {  // found one
                 tin[index] = REMOVED;
                 return bean;
         }
         }
    }

    /**
 * @requires tin is not null and has at least two elements
 *           bean1 and bean2 are valid bean types
 * @modifies tin
 * @effects swaps the positions of bean1 and bean2 in the tin array if both are present in the array
 *          otherwise, does nothing
 */
    private static void updateTin(char[] tin, char bean1, char bean2) {
        char desiredBean = (bean1 == 'G') ? 'B' : 'G'; // the desired bean is the opposite type of bean1
        char newBean = getBean(BeansBag, desiredBean); // get a new bean from the BeansBag of the desired type
        for (int i = 0; i < tin.length; i++) {
            if (tin[i] == ' ') {
                tin[i] = newBean; // place the new bean in the first available space in the tin
                break;
            }
        }
        for (int i = 0; i < tin.length; i++) {
            if (tin[i] == bean1) {
                tin[i] = bean2; // replace the first bean with the second bean in the tin
                break;
            }
        }
    }

    /**
     * @requires tin has vacant positions for new beans
     * @modifies tin
     * @effects
     *   place bean into any vacant position in tin
     */
    private static void putIn(char[] tin, char bean) {
        for (int i = 0; i < tin.length; i++) {
            if (tin[i] == REMOVED) { // vacant position
                tin[i] = bean;
                break;
            }
        }
    }

    /**
     * @effects
     *  if there are beans in tin
     *    return any such bean
     *  else
     *    return '\u0000' (null character)
     */
    private static char anyBean(char[] tin) {
        for (char bean : tin) {
            if (bean != REMOVED) {
                return bean;
            }
        }

        // no beans left
        return NULL;
    }
    /**
 * Returns a random integer between 0 (inclusive) and n (exclusive).
 * 
 * @param n the exclusive upper bound of the range for the random number
 * @requires n > 0
 * @modifies nothing
 * @effects generates a random integer between 0 (inclusive) and n (exclusive)
 * @return a random integer between 0 (inclusive) and n (exclusive)
 */
    public static int randInt(int n){
        return (int)(Math.random() * n);
    }
/**
 * @param beans    an array of characters representing the beans
 * @param beanType the type of bean to be retrieved
 * @return a character representing the first occurrence of the specified bean type, or null if not found
 * @throws IllegalArgumentException if beans is null or empty
 * @throws IllegalArgumentException if beanType is not a valid character
 * @requires beans is not null and contains at least one element
 * @requires beanType is a valid character
 * @modifies nothing
 * @effects returns a character representing the first occurrence of the specified bean type, or null if not found
 */   
    public static char getBean(char[] beans, char beanType){
        int countBlue = 0;
        int countGreen = 0;
        for(int i = 0; i < beans.length; i++){
            if(beans[i] == BLUE)
            countBlue++;
            else if (beans[i] == GREEN)
            countGreen++;
        }
        int index = -1;
        int loop = 0;
        if(beanType == BLUE)
        loop = countBlue;
        else if(beanType == GREEN)
        loop = countGreen;
        while (loop > 0 && index == -1) {
            int randIndex = randInt(beans.length);
            if (beans[randIndex] == beanType) {
                index = randIndex;
                loop = 0;
            }
        }
        
        if(index == -1){
            putIn(beans, beanType);
        return beanType;

        }
        char bean = beans[index];
        beans[index] = REMOVED; // remove the bean from the array by replacing it with a space
        return bean;
    }
}

