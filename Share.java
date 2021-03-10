import java.util.List;

public class Share {
    String name,link,date,date1,date2;
    double price,value;
    int amount;

    Share(String name,int amount,double price,String link,String date)
    {
        this.name=name;
        this.price=price;
        this.amount=amount;
        this.link=link;
        this.date=date;
    }
    Share(String name,int amount,double price,double value,String date1,String date2)
    {
        this.name=name;
        this.price=price;
        this.amount=amount;
        this.value=value;
        this.date1=date1;
        this.date2=date2;
    }

    public static double TotalPrice(int amount,double price)
    {
        return price*amount;
    }

    public static Double sumTotalPrice(List<Share> shares) {
        double sum=0;
        for (Share share : shares) {
            sum += share.price * share.amount;
        }
        return sum;
    }

    public static int sumAmount(List<Share> shares) {
        int sum=0;
        for (Share share : shares) {
            sum += share.amount;
        }
        return sum;
    }
}
