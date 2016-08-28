package helpers;

import org.mindrot.jbcrypt.BCrypt;

import java.security.NoSuchAlgorithmException;

public class PasswordHelper {
    /**
     * @param literalPassword the unhashed password
     * @return the hashed password
     * @throws NoSuchAlgorithmException if the password is empty
     */
    public static String createPassword(String literalPassword) throws NoSuchAlgorithmException {
        if (null == literalPassword) {
            throw new NoSuchAlgorithmException("empty password");
        }
        return BCrypt.hashpw(literalPassword, BCrypt.gensalt());
    }

    /**
     * @param candidate the password to check
     * @param encryptedPassword the encrypted password
     * @return whether the passwords match
     */
    public static boolean checkPassword(String candidate, String encryptedPassword) {
        return !(null == candidate || null == encryptedPassword) && BCrypt.checkpw(candidate, encryptedPassword);
    }
}
