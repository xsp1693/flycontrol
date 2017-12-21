/**
 * <b>项目名：</b>test<br/>  
 * <b>包名：</b>css.com.xsp.jacob<br/>  
 * <b>文件名：</b>WordToXml.java<br/>  
 * <b>版本信息：</b>1.0<br/>  
 * <b>日期：</b>2017年4月21日 上午9:28:22<br/>  
 * <b>COPYRIGHT 2010-2016 ALL RIGHTS RESERVED 中国软件与技术服务股份有限公司</b>-版权所有<br/>
 */
package css.com.xsp.jacob;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

/**
 * @description TODO
 * @createTime 2017年4月21日 上午9:28:22
 * @modifyTime 
 * @author xieshp@css.com.cn
 * @version 1.0
 */
public class WordToXml {

	public static void main(String[] args) {
		//wordToXml("d:/ynaj/抽查事项清单管理部署文档.docx", "d:/抽查事项清单管理部署文档.xml");
		xmlToWord("d:/抽查事项清单管理部署文档.xml", "d:/抽查事项清单管理部署文档.rtf");
	}

	/**
     * 
     * @Description: 
     * @param filePath word目录
     * @param xmlFilePath 生成xml存放路径
     * @author Administrator
     */
    public static void wordToXml(String filePath,String xmlFilePath){
        try {  
               ActiveXComponent app = new ActiveXComponent( "Word.Application"); //启动word  
               app.setProperty("Visible", new Variant(false)); //为false时设置word不可见，为true时是可见要不然看不到Word打打开文件的过程  
               Dispatch docs = app.getProperty("Documents").toDispatch();  
               //打开编辑器
               Dispatch doc = Dispatch.invoke(docs, "Open", Dispatch.Method, new Object[] {filePath, new Variant(false), new Variant(true)} , new int[1]).toDispatch(); //打开word文档
               Dispatch.call(doc, "SaveAs", xmlFilePath, 11);//xml文件格式宏11  
               Dispatch.call(doc, "Close", false);  
               app.invoke("Quit",0); 
               System.out.println("---------word转换完成--------");
          }catch (Exception e) {  
             e.printStackTrace();  
          }  
    }
    public static void xmlToWord(String filePath,String docFilePath){
        try {  
               ActiveXComponent app = new ActiveXComponent( "Word.Application"); //启动word  
               app.setProperty("Visible", new Variant(false)); //为false时设置word不可见，为true时是可见要不然看不到Word打打开文件的过程  
               Dispatch docs = app.getProperty("Documents").toDispatch();  
               //打开编辑器
               Dispatch doc = Dispatch.invoke(docs, "Open", Dispatch.Method, new Object[] {filePath, new Variant(false), new Variant(true)} , new int[1]).toDispatch(); //打开word文档
               //Dispatch.call(doc, "SaveAs", docFilePath, 11);//xml文件格式宏11  
               Dispatch.call(doc,"SaveAs" , docFilePath,6);//word2007 docx
               Dispatch.call(doc, "Close", false);  
               app.invoke("Quit",0); 
               System.out.println("---------word转换完成--------");
          }catch (Exception e) {  
             e.printStackTrace();  
          }  
    }
}
