package chatting_Application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.net.*;
   import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Client  implements ActionListener {
   static JPanel a1;
    JTextField text;
       static JFrame j = new JFrame();
    static Box vertical=Box.createVerticalBox();
    static DataOutputStream dataOutput;
    Client() {
        j.setLayout(null);
        JPanel p = new JPanel();
        p.setBackground(Color.darkGray);
        p.setBounds(0, 0, 450, 70);
         j.add(p);
        p.setLayout(null);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("assets/backward_icon.png"));
        Image i2 = i1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel back = new JLabel(i3);
        back.setBounds(5, 20, 25, 25);
        p.add(back);
        back.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent ae) {
                System.exit(0);
            }
        });
        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("assets/orangeGhost.png"));
        Image i5 = i4.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        ImageIcon i6 = new ImageIcon(i5);
        JLabel profile = new JLabel(i6);
        profile.setBounds(40, 10, 50, 50);
        p.add(profile);

        JLabel name = new JLabel("POOKIE GHOST");
        name.setBounds(110, 15, 100, 18);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SAN SERIF", Font.BOLD, 18));
        p.add(name);

        JLabel status = new JLabel("Active Now");
        status.setBounds(110, 40, 100, 18);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("SAN_SERIF", Font.BOLD, 13));
        p.add(status);

        a1 = new JPanel();
        a1.setBounds(5, 75, 440, 570);
         j.add(a1);

        text = new JTextField();
        text.setBounds(5, 655, 310, 40);
        text.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
         j.add(text);

        JButton send = new JButton("Send"); // Creates a button with text "Send"

        send.setBounds(320, 655, 123, 40); // Sets position (x=2320, y=655) and size (width=123, height=40)
        send.setFont(new Font("SAN_SERIF", Font.PLAIN,16));
        send.setBackground(new Color(7, 94, 84)); // Sets background color (custom dark green)
        send.setForeground(Color.white); // Sets text color to white
        send.addActionListener(this);
         j.add(send); // Adds button to the container (like JFrame or JPanel)

         j.setSize(450, 700);
         j.setLocation(800, 50);
         j.setUndecorated(true);
         j.getContentPane().setBackground(Color.WHITE);
         j.setVisible(true);

    }



    public void actionPerformed(ActionEvent ae) {
        try {
    String output=text.getText();

    System.out.println(output);

  JPanel p2=formateLabel(output);
    // p2.add(out);
    
    a1.setLayout(new BorderLayout());
    text.setText("");

    JPanel right=new JPanel(new BorderLayout());
    right.add(p2,BorderLayout.LINE_END);  // allign message right side 
    vertical.add(right);// allign multiple messages vertically 
    vertical.add(Box.createVerticalStrut(15));
    a1.add(vertical,BorderLayout.PAGE_START);

    dataOutput.writeUTF(output);
    // saveMessage("Client", output);
    j.validate();
    j.repaint();
    j.invalidate();
        }
        catch(Exception e){
            e.printStackTrace();

        }
    }
    public static JPanel formateLabel(String output){
        JPanel panel =new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));


        JLabel out=new JLabel("<html><p style=\"width:150px\">"+output+"</p></html>");
        out.setFont(new Font("Tahoma",Font.PLAIN,16));
        out.setBackground(new Color(37,211,102));
        out.setOpaque(true);
        out.setBorder(new EmptyBorder(15,15,15,15));
        panel.add(out);


        Calendar cal=Calendar.getInstance();
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
        JLabel time =new JLabel();
        time.setText(sdf.format(cal.getTime()));
        panel.add(time);

        return panel;
    }

public static void saveMessage(String sender, String message) {
    try {
        BufferedWriter writer = new BufferedWriter(
                new FileWriter("chat_history.txt", true)); // true = append mode
        
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String time = sdf.format(Calendar.getInstance().getTime());

        writer.write("[" + time + "] " + sender + ": " + message);
        writer.newLine();
        writer.close();

    } catch (IOException e) {
        e.printStackTrace();
    }
}
    public static void main(String[] args) {
        new Client();
        try {
            Socket s =new Socket("127.0.0.1",6001);
            DataInputStream dataInput=new DataInputStream(s.getInputStream());
            dataOutput=new DataOutputStream(s.getOutputStream());
            
               while (true) {
                a1.setLayout(new BorderLayout());
                    String msg = dataInput.readUTF();
                    // saveMessage("Server", msg);
                    JPanel panel = formateLabel(msg);
                    JPanel left = new JPanel(new BorderLayout());
                    left.add(panel, BorderLayout.LINE_START);
                    vertical.add(left);
                    vertical.add(Box.createVerticalStrut(15));
                    a1.add(vertical,BorderLayout.LINE_START);
                    j.validate();
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

