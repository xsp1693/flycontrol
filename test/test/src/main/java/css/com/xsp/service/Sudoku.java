/**
 * <b>项目名：</b>test<br/>  
 * <b>包名：</b>css.com.xsp.service<br/>  
 * <b>文件名：</b>Sudoku.java<br/>  
 * <b>版本信息：</b>1.0<br/>  
 * <b>日期：</b>2018年2月9日 上午10:34:41<br/>  
 * <b>COPYRIGHT 2010-2016 ALL RIGHTS RESERVED 中国软件与技术服务股份有限公司</b>-版权所有<br/>
 */
package css.com.xsp.service;

import net.spy.memcached.MemcachedClient;

/**
 * @createTime 2018年2月9日 上午10:34:41
 * @modifyTime 
 * @author xieshp@css.com.cn
 * @version 1.0
 */
public class Sudoku {
//	private  Map<String, String> hint=new HashMap<String, String>();//记录候选数，key为坐标，value为候选数
	private  int[][] cells=new int[9][9];
	public static void main(String[] args) {
//		String q="810003290,067000000,900500006,000408000,604000809,000209000,700001008,000000370,053800042";//28
//		String q="050000020,400206007,008030100,010000060,009000500,070000090,005080300,700901004,020000070";//24
//		String q="500090201,002007008,080000300,014005000,000903000,000800940,003000060,600200100,809060005";//26
//		String q="000020040,000000900,000300070,003040000,600050093,970080006,010005200,060007050,800600000";//23个数
//		String q="600000400,008070620,350000080,000640800,000000000,004053000,080000096,041020500,002000001";//24
//		String q="005300000,800000020,070010500,400005300,010070006,003200080,060500009,004000030,000009700";
		String q="003620000,006903000,000001053,000014279,000000000,450000000,860000000,000002500,000007800";
		Sudoku t=new Sudoku();
		MemcachedClient mcc=MemcachedUtil.getMcc();
		long t1=System.currentTimeMillis();
		String resutl=(String)mcc.get(q);
		if(resutl==null){
			t.initCells(q);
			t.print(q);
			t.compute();
			boolean b=t.checkResult();
			if(b){
				resutl=t.toStr();
				mcc.add(q, 3600*8, resutl);
			}else{
				System.out.println("有错或未完成");
			}
		}
		long t2=System.currentTimeMillis();
		mcc.shutdown();
		t.print(resutl);
		System.out.println("共耗时"+(t2-t1)+"毫秒。");
	}
	
	private void compute(){
		findAllHint();
		boolean filled=false;
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				if(cells[i][j]>10){
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
		}/*else{
			filled=checkTwoHintNum();
			if(filled){
				compute();
			}
		}*/
		bruteForce();
	}

	/**
	 * 查找所有的候选数
	 * @create 2018年3月1日 下午12:38:12 ：xieshp@css.com.cn 
	 * @modify
	 */
	private  void findAllHint() {
		int n=0;
		do{
			n=0;
			for(int i=0;i<9;i++){
				for(int j=0;j<9;j++){
					if(cells[i][j]==0){
						boolean b=checkEmptyCell(i, j);
						n+=(b?1:0);
					}
				}
			}
			System.out.println("====================================");
		}while(n>0);
	}
	//初始化所有单元格
	private void initCells(String question) {
		String[] rows=question.split(",");
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				cells[i][j]=Integer.valueOf(rows[i].substring(j, j+1));
			}
		}
	}
	/**
	 * 在指定单元格中填入指定数字，然后排除本行、列、宫中的其它单元格的候选数中的该数。
	 * @param row
	 * @param col
	 * @param num
	 * @throws Exception 
	 * @create 2018年2月28日 下午4:52:21 ：xieshp@css.com.cn 
	 * @modify
	 */
	private  void fillCell(int row,int col,int num){
		cells[row][col]=num;
		if(num>10){
			return;
		}
		System.out.println("["+row+","+col+"]="+num);
		for(int i=0;i<9;i++){
			if(cells[row][i]>10){
				removeHintChar(row, i, num);
			}
			if(cells[i][col]>10){
				removeHintChar(i, col, num);
			}
		}
		int br=(row/3)*3;
		int bc=(col/3)*3;
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				if(cells[br+i][bc+j]>10){
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
	private  void removeHintChar(int row,int col,int num){
		String s=cells[row][col]+"";
		int p=s.indexOf(""+num);
		if(p>=0){
			s=s.substring(0, p)+s.substring(p+1);
			if(s.length()==1){
				fillCell(row, col, Integer.valueOf(s));
			}else{
				cells[row][col]=Integer.valueOf(s);
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
	private  boolean checkEmptyCell(int row,int col){
		StringBuffer sbBuffer=new StringBuffer();
		//记录第row行的所有数字
		for(int i=0;i<9;i++){
			if(cells[row][i]<10){
				sbBuffer.append(cells[row][i]);
			}
		}
		//记录第col列所有数字
		for(int i=0;i<9;i++){
			if(cells[i][col]<10){
				sbBuffer.append(cells[i][col]);
			}
		}
		//检查本宫所有数字
		int boxR=(row / 3)*3;
		int boxC=(col / 3)*3;
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				if(cells[boxR+i][boxC+j]<10){
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
		fillCell(row, col, Integer.parseInt(hintNumber.toString()));
		if(hintNumber.length()==1){
			return true;
		}
		return false;
	}
	/**
	 * 如果同一行/列/宫有两个单元格中存在相同的两个候选数，且无其它候选数时，则本行/列/宫中其它单元格中不能再有这两个候选数
	 * @return
	 * @create 2018年2月28日 上午9:41:23 ：xieshp@css.com.cn 
	 * @modify
	 */
	/*private  boolean checkTwoHintNum(){
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
										if(i!=c1 && i!=c2 && cells[r][i]==0){
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
												fillCell(r, i, Integer.valueOf(sb.toString()));
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
										if(i!=r1 && i!=r2 && cells[i][c]==0){
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
												fillCell(i, c, Integer.valueOf(sb.toString()));
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
														if(cells[r+r3][c+c3]==0){
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
																fillCell(r+r3, c+c3, Integer.valueOf(sb.toString()));
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
	}*/
	
	/**
	 * 基础摒除法： 如果某候选数在行/列/宫内的唯一一个单元格中出现，则此单元格中只能是该数
	 * @create 2018年2月9日 下午5:01:00 ：xieshp@css.com.cn 
	 * @modify
	 */
	private  boolean checkHintInBox(int row,int col){
		if(cells[row][col]<10){
			return false;
		}
		String s=cells[row][col]+"";
		for(int i=0;i<s.length();i++){
			int h=(s.charAt(i)-'0');
			boolean isExists=false;//不存在
			//本宫起始位置
			int boxR=(row / 3)*3;
			int boxC=(col / 3)*3;
			//检查当前列每一单元格
			for(int n=0;n<9;n++){
				if(n!=row && cells[n][col]>10){
					String s1=cells[n][col]+"";
					if(s1.contains(h+"")){
						isExists=true;
						break;
					}
				}
			}
			if(isExists){
				isExists=false;
				for(int n=0;n<9;n++){
					if(n!=col && cells[row][n]>10){
						String s1=cells[row][n]+"";
						if(s1.contains(h+"")){
							isExists=true;
							break;
						}
					}
				}
			}
			if(isExists){
				isExists=false;
				for(int n=0;n<9;n++){
					if(!(row==(boxR+n/3) && col==(boxC+n % 3)) && cells[boxR+n/3][boxC+n % 3]>10){
						String s1=cells[boxR+n/3][boxC+n % 3]+"";
						if(s1.contains(h+"")){
							isExists=true;
							break;
						}
					}
				}
			}
			if(!isExists){
				fillCell(row, col, h);
				return true;
			}
		}
		return false;
	}
	/**
	 * 暴力破解
	 * @create 2018年3月1日 上午11:33:47 ：xieshp@css.com.cn 
	 * @modify
	 */
	private  boolean bruteForce(){
		int r=0;
		int c=0;
		int h=0;
		for(int i=0;i<81;i++){
			r=i/9;
			c=i%9;
			h=cells[r][c];
			if(h>10 && h<100){
				break;
			}
		}
		if(h>10){
//			String s=toStr();
			int[][] bak=copyArray(cells);
			fillCell(r, c, h/10);
			boolean b=bruteForce();
			if(b){
				return b;
			}
//			initCells(s);
//			findAllHint();
			cells=copyArray(bak);
			fillCell(r, c, h%10);
			return bruteForce();
		}
		return checkResult();
	}
	/**
	 * 检查当前的结果是否正确
	 * @return
	 * @create 2018年3月1日 上午11:30:55 ：xieshp@css.com.cn 
	 * @modify
	 */
	private  boolean checkResult(){
		for(int i=0;i<9;i++){
			StringBuilder rs=new StringBuilder();
			StringBuilder rc=new StringBuilder();
			for(int j=0;j<9;j++){
				if(cells[i][j]>10 || rs.indexOf(""+cells[i][j])>=0){
					System.out.println("["+i+","+j+"]="+cells[i][j]);
					return false;
				}else{
					rs.append(cells[i][j]);
				}
				if(cells[j][i]>10 || rc.indexOf(""+cells[j][i])>=0){
					System.out.println("["+j+","+i+"]="+cells[j][i]);
					return false;
				}else{
					rc.append(cells[j][i]);
				}
			}
		}
		return true;
	}
	private  String toStr(){
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				sb.append(cells[i][j]>10?0:cells[i][j]);
			}
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	private int[][] copyArray(int[][] src){
		int[][] newCells=new int[9][9];
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				newCells[i][j]=src[i][j];
			}
		}
		return newCells;
	}
	private void print(int[][] cells){
		for(int i=0;i<9;i++){
			if(i%3==0){
				System.out.println("-------------------------");
			}
			for(int j=0;j<9;j++){
				if(j%3==0){
					System.out.print("| ");
				}
				System.out.print((cells[i][j]<10?cells[i][j]:0)+" ");
			}
			System.out.println("|");
		}
		System.out.println("-------------------------");
	}
	private void print(String question){
		String[] arr=question.split(",");
		for(int i=0;i<9;i++){
			if(i%3==0){
				System.out.println("-------------------------");
			}
			for(int j=0;j<9;j++){
				if(j%3==0){
					System.out.print("| ");
				}
				System.out.print(arr[i].charAt(j)+" ");
			}
			System.out.println("|");
		}
		System.out.println("-------------------------");
	}
}
