/**
 * <b>项目名：</b>test<br/>  
 * <b>包名：</b>css.com.xsp.service<br/>  
 * <b>文件名：</b>BinFileToString.java<br/>  
 * <b>版本信息：</b>1.0<br/>  
 * <b>日期：</b>2018年1月16日 下午2:53:52<br/>  
 * <b>COPYRIGHT 2010-2016 ALL RIGHTS RESERVED </b>-版权所有<br/>
 */
package css.com.xsp.service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * @createTime 2018年1月16日 下午2:53:52
 * @modifyTime 
 * @author 
 * @version 1.0
 */
public class BinFileToString {

	public static void main(String[] args) {
		BinFileToString c=new BinFileToString();
		byte[] bs=c.readBinFile("c:/users/xsp/pictures/data.jpg");//29741
		System.out.println(Arrays.toString(bs));
//		BigInteger bigInteger=new BigInteger(1,bs);
//		String s=bigInteger.toString(16);
		String s=bytesToHexFun3(bs);
		System.out.println(s);
		System.out.println(s.length());
//		bigInteger=new BigInteger(s, 16);
//		byte[] bs2=bigInteger.toByteArray();
		byte[] bs2=toBytes(s);
		System.out.println(bs2.length);
		System.out.println(Arrays.toString(bs2));
		c.writeBytesToFile(bs2, "d:\\data.jpg");
	}
	public static String bytesToHexFun3(byte[] bytes) {
        StringBuilder buf = new StringBuilder(bytes.length * 2);
        for(byte b : bytes) { // 使用String的format方法进行转换
            buf.append(String.format("%02x", new Integer(b & 0xff)));
        }

        return buf.toString();
    }
	/**
     * 将16进制字符串转换为byte[]
     * 
     * @param str
     * @return
     */
    public static byte[] toBytes(String str) {
        if(str == null || str.trim().equals("")) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for(int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }

        return bytes;
    }
	private byte[] readBinFile(String filename){
		FileInputStream fis=null;
		ByteArrayOutputStream buffer=new ByteArrayOutputStream();
		try {
			fis=new FileInputStream(filename);
			byte[] bs=new byte[8192];
			int n=0;
			while ((n=fis.read(bs))>0) {
//				System.out.println(n);
				buffer.write(bs, 0, n);
			}
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println(buffer.size());
		return buffer.toByteArray();
	}
	private void writeBytesToFile(byte[] bs,String filename){
		
		FileOutputStream fos=null;
		try{
			fos=new FileOutputStream(filename);
//			fos.write(bs,1,bs.length-1);
			fos.write(bs);
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
