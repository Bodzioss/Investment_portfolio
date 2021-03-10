import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class AddShareWin extends JFrame implements ActionListener {
    List<Share> shares;
    MainWin win;
    JPanel top,bottom;
    JLabel lID,lAmount,lPrice,lLink;
    JTextField tID,tAmount,tPrice,tLink;
    JButton addShare,addMoreShare,sellShare;
    int rows,index;

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

    public AddShareWin(List<Share> shares,int index,boolean sell)
    {
        this.index=index;
        this.shares=shares;
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700,140);
        setLocationRelativeTo(null);
        if(!sell) {
            setTitle("Dodawanie akcji " + shares.get(index).name);
            setVisible(true);

            top = new JPanel(new GridLayout(1, 5, 20, 10));
            top.setBounds(10, 25, 660, 25);
            add(top);


            lAmount = new JLabel("Amount", SwingConstants.CENTER);
            top.add(lAmount);

            lPrice = new JLabel("Price", SwingConstants.CENTER);
            top.add(lPrice);

            JLabel nothing = new JLabel("", SwingConstants.CENTER);
            top.add(nothing);

            bottom = new JPanel(new GridLayout(1, 5, 20, 10));
            bottom.setBounds(10, 50, 660, 25);
            add(bottom);


            tAmount = new JTextField();
            bottom.add(tAmount);

            tPrice = new JTextField();
            bottom.add(tPrice);


            addMoreShare = new JButton("Dokup akcję");
            addMoreShare.addActionListener(this);
            bottom.add(addMoreShare);
        }else{
            setTitle("Sprzedawanie akcji " + shares.get(index).name);
            setVisible(true);

            top = new JPanel(new GridLayout(1, 5, 20, 10));
            top.setBounds(10, 25, 660, 25);
            add(top);


            lAmount = new JLabel("Amount", SwingConstants.CENTER);
            top.add(lAmount);

            lPrice = new JLabel("Price", SwingConstants.CENTER);
            top.add(lPrice);

            JLabel nothing = new JLabel("", SwingConstants.CENTER);
            top.add(nothing);

            bottom = new JPanel(new GridLayout(1, 5, 20, 10));
            bottom.setBounds(10, 50, 660, 25);
            add(bottom);


            tAmount = new JTextField();
            bottom.add(tAmount);

            tPrice = new JTextField();
            bottom.add(tPrice);


            sellShare = new JButton("Sprzedaj akcje");
            sellShare.addActionListener(this);
            bottom.add(sellShare);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == addShare) {
            if (tID.getText().equals("") || tAmount.getText().equals("") || tPrice.getText().equals("") || tLink.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Uzupełnij wszystkie pola");
            } else {
                try {
                    shares.add(new Share(tID.getText(), Integer.parseInt(tAmount.getText()), Double.parseDouble(tPrice.getText()), tLink.getText(),String.valueOf(java.time.LocalDate.now())));
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
        }else if(source == addMoreShare)
        {
            int a;
            System.out.println(shares.get(index).name);
            a=shares.get(index).amount;
            shares.get(index).amount=shares.get(index).amount+ Integer.parseInt(tAmount.getText());
            shares.get(index).price=((shares.get(index).price*a)+(Double.parseDouble(tAmount.getText())*Double.parseDouble(tPrice.getText())))/shares.get(index).amount;
            try {
                Sys.save(shares);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            new MainWin(shares);
            setVisible(false);
        }
        else if(source == sellShare)
        {
            if(shares.get(index).amount>Integer.parseInt(tAmount.getText())) {
                shares.get(index).amount = shares.get(index).amount - Integer.parseInt(tAmount.getText());
                try{
                    Sys.saveSold(shares,index,String.valueOf(Integer.parseInt(tAmount.getText())),tPrice.getText(),String.valueOf(java.time.LocalDate.now()));
                }catch (IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }else if(shares.get(index).amount==Integer.parseInt(tAmount.getText()))
            {
                try{
                    Sys.saveSold(shares,index,String.valueOf(shares.get(index).amount),tPrice.getText(),String.valueOf(java.time.LocalDate.now()));
                }catch (IOException ioe)
                {
                    ioe.printStackTrace();
                }
                shares.remove(index);
            }
            else{
                JOptionPane.showMessageDialog(this, "Możesz sprzedać maksymalnie ("+shares.get(index).amount+") akcji "+shares.get(index).name);
            }
            try {
                Sys.save(shares);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            new MainWin(shares);
            setVisible(false);
        }
    }
}