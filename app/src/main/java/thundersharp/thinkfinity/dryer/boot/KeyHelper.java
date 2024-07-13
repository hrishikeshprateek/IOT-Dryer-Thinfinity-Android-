package thundersharp.thinkfinity.dryer.boot;

import android.util.Base64;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.nio.charset.StandardCharsets;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class KeyHelper {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static final String ALGORITHM = "AES/ECB/NoPadding";

    public static String decrypt(String encryptedText, String keyHex) throws Exception {
        byte[] keyBytes = hexStringToByteArray(keyHex);
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance(ALGORITHM, "BC");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] decodedBytes = Base64.decode(encryptedText, Base64.DEFAULT);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);

        return new String(decryptedBytes, StandardCharsets.UTF_8).trim();
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

}