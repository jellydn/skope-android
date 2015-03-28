package vn.dev.Skope.apis;
/**
 * Created by tientun on 3/4/15.
 */

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class TCrypt {
	private static final String PRIVATE = "3AVtY9urGX0foim9jAQi";
	private IvParameterSpec ivspec;
	private SecretKeySpec keyspec;
	private Cipher cipher;
	private byte[] iv, secretKey;

	public TCrypt(String secret) {
		this.iv = vn.dev.Skope.apis.TCryptUtil.getKeyBytes("test~");
		this.secretKey = vn.dev.Skope.apis.TCryptUtil.getKeyBytes(secret);
		
		ivspec = new IvParameterSpec(this.iv);
		keyspec = new SecretKeySpec(this.secretKey, "AES");

		try {
			cipher = Cipher.getInstance("AES/CBC/NoPadding");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}
    public String createVerifyString(){
        String time=String.valueOf(System.currentTimeMillis()/1000);
        String verifyStr="";
        try {
            verifyStr = vn.dev.Skope.apis.TCryptUtil.bytesToHex(encrypt(time));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return verifyStr;
    }

	public byte[] encrypt(String text) throws Exception {
		if (text == null || text.length() == 0)
			throw new Exception("Empty string");
		byte[] encrypted = null;
		try {
			cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
			encrypted = cipher.doFinal(vn.dev.Skope.apis.TCryptUtil.padString(text, 16).getBytes());
		} catch (Exception e) {
			throw new Exception("[encrypt] " + e.getMessage());
		}
		return encrypted;
	}

	public byte[] decrypt(String code) throws Exception {
		if (code == null || code.length() == 0)
			throw new Exception("Empty string");
		byte[] decrypted = null;
		try {
			cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
			decrypted = cipher.doFinal(vn.dev.Skope.apis.TCryptUtil.hexToBytes(code));
		} catch (Exception e) {
			throw new Exception("[decrypt] " + e.getMessage());
		}
		return decrypted;
	}
}
