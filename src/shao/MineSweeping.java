package shao;

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

/**
 * @author shaojunying
 */
public class MineSweeping implements ActionListener {
    JFrame jFrame = new JFrame("扫雷游戏");
    JButton reset = new JButton("重新来过");
    Container container = new Container();

    MineSweepingConstant constant = new MineSweepingConstant();
    JButton[][] buttons = new JButton[constant.lines][constant.columns];
    int[][] counts = new int[constant.lines][constant.columns];


    public MineSweeping() {
        //显示窗口
        jFrame.setSize(1000, 900);
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLayout(new BorderLayout());

        //显示重来按钮
        //测试git
        addResetButton();

        //定义游戏数据结构

        //添加方格
        addButtons();

        //埋雷111
        addMine();

        //计算一个方格周围雷的个数
        calcNumberOfMineAround();

        jFrame.setVisible(true);
    }

    public static void main(String[] args) {
        MineSweeping mineSweeping = new MineSweeping();
    }

    void addResetButton() {
        reset.setBackground(Color.GREEN);
        reset.setOpaque(true);
        reset.addActionListener(this);
        jFrame.add(reset, BorderLayout.NORTH);
    }

    private void addButtons() {
        jFrame.add(container, BorderLayout.CENTER);
        container.setLayout(new GridLayout(constant.lines, constant.columns));
        for (int i = 0; i < constant.lines; i++) {
            for (int j = 0; j < constant.columns; j++) {
                JButton button = new JButton();
                button.setBackground(Color.WHITE);
                button.setOpaque(true);
                button.addActionListener(this);
                buttons[i][j] = button;
                container.add(button);
            }
        }
    }

    /**
     * 当表格内的方块被点击之后,立刻找该方法
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        if (button.equals(reset)) {
            for (int i = 0; i < constant.lines; i++) {
                for (int j = 0; j < constant.columns; j++) {
                    buttons[i][j].setText("");
                    buttons[i][j].setEnabled(true);
                    buttons[i][j].setBackground(Color.WHITE);
                    counts[i][j] = 0;
                }
            }
            addMine();
            calcNumberOfMineAround();
        } else {
            int count = -1;
            for (int i = 0; i < constant.lines; i++) {
                for (int j = 0; j < constant.columns; j++) {
                    if (button == (buttons[i][j])) {
                        count = counts[i][j];
                        if (count == constant.mineCode) {
                            loseGame();
                        } else {
                            openCell(i, j);
                            checkWin();
                        }
                        return;
                    }
                }
            }


        }
    }

    /**
     * 埋雷
     */
    private void addMine() {
        Random random = new Random();
        int randline, randlie;
        for (int i = 0; i < constant.mineCount; i++) {
            randline = random.nextInt(constant.lines);
            randlie = random.nextInt(constant.columns);
            if (counts[randline][randlie] != constant.mineCode) {
                counts[randline][randlie] = constant.mineCode;
                //buttons[randline][randlie].setText("x");
            } else {
                i--;
            }
        }
    }

    /**
     * 计算一个方格周围雷的个数
     */
    private void calcNumberOfMineAround() {
        for (int i = 0; i < constant.lines; i++) {
            for (int j = 0; j < constant.columns; j++) {
                int count = 0;
                if (counts[i][j] == constant.mineCode) continue;
                if (i > 0 && j > 0 && counts[i - 1][j - 1] == constant.mineCode) count++;
                if (i > 0 && counts[i - 1][j] == constant.mineCode) count++;
                if (j > 0 && counts[i][j - 1] == constant.mineCode) count++;
                if (i > 0 && j < 19 && counts[i - 1][j + 1] == constant.mineCode) count++;
                if (i < 19 && j > 0 && counts[i + 1][j - 1] == constant.mineCode) count++;
                if (i < 19 && j < 19 && counts[i + 1][j + 1] == constant.mineCode) count++;
                if (i < 19 && counts[i + 1][j] == constant.mineCode) count++;
                if (j < 19 && counts[i][j + 1] == constant.mineCode) count++;

                counts[i][j] = count;
                //buttons[i][j].setText(""+count);

            }
        }
    }

    private void loseGame() {
        for (int i = 0; i < constant.lines; i++) {
            for (int j = 0; j < constant.columns; j++) {
                int count = counts[i][j];
                if (count == 10) {
                    buttons[i][j].setText("9");
                    buttons[i][j].setBackground(Color.RED);
                } else {
                    buttons[i][j].setText("" + count);
                    buttons[i][j].setBackground(Color.YELLOW);
                }
                buttons[i][j].setOpaque(true);
                //按钮被点一次之后将这个按钮变成不可用状态
                buttons[i][j].setEnabled(false);
            }
        }

    }

    //将0周围的零都显示出来
    private void openCell(int i, int j) {
        if (buttons[i][j].isEnabled() == false) {
            return;
        }

        //按钮被点一次之后将这个按钮变成不可用状态
        buttons[i][j].setEnabled(false);
        if (counts[i][j] == 0) {
            buttons[i][j].setBackground(Color.YELLOW);
            if (i > 0 && j > 0 && counts[i - 1][j - 1] != constant.mineCode) openCell(i - 1, j - 1);
            if (i > 0 && counts[i - 1][j] != constant.mineCode) openCell(i - 1, j);
            if (j > 0 && counts[i][j - 1] != constant.mineCode) openCell(i, j - 1);
            if (i > 0 && j < 19 && counts[i - 1][j + 1] != constant.mineCode) openCell(i - 1, j + 1);
            if (i < 19 && j > 0 && counts[i + 1][j - 1] != constant.mineCode) openCell(i + 1, j - 1);
            if (i < 19 && j < 19 && counts[i + 1][j + 1] != constant.mineCode) openCell(i + 1, j + 1);
            if (i < 19 && counts[i + 1][j] != constant.mineCode) openCell(i + 1, j);
            if (j < 19 && counts[i][j + 1] != constant.mineCode) openCell(i, j + 1);
        } else {
            buttons[i][j].setFont(new Font("微软雅黑", 0, 25));
            buttons[i][j].setText("" + counts[i][j]);
            buttons[i][j].setBackground(Color.YELLOW);
            buttons[i][j].setOpaque(true);
        }
    }

    private void checkWin() {
        for (int i = 0; i < constant.lines; i++) {
            for (int j = 0; j < constant.columns; j++) {
                if (buttons[i][j].isEnabled() && counts[i][j] != constant.mineCode) {
                    return;
                }
            }
        }
        JOptionPane.showMessageDialog(jFrame, "Yeah,你赢了");
    }
}
