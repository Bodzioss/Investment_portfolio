import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class AddShareWin extends JFrame implements ActionListener {
    List<Share> shares;
    int rows;
    MainWin win;
    JPanel top,bottom;
    JLabel lID,lAmount,lPrice,lLink;
    JTextField tID,tAmount,tPrice,tLink;
    JButton addShare;
    public AddShareWin(List<Share> shares,MainWin win,int rows)
    {
        this.rows=rows;
        this.shares=shares;
        this.win = win;
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700,140);
        setLocationRelativeTo(null);
        setTitle("Dodawanie akcji");
        setVisible(true);

        top=new JPanel(new GridLayout(1,5,20,10));
        top.setBounds(10,25,660,25);
        add(top);

        lID=new JLabel("ID",SwingConstants.CENTER);
        lID.setSize(50,50);
        top.add(lID);

        lAmount=new JLabel("Amount",SwingConstants.CENTER);
        top.add(lAmount);

        lPrice=new JLabel("Price",SwingConstants.CENTER);
        top.add(lPrice);

        lLink=new JLabel("Link",SwingConstants.CENTER);
        top.add(lLink);

        JLabel nothing=new JLabel("",SwingConstants.CENTER);
        top.add(nothing);

        bottom=new JPanel(new GridLayout(1,5,20,10));
        bottom.setBounds(10,50,660,25);
        add(bottom);

        tID=new JTextField();
        tID.setSize(50,50);
        bottom.add(tID);

        tAmount=new JTextField();
        bottom.add(tAmount);

        tPrice=new JTextField();
        bottom.add(tPrice);

        tLink=new JTextField();
        bottom.add(tLink);

        addShare=new JButton("Dodaj akcję");
        addShare.addActionListener(this);
        bottom.add(addShare);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(tID.getText().equals("") || tAmount.getText().equals("") || tPrice.getText().equals("") || tLink.getText().equals(""))
        {
            JOptionPane.showMessageDialog(this,"Uzupełnij wszystkie pola");
        }
        else {
            try {
                Share.addShare(shares, tID.getText(), Integer.parseInt(tAmount.getText()), Double.parseDouble(tPrice.getText()), tLink.getText());
                win.addJ(rows++, shares, shares.size() - 1);

                try {
                    Sys.save(shares);
                } catch (IOException ioe) {
                    // nothing to see here
                }
                setVisible(false);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Złe dane wejściowe dla nowej akcji");
            }
        }

    }
}
