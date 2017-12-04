package jp.ac.chiba_fjb.c.chet.SubModule;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * Created by x15g009 on 2017/11/15.
 */

public class DataStorage {

    //設定値
    private static final String DEFAULT_ENCORDING = "UTF-8";  //デフォルトのエンコード
    private static final int DEFAULT_READ_LENGTH = 8192;      //一度に読み込むバッファサイズ

    //ストリームから読み込み、バイト配列で返す
    public static final byte[] readStream(InputStream inputStream, int readLength) throws IOException {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream(readLength);  //一時バッファのように使う
        final byte[] bytes = new byte[readLength];    //read() 毎に読み込むバッファ
        final BufferedInputStream bis = new BufferedInputStream(inputStream, readLength);

        try {
            int len = 0;
            while ((len = bis.read(bytes, 0, readLength)) > 0) {
                byteStream.write(bytes, 0, len);    //ストリームバッファに溜め込む
            }
            return byteStream.toByteArray();    //byte[] に変換

        } finally {
            try {
                byteStream.reset();     //すべてのデータを破棄
                bis.close();            //ストリームを閉じる
            } catch (Exception e) {
                //IOException
            }
        }
    }

    //ストリームから読み込み、テキストエンコードして返す
    public static final String loadText(InputStream inputStream, String charsetName)
            throws IOException, UnsupportedEncodingException {
        return new String(readStream(inputStream, DEFAULT_READ_LENGTH), charsetName);
    }


    //ローカルシステムから、テキストファイルを読み込む
    public static final String loadTextLocal(String fileName) throws IOException, FileNotFoundException {
        InputStream is = new FileInputStream(fileName);
        return loadText(is, DEFAULT_ENCORDING);
    }
}
