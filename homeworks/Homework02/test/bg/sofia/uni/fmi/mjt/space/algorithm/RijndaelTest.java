package bg.sofia.uni.fmi.mjt.space.algorithm;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class RijndaelTest {
    private static Rijndael rijndael;

    @BeforeAll
    static void setUpTestCase() {
        SecretKey secretKey = loadSecretKey();
        rijndael = new Rijndael(secretKey);
    }

    private static SecretKey loadSecretKey() {
        byte[] keyBytes = "q0v2jGG99Ox+11srKkaolw==".getBytes();
        return new SecretKeySpec(keyBytes, "AES");
    }

    @Test
    void testEncrypt() {
        InputStream in = new ByteArrayInputStream("Msg to encrypt.".getBytes());
        OutputStream out = new ByteArrayOutputStream();

        assertDoesNotThrow(() -> rijndael.encrypt(in, out),
            "encrypt(...) method should not throw when called with valid streams.");

        assertEquals("�AR�pư�RYS�g", out.toString(),
            "The encryption in encrypt(...) should be correctly written to the out OutputStream.");
    }

    @Test
    void testDecrypt() {
        InputStream in = new ByteArrayInputStream("Msg to encrypt.".getBytes());
        OutputStream out = new ByteArrayOutputStream();

        assertDoesNotThrow(() -> rijndael.encrypt(in, out),
            "encrypt(...) method should not throw when called with valid streams.");

        InputStream in1 = new ByteArrayInputStream(((ByteArrayOutputStream) out).toByteArray());
        OutputStream out1 = new ByteArrayOutputStream();

        assertDoesNotThrow(() -> rijndael.decrypt(in1, out1),
            "decrypt(...) method should not throw when called with valid streams.");

        assertEquals("Msg to encrypt.", out1.toString(),
            "The decryption in decrypt(...) should be correctly written to the out OutputStream.");
    }
}
