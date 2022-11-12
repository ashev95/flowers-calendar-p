import org.springframework.security.crypto.bcrypt.BCrypt;

public class BCryptTester {

    private static final String KEY = "0"; //revision id

    private static final String GENERATED_HASH = "$2a$10$LRdDiwcmLsG0qmqEfvjHN.fwRKy5l05uU447MAFLO4XsX.m4phvaC";
    private static final String CHECK_KEY = "0";

    public static void main (String... args) {
        if (true) { //generate
            String generatedSalt = BCrypt.gensalt();
            System.out.println("salt = " + generatedSalt);
            System.out.println("key = " + KEY);
            String hash = BCrypt.hashpw(KEY, generatedSalt);
            System.out.println("hash = " + hash);
        } else { //check
            final boolean result = BCrypt.checkpw(CHECK_KEY, GENERATED_HASH);
            System.out.println("result = " + result);
        }
    }

}
