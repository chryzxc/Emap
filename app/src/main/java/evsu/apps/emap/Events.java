package evsu.apps.emap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class Events extends AppCompatActivity {
 WebView webView;
 private ImageView backButtonE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        backButtonE = findViewById(R.id.backButtonE);
        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new EvsuWebView());
        String url = "http://design6500.com/news";
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);

        backButtonE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(3, intent);
                finish();
            }
        });
    }



    private class EvsuWebView extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view,String url){
            view.loadUrl(url);
            return true;
        }
    }
}
