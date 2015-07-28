package com.huiyin.ui.store;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.utils.Code;
import com.huiyin.utils.JSONParseUtils;
import com.huiyin.utils.ResourceUtils;

import org.apache.http.Header;

public class StoreLehuActivity extends Activity {
	public static String ID = "id";
	public static String PRiCE = "price";
	public static String TITLE = "title";
	public static String START = "start";
	public static String END = "end";
	public static String RANGE = "range";
	private TextView ab_back;
	private TextView ab_title;
	private LinearLayout content;
	private TextView text_value;
	private TextView text_tip;
	private View line2;
	private TextView text_title;
	private TextView text_condition;
	private LinearLayout check_ll;
	private EditText edit_check;
	private ImageView image_ma;
	private Button submit;

	private String id;
	private String price;
	private String title;
	private String start;
	private String end;
	private String range;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_lehu);
		ab_back = (TextView) findViewById(R.id.ab_back);
		ab_title = (TextView) findViewById(R.id.ab_title);
		ab_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		ab_title.setText("领取乐虎券");
		bindViews();
        id=getIntent().getStringExtra(ID);
        price=getIntent().getStringExtra(PRiCE);
        title=getIntent().getStringExtra(TITLE);
        start=getIntent().getStringExtra(START);
        end=getIntent().getStringExtra(END);
        range=getIntent().getStringExtra(RANGE);

        init();
        setListener();
	}

    private void setListener() {
        image_ma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_ma.setImageBitmap(Code.getInstance().createBitmap());
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()){
                    getLHTicket(id);
                }else{
                    image_ma.setImageBitmap(Code.getInstance()
                            .createBitmap());
                }
            }
        });
    }

    /**
     * 领取乐虎券
     * @param acitveId
     */
    private void getLHTicket(String acitveId ){

        if(null == acitveId){
            return;
        }

        RequstClient.getLHTicket(acitveId, new CustomResponseHandler(this, true) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String content) {
                super.onSuccess(statusCode, headers, content);

                //异常消息显示
                if (JSONParseUtils.isErrorJSONResult(content)) {
                    String msg = JSONParseUtils.getString(content, "msg");
                    UIHelper.showToast(msg);
                    return;
                }
                UIHelper.showToast("领取成功");
                setResult(RESULT_OK);
                finish();
            }
        });
    }
    private void init() {
        text_value.setText(price);
        text_title.setText(title);
        Spanned mSpannable= Html.fromHtml("有效期<br/>"+start+"-"+end+"<br/>"+ ResourceUtils.changeStringColor("#1CA7F7",range));
        text_condition.setText(mSpannable);
        image_ma.setImageBitmap(Code.getInstance().createBitmap());
    }

    private void bindViews() {
		content = (LinearLayout) findViewById(R.id.content);
		text_value = (TextView) findViewById(R.id.text_value);
		text_tip = (TextView) findViewById(R.id.text_tip);
		line2 = (View) findViewById(R.id.line2);
		text_title = (TextView) findViewById(R.id.text_title);
		text_condition = (TextView) findViewById(R.id.text_condition);
		check_ll = (LinearLayout) findViewById(R.id.check_ll);
		edit_check = (EditText) findViewById(R.id.edit_check);
		image_ma = (ImageView) findViewById(R.id.image_ma);
		submit = (Button) findViewById(R.id.submit);
	}

    private boolean check(){
        if (TextUtils.isEmpty(edit_check.getText().toString())) {
            UIHelper.showToast("请输入验证码");
            return false;
        } else if (!edit_check.getText().toString().toLowerCase()
                .equals(Code.getInstance().getCode().toLowerCase())) {
            UIHelper.showToast("验证码错误!");
            return false;
        }
        return true;
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_store_lehu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		// noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
