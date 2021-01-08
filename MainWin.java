import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainWin extends JFrame implements ActionListener {
        List<JLabel> labels=new ArrayList<>();
        List<Share> shares;
        JPanel cat,cont,sum;
        JLabel title,jSumValue,jSumTotalValue,jSumProfit;
        JButton addShare,getData,getHistory;
        Border border =BorderFactory.createLineBorder(Color.BLACK);
        final int FRAME_WIDTH=1000;
        final int FRAME_HEIGHT=550;
        int rows=1;
        double sValue=0;
        double sTotalValue=0;
        double sProfit=0;

        public MainWin(List<Share> shares)
        {
            this.shares=shares;
            setTitle("Rachunek maklerski");
            setLayout(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(FRAME_WIDTH,FRAME_HEIGHT);
            setLocationRelativeTo(null);
            Image icon = Toolkit.getDefaultToolkit().getImage("TkyHAZ_3.jpg");
            setIconImage(icon);
            setVisible(true);

            /*Title*/
            title=new JLabel("Rachunek maklerski",SwingConstants.CENTER);
            title.setBounds(0,0,FRAME_WIDTH,50);
            add(title);

            /*Categories Panel*/
            cat=new JPanel(new GridLayout(1,7));
            cat.setBounds(50,50,FRAME_WIDTH-100,30);
            cat.setBackground(Color.lightGray);
            cat.setBorder(border);
            add(cat);
            cat.add(new JLabel("ID",SwingConstants.CENTER));
            cat.add(new JLabel("Ilość",SwingConstants.CENTER));
            cat.add(new JLabel("Cena kupna",SwingConstants.CENTER));
            cat.add(new JLabel("Cena całkowita",SwingConstants.CENTER));
            cat.add(new JLabel("Wartość",SwingConstants.CENTER));
            cat.add(new JLabel("Wartość całkowita",SwingConstants.CENTER));
            cat.add(new JLabel("Zysk",SwingConstants.CENTER));

            /*Data Panel*/
            cont=new JPanel(new GridLayout(rows,7));
            cont.setBounds(50,80,FRAME_WIDTH-100,300);
            cont.setBackground(Color.lightGray);
            cont.setBorder(border);
            add(cont);

            /*Summary Panel*/
            sum=new JPanel(new GridLayout(rows,7));
            sum.setBounds(50,380,FRAME_WIDTH-100,30);
            sum.setBackground(Color.lightGray);
            sum.setBorder(border);
            add(sum);
            sum.add(new JLabel("",SwingConstants.CENTER));
            sum.add(new JLabel(String.valueOf(Share.sumAmount(shares)),SwingConstants.CENTER));
            sum.add(new JLabel("",SwingConstants.CENTER));
            sum.add(new JLabel(String.valueOf(myRound(Share.sumTotalPrice(shares))),SwingConstants.CENTER));
            jSumValue = new JLabel("",SwingConstants.CENTER);
            sum.add(jSumValue);
            jSumTotalValue= new JLabel("",SwingConstants.CENTER);
            sum.add(jSumTotalValue);
            jSumProfit = new JLabel("",SwingConstants.CENTER);
            sum.add(jSumProfit);

            /*Action Buttons*/
            addShare=new JButton("Dodaj akcję");
            addShare.addActionListener(this);
            addShare.setBounds(275,430,150,50);
            add(addShare);

            getData=new JButton("Pobierz dane");
            getData.addActionListener(this);
            getData.setBounds(425,430,150,50);
            add(getData);

            getHistory=new JButton("Historia");
            getHistory.addActionListener(this);
            getHistory.setBounds(575,430,150,50);
            add(getHistory);
            

            //Showing existing shares//
            for(int i=0;i<shares.size();i++)
            {
                addJ(rows++,shares,i);
            }
        }


        @Override
        public void actionPerformed(ActionEvent e) {
            Object source=e.getSource();

            /*Adding share button action*/
            if(source==addShare) {
                new AddShareWin(shares,this,rows++);
            }
            /*Getting data button action*/
            else if(source==getData)
            {
                ExecutorService executorService = Executors.newFixedThreadPool(shares.size());
                long time = System.currentTimeMillis();
                for(int i=0;i<shares.size();i++) {
                    int finalI = i;
                    executorService.submit(() -> getData(shares, finalI));
                }

                time -= System.currentTimeMillis();
                System.out.println(Math.abs(time)/1000);
            }
            /*Showing history button action*/
            else if(source==getHistory)
            {
                //TO DO
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
            labels.add(new JLabel(String.valueOf(myRound(Share.TotalPrice(shares.get(size).amount,shares.get(size).price))),SwingConstants.CENTER));
            cont.add(labels.get(labels.size()-1));

            labels.add(new JLabel("",SwingConstants.CENTER));
            cont.add(labels.get(labels.size()-1));
            labels.add(new JLabel("",SwingConstants.CENTER));
            cont.add(labels.get(labels.size()-1));
            labels.add(new JLabel("",SwingConstants.CENTER));
            cont.add(labels.get(labels.size()-1));

            cont.setLayout(new GridLayout(rows,7));
            revalidate();
        }

        /*Data approximation*/
        public double myRound(double a)
        {
            a*=100;
            a=Math.round(a);
            a/=100;
            return a;
        }

        /*Thread's getting data method
        * proper method providing data on Url class*/
        public void getData(List<Share> shares,int i)
        {


                double value,totalV,totalP;
                value=Url.getPrice(shares.get(i).link);

                /*MalformedURLException*/
                if(value==-2)
                {
                    JOptionPane.showMessageDialog(this,"Dla akcji " + shares.get(i).name + " podano zły link \r\nEdytuj lub usuń tę akcję");
                    return;
                }
                labels.get(i*7+4).setText(String.valueOf(myRound(value)));
                sValue+=value;

                totalV=Share.TotalPrice(Integer.parseInt(labels.get(i*7+1).getText()),value);
                labels.get(i*7+5).setText(String.valueOf(myRound(totalV)));
                sTotalValue+=totalV;

                totalP=Double.parseDouble(labels.get(i*7+3).getText());
                labels.get(i*7+6).setText(String.valueOf(myRound(totalV-totalP)));
                sProfit+=totalV-totalP;

            //jSumValue.setText(String.valueOf(myRound(sValue)));
            jSumTotalValue.setText(String.valueOf(myRound(sTotalValue)));
            jSumProfit.setText(String.valueOf(myRound(sProfit)));
        }
}