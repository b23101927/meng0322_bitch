//SHIT
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class main extends JFrame implements ActionListener, Broadcast {

	ServerSocket serverSock;// 連線
	private int judge_pos_neg;// 判斷正負 用在+10-10
	private int judge_ten_or_twenty=10;// 判斷10或20
	private static ArrayList cardbase = new ArrayList();// 牌庫
	private int judge_times_of_card4 = 1;// 看迴轉用幾次
	private int judge_whether_use_card5 = 0;// 看有沒有用指定
	private int judge_player = 1;// 判斷換誰出牌
	private int counts_cards = 5;// 發牌用ㄉ 拿完手牌的
	private static String card_01,card_02,card_03,card_04,card_05;// BUTTON上的字 初始手牌
	private int total_number_int; // ��憿舐內�閮��
	private String total_number_str = "00";// 憿舐內�
	private JLabel show_number; // 顯示現在數字多少(上面的總數
	private JLabel show_function; // 顯示用了哪些功能牌(下面的
	private JLabel show_player; // 顯示換誰
	private int judge_player_when_use5 = 1;// check 5
	private JButton bt1, bt2, bt3, bt4, bt5, bt6, bt7, bt8, bt9, bt10;
	private String show_leftword = "開始";
	private static ArrayList win_or_lose = new ArrayList();
	ArrayList<Socket> clientDatas = new ArrayList<>();

	public static void main(String[] args) {
		main frame = new main();
	}

	public main() {
		//輸贏
		win_or_lose.add(null);
		win_or_lose.add(null);
		win_or_lose.add(null);
		// frame相關參數
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		// 顯示總數
		show_number = new JLabel(total_number_str);
		show_number.setSize(200, 200);
		show_number.setLocation(350, 0);
		show_number.setFont(new java.awt.Font("Dialog", 1, 100));
		// 顯示功能
		show_function = new JLabel("開始!");
		show_function.setSize(200, 200);
		show_function.setLocation(200, 200);
		show_function.setFont(new java.awt.Font("Dialog", 1, 30));
		// 顯示換誰
		show_player = new JLabel("換Player" + judge_player);
		show_player.setSize(200, 200);
		show_player.setLocation(500, 200);
		show_player.setFont(new java.awt.Font("Dialog", 1, 30));

		this.add(show_number);
		this.add(show_player);
		this.add(show_function);
	
		setTitle("Player1");// 改名字
		setVisible(true);
		setResizable(false);
		try {
			ServerSocket serverSock = new ServerSocket(8000);
			System.out.print("Server started...");
			while (true) {
				Socket cSock = serverSock.accept();
				Chat chat = new Chat(cSock, this);

				clientDatas.add(cSock);
				Thread chatthread = new Thread(chat);
//				System.out.println("connect");
				chatthread.start();
				System.out.println("connecting");
			}
		} catch (IOException e) {
			System.out.println("disconnected...");
		}

	}

	public void function_check_winner() {
		if (judge_times_of_card4 % 2 == 1) {
			int i = 0;
			if ((win_or_lose.get(i) == "LOSE" & win_or_lose.get(i + 1) == "LOSE")
					|| (win_or_lose.get(i + 1) == "LOSE" & win_or_lose.get(i + 2) == "LOSE")
					|| (win_or_lose.get(i) == "LOSE" & win_or_lose.get(i + 2) == "LOSE")) {
				show_function.setText("WINNER P" + judge_player);
			}
		} else if (judge_times_of_card4 % 2 == 0) {
			int i = 2;
			if ((win_or_lose.get(i) == "LOSE" & win_or_lose.get(i - 1) == "LOSE")
					|| (win_or_lose.get(i - 1) == "LOSE" & win_or_lose.get(i - 2) == "LOSE")
					|| (win_or_lose.get(i) == "LOSE" & win_or_lose.get(i - 2) == "LOSE")) {
				show_function.setText("WINNER P" + judge_player);
			}
		}

	}

	/**
	 * 
	 */
	public void function_player_lose_and_skip() {

		if (judge_times_of_card4 % 2 == 1) {
			int a = judge_player - 1;
			if (a + 1 == 3) {
				a = -1;
			}
			if (win_or_lose.get(a + 1) == "LOSE") {
				System.out.println(win_or_lose);
				System.out.println(judge_player);
				judge_player++;

			}
		} else if (judge_times_of_card4 % 2 == 0) {
			int b = judge_player - 1;
			if (b - 1 == -1) {
				b = 3;
			}
			if (win_or_lose.get(b - 1) == "LOSE") {
				System.out.println(win_or_lose);
				System.out.println(judge_player);
				judge_player--;

			}
		}

	}

	// 看換誰
	public void function_show_player() {
		function_player_lose_and_skip();
		function_check_card5();
		if (judge_player == 4) {
			judge_player = 1;
		} else if (judge_player == 5) {
			judge_player = 2;
		} else if (judge_player == 0) {
			judge_player = 3;
		} else if (judge_player == -1) {
			judge_player = 2;
		}
		System.out.println(judge_player);
		System.out.println(win_or_lose);
		function_check_winner();
		show_player.setText("換Player" + judge_player);

	}

	// 看有沒有用過5
	public void function_check_card5() {
		if (judge_whether_use_card5 == 0) {// 沒有用指定
			function_check_card4();
		} else if (judge_whether_use_card5 % 2 == 1) {// 有用指定
			judge_whether_use_card5--;
			judge_player = judge_player_when_use5;

		}
	}

	// 看有沒有用過4
	public void function_check_card4() {
		if (judge_times_of_card4 % 2 == 1) {// 用了偶數次迴轉
			judge_player++;// 換下一個人
		} else if (judge_times_of_card4 % 2 == 0) {// 用了奇數次迴轉
			judge_player--;// 換上一個人
		}

	}

	// 檢查有沒有超過99
	public void function_check() {
		if (Integer.valueOf(show_number.getText()) > 99) {

			show_number.setText("99");
			total_number_str = "99";
			total_number_int = 99;
			show_leftword = "YOU LOSE";
			System.out.println(show_leftword);
			show_function.setText(show_leftword);
			win_or_lose.set(judge_player - 1, "LOSE");

		}
	}

	// 判別出的牌是哪一張
	public void function_A() {
		total_number_int = Integer.valueOf(total_number_str); // 把顯示的數字轉成整數做計算
		total_number_int += 1;
		show_function.setText("+1");
		total_number_str = String.valueOf(total_number_int);// 再把數字轉成字串
		show_number.setText(total_number_str);// 印在顯示器上
		show_leftword = "+1";
		function_check();
		function_show_player();

	}

	public void function_2() {
		total_number_int = Integer.valueOf(total_number_str); // 把顯示的數字轉成整數做計算
		total_number_int += 2;
		show_function.setText("+2");
		total_number_str = String.valueOf(total_number_int);// 再把數字轉成字串
		show_number.setText(total_number_str);// 印在顯示器上
		show_leftword = "+2";
		function_check();
		function_show_player();

	}

	public void function_3() {
		total_number_int = Integer.valueOf(total_number_str); // 把顯示的數字轉成整數做計算
		total_number_int += 3;
		show_function.setText("+3");
		total_number_str = String.valueOf(total_number_int);// 再把數字轉成字串
		show_number.setText(total_number_str);// 印在顯示器上
		show_leftword = "+3";
		function_check();
		function_show_player();

	}

	public void function_4() {
		show_function.setText("迴轉!");// 印在顯示器上
		judge_times_of_card4++;
		show_leftword = "迴轉!";
		function_check();
		function_show_player();

	}

	public void function_5() {
		show_function.setText("指定");
		judge_whether_use_card5++;
		show_leftword = "指定";
		function_check();

	}

	public void function_6() {
		total_number_int = Integer.valueOf(total_number_str); // 把顯示的數字轉成整數做計算
		total_number_int += 6;
		show_function.setText("+6");
		total_number_str = String.valueOf(total_number_int);// 再把數字轉成字串
		show_number.setText(total_number_str);// 印在顯示器上
		show_leftword = "+6";
		function_check();
		function_show_player();
	}

	public void function_7() {
		total_number_int = Integer.valueOf(total_number_str); // 把顯示的數字轉成整數做計算
		total_number_int += 7;
		show_function.setText("+7");
		total_number_str = String.valueOf(total_number_int);// 再把數字轉成字串
		show_number.setText(total_number_str);// 印在顯示器上
		show_leftword = "+7";
		function_check();
		function_show_player();
	}

	public void function_8() {
		total_number_int = Integer.valueOf(total_number_str); // 把顯示的數字轉成整數做計算
		total_number_int += 8;
		show_function.setText("+8");
		total_number_str = String.valueOf(total_number_int);// 再把數字轉成字串
		show_number.setText(total_number_str);// 印在顯示器上
		show_leftword = "+8";
		function_check();
		function_show_player();
	}

	public void function_9() {
		total_number_int = Integer.valueOf(total_number_str); // 把顯示的數字轉成整數做計算
		total_number_int += 9;
		show_function.setText("+9");
		total_number_str = String.valueOf(total_number_int);// 再把數字轉成字串
		show_number.setText(total_number_str);// 印在顯示器上
		show_leftword = "+9";
		function_check();
		function_show_player();
	}

	public void function_10() {
		judge_ten_or_twenty = 10;
		show_function.setText("+-10");
		show_leftword = "+-10";

	}

	public void function_J() {
		show_function.setText("PASS");
		show_leftword = "Pass";
		function_check();
		function_show_player();
	}

	public void function_Q() {
		judge_ten_or_twenty = 20;
		show_function.setText("+-20");
		show_leftword = "+-20";

	}

	public void function_K() {
		total_number_int = Integer.valueOf(total_number_str); // 把顯示的數字轉成整數做計算
		total_number_int = 99;
		show_function.setText("玖拾玖");
		total_number_str = String.valueOf(total_number_int);// 再把數字轉成字串
		show_number.setText(total_number_str);// 印在顯示器上
		show_leftword = "玖拾玖";
		function_check();
		function_show_player();

	}

	// 判別用加十還是減十
	public void function_positive() {
		judge_pos_neg = 1;
		total_number_int = Integer.valueOf(total_number_str); // 把顯示的數字轉成整數做計算
		total_number_int += judge_ten_or_twenty * judge_pos_neg;
		total_number_str = String.valueOf(total_number_int);// 再把數字轉成字串
		show_number.setText(total_number_str);// 印在顯示器上
		show_leftword = "+" + judge_ten_or_twenty;
		function_check();
		function_show_player();
	}

	public void function_negetive() {
		judge_pos_neg = -1;
		total_number_int = Integer.valueOf(total_number_str); // 把顯示的數字轉成整數做計算
		total_number_int += judge_ten_or_twenty * judge_pos_neg;
		total_number_str = String.valueOf(total_number_int);// 再把數字轉成字串
		show_number.setText(total_number_str);// 印在顯示器上
		show_leftword = "-" + judge_ten_or_twenty;
		function_check();
		function_show_player();
	}

	// 判斷出的牌是什麼
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("1")) {
			function_A();
		} else if (e.getActionCommand().equals("2")) {
			function_2();
		} else if (e.getActionCommand().equals("3")) {
			function_3();
		} else if (e.getActionCommand().equals("4")) {
			function_4();
		} else if (e.getActionCommand().equals("5")) {
			function_5();
		} else if (e.getActionCommand().equals("6")) {
			function_6();
		} else if (e.getActionCommand().equals("7")) {
			function_7();
		} else if (e.getActionCommand().equals("8")) {
			function_8();
		} else if (e.getActionCommand().equals("9")) {
			function_9();
		} else if (e.getActionCommand().equals("10")) {
			function_10();
		} else if (e.getActionCommand().equals("11")) {
			function_J();
		} else if (e.getActionCommand().equals("12")) {
			function_Q();
		} else if (e.getActionCommand().equals("13")) {
			function_K();
		} else if (e.getActionCommand().equals("+")) {
			function_positive();
		} else if (e.getActionCommand().equals("-")) {
			function_negetive();
		}

		// implement button listener
	}

	class Chat implements Runnable {
		private Socket socket;
		Broadcast broadcast;

		public Chat(Socket socket, Broadcast broadcast) {
			this.socket = socket;
			this.broadcast = broadcast;
		}// ip位址

		public void run() {
			try {

				BufferedReader clientInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//					System.out.println("wtf");
				while (true) {
					String clientText = clientInput.readLine();
//					System.out.println("From Client: " + clientText);
//					System.out.print(clientText);
//					
					if (clientText.equals("1")) {
						function_A();
					} else if (clientText.equals("2")) {
						function_2();
					} else if (clientText.equals("3")) {

						function_3();
					} else if (clientText.equals("4")) {

						function_4();
					} else if (clientText.equals("5")) {

						function_5();
					} else if (clientText.equals("6")) {
						function_6();
					} else if (clientText.equals("7")) {
						function_7();
					} else if (clientText.equals("8")) {
						function_8();
					} else if (clientText.equals("9")) {
						function_9();
					} else if (clientText.equals("10")) {
						function_10();
					} else if (clientText.equals("11")) {
						function_J();
					} else if (clientText.equals("12")) {
						function_Q();
					} else if (clientText.equals("13")) {
						function_K();
					} else if (clientText.equals("+")) {
						function_positive();
					} else if (clientText.equals("-")) {
						function_negetive();
					} else if (clientText.equals("P1")) {
						judge_player_when_use5 = 1;
						function_show_player();
					} else if (clientText.equals("P2")) {
						judge_player_when_use5 = 2;
						function_show_player();
					} else if (clientText.equals("P3")) {
						judge_player_when_use5 = 3;
						function_show_player();
					}

					// 出牌後要抽牌
					if ((clientText.equals(bt1))) {
						bt1.setText(String.valueOf(cardbase.get(counts_cards)));
						counts_cards++;

					} else if ((clientText.equals(bt2))) {
						bt2.setText(String.valueOf(cardbase.get(counts_cards)));
						counts_cards++;

					} else if ((clientText.equals(bt3))) {
						bt3.setText(String.valueOf(cardbase.get(counts_cards)));
						counts_cards++;

					} else if ((clientText.equals(bt4))) {
						bt4.setText(String.valueOf(cardbase.get(counts_cards)));
						counts_cards++;

					} else if ((clientText.equals(bt5))) {
						bt5.setText(String.valueOf(cardbase.get(counts_cards)));
						counts_cards++;
					}
					String a = String.format("%02d", total_number_int);
					broadcast.sendAllMsg(a, judge_player + "", show_leftword);
					System.out.print(show_leftword);

					// broadcast.sendAllMsg2(judge_player+"");
					System.out.println("From Client: " + clientText);
					System.out.print(clientText);
					// implement button listener
				}
//				String clientText = clientInput.readLine();

				//
//					if (clientText.equals("bye"))
//						break;
//				}

//					System.out.println("wtf2");
//				clientInput.close();
//					socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void sendAllMsg(String msg, String msg2, String msg3) {
		for (int i = 0; i < clientDatas.size(); i++) {
			try {
				PrintStream writer = new PrintStream(clientDatas.get(i).getOutputStream(), true);
				writer.println(msg);
				writer.println(msg2);
				writer.println(msg3);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
