import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class Controller {

    @FXML private TextField p;
    @FXML private TextField g;
    @FXML private TextField a;
    @FXML private TextField b;
    @FXML private Button ok;
    @FXML private TextField A;
    @FXML private TextField B;
    @FXML private TextField s;
    @FXML private Label label_p;
    @FXML private Label label_g;
    @FXML private Button find_a;

    /**
     * check primitive root module.
     */

    public boolean check_primitive(BigInteger p, BigInteger g){
            long i, j;
            long phi = p.longValue() - 1;
            System.out.println(phi);
            List<Long> prime = new ArrayList<Long>();
            List<Long> pow = new ArrayList<Long>();
            for(i = 2; i < p.longValue() - 1;i++){
                if(phi % i == 0){
                    long x = phi/i;   //phi / prime
                    prime.add(x);
                }
            }
            for(i = 0; i < prime.size(); i++) {
                pow.add(phi/prime.get((int) i));
            }
            List<Long> primitiveIntegers =new ArrayList<Long>();
            long flag = 0;
            for(i = 2; i <= phi; i++){
                for(j = 0; j < pow.size();j++){
                    if(Math.pow(i,pow.get((int) j)) % p.longValue() == 1){
                        flag = 1;
                        break;
                    }
                    else
                        flag = 0;
                }
                if(flag == 0)
                    primitiveIntegers.add(i);
            }
            if(primitiveIntegers.contains(g.longValue())){
                return true;
            }
            return false;
    }

    /**
     * check prime.
     */
    public boolean isPrime(int p) {
        if(p <= 1) return false;

        if(p <= 3) return true;

        if(p % 2 == 0 || p % 3 == 0) return false;

        for (long i = 5; i * i <= p; i = i + 6) {
            if (p % i == 0 || p % (i + 2) == 0) {
                return false;
            }
        }

        return true;

    }

    public void run(ActionEvent event) throws IOException {
        try {
            BigInteger p_num = new BigInteger(p.getText());
            BigInteger g_num = new BigInteger(g.getText());
            BigInteger a_num = new BigInteger(a.getText());
            BigInteger b_num = new BigInteger(b.getText());

            if(isPrime(p_num.intValue()) == false) {
                label_p.setText("Your number is not prime. Please re-enter.");
                label_g.setText("");
                A.setText("");
                B.setText("");
                s.setText("");
            } else {
                if (check_primitive(p_num, g_num) == false) {
                    label_p.setText("");
                    label_g.setText("Your num is not primitive root module of " + p_num + ". Please re-enter." );
                    A.setText("");
                    B.setText("");
                    s.setText("");
                }
                else {
                    label_p.setText("");
                    label_g.setText("");

                    BigInteger keyA = g_num.modPow(a_num, p_num);
                    BigInteger keyB = g_num.modPow(b_num, p_num);

                    BigInteger SecretKey = keyB.modPow(a_num, p_num);

                    A.setText(keyA.toString());
                    B.setText(keyB.toString());

                    s.setText(SecretKey.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Function to find smallest primitive root of n
    public int findPrimitive(int n) {
        HashSet<Integer> s = new HashSet<Integer>();

        // Check if n is prime or not
        if (isPrime(n) == false) {
            return -1;
        }

        int phi = n - 1;

        // Find prime factors of phi and store in a set
        findPrimeFactors(s, phi);
        // Check for every number from 2 to phi
        for (int r = 2; r <= phi; r++) {
            // Iterate through all prime factors of phi.
            // and check if we found a power with value 1
            boolean flag = false;
            for (Integer a : s) {

                // Check if r^((phi)/primeFactors) mod n
                // is 1 or not
                if (power(r, phi / (a), n) == 1) {
                    flag = true;
                    break;
                }
            }

            // If there was no power with value 1.
            if (flag == false) {
                return r;
            }
        }

        // If no primitive root found
        return -1;
    }

    // Utility function to store prime factors of a number
    static void findPrimeFactors(HashSet<Integer> s, int n) {
        // Print the number of 2s that divide n
        while (n % 2 == 0) {
            s.add(2);
            n = n / 2;
        }

        // n must be odd at this point. So we can skip
        // one element (Note i = i +2)
        for (int i = 3; i <= Math.sqrt(n); i = i + 2) {
            // While i divides n, print i and divide n
            while (n % i == 0) {
                s.add(i);
                n = n / i;
            }
        }

        // This condition is to handle the case when
        // n is a prime number greater than 2
        if (n > 2) {
            s.add(n);
        }
    }

    /*
     * Iterative Function to calculate (x^n)%p in O(logy)
     */
    static int power(int x, int y, int p) {
        int res = 1; // Initialize result

        x = x % p; // Update x if it is more than or
        // equal to p

        while (y > 0) {
            // If y is odd, multiply x with result
            if (y % 2 == 1) {
                res = (res * x) % p;
            }

            // y must be even now
            y = y >> 1; // y = y/2
            x = (x * x) % p;
        }
        return res;
    }

    public void findButton_Clicked(ActionEvent event) {
        BigInteger g_num = BigInteger.valueOf(findPrimitive(Integer.parseInt(p.getText())));
        g.setText(g_num.toString());
    }
}
