package com.hp.idc.cas.common;

import java.security.MessageDigest;

/**
 * �򵥵������ܲ��ַ����Ĺ�����
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class Encrypt{

	private final static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'}; 
	/**
	 * MD5����,���� MD5 32λ��	
	 * @param s ԭʼ�ַ���
	 * @return String 
	 * add by juyf@lianchuang.com
	 */
	public final static String MD5(String s){ 	
	   try {
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return "";
		} 
	} 

	
    private final static String DEFAULT_KEY = "�Ͼ����� Toptea";

    private Encrypt(){
    }

    /**
     * ���ܵ�Ĭ�Ϸ���
     * @param passwd String��Ҫ���ܵ��ִ�
     * @return String���ܺ���ִ�
     */
    public static String encryptPasswd(String passwd){
        return encryptPasswd(passwd,DEFAULT_KEY);
    }

    /**
     * ���ܷ���
     * @param passwd String��Ҫ���ܵ��ִ�
     * @param key String�����ִ�
     * @return String���ܺ���ִ�
     */
    public static String encryptPasswd(String passwd,String key){
        byte[] bPasswd = passwd.getBytes();
        byte[] bKey = key.getBytes();
        char[] encrypt = new char[bPasswd.length<<1];
        for(int i = 0;i < bPasswd.length;i++){
            int _passwd = bPasswd[i] & 255;
            int _key = bKey[getIndex(bKey.length,i)] & 255;
            int _encrypt = _passwd ^ _key;
            encrypt[i << 1] = (char)(64 + (_encrypt >> 4));
            encrypt[i << 1 | 1] = (char)(64 + (_encrypt & 15));
            //System.out.println(0 + encrypt[i << 1]);
            //System.out.println(0 + encrypt[i << 1 | 1]);
        }
        return new String(encrypt);}

    /**
     * ���ܵ�Ĭ�Ϸ���
     * @param encrypt String��Ҫ���ܵ��ִ�
     * @return String���ܺ���ִ�
     */
    public static String decryptPasswd(String encrypt){
        return decryptPasswd(encrypt,DEFAULT_KEY);
    }

    /**
     * ����
     * @param encrypt String��Ҫ���ܵ��ִ�
     * @param key String�����ִ�
     * @return String���ܺ���ִ�
     */
    public static String decryptPasswd(String encrypt,String key){
        byte[] bKey = key.getBytes();
        byte[] _decrypt = new byte[encrypt.length() >> 1];
        for(int i = 0;i < encrypt.length() >> 1;i++){
            int _high = encrypt.charAt(i << 1) - 64;
            int _low = encrypt.charAt(i << 1 | 1) - 64;
            _decrypt[i] = (byte)((_high << 4 | _low) ^
                                 bKey[getIndex(bKey.length,i)]);
        }
        return new String(_decrypt);}

    /**
     * �������ļ����ִ���������
     * @param len int�����ִ�����
     * @param i int��Ҫ���ܵĵ�
     * @return int�����ִ���������
     */
    private static int getIndex(int len,int i){
        int length = i;
        while(length >= len){
            length = length - len;
        }
        return length;}
    
    public static void main(String[] args) {
    	System.out.println(Encrypt.MD5("123456"));
    }
}
