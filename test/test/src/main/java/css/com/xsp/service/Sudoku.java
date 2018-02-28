/**
 * <b>项目名：</b>test<br/>  
 * <b>包名：</b>css.com.xsp.service<br/>  
 * <b>文件名：</b>Sudoku.java<br/>  
 * <b>版本信息：</b>1.0<br/>  
 * <b>日期：</b>2018年2月9日 上午10:34:41<br/>  
 * <b>COPYRIGHT 2010-2016 ALL RIGHTS RESERVED 中国软件与技术服务股份有限公司</b>-版权所有<br/>
 */
package css.com.xsp.service;

import java.util.HashMap;
import java.util.Map;

/**
 * @createTime 2018年2月9日 上午10:34:41
 * @modifyTime 
 * @author xieshp@css.com.cn
 * @version 1.0
 */
public class Sudoku {
	private static Map<String, String> hint=new HashMap<String, String>();//记录候选数，key为坐标，value为候选数
	private static char[][] cells=new char[9][9];
	public static void main(String[] args) {
//		String q="810003290,067000000,900500006,000408000,604000809,000209000,700001008,000000370,053800042";//28
//		String q="050000020,400206007,008030100,010000060,009000500,070000090,005080300,700901004,020000070";//24
//		String q="500090201,002007008,080000300,014005000,000903000,000800940,003000060,600200100,809060005";//26
		String q="000020040,000000900,000300070,003040000,600050093,970080006,010005200,060007050,800600000";//23个数
		initCells(q);
		print();
		compute();
		print();
	}
	
	private static void compute(){
		int n=0;
		do{
			n=0;
			for(int i=0;i<9;i++){
				for(int j=0;j<9;j++){
					if(cells[i][j]=='0'){
						boolean b=checkEmptyCell(i, j);
						n+=(b?1:0);
					}
				}
			}
			System.out.println("====================================");
		}while(n>0);
		
		boolean filled=false;
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				if(cells[i][j]=='0'){
					filled=checkHintInBox(i, j);
					if(filled){
						break;
					}
				}
			}
			if(filled){
				break;
			}
		}
		if(filled){
			compute();
		}else{
			filled=checkTwoHintNum();
			if(filled){
				compute();
			}
		}
	}
	//初始化所有单元格
	private static void initCells(String question) {
		String[] rows=question.split(",");
		for(int i=0;i<9;i++){
			char[] cols=rows[i].toCharArray();
			for(int j=0;j<9;j++){
				cells[i][j]=cols[j];
			}
		}
	}
	/**
	 * 在指定单元格中填入指定数字，然后排除本行、列、宫中的其它单元格的候选数中的该数。
	 * @param row
	 * @param col
	 * @param num
	 * @create 2018年2月28日 下午4:52:21 ：xieshp@css.com.cn 
	 * @modify
	 */
	private static void fillCell(int row,int col,char num){
		System.out.println("["+row+","+col+"]="+num);
		cells[row][col]=num;
		hint.remove(row+","+col);
		for(int i=0;i<9;i++){
			if(cells[row][i]=='0'){
				removeHintChar(row, i, num);
			}
			if(cells[i][col]=='0'){
				removeHintChar(i, col, num);
			}
		}
		int br=(row/3)*3;
		int bc=(col/3)*3;
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				if(cells[br+i][bc+j]=='0'){
					removeHintChar(br+i, bc+j, num);
				}
			}
		}
	}
	/**
	 * 排除指定单元格中的指定候选数，排除后的候选数如果唯一，则填入单元格，继续排除相关单元格。
	 * 如果单元格中无此数，则不作改变
	 * @param row
	 * @param col
	 * @param num
	 * @create 2018年2月28日 下午4:49:34 ：xieshp@css.com.cn 
	 * @modify
	 */
	private static void removeHintChar(int row,int col,char num){
		String pos=row+","+col;
		String s=hint.get(pos);
		int p=s.indexOf(""+num);
		if(p>=0){
			s=s.substring(0, p)+s.substring(p+1);
			if(s.length()==1){
				fillCell(row, col, s.charAt(0));
			}else{
				hint.put(pos, s);
			}
		}
	}
	/**余数解法：按行、列、宫检查一个空的单元格可以填入的数字，如果只有一个数字，则填入，如果有多个，则放入到候选数字中
	 * @param row 空单元格所在行
	 * @param col 空单元格所在列
	 * @return
	 * @create 2018年2月9日 下午3:40:34 ：xieshp@css.com.cn 
	 * @modify
	 */
	private static boolean checkEmptyCell(int row,int col){
		StringBuffer sbBuffer=new StringBuffer();
		//记录第row行的所有数字
		for(int i=0;i<9;i++){
			if(cells[row][i]!='0'){
				sbBuffer.append(cells[row][i]);
			}
		}
		//记录第col列所有数字
		for(int i=0;i<9;i++){
			if(cells[i][col]!='0'){
				sbBuffer.append(cells[i][col]);
			}
		}
		//检查本宫所有数字
		int boxR=(row / 3)*3;
		int boxC=(col / 3)*3;
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				if(cells[boxR+i][boxC+j]!='0'){
					sbBuffer.append(cells[boxR+i][boxC+j]);
				}
			}
		}
		//检查所缺的数字
		StringBuffer hintNumber=new StringBuffer();
		for(int i=1;i<=9;i++){
			if(sbBuffer.indexOf(""+i)<0){
				hintNumber.append(i);
			}
		}
		String rc=row+","+col;
		if(hintNumber.length()==1){
			cells[row][col]=hintNumber.charAt(0);
			System.out.println("["+rc+"]="+hintNumber.toString());
			if(hint.containsKey(rc)){
				hint.remove(rc);
			}
			return true;
		}else if(hintNumber.length()>1){
			hint.put(rc, hintNumber.toString());
			System.out.println("["+rc+"] 候选数有："+hintNumber.toString());
		}
		return false;
	}
	/**
	 * 如果同一行/列/宫有两个单元格中存在相同的两个候选数，且无其它候选数时，则本行/列/宫中其它单元格中不能再有这两个候选数
	 * @return
	 * @create 2018年2月28日 上午9:41:23 ：xieshp@css.com.cn 
	 * @modify
	 */
	private static boolean checkTwoHintNum(){
		boolean changed=false;
		//检查每一行
		for(int r=0;r<9;r++){
			for(int c1=0;c1<9;c1++){
				String pos=r+","+c1;
				if(hint.containsKey(pos)){
					String s1=hint.get(pos);
					if(s1.length()==2){//只有两个候选数
						for(int c2=c1+1;c2<9;c2++){
							String pos2=r+","+c2;
							if(hint.containsKey(pos2)){
								String s2=hint.get(pos2);
								if(s1.equals(s2)){//同一行中的两个单元格中的都是两个候选数，且数字相同
									for(int i=0;i<9;i++){
										if(i!=c1 && i!=c2 && cells[r][i]=='0'){
											String p=r+","+i;
											String s=hint.get(p);
											StringBuffer sb=new StringBuffer(s);
											for(int j=s.length()-1;j>=0;j--){
												if(sb.charAt(j)==s1.charAt(0)||sb.charAt(j)==s1.charAt(1)){
													sb.deleteCharAt(j);
													changed=true;
												}
											}
											if(sb.length()==1){
												System.out.println("["+p+"]="+sb.charAt(0));
												cells[r][i]=sb.charAt(0);
												hint.remove(p);
												return true;
											}else{
												hint.put(p, sb.toString());
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		//检查每一列
		for(int c=0;c<9;c++){
			for(int r1=0;r1<9;r1++){
				String pos=r1+","+c;
				if(hint.containsKey(pos)){
					String s1=hint.get(pos);
					if(s1.length()==2){//只有两个候选数
						for(int r2=r1+1;r2<9;r2++){
							String pos2=r2+","+c;
							if(hint.containsKey(pos2)){
								String s2=hint.get(pos2);
								if(s1.equals(s2)){//同一行中的两个单元格中的都是两个候选数，且数字相同
									for(int i=0;i<9;i++){
										if(i!=r1 && i!=r2 && cells[i][c]=='0'){
											String p=i+","+c;
											String s=hint.get(p);
											StringBuffer sb=new StringBuffer(s);
											for(int j=s.length()-1;j>=0;j--){
												if(sb.charAt(j)==s1.charAt(0)||sb.charAt(j)==s1.charAt(1)){
													sb.deleteCharAt(j);
													changed=true;
												}
											}
											if(sb.length()==1){
												System.out.println("["+p+"]="+sb.charAt(0));
												cells[i][c]=sb.charAt(0);
												hint.remove(p);
												return true;
											}else{
												hint.put(p, sb.toString());
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		//检查每一宫,三行三列共9宫
		for(int br=0;br<3;br++){
			for(int bc=0;bc<3;bc++){
				int r=br*3;//起始单元格的实际位置-行
				int c=bc*3;//起始单元格的实际位置-列
				//本宫内检查
				for(int r1=0;r1<3;r1++){
					for(int c1=0;c1<3;c1++){
						String pos1=(r+r1)+","+(c+c1);
						if(hint.containsKey(pos1)){
							String s1=hint.get(pos1);
							if(s1.length()==2){//只有两个候选数
								for(int r2=r1+1;r2<3;r2++){//同一行已经检查过了，从下一行开始查
									for(int c2=0;c2<3;c2++){
										if(c2==c1){//同一列的已经查过，跳过
											continue;
										}
										String pos2=(r+r2)+","+(c+c2);
										if(hint.containsKey(pos2)){
											String s2=hint.get(pos2);
											if(s1.equals(s2)){//同一宫中的两个单元格中的都是两个候选数，且数字相同
												for(int r3=0;r3<3;r3++){
													for(int c3=0;c3<3;c3++){
														if(cells[r+r3][c+c3]=='0'){
															if(r3==r1 && c3==c1 || r3==r2 && c3==c2){
																continue;
															}
															String pos3=(r+r3)+","+(c+c3);
															String s3=hint.get(pos3);
															StringBuffer sb=new StringBuffer(s3);
															for(int j=s3.length()-1;j>=0;j--){
																if(sb.charAt(j)==s1.charAt(0)||sb.charAt(j)==s1.charAt(1)){
																	sb.deleteCharAt(j);
																	changed=true;
																}
															}
															if(sb.length()==1){
																System.out.println("["+pos3+"]="+sb.charAt(0));
																cells[r+r3][c+c3]=sb.charAt(0);
																hint.remove(pos3);
																return true;
															}else{
																hint.put(pos3, sb.toString());
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if(changed){
			return checkTwoHintNum();
		}
		return false;
	}
	
	/**
	 * 基础摒除法： 如果某候选数在行/列/宫内的唯一一个单元格中出现，则此单元格中只能是该数
	 * @create 2018年2月9日 下午5:01:00 ：xieshp@css.com.cn 
	 * @modify
	 */
	private static boolean checkHintInBox(int row,int col){
		String curPos=row+","+col;
		String s=hint.get(curPos);
		for(int i=0;i<s.length();i++){
			char h=s.charAt(i);
			boolean isExists=false;//不存在
			//检查当前列每一单元格
			for(int r=0;r<9;r++){
				if(r==row){
					continue;
				}
				String pos=r+","+col;
				if(hint.containsKey(pos)){
					String s1=hint.get(pos);
					if(s1.contains(h+"")){
						isExists=true;
						break;
					}
				}
			}
			if(isExists){
				isExists=false;
				//检查当前行每一个单元格
				for(int c=0;c<9;c++){
					if(c==col){
						continue;
					}
					String pos=row+","+c;
					if(hint.containsKey(pos)){
						String s1=hint.get(pos);
						if(s1.contains(h+"")){
							isExists=true;
							break;
						}
					}
				}
			}
			if(isExists){
				isExists=false;
				//检查当前宫每一个单元格
				//检查本宫所有数字
				int boxR=(row / 3)*3;
				int boxC=(col / 3)*3;
				for(int r=0;r<3;r++){
					for(int c=0;c<3;c++){
						if(row==(boxR+r) && col==(boxC+c)){
							continue;
						}
						String pos=(boxR+r)+","+(boxC+c);
						if(hint.containsKey(pos)){
							String s1=hint.get(pos);
							if(s1.contains(h+"")){
								isExists=true;
								break;
							}
						}
					}
				}
			}
			if(!isExists){
				fillCell(row, col, h);
				/*System.out.println("["+curPos+"]="+h);
				cells[row][col]=h;
				hint.remove(curPos);*/
				return true;
			}
		}
		return false;
	}
	private static void print(){
		for(int i=0;i<9;i++){
			if(i%3==0){
				System.out.println("-------------------------");
			}
			for(int j=0;j<9;j++){
				if(j%3==0){
					System.out.print("| ");
				}
				System.out.print(cells[i][j]+" ");
			}
			System.out.println("|");
		}
		System.out.println("-------------------------");
	}

}
