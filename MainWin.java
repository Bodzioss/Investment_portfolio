import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.sleep;


public class MainWin extends JFrame implements ActionListener {
        List<JLabel> labels=new ArrayList<>();
        List<Share> shares;
        JPanel cat,cont,sum;
        JLabel title,lDate,jSumValue,jSumTotalValue,jSumProfit,jPercent;
        JButton addShare,getData,sell,buyMore,showHistory;
        Border border =BorderFactory.createLineBorder(Color.BLACK);
        final int FRAME_WIDTH=1100;
        final int FRAME_HEIGHT=550;
        private int rows=1;
        private double value,totalV,totalP,sPercent,sProfit,sTotalValue,sTotalProfit;
        private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

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
            setResizable(false); 



            /*Title*/
            title=new JLabel("Rachunek maklerski",SwingConstants.CENTER);
            title.setBounds(FRAME_WIDTH/2-100,0,200,50);
            add(title);

            /*Date label*/
            lDate=new JLabel("Date",SwingConstants.CENTER);
            lDate.setBounds(FRAME_WIDTH-185,0,150,50);
            add(lDate);

            /*Updating date*/
            updateDate();

            /*Creating Panels*/
            createNamePanel();
            createDataPanel();
            createSumPanel(shares);
            createButtonsPanel();
            updateShares(shares);
            getDataButton();
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setVisible(true);

        }

    private void updateShares(List<Share> shares) {
        //Showing existing shares//
        for(int i=0;i<shares.size();i++)
        {
            addJ(rows++,shares,i);
        }
    }

    private void createNamePanel() {
        /*Categories Panel*/
        cat=new JPanel(new GridLayout(1,7));
        cat.setBounds(50,50,FRAME_WIDTH-100,30);
        cat.setBackground(Color.lightGray);
        cat.setBorder(border);
        add(cat);
        cat.add(new JLabel("ID",SwingConstants.CENTER));
        cat.add(new JLabel("Ilość",SwingConstants.CENTER));
        cat.add(new JLabel("Cena kupna [zł]",SwingConstants.CENTER));
        cat.add(new JLabel("Cena całkowita [zł]",SwingConstants.CENTER));
        cat.add(new JLabel("Wartość [zł]",SwingConstants.CENTER));
        cat.add(new JLabel("Wartość całkowita [zł]",SwingConstants.CENTER));
        cat.add(new JLabel("Zysk [zł]",SwingConstants.CENTER));
        cat.add(new JLabel("Procent zysku",SwingConstants.CENTER));
    }

    private void createDataPanel() {
        cont=new JPanel(new GridLayout(rows,7));
        cont.setBounds(50,80,FRAME_WIDTH-100,300);
        cont.setBackground(Color.lightGray);
        cont.setBorder(border);
        add(cont);
    }

    private void createSumPanel(List<Share> shares) {
        sum=new JPanel(new GridLayout(rows,8));
        sum.setBounds(50,380,FRAME_WIDTH-100,30);
        sum.setBackground(Color.lightGray);
        sum.setBorder(border);
        add(sum);
        sum.add(new JLabel("",SwingConstants.CENTER));
        sum.add(new JLabel(String.valueOf(Share.sumAmount(shares)),SwingConstants.CENTER));
        sum.add(new JLabel("",SwingConstants.CENTER));
        sum.add(new JLabel(String.valueOf(Sys.myRound(Share.sumTotalPrice(shares))),SwingConstants.CENTER));
        jSumValue = new JLabel("",SwingConstants.CENTER);
        sum.add(jSumValue);
        jSumTotalValue= new JLabel("",SwingConstants.CENTER);
        sum.add(jSumTotalValue);
        jSumProfit = new JLabel("",SwingConstants.CENTER);
        sum.add(jSumProfit);
        jPercent = new JLabel("",SwingConstants.CENTER);
        sum.add(jPercent);
    }

    private void createButtonsPanel() {
        addShare=new JButton("Dodaj akcję");
        addShare.addActionListener(this);
        addShare.setBounds(FRAME_WIDTH/2-425,430,150,50);
        add(addShare);

        getData=new JButton("Pobierz dane");
        getData.addActionListener(this);
        getData.setBounds(FRAME_WIDTH/2-75,430,150,50);
        add(getData);

        sell=new JButton("Sprzedaj akcję");
        sell.addActionListener(this);
        sell.setBounds(FRAME_WIDTH/2+125,430,150,50);
        add(sell);

        buyMore=new JButton("Dokup akcję");
        buyMore.addActionListener(this);
        buyMore.setBounds(FRAME_WIDTH/2-275,430,150,50);
        add(buyMore);

        showHistory=new JButton("Pokaż historię");
        showHistory.addActionListener(this);
        showHistory.setBounds(FRAME_WIDTH/2+275,430,150,50);
        add(showHistory);
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
                getDataButton();
            }
            /*Showing history button action*/
            else if(source==buyMore)
            {
                new typeID(shares,this,false);

            }
            else if(source==sell)
            {
                new typeID(shares,this,true);
            }
            else if(source==showHistory)
            {
                new HistoryWin(shares);
                setVisible(false);
            }
        }

    private void getDataButton() {
        sTotalValue=0;
        sProfit=0;
        sPercent=0;
        sTotalProfit=0;
        ExecutorService executorService = Executors.newFixedThreadPool(shares.size());
        Date date = new Date();

        lDate.setText(formatter.format(date));
        String sDate=formatter.format(date);
        sDate=sDate.substring(11,13);
        int iDate=Integer.parseInt(sDate);

        try {
            if (Sys.isNew() && iDate > 16)
            {
                for(int i=0;i<shares.size();i++) {
                    int finalI = i;
                    executorService.submit(() -> {
                        try {
                            getData(shares, finalI);
                            Sys.saveHistory(shares,finalI,String.valueOf(Sys.myRound(value)), String.valueOf(Sys.myRound(totalV)), String.valueOf(Sys.myRound(totalV - totalP)), String.valueOf(java.time.LocalDate.now()));
                        }
                        catch (IOException ex){
                            ex.printStackTrace();
                        }
                    });
                }
            }
            else
            {
                for(int i=0;i<shares.size();i++) {
                    int finalI = i;
                    executorService.submit(() -> getData(shares, finalI));
                }
            }
        }catch(IOException ioe)
        {
            //Nothing to show here
        }
    }

    //Adding new share labels into content panel//
        public void addJ(int rows,List<Share> shares,int size)
        {
            labels.add(new JLabel(shares.get(size).name,SwingConstants.CENTER));
            cont.add(labels.get(labels.size()-1));
            labels.add(new JLabel(String.valueOf(shares.get(size).amount),SwingConstants.CENTER));
            cont.add(labels.get(labels.size()-1));
            labels.add(new JLabel(String.valueOf(Sys.myRound(shares.get(size).price)),SwingConstants.CENTER));
            cont.add(labels.get(labels.size()-1));
            labels.add(new JLabel(String.valueOf(Sys.myRound(Share.TotalPrice(shares.get(size).amount,shares.get(size).price))),SwingConstants.CENTER));
            cont.add(labels.get(labels.size()-1));

            labels.add(new JLabel("",SwingConstants.CENTER));
            cont.add(labels.get(labels.size()-1));
            labels.add(new JLabel("",SwingConstants.CENTER));
            cont.add(labels.get(labels.size()-1));
            labels.add(new JLabel("",SwingConstants.CENTER));
            cont.add(labels.get(labels.size()-1));
            labels.add(new JLabel("",SwingConstants.CENTER));
            cont.add(labels.get(labels.size()-1));

            cont.setLayout(new GridLayout(rows,8));
            revalidate();
        }



        /*Thread's getting data method
        * proper method providing data on Url class*/
        private void getData(List<Share> shares,int i){
            try {
                value = Url.getPrice(shares.get(i).link);

                /*MalformedURLException*/
                if (value == -2) {
                    JOptionPane.showMessageDialog(this, "Dla akcji " + shares.get(i).name + " podano zły link \r\nEdytuj lub usuń tę akcję");
                    return;
                }
                labels.get(i * 8 + 4).setText(String.valueOf(Sys.myRound(value)));

                totalV = Share.TotalPrice(Integer.parseInt(labels.get(i * 8 + 1).getText()), value);
                labels.get(i * 8 + 5).setText(String.valueOf(Sys.myRound(totalV)));
                sTotalValue += totalV;

                totalP = Double.parseDouble(labels.get(i * 8 + 3).getText());
                labels.get(i * 8 + 6).setText(String.valueOf(Sys.myRound(totalV - totalP)));


                sProfit = totalV - totalP;
                sTotalProfit += sProfit;
                sPercent=Sys.myRound(sProfit/Double.parseDouble(labels.get(i * 8 + 3).getText())*100);
                labels.get(i * 8 + 7).setText(sPercent+"%");


                jSumTotalValue.setText(String.valueOf(Sys.myRound(sTotalValue)));
                jSumProfit.setText(String.valueOf(Sys.myRound(sTotalProfit)));

                jPercent.setText(Sys.myRound(Double.parseDouble(jSumProfit.getText())/Share.sumTotalPrice(shares)*100)+"%");
            }catch (IOException ioe)
            {
                JOptionPane.showMessageDialog(this,"Błąd połączenia z serwerem \n Spróbuj ponownie później");
            }
        }

    private void updateDate()
    {
        Thread tDate = new Thread(() -> {
            while (true)
            {
                try {
                    Date date = new Date();
                    lDate.setText(formatter.format(date));
                    revalidate();
                    sleep(1000);
                } catch (InterruptedException iex)
                {
                    iex.printStackTrace();
                }
            }
        });
        tDate.start();
    }
}