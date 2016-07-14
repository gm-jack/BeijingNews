package irdc.ex08_11;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Request;
import okhttp3.Response;

/* import相关class */

public class EX08_11 extends Activity {
  /*
   * 变量声明 filename：上传后在服务器上的文件名称 uploadFile：要上传的文件路径 actionUrl：服务器上对应的程序路径
   */

    private String uploadFile = Environment.getExternalStorageDirectory() + "/download/1.png";
    private String srcPath = Environment.getExternalStorageDirectory() + "/download/1.png";
    private String actionUrl = "http://192.168.129.1:8888/upload_file_service/upload.jsp";
    private TextView mText1;
    private TextView mText2;
    private Button mButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mText1 = (TextView) findViewById(R.id.myText2);
        mText1.setText("文件路径：\n" + uploadFile);
        mText2 = (TextView) findViewById(R.id.myText3);
        mText2.setText("上传网址：\n" + actionUrl);
    /* 设置mButton的onClick事件处理 */
        mButton = (Button) findViewById(R.id.myButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Toast.makeText(EX08_11.this, "外部路径:"+ Environment.getExternalStorageDirectory(), Toast.LENGTH_SHORT).show();
//                uploadFile();
                upLoadFile();
            }
        });
    }

    private void upLoadFile() {
        String uploadUrl = "http://192.168.129.1:8888/upload_file_service/UploadServlet";
        OkHttpUtils.postFile().url(uploadUrl).file(new File(srcPath)).build().execute(new Callback() {
            @Override
            public Object parseNetworkResponse(Response response) throws Exception {
                return null;
            }

            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(EX08_11.this, "onError()" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Object response) {
                Toast.makeText(EX08_11.this, "response" + response, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* 上传文件至Server的方法 */
    private void uploadFile() {

        final String uploadUrl = "http://192.168.129.1:8888/upload_file_service/UploadServlet";
        final String end = "\r\n";
        final String twoHyphens = "--";
        final String boundary = "******";
        new Thread() {
            public void run() {
                try {
                    URL url = new URL(uploadUrl);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url
                            .openConnection();
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setUseCaches(false);
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
                    httpURLConnection.setRequestProperty("Charset", "UTF-8");
                    httpURLConnection.setRequestProperty("Content-Type",
                            "multipart/form-data;boundary=" + boundary);

                    DataOutputStream dos = new DataOutputStream(httpURLConnection
                            .getOutputStream());
                    dos.writeBytes(twoHyphens + boundary + end);
                    dos
                            .writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
                                    + srcPath.substring(srcPath.lastIndexOf("/") + 1)
                                    + "\"" + end);
                    dos.writeBytes(end);

                    FileInputStream fis = new FileInputStream(srcPath);
                    byte[] buffer = new byte[fis.available()]; // 8k
                    int count = 0;
                    while ((count = fis.read(buffer)) != -1) {
                        dos.write(buffer, 0, count);

                    }
                    fis.close();

                    dos.writeBytes(end);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
                    dos.flush();

                    InputStream is = httpURLConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is, "utf-8");
                    BufferedReader br = new BufferedReader(isr);
                    String result = br.readLine();

                    Toast.makeText(EX08_11.this, result, Toast.LENGTH_LONG).show();
                    dos.close();
                    is.close();

                } catch (Exception e) {
//                    e.printStackTrace();
                    Log.e("TAG", e.getMessage());
                }
            }
        }.start();
    }
}