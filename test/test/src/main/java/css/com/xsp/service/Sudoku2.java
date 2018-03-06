/**
 * <b>项目名：</b>test<br/>  
 * <b>包名：</b>css.com.xsp.service<br/>  
 * <b>文件名：</b>Sudoku2.java<br/>  
 * <b>版本信息：</b>1.0<br/>  
 * <b>日期：</b>2018年3月2日 上午11:23:24<br/>  
 * <b>COPYRIGHT 2010-2016 ALL RIGHTS RESERVED 中国软件与技术服务股份有限公司</b>-版权所有<br/>
 */
package css.com.xsp.service;

import java.util.Calendar;

public class Sudoku2 {
	private int matrix[][];
	private long timeAfter = 0;
	private long timeBefore = 0;

	public Sudoku2(int m[][]) {
		matrix = new int[9][9];
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				matrix[i][j] = m[i][j];
		this.timeBefore = Calendar.getInstance().getTimeInMillis();
	}

	public void backTrack(int i, int j) {
		if (i == 8 && j >= 9) {
			this.timeAfter = Calendar.getInstance().getTimeInMillis();
			// 成功输出矩阵
			this.showMatrix();
			return;
		}

		if (j == 9) {
			j = 0;
			i++;
		}

		if (matrix[i][j] == 0) {
			// 数字为零
			for (int k = 1; k <= 9; k++) {
				if (bound(i, j, k)) {
					matrix[i][j] = k;
					// 符合条件，查找下一个方格
					backTrack(i, j + 1);
					matrix[i][j] = 0;
				}
			}
		} else {
			// 数字不为零，直接查找下一个
			backTrack(i, j + 1);
		}
	}

	/**
	 * 判断要填入的数字和同行同列以及同一九宫格内数字是否重复
	 */
	private boolean bound(int i, int j, int k) {
		int m = i / 3;
		int n = j / 3;
		for (int p = 0; p < 9; p++) {
			if (k == matrix[i][p]) {
				return false;
			}
			if (k == matrix[p][j]) {
				return false;
			}
			if (k == matrix[3 * m + p / 3][3 * n + p % 3]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 打印解题时间
	 * 
	 * @return
	 */
	public long printTime() {
		return this.timeAfter - this.timeBefore;
	}

	/**
	 * 打印矩阵
	 */
	public void showMatrix() {
		for(int i=0;i<9;i++){
			if(i%3==0){
				System.out.println("-------------------------");
			}
			for(int j=0;j<9;j++){
				if(j%3==0){
					System.out.print("| ");
				}
				System.out.print(matrix[i][j]+" ");
			}
			System.out.println("|");
		}
		System.out.println("-------------------------");
		System.out.println("解题时间： " + printTime() + "毫秒");
	}
	private static int[][] initCells(String question) {
		int[][] a=new int[9][9];
		String[] rows=question.split(",");
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				a[i][j]=Integer.valueOf(rows[i].substring(j, j+1));
			}
		}
		return a;
	}
	public static void main(String[] args) {
		int matrix[][] = { 
				{ 3, 0, 6, 0, 5, 7, 0, 0, 0 }, 
				{ 7, 9, 0, 0, 2, 4, 0, 0, 0 }, 
				{ 0, 5, 0, 6, 0, 0, 9, 7, 4 },
				{ 8, 0, 1, 0, 0, 9, 0, 0, 0 }, 
				{ 0, 2, 0, 3, 0, 8, 0, 0, 7 }, 
				{ 4, 0, 0, 0, 6, 0, 5, 0, 0 },
				{ 0, 0, 4, 0, 3, 6, 0, 5, 0 }, 
				{ 2, 0, 3, 7, 0, 5, 0, 0, 1 }, 
				{ 0, 0, 7, 4, 1, 0, 6, 0, 0 } };

		int ma1[][] = { 
				{ 0, 3, 0, 0, 0, 5, 0, 6, 0 }, 
				{ 0, 1, 0, 0, 0, 3, 0, 8, 0 }, 
				{ 0, 4, 0, 0, 0, 0, 0, 0, 7 },
				{ 0, 0, 7, 0, 2, 4, 0, 0, 0 }, 
				{ 5, 0, 0, 0, 9, 0, 0, 0, 0 }, 
				{ 0, 8, 0, 3, 0, 0, 5, 0, 0 },
				{ 0, 0, 0, 8, 0, 0, 0, 0, 0 }, 
				{ 0, 0, 9, 0, 0, 0, 0, 7, 3 }, 
				{ 0, 5, 0, 9, 0, 0, 0, 0, 2 } };

		int ma2[][] = { 
				{ 0, 0, 0, 0, 0, 4, 0, 0, 0 }, // 8
				{ 0, 0, 0, 2, 0, 3, 0, 8, 0 }, 
				{ 8, 3, 0, 9, 0, 0, 0, 5, 0 }, 
				{ 0, 5, 3, 0, 9, 0, 7, 0, 0 },
				{ 0, 0, 0, 6, 3, 0, 0, 4, 5 }, // 7
				{ 0, 7, 0, 5, 0, 0, 0, 0, 0 }, 
				{ 0, 0, 6, 8, 0, 0, 0, 0, 0 }, 
				{ 3, 0, 0, 0, 2, 9, 0, 0, 0 },
				{ 2, 0, 9, 0, 0, 0, 0, 0, 1 } };// 3

		// 号称世界上最难数独
		int[][] sudoku = { 
				{ 8, 0, 0, 0, 0, 0, 0, 0, 0 }, 
				{ 0, 0, 3, 6, 0, 0, 0, 0, 0 }, 
				{ 0, 7, 0, 0, 9, 0, 2, 0, 0 },
				{ 0, 5, 0, 0, 0, 7, 0, 0, 0 }, 
				{ 0, 0, 0, 0, 4, 5, 7, 0, 0 }, 
				{ 0, 0, 0, 1, 0, 0, 0, 3, 0 },
				{ 0, 0, 1, 0, 0, 0, 0, 6, 8 }, 
				{ 0, 0, 8, 5, 0, 0, 0, 1, 0 }, 
				{ 0, 9, 0, 0, 0, 0, 4, 0, 0 } };
		String q="000020040,000000900,000300070,003040000,600050093,970080006,010005200,060007050,800600000";//23个数
		int[][] a=initCells(q);
		Sudoku2 m = new Sudoku2(a);
		m.backTrack(0, 0);
	}
}