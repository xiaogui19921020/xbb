package com.boot.webserver.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

public class RSAUtils {

  private static Logger log = LoggerFactory.getLogger(RSAUtils.class);

  public static String defaultPrivateKey =
      "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIGz75Kf/v3PnyirzpcyZiMipbIp04EYQHj7exL7RRq4jcdxlyTZraxVqCgpcproTkWj4tF7n/0Tsja+fBMwwsEweT6ev2mSYlTm2o+Xy3UfkTAEwI3sPr+AiB6IKqeQRiHDxK/uWcmR2QJ8HFeDdUKGVQ2J/46onNlQpLqr30PpAgMBAAECgYBgHi9hUo5OG2nQYxnzNFc6nHd8g8CIv6cvwPXhCDKa7b+r2MLwfRxWRRgbwzN6FsPCmgQByAK1Mr50EU+zjQQVxPI2/Enhd/TByEJe4XGPUatp8udmW4vLkFVqfn2RXXdmGxo1TKzXayPDS1a+1sMq+69z57WEcV2HAb35HrblXQJBAMxgTPu6WfPerUBGc0cnbSZGeRoGJEgXF52d0s5mXiXDVcC1u2i0l1hjMf+9a+n0ssXFCXBI28bUGspPB35rmTsCQQCidv2cKa4vMYG0RaEBLVPzJFUlvxo/qUKg49GNpcdSFK0OviMTpauLIRMMYEzmn4fIejePoBBhfIswOghlhSUrAkAtx4R2/o8XqVF1jFJWJea1JCQMSEPoQgPwMmH+CcAdBVw4Bn0sPweHrPCOMIfzp/RDiJdMp4VTTD+UAY/UKZXxAkBPk0C9EJ1EeFczWTd3QCByY0Vha60LqXf9Hhkx7BUo2v+4zOCZavgO+XA/C5wfCdyUEWjNbHUZissSIBqkvwZpAkEAyS0ZyondsLdF5LHMhPY7DU9bdMTpPk+oMwmuBrPvJNkjGvj6cCXuhNfjssIpQ3TNG/T6kQw6KgsxWMSO+nLkIw==";
  public static String defaultPublicKey =
      "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCBs++Sn/79z58oq86XMmYjIqWyKdOBGEB4+3sS+0UauI3HcZck2a2sVagoKXKa6E5Fo+LRe5/9E7I2vnwTMMLBMHk+nr9pkmJU5tqPl8t1H5EwBMCN7D6/gIgeiCqnkEYhw8Sv7lnJkdkCfBxXg3VChlUNif+OqJzZUKS6q99D6QIDAQAB";

  /** RSA最大加密明文大小 */
  private static final int MAX_ENCRYPT_BLOCK = 117;

  /** RSA最大解密密文大小 */
  private static final int MAX_DECRYPT_BLOCK = 128;

  /** 数字签名，密钥算法 */
  private static final String RSA_KEY_ALGORITHM = "RSA";

  /**
   * map 2 paramString
   *
   * @param map
   * @return
   */
  public static String getSignStr(Map map) {
    if (map == null || map.isEmpty()) {
      return null;
    }
    Object[] keyArray = map.keySet().toArray();
    Arrays.sort(keyArray);
    StringBuilder s = new StringBuilder();
    for (int i = 0; i < keyArray.length; i++) {
      Object o = keyArray[i];
      String v = map.get(o).toString();
      if (v != null && v.length() > 0) {
        s.append(o).append("=").append(v).append("&");
      }
    }
    return s.substring(0, s.length() - 1);
  }

  public static String doSign(Map map, String privateKey) throws Exception {
    map = new TreeMap(map);
    String signStr = getSignStr(map);
    String sign = sign(signStr, getPrivateKey(privateKey));
    map.put("sign", sign);
    return getSignStr(map);
  }

  /**
   * paramString 2 map
   *
   * @param params
   * @return
   */
  public static Map<String, String> getMap(String params) {
    HashMap<String, String> map = new HashMap<>();
    int start = 0, len = params.length();
    while (start < len) {
      int i = params.indexOf('&', start);
      if (i == -1) {
        i = params.length();
      }
      String keyValue = params.substring(start, i);
      int j = keyValue.indexOf('=');
      String key = keyValue.substring(0, j);
      String value = keyValue.substring(j + 1);
      map.put(key, value);
      if (i == params.length()) {
        break;
      }
      start = i + 1;
    }
    return map;
  }

  /**
   * 获取密钥对
   *
   * @return 密钥对
   */
  public static KeyPair getKeyPair() throws Exception {
    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
    generator.initialize(1024);
    return generator.generateKeyPair();
  }

  /**
   * 获取私钥
   *
   * @param privateKey 私钥字符串
   * @return
   */
  public static PrivateKey getPrivateKey(String privateKey) throws Exception {
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    byte[] decodedKey = Base64.getDecoder().decode(privateKey.getBytes());
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
    return keyFactory.generatePrivate(keySpec);
  }

  /**
   * 私钥加密
   *
   * @param data 加密前的字符串
   * @param privateKey 私钥
   * @return 加密后的字符串
   * @throws Exception
   */
  public static String encryptByPriKey(String data, String privateKey) throws Exception {
    byte[] priKey = RSAUtils.decodeBase64(privateKey);
    byte[] enSign = encryptByPriKey(data.getBytes(), priKey);
    return org.apache.commons.codec.binary.Base64.encodeBase64String(enSign);
  }

  /**
   * 私钥加密
   *
   * @param data 待加密的数据
   * @param priKey 私钥
   * @return 加密后的数据
   * @throws Exception
   */
  public static byte[] encryptByPriKey(byte[] data, byte[] priKey) throws Exception {
    PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(priKey);
    KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
    PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
    Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
    cipher.init(Cipher.ENCRYPT_MODE, privateKey);
    return cipher.doFinal(data);
  }

  /**
   * 密钥转成byte[]
   *
   * @param key
   * @return
   */
  public static byte[] decodeBase64(String key) {
    return org.apache.commons.codec.binary.Base64.decodeBase64(key);
  }

  /**
   * 获取公钥
   *
   * @param publicKey 公钥字符串
   * @return
   */
  public static PublicKey getPublicKey(String publicKey) throws Exception {
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    byte[] decodedKey = Base64.getDecoder().decode(publicKey.getBytes());
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
    return keyFactory.generatePublic(keySpec);
  }

  /**
   * RSA加密
   *
   * @param data 待加密数据
   * @param publicKey 公钥
   * @return
   */
  public static String encrypt(String data, PublicKey publicKey) throws Exception {
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    int inputLen = data.getBytes().length;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int offset = 0;
    byte[] cache;
    int i = 0;
    // 对数据分段加密
    while (inputLen - offset > 0) {
      if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
        cache = cipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
      } else {
        cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
      }
      out.write(cache, 0, cache.length);
      i++;
      offset = i * MAX_ENCRYPT_BLOCK;
    }
    byte[] encryptedData = out.toByteArray();
    out.close();
    // 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
    // 加密后的字符串
    return Base64.getEncoder().encodeToString(encryptedData);
  }

  /**
   * RSA解密
   *
   * @param data 待解密数据
   * @param privateKey 私钥
   * @return
   */
  public static String decrypt(String data, PrivateKey privateKey) throws Exception {
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE, privateKey);
    byte[] dataBytes = Base64.getMimeDecoder().decode(data);
    int inputLen = dataBytes.length;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int offset = 0;
    byte[] cache;
    int i = 0;
    // 对数据分段解密
    while (inputLen - offset > 0) {
      if (inputLen - offset > MAX_DECRYPT_BLOCK) {
        cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
      } else {
        cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
      }
      out.write(cache, 0, cache.length);
      i++;
      offset = i * MAX_DECRYPT_BLOCK;
    }
    byte[] decryptedData = out.toByteArray();
    out.close();
    // 解密后的内容
    return new String(decryptedData, Charset.forName("UTF-8"));
  }

  /**
   * 签名
   *
   * @param data 待签名数据
   * @param privateKey 私钥
   * @return 签名
   */
  public static String sign(String data, String privateKey) throws Exception {
    return RSAUtils.sign(data, RSAUtils.getPrivateKey(privateKey));
  }

  /**
   * 签名
   *
   * @param data 待签名数据
   * @param privateKey 私钥
   * @return 签名
   */
  public static String sign(String data, PrivateKey privateKey) throws Exception {
    byte[] keyBytes = privateKey.getEncoded();
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    PrivateKey key = keyFactory.generatePrivate(keySpec);
    Signature signature = Signature.getInstance("MD5withRSA");
    signature.initSign(key);
    signature.update(data.getBytes());
    return Base64.getEncoder().encodeToString(signature.sign());
  }

  /**
   * 验签
   *
   * @param srcData 原始字符串
   * @param publicKey 公钥
   * @param sign 签名
   * @return 是否验签通过
   */
  public static boolean verify(String srcData, String publicKey, String sign) throws Exception {
    return RSAUtils.verify(srcData, RSAUtils.getPublicKey(publicKey), sign);
  }

  public static boolean doVerify(Map map, String publicKey) throws Exception {
    map = new TreeMap(map);
    String oldSign = map.remove("sign").toString();
    return verify(getSignStr(map), publicKey, oldSign);
  }

  /**
   * 验签
   *
   * @param srcData 原始字符串
   * @param publicKey 公钥
   * @param sign 签名
   * @return 是否验签通过
   */
  public static boolean verify(String srcData, PublicKey publicKey, String sign) throws Exception {
    byte[] keyBytes = publicKey.getEncoded();
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    PublicKey key = keyFactory.generatePublic(keySpec);
    Signature signature = Signature.getInstance("MD5withRSA");
    signature.initVerify(key);
    signature.update(srcData.getBytes());
    return signature.verify(Base64.getDecoder().decode(sign.getBytes()));
  }


  public static String encrypt(String data) {
    return encrypt(data, defaultPublicKey);
  }

  public static String decrypt(String data) {
    return decrypt(data, defaultPrivateKey);
  }

  public static String encrypt(String data, String publicKey) {
    try {
      return encrypt(data, getPublicKey(publicKey));
    } catch (Exception e) {
      log.error(" 加密发生异常:{} - {} - {}", data, publicKey, e);
    }
    return null;
  }

  public static String decrypt(String data, String privateKey) {
    try {
      return decrypt(data, getPrivateKey(privateKey));
    } catch (Exception e) {
      log.error(" 解密发生异常:{} - {} - {}", data, privateKey, e);
    }
    return null;
  }

  public static void main(String[] args) throws Exception {
    // 生成密钥对
    KeyPair keyPair = getKeyPair();
    String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
    String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
    System.out.println("Length: " + privateKey.length() + "  PrivateKey: " + privateKey);
    System.out.println("Length: " + publicKey.length() + "  PublicKey: " + publicKey);
  }
}
