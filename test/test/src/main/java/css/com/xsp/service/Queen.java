/**
 * <b>项目名：</b>test<br/>  
 * <b>包名：</b>css.com.xsp.service<br/>  
 * <b>文件名：</b>Queen.java<br/>  
 * <b>版本信息：</b>1.0<br/>  
 * <b>日期：</b>2018年3月5日 上午9:25:34<br/>  
 * <b>COPYRIGHT 2010-2016 ALL RIGHTS RESERVED 中国软件与技术服务股份有限公司</b>-版权所有<br/>
 */
package css.com.xsp.service;

/**
 * @description TODO
 * @createTime 2018年3月5日 上午9:25:34
 * @modifyTime 
 * @author xieshp@css.com.cn
 * @version 1.0
 */
public class Queen {
	private int[][] board=new int[8][8];
	private int count=0;
	private long start;
	public static void main(String[] args){
		Queen t=new Queen();
		t.start=System.currentTimeMillis();
		t.backTrace(0, 0);
		System.out.println("总耗时："+(System.currentTimeMillis()-t.start));
	}
	
	private void backTrace(int i,int j){
		if(i>7){
			count++;
			printBoard();
			return;
		}
		if(checkAllow(i,j)){
			board[i][j]=1;
			backTrace(i+1, 0);
			board[i][j]=0;
		}
		if(j<7){
			backTrace(i, j+1);
		}
	}

	private boolean checkAllow(int i, int j) {
		for(int n=0;n<8;n++){
			//列
			if(board[n][j]==1){
				return false;
			}
			//行
			if(board[i][n]==1){
				return false;
			}
			//向下
			if(n+i<8){
				//向右
				if(j+n<8){
					if(board[n+i][n+j]==1){
						return false;
					}
				}
				//向左
				if(j-n>=0){
					if(board[n+i][j-n]==1){
						return false;
					}
				}
			}
			//向上
			if(i-n>=0){
				//向右
				if(j+n<8){
					if(board[i-n][n+j]==1){
						return false;
					}
				}
				//向左
				if(j-n>=0){
					if(board[i-n][j-n]==1){
						return false;
					}
				}
			}
		}
		return true;
	}
	private void printBoard(){
		System.out.println("第"+count+"种解法:");
		for(int r=0;r<8;r++){
			for(int c=0;c<8;c++){
				System.out.print(board[r][c]+" ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
}
