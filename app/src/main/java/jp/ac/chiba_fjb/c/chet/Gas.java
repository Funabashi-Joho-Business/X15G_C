package jp.ac.chiba_fjb.c.chet;

//送信するデータ
class SendData{
    public String userid;
    public String username;
    public int positionN;
    public int positionE;
    public int pinN;
    public int pinE;
    public String imageurl;
    public String parentid;
    public String chettext;
}
//受信するデータ
class RecvData{
    public String userid;
    public String username;
    public int positionN;
    public int positionE;
    public int pinN;
    public int pinE;
    public String imageurl;
    public String parentid;
    public String chettext;
}


//GASと通信を行う
public class Gas {

    public void main(String user,String name,int positionN,int positione,int pinn,int pine,
            String image,String parent,String chet) {
        System.out.println("データ送信開始");

        //GASのアドレス
        final String GAS_ADR = "GASのアドレス";

        //通信はスレッドで行う必要がある
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
        //送信データ作成
        SendData data = new SendData();
        data.userid ="15g000";
        data.username ="船橋太郎";
        data.positionN =0;
        data.positionE =111;
        data.pinN =222;
        data.pinE =333;
        data.imageurl ="aaa";
        data.parentid ="sss";
        data.chettext ="ddd";

        RecvData recvData = Json.send(GAS_ADR, data, RecvData.class);
        if(recvData ==null)
                System.out.println("受信エラー");
        else
            System.out.println("正常終了");
    }
}

