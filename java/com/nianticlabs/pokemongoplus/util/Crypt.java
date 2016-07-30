package com.nianticlabs.pokemongoplus.util;

import android.content.SharedPreferences;
import android.util.Base64;
import com.nianticlabs.pokemongoplus.bridge.BackgroundBridge;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypt {
    private static final String ALGORITHM = "AES/CTR/NoPadding";
    private static final SecureRandom secureRandom = new SecureRandom();

    public static byte[] generateNonce() {
        byte[] nonce = new byte[16];
        secureRandom.nextBytes(nonce);
        return nonce;
    }

    public static byte[] unencryptNonce(byte[] key, byte[] encrypted) {
        if (encrypted.length == 32) {
            try {
                SecretKey secretKey = new SecretKeySpec(key, "AES");
                byte[] iv = new byte[16];
                byte[] nonce = new byte[16];
                System.arraycopy(encrypted, 0, iv, 0, 16);
                System.arraycopy(encrypted, 16, nonce, 0, 16);
                Cipher c = Cipher.getInstance(ALGORITHM);
                c.init(2, secretKey, new IvParameterSpec(iv));
                return c.doFinal(nonce);
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
        }
        return new byte[0];
    }

    public static byte[] encryptNonce(byte[] key, byte[] nonce) {
        try {
            byte[] iv = new byte[16];
            secureRandom.nextBytes(iv);
            SecretKey secretKey = new SecretKeySpec(key, "AES");
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(1, secretKey, new IvParameterSpec(iv));
            byte[] encrypted = c.doFinal(nonce);
            byte[] combined = new byte[(iv.length + encrypted.length)];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);
            return combined;
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    public static void persistByteArray(String key, byte[] bytes) {
        getPreferences().edit().putString(key, Base64.encodeToString(bytes, 0)).commit();
    }

    public static byte[] getPersistedByteArray(String key) {
        String encoded = getPreferences().getString(key, null);
        if (encoded != null) {
            return Base64.decode(encoded, 0);
        }
        return new byte[0];
    }

    private static SharedPreferences getPreferences() {
        return BackgroundBridge.currentContext.getSharedPreferences("pgp", 0);
    }
}
