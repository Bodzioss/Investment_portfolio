import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Sys {

    public static void main(String[] args) {
        List<Share> shares=new ArrayList<>();

        try {
            shares=read();
        }
        catch (FileNotFoundException fine)
        {
            //Nothing to show here
        }
        new MainWin(shares);
    }

    //Reading shares from txt file//
    public static List<Share> read() throws FileNotFoundException {
        List<Share> shares=new ArrayList<>();
        Scanner readB = new Scanner(new File("file.txt"));

        while(readB.hasNext()) {
            String id = readB.next();
            int amount = Integer.parseInt(readB.next());
            double price = Double.parseDouble(readB.next());
            String link = readB.next();
            shares.add(new Share(id, amount, price, link));
        }

        return shares;
    }

    //Saving shares into txt file//
    public static void save(List<Share> shares) throws IOException {

        for (Share share : shares) {
            FileWriter file = new FileWriter("file.txt", true);
            BufferedWriter out = new BufferedWriter(file);
            out.write(share.name + " " + share.amount + " " + share.price + " " + share.link);
            out.newLine();
            out.close();
        }
    }
}
