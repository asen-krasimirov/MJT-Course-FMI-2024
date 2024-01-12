package bg.sofia.uni.fmi.mjt.space.algorithm;

import bg.sofia.uni.fmi.mjt.space.exception.CipherException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Rijndael implements SymmetricBlockCipher {
    private final SecretKey secretKey;
    private static final int KILOBYTE = 1024;

    public Rijndael(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public void encrypt(InputStream inputStream, OutputStream outputStream) throws CipherException {
        Cipher cipher = initiateCipher(true);

        try (var cipherOutputStream = new CipherOutputStream(outputStream, cipher)) {
            byte[] buffer = new byte[KILOBYTE];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                cipherOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Problem occurred while encrypting!", e);
        }
    }

    @Override
    public void decrypt(InputStream inputStream, OutputStream outputStream) throws CipherException {
        Cipher cipher = initiateCipher(false);

        try (OutputStream decryptedOutputStream = new CipherOutputStream(outputStream, cipher)) {

            byte[] buffer = new byte[KILOBYTE];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                decryptedOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Problem occurred while decrypting!", e);
        }
    }

    private Cipher initiateCipher(boolean isEncrypt) throws CipherException {
        Cipher cipher;

        try {
            cipher = Cipher.getInstance("AES");

            if (isEncrypt) {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
            }
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new CipherException("A problem occurred while initializing the cipher!", e);
        }

        return cipher;
    }
}
