/**
 * <b>项目名：</b>test<br/>  
 * <b>包名：</b>css.com.xsp.service<br/>  
 * <b>文件名：</b>Mine.java<br/>  
 * <b>版本信息：</b>1.0<br/>  
 * <b>日期：</b>2018年3月12日 下午4:13:13<br/>  
 * <b>COPYRIGHT 2010-2016 ALL RIGHTS RESERVED 中国软件与技术服务股份有限公司</b>-版权所有<br/>
 */
package css.com.xsp.service;
/**
 * @description 生成扫雷游戏的地雷分布和提示数字
 * @createTime 2018年3月12日 下午4:13:13
 * @modifyTime 
 * @author xieshp@css.com.cn
 * @version 1.0
 */
public class Mine {
	private int w=10;
	private int h=10;
	private int[][] cells=new int[w][h];
	public static void main(String[] args) {
		Mine t=new Mine();
		t.InitMine();
		t.print(t.cells);
	}
	private void InitMine(){
		int n=cells.length;
		for(int i=0;i<n;i++){
			int r=(int)(Math.random()*100);
			cells[r/n][r % n]=-1;
		}
		for(int i=0;i<cells.length;i++){
			for(int j=0;j<cells[i].length;j++){
				countMine(i,j);
			}
		}
	}
	private void countMine(int i,int j) {
		if(cells[i][j]==-1){
			return;
		}
		int c=0;
		for(int k=0;k<9;k++){
			int row=i-1+k/3;
			int col=j-1+k%3;
			if(row>=0 && col>=0 && row<w && col<h && k!=4){
				if(cells[row][col]==-1){
					c++;
				}
			}
		}
		cells[i][j]=c;
	}
	private void print(int[][]cells){
		for(int i=0;i<cells.length;i++){
			for(int j=0;j<cells[i].length;j++){
				System.out.print((cells[i][j]>=0?cells[i][j]:"X")+" ");
			}
			System.out.println();
		}
	}
}
