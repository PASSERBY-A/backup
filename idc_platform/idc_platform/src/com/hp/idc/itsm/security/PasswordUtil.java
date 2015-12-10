package com.hp.idc.itsm.security;

/**
 * 密码工具类,通过本类可以进行密码的加/解密 算法同HP OVSD4.5
 * 
 * @author 梅园
 * 
 */
public class PasswordUtil {
	/**
	 * FROM HP SOURCE CODE
	 */
	protected long LFSR_A;

	/**
	 * FROM HP SOURCE CODE
	 */
	protected long LFSR_B;

	/**
	 * FROM HP SOURCE CODE
	 */
	protected long LFSR_C;

	/**
	 * FROM HP SOURCE CODE
	 */
	protected long Mask_A;

	/**
	 * FROM HP SOURCE CODE
	 */
	protected long Mask_B;

	/**
	 * FROM HP SOURCE CODE
	 */
	protected long Mask_C;

	/**
	 * FROM HP SOURCE CODE
	 */
	protected long Rot0_A;

	/**
	 * FROM HP SOURCE CODE
	 */
	protected long Rot0_B;

	/**
	 * FROM HP SOURCE CODE
	 */
	protected long Rot0_C;

	/**
	 * FROM HP SOURCE CODE
	 */
	protected long Rot1_A;

	/**
	 * FROM HP SOURCE CODE
	 */
	protected long Rot1_B;

	/**
	 * FROM HP SOURCE CODE
	 */
	protected long Rot1_C;

	/**
	 * FROM HP SOURCE CODE
	 * 
	 * @param key
	 */
	private void setKey(String key) {
		this.LFSR_A = 0x13579bdfL;
		this.LFSR_B = 0x2468ace0L;
		this.LFSR_C = 0xfffffffffdb97531L;
		this.Mask_A = 0xffffffff80000062L;
		this.Mask_B = 0x40000020L;
		this.Mask_C = 0x10000002L;
		this.Rot0_A = 0x7fffffffL;
		this.Rot0_B = 0x3fffffffL;
		this.Rot0_C = 0xfffffffL;
		this.Rot1_A = 0xffffffff80000000L;
		this.Rot1_B = 0xffffffffc0000000L;
		this.Rot1_C = 0xfffffffff0000000L;

		StringBuffer stringbuffer = new StringBuffer(key);
		if (stringbuffer.length() == 0)
			stringbuffer = new StringBuffer("Default seed");
		for (int i = 0; stringbuffer.length() < 12; i++)
			stringbuffer.append(stringbuffer.charAt(i));

		for (int j = 0; j < 4; j++) {
			this.LFSR_A = (this.LFSR_A <<= 8)
					| (long) stringbuffer.charAt(j + 0);
			this.LFSR_B = (this.LFSR_B <<= 8)
					| (long) stringbuffer.charAt(j + 4);
			this.LFSR_C = (this.LFSR_C <<= 8)
					| (long) stringbuffer.charAt(j + 8);
		}

		if (this.LFSR_A == 0L)
			this.LFSR_A = 0x13579bdfL;
		if (this.LFSR_B == 0L)
			this.LFSR_B = 0x2468ace0L;
		if (this.LFSR_C == 0L)
			this.LFSR_C = 0xfffffffffdb97531L;
	}

	/**
	 * FROM HP SOURCE CODE
	 * 
	 * @param byte0
	 * @return FROM HP SOURCE CODE
	 */
	private byte transform(byte byte0) {
		byte byte1 = 0;
		long l = this.LFSR_B & 1L;
		long l1 = this.LFSR_C & 1L;
		for (int i = 0; i < 8; i++) {
			if ((this.LFSR_A & 1L) == 1L) {
				this.LFSR_A = this.LFSR_A ^ 0xffffffffc0000031L | 0xffffffff80000000L;
				if ((this.LFSR_B & 1L) == 1L) {
					this.LFSR_B = this.LFSR_B ^ 0x20000010L | 0xffffffffc0000000L;
					l = 1L;
				} else {
					this.LFSR_B = this.LFSR_B >> 1 & 0x3fffffffL;
					l = 0L;
				}
			} else {
				this.LFSR_A = this.LFSR_A >> 1 & 0x7fffffffL;
				if ((this.LFSR_C & 1L) == 1L) {
					this.LFSR_C = this.LFSR_C ^ 0x8000001L | 0xfffffffff0000000L;
					l1 = 1L;
				} else {
					this.LFSR_C = this.LFSR_C >> 1 & 0xfffffffL;
					l1 = 0L;
				}
			}
			byte1 = (byte) (int) ((long) (byte1 << 1) | l ^ l1);
		}

		return (byte) (byte0 ^ byte1);
	}

	/**
	 * BASE64编码字符列表
	 */
	private static char base64Alphabet[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G',
			'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
			'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
			'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
			'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', '+', '/' };

	/**
	 * 将二进制数据转为BASE64编码
	 * 
	 * @param abyte0
	 *            要转换的数据
	 * @return 转换后的字符串
	 */
	public static String toBase64(byte abyte0[]) {
		char ac[] = new char[((abyte0.length - 1) / 3 + 1) * 4];
		int i = 0;
		int j;
		for (j = 0; j + 3 <= abyte0.length;) {
			int k = (abyte0[j++] & 0xff) << 16;
			k |= (abyte0[j++] & 0xff) << 8;
			k |= abyte0[j++] & 0xff;
			int j1 = (k & 0xfc0000) >> 18;
			ac[i++] = base64Alphabet[j1];
			j1 = (k & 0x3f000) >> 12;
			ac[i++] = base64Alphabet[j1];
			j1 = (k & 0xfc0) >> 6;
			ac[i++] = base64Alphabet[j1];
			j1 = k & 0x3f;
			ac[i++] = base64Alphabet[j1];
		}

		if (abyte0.length - j == 2) {
			int l = (abyte0[j] & 0xff) << 16;
			l |= (abyte0[j + 1] & 0xff) << 8;
			int k1 = (l & 0xfc0000) >> 18;
			ac[i++] = base64Alphabet[k1];
			k1 = (l & 0x3f000) >> 12;
			ac[i++] = base64Alphabet[k1];
			k1 = (l & 0xfc0) >> 6;
			ac[i++] = base64Alphabet[k1];
			ac[i++] = '=';
		} else if (abyte0.length - j == 1) {
			int i1 = (abyte0[j] & 0xff) << 16;
			int l1 = (i1 & 0xfc0000) >> 18;
			ac[i++] = base64Alphabet[l1];
			l1 = (i1 & 0x3f000) >> 12;
			ac[i++] = base64Alphabet[l1];
			ac[i++] = '=';
			ac[i++] = '=';
		}
		return new String(ac);
	}

	/**
	 * 将BASE64编码的字符转换为原6bit的数据
	 * 
	 * @param index
	 *            字符
	 * @return 6bit的数据
	 */
	private static int fromBase64Char(char index) {
		if (index >= 'a')
			return index - 'a' + 26;
		if (index >= 'A')
			return index - 'A';
		if (index >= '0')
			return index - '0' + 52;
		if (index == '+')
			return 62;
		if (index == '/')
			return 63;
		return 0;
	}

	/**
	 * 将BASE64编码转换为原始数据
	 * 
	 * @param src
	 *            编码的字符串
	 * @return 返回原始数据
	 */
	public static byte[] fromBase64(String src) {
		int size;
		int s0 = src.length();
		size = (s0 - 1) / 4 * 3;
		if (s0 > 0) {
			if (src.charAt(s0 - 2) == '=')
				size++;
			else if (src.charAt(s0 - 1) == '=')
				size += 2;
			else
				size += 3;
		}
		if (size == 0)
			return new byte[0];

		byte[] dest = new byte[size];

		int b1, b2, b3, b4;
		int p = 0, p1 = 0;
		while (p < s0) {
			if (src.charAt(p + 2) == '=') {
				b1 = fromBase64Char(src.charAt(p++));
				b2 = fromBase64Char(src.charAt(p));
				dest[p1] = (byte) ((b1 << 2) | (b2 >> 4));
				break;
			} // 2 to 1
			else if (src.charAt(p + 3) == '=') {
				b1 = fromBase64Char(src.charAt(p++));
				b2 = fromBase64Char(src.charAt(p++));
				b3 = fromBase64Char(src.charAt(p));
				dest[p1++] = (byte) ((b1 << 2) | (b2 >> 4));
				dest[p1] = (byte) ((b2 << 4) | (b3 >> 2));
				break;
			} // 3 to 2
			else {
				b1 = fromBase64Char(src.charAt(p++));
				b2 = fromBase64Char(src.charAt(p++));
				b3 = fromBase64Char(src.charAt(p++));
				b4 = fromBase64Char(src.charAt(p++));
				dest[p1++] = (byte) ((b1 << 2) | (b2 >> 4));
				dest[p1++] = (byte) ((b2 << 4) | (b3 >> 2));
				dest[p1++] = (byte) ((b3 << 6) | b4);
			} // 4 to 3
		}
		return dest;
	}

	/**
	 * 将字符串转换为byte数组
	 * 
	 * @param s
	 *            要转换的字符串
	 * @return 生成的byte数组
	 */
	private static byte[] toByteArray(String s) {
		char ac[] = s != null ? s.toCharArray() : new char[0];
		byte abyte0[] = new byte[ac.length * 2];
		int i = 0;
		for (int j = 0; j < ac.length; j++) {
			abyte0[i++] = (byte) (ac[j] >> 8);
			abyte0[i++] = (byte) ac[j];
		}

		return abyte0;
	}

	/**
	 * 将byte数组转换为字符串
	 * 
	 * @param b
	 *            要转换的数据
	 * @return 生成的字符串
	 */
	private static String toString(byte[] b) {
		if (b == null || b.length == 0)
			return new String();

		StringBuffer sb = new StringBuffer();
		for (int j = 0; j < b.length / 2; j++) {
			sb.append((char) (b[j * 2] << 8 | b[j * 2 + 1]));
		}

		return sb.toString();
	}

	/**
	 * 对密码进行加密
	 * 
	 * @param s
	 *            要加密的密码
	 * @return 加密后的字符串
	 */
	public static String crypt(String s) {
		if (s == null || s.length() == 0)
			return "";
		PasswordUtil p = new PasswordUtil();
		p.setKey("sdk39df#@4!7-sd");
		byte[] b = toByteArray(s);
		int i = b.length;
		for (int j = 0; j < i; j++)
			b[j] = p.transform(b[j]);
		return toBase64(b);
	}

	/**
	 * 对密码进行解密
	 * 
	 * @param s
	 *            加密的密码
	 * @return 解密后的密码
	 */
	public static String decrypt(String s) {
		if (s == null || s.length() == 0)
			return "";
		PasswordUtil p = new PasswordUtil();
		p.setKey("sdk39df#@4!7-sd");
		byte[] b = fromBase64(s);
		int i = b.length;
		for (int j = 0; j < i; j++)
			b[j] = p.transform(b[j]);
		return toString(b);
	}
}
