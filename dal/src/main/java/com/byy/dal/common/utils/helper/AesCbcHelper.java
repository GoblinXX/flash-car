package com.byy.dal.common.utils.helper;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;

/**
 * 微信解密工具
 *
 * @author: yyc
 * @date: 19-6-10 上午11:28
 */
public class AesCbcHelper {

  /**
   * AES解密
   *
   * @param data 密文，被加密的数据
   * @param key 秘钥
   * @param iv 偏移量
   * @return
   */
  public static String decrypt(String data, String key, String iv) {
    // 被加密的数据
    byte[] dataByte = Base64.decode(data);
    // 加密秘钥
    byte[] keyByte = Base64.decode(key);
    // 偏移量
    byte[] ivByte = Base64.decode(iv);
    try {
      // 如果密钥不足16位,那么就补足,这个if中的内容很重要
      int base = 16;
      if (keyByte.length % base != 0) {
        int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
        byte[] temp = new byte[groups * base];
        Arrays.fill(temp, (byte) 0);
        System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
        keyByte = temp;
      }
      Security.addProvider(new BouncyCastleProvider());
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
      SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
      AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
      parameters.init(new IvParameterSpec(ivByte));
      cipher.init(Cipher.DECRYPT_MODE, spec, parameters);
      byte[] resultByte = cipher.doFinal(dataByte);
      if (null != resultByte && resultByte.length > 0) {
        return new String(resultByte, StandardCharsets.UTF_8);
      }
    } catch (NoSuchAlgorithmException
        | NoSuchPaddingException
        | InvalidParameterSpecException
        | InvalidKeyException
        | InvalidAlgorithmParameterException
        | IllegalBlockSizeException
        | BadPaddingException e) {
      e.printStackTrace();
    }
    return "";
  }
}
