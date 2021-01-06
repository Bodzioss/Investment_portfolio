import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainWin extends JFrame implements ActionListener {
        JLabel title;
        JPanel cat,cont,bottom;
        JTextField tID,tPrice,tAmount,tLink;
        JButton addShare,getData;
        int rows=1;
        List<JLabel> labels=new ArrayList<>();
        List<Share> shares;

        public MainWin(List<Share> shares)
        {
            this.shares=shares;

            setLayout(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(1000,600);

            title=new JLabel("Rachunek maklerski",SwingConstants.CENTER);
            title.setBounds(0,0,1000,50);
            add(title);
            cat=new JPanel(new GridLayout(1,7));
            cat.setBounds(50,50,900,30);
            cat.setBackground(Color.lightGray);
            add(cat);

            cat.add(new JLabel("ID",SwingConstants.CENTER));
            cat.add(new JLabel("Ilość",SwingConstants.CENTER));
            cat.add(new JLabel("Cena kupna",SwingConstants.CENTER));
            cat.add(new JLabel("Cena całkowita",SwingConstants.CENTER));
            cat.add(new JLabel("Wartość",SwingConstants.CENTER));
            cat.add(new JLabel("Wartość całkowita",SwingConstants.CENTER));
            cat.add(new JLabel("Zysk",SwingConstants.CENTER));

            cont=new JPanel(new GridLayout(rows,7));
            cont.setBounds(50,80,900,300);
            cont.setBackground(Color.lightGray);
            add(cont);

            bottom=new JPanel(new GridLayout(1,5,20,20));
            bottom.setBounds(50,400,600,50);
            bottom.setBackground(Color.lightGray);
            add(bottom);

            tID=new JTextField();
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

            getData=new JButton("Pobierz dane");
            getData.addActionListener(this);
            getData.setBounds(425,480,150,50);
            add(getData);
            

            //Showing existing shares//
            for(int i=0;i<shares.size();i++)
            {
                addJ(rows++,shares,i);
            }
            Image icon = Toolkit.getDefaultToolkit().getImage("TkyHAZ_3.jpg");
            setIconImage(icon);
            setVisible(true);
        }


        @Override
        public void actionPerformed(ActionEvent e) {
            Object source=e.getSource();

            if(source==addShare) {
                Share.addShare(shares, tID.getText(), Integer.parseInt(tAmount.getText()), Double.parseDouble(tPrice.getText()), tLink.getText());
                addJ(rows++, shares, shares.size() - 1);

                try {
                    Sys.save(shares);
                }
                catch (IOException ioe)
                {
                    // nothing to see here
                }
            }
            else if(source==getData)
            {
                for(int i=0;i<shares.size();i++)
                {
                    double value,totalV,totalP;
                    value=Url.getPrice(shares.get(i).link);
                    if(value==-2)
                    {
                        JOptionPane.showMessageDialog(this,"Dla akcji " + shares.get(i).name + " podano zły link \r\nEdytuj lub usuń tę akcję");
                        break;
                    }
                    labels.get(i*7+4).setText(String.valueOf(value));
                    totalV=Share.TotalPrice(Integer.parseInt(labels.get(i*7+1).getText()),value);
                    totalV*=100;
                    totalV=Math.round(totalV);
                    totalV/=100;
                    labels.get(i*7+5).setText(String.valueOf(totalV));
                    totalP=Double.parseDouble(labels.get(i*7+3).getText());
                    totalP*=100;
                    totalP=Math.round(totalP);
                    totalP/=100;
                    labels.get(i*7+6).setText(String.valueOf(totalV-totalP));
                }
            }
        }

        //Adding new share labels into content panel//
        public void addJ(int rows,List<Share> shares,int size)
        {
            labels.add(new JLabel(shares.get(size).name,SwingConstants.CENTER));
            cont.add(labels.get(labels.size()-1));
            labels.add(new JLabel(String.valueOf(shares.get(size).amount),SwingConstants.CENTER));
            cont.add(labels.get(labels.size()-1));
            labels.add(new JLabel(String.valueOf(shares.get(size).price),SwingConstants.CENTER));
            cont.add(labels.get(labels.size()-1));
            labels.add(new JLabel(String.valueOf(Share.TotalPrice(shares.get(size).amount,shares.get(size).price)),SwingConstants.CENTER));
            cont.add(labels.get(labels.size()-1));


            labels.add(new JLabel("",SwingConstants.CENTER));
            cont.add(labels.get(labels.size()-1));
            labels.add(new JLabel("",SwingConstants.CENTER));
            cont.add(labels.get(labels.size()-1));
            labels.add(new JLabel("",SwingConstants.CENTER));
            cont.add(labels.get(labels.size()-1));

            cont.setLayout(new GridLayout(rows++,7));
            revalidate();
        }


}
