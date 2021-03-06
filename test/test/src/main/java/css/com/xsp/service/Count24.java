/**
 * <b>项目名：</b>test<br/>  
 * <b>包名：</b>css.com.xsp.service<br/>  
 * <b>文件名：</b>Count24.java<br/>  
 * <b>版本信息：</b>1.0<br/>  
 * <b>日期：</b>2018年1月12日 上午9:34:56<br/>  
 * <b>COPYRIGHT 2010-2016 ALL RIGHTS RESERVED </b>-版权所有<br/>
 */
package css.com.xsp.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * @createTime 2018年1月12日 上午9:34:56
 * @modifyTime 
 * @author 
 * @version 1.0
 */
public class Count24 {
    private List<String> answerList=new ArrayList<String>();
    public List<String> getAnswerList() {
        return answerList;
    }
    public static class Data{
        public float[]  arr;
        public String expStr="";
        public String[] strs;
        public Data(){}
        public Data(int a,int b,int c,int d) {
            arr=new float[]{a,b,c,d};
            strs=new String[]{a+"",b+"",c+"",d+""};
            expStr=a+"";
        }
        public Data(int arr[]) {
            this.arr=new float[]{arr[0],arr[1],arr[2],arr[3]};
            this.strs=new String[]{arr[0]+"",arr[1]+"",arr[2]+"",arr[3]+""};
        }
    }
    public void count(Data data){
        float[] arr=data.arr;
        if(arr.length<=1){
            if(arr.length==1&&arr[0]==24){
                answerList.add(data.expStr.substring(1, data.expStr.length()-1));
            }
            return ;
        }
        for(int i=0,len=arr.length;i<len-1; i++){
            for(int j=i+1;j<len;j++){
                float x=arr[i];
                float y=arr[j];
                String xs=data.strs[i];
                String ys=data.strs[j];
                for(int k=0;k<6;k++){
                    Data newData=getNewArr(data,i);
                    switch(k){
                        case 0:
                        newData.arr[j-1]=x+y;
                            newData.expStr=xs+"+"+ys;
                        break;
                        case 1:
                        newData.arr[j-1]=x-y;
                            newData.expStr=xs+"-"+ys;
                        break;
                        case 2:
                        newData.arr[j-1]=y-x;
                        newData.expStr=ys+"-"+xs;
                        break;
                        case 3:
                        newData.arr[j-1]=x*y;
                            newData.expStr=xs+"*"+ys;
                        break;
                        case 4:
                        if(y!=0){
                            newData.arr[j-1]=x/y;
                                newData.expStr=xs+"/"+ys;
                        }else {
                            continue;
                        }
                        break;
                        case 5:
                        if(x!=0){
                            newData.arr[j-1]=y/x;
                                newData.expStr=ys+"/"+xs;
                        }else {
                            continue;
                        }
                        break;
                    }
                    newData.expStr="("+newData.expStr+")";
                    newData.strs[j-1]=newData.expStr;
                    count(newData);
                }
            }
        }
        
    }
    private static Data getNewArr(Data data, int i) {
        Data newData=new Data();
        newData.expStr=data.expStr;
        newData.arr=new float[data.arr.length-1];
        newData.strs=new String[data.arr.length-1];
        for(int m=0,len=data.arr.length,n=0;m<len;m++){
            if(m!=i){
                newData.arr[n]=data.arr[m];
                newData.strs[n]=data.strs[m];
                n++;
            }
        }
        return newData;
    }
    
    public static final List<String> easyCount(int[] curRandNums){
        Count24 count24=new Count24();
        count24.count(new Data(curRandNums));
        Set<String> set=new HashSet<String>(count24.getAnswerList());//去重
        return new ArrayList<String>(set);
    }
    
    public static void main(String[] args) throws InterruptedException {
        long start=System.currentTimeMillis();
        List<String> answerStris=easyCount(new int[]{4,7,9,6});
        System.out.println(System.currentTimeMillis()-start);

        for (String string : answerStris) {
            System.out.println(string);
        }
    }
}
