package 扫雷;

import java.awt.BorderLayout;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class saolei implements ActionListener{
	JFrame jFrame =new JFrame("扫雷游戏");
	JButton reset=new JButton("重新来过");
	Container container=new Container();
	
	//游戏数据结构
	saoleiConstant constant =new saoleiConstant();
	JButton[][] buttons=new JButton[constant.line][constant.lie];
	int[][] counts =new int[constant.line][constant.lie];

	
	
	
	public saolei() {
		// TODO 自动生成的构造函数存根
		//显示窗口
		jFrame.setSize(1000, 900);
		jFrame.setResizable(false);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setLayout(new BorderLayout());
		
		//显示重来按钮
		addResetButton();
		
		//定义游戏数据结构
		
		//添加方格
		addButtons();
		
		//埋雷
		addLei();
		
		//计算一个方格周围雷的个数
		calcNeiboLei();
		
		jFrame.setVisible(true);
	}
	public static void main(String[] args) {
		saolei lei=new saolei();
	}
	void addResetButton() {
		// TODO 自动生成的方法存根
		reset.setBackground(Color.GREEN);
		reset.setOpaque(true);
		reset.addActionListener(this);
		jFrame.add(reset,BorderLayout.NORTH);
	}
	private void addButtons() {
		// TODO 自动生成的方法存根
		jFrame.add(container,BorderLayout.CENTER);
		container.setLayout(new GridLayout(constant.line, constant.lie));
		for(int i=0;i<constant.line;i++)
		{
			for(int j=0;j<constant.lie;j++)
			{
				JButton button=new JButton();
				button.setBackground(Color.WHITE);
				button.setOpaque(true);
				button.addActionListener(this);
				buttons[i][j]=button;
				container.add(button);
			}
		}
	}
	@Override
	//当表格内的方块被点击之后,立刻找该方法
	public void actionPerformed(ActionEvent e) {
		// TODO 自动生成的方法存根
		JButton button=(JButton)e.getSource();
		if (button.equals(reset)) {
			for(int i=0;i<constant.line;i++)
			{
				for(int j=0;j<constant.lie;j++)
				{
					buttons[i][j].setText("");
					buttons[i][j].setEnabled(true);
					buttons[i][j].setBackground(Color.WHITE);
					counts[i][j]=0;
				}
			}
			addLei();
			calcNeiboLei();
		}
		else
		{
			int count=-1;
			for(int i=0;i<constant.line;i++)
			{
				for(int j=0;j<constant.lie;j++)
				{
					if(button==(buttons[i][j]))
					{
						count=counts[i][j];
						if (count==constant.leiCode) {
							loseGame();
						}
						else
						{
							openCell(i, j);
							checkWin();
						}
						return;
					}
				}
			}


		}
	}
	//埋雷
	private void addLei() {
		// TODO 自动生成的方法存根
		Random random =new Random();
		int randline,randlie;
		for(int i=0;i<constant.leiCount;i++)
		{
			randline=random.nextInt(constant.line);
			randlie=random.nextInt(constant.lie);
			if (counts[randline][randlie]!=constant.leiCode) {
				counts[randline][randlie]=constant.leiCode;
				//buttons[randline][randlie].setText("x");
			}
			else {
				i--;
			}
		}
	}
	//计算一个方格周围雷的个数
	private void calcNeiboLei() {
		// TODO 自动生成的方法存根
		for(int i=0;i<constant.line;i++)
		{
			for(int j=0;j<constant.lie;j++)
			{
				int count=0;
				if(counts[i][j]==constant.leiCode)	continue;
				if(i>0&&j>0&&counts[i-1][j-1]==constant.leiCode)	count++;
				if(i>0&&counts[i-1][j]==constant.leiCode)	count++;
				if(j>0&&counts[i][j-1]==constant.leiCode)	count++;
				if(i>0&&j<19&&counts[i-1][j+1]==constant.leiCode)	count++;
				if(i<19&&j>0&&counts[i+1][j-1]==constant.leiCode)	count++;
				if(i<19&&j<19&&counts[i+1][j+1]==constant.leiCode)	count++;
				if(i<19&&counts[i+1][j]==constant.leiCode)	count++;
				if(j<19&&counts[i][j+1]==constant.leiCode)	count++;
				
				counts[i][j]=count;
				//buttons[i][j].setText(""+count);

			}
		}
	}
	private void loseGame() {
		// TODO 自动生成的方法存根
		for(int i=0;i<constant.line;i++)
		{
			for(int j=0;j<constant.lie;j++)
			{
				int count=counts[i][j];
				buttons[i][j].setFont(new Font("微软雅黑", 0, 25));
				if (count==10) {
					buttons[i][j].setText("9");
					buttons[i][j].setBackground(Color.RED);
				}
				else
				{
					buttons[i][j].setText(""+count);
					buttons[i][j].setBackground(Color.YELLOW);
				}
				buttons[i][j].setOpaque(true);
				//按钮被点一次之后将这个按钮变成不可用状态
				buttons[i][j].setEnabled(false);
			}
		}

	}
	//将0周围的零都显示出来
	private void openCell(int i,int j) {
		// TODO 自动生成的方法存根
		if (buttons[i][j].isEnabled()==false) return;
		
		//按钮被点一次之后将这个按钮变成不可用状态
		buttons[i][j].setEnabled(false);
		if (counts[i][j]==0) {
			buttons[i][j].setBackground(Color.YELLOW);
			if(i>0&&j>0&&counts[i-1][j-1]!=constant.leiCode)	openCell(i-1, j-1);
			if(i>0&&counts[i-1][j]!=constant.leiCode)	openCell(i-1, j);
			if(j>0&&counts[i][j-1]!=constant.leiCode)	openCell(i, j-1);
			if(i>0&&j<19&&counts[i-1][j+1]!=constant.leiCode)	openCell(i-1, j+1);
			if(i<19&&j>0&&counts[i+1][j-1]!=constant.leiCode)	openCell(i+1, j-1);
			if(i<19&&j<19&&counts[i+1][j+1]!=constant.leiCode)	openCell(i+1, j+1);
			if(i<19&&counts[i+1][j]!=constant.leiCode)	openCell(i+1, j);
			if(j<19&&counts[i][j+1]!=constant.leiCode)	openCell(i, j+1);
		}
		else
		{
			buttons[i][j].setFont(new Font("微软雅黑", 0, 25));
			buttons[i][j].setText(""+counts[i][j]);
			buttons[i][j].setBackground(Color.YELLOW);
			buttons[i][j].setOpaque(true);
		}
	}
	private void checkWin() {
		// TODO 自动生成的方法存根
		for(int i=0;i<constant.line;i++)
		{
			for(int j=0;j<constant.lie;j++)
			{
				if (buttons[i][j].isEnabled()&&counts[i][j]!=constant.leiCode) {
					return ;
				}
			}
		}
		JOptionPane.showMessageDialog(jFrame, "Yeah,你赢了");
	}
}
