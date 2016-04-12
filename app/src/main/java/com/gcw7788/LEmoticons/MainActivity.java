package com.gcw7788.LEmoticons;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.PopupWindow.OnDismissListener;

/**
 * 这是一个聊天表情的添加与解析demo
 * @author leardr
 *
 */
public class MainActivity extends Activity implements OnItemSelectedListener, OnItemClickListener{
	//表情
	private int[] resIds = new int[] { R.drawable.face1, R.drawable.face2,
			R.drawable.face3, R.drawable.face4, R.drawable.face5,
			R.drawable.face6, R.drawable.face7, R.drawable.face8,
			R.drawable.face9, };
	private TextView add,tv,ana;
	private String TAG="LEmoticons";
	private EditText editText;
	private PopupWindow pop;
	private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText=(EditText) findViewById(R.id.editView);
        add = (TextView) findViewById(R.id.add);
        tv=(TextView) findViewById(R.id.tv);
        ana=(TextView) findViewById(R.id.ana);
        LEmoticons();
        ana.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				AnALEmoticons();
			}
		});
    }
    
	/**
	 * 添加表情
	 */
	private void LEmoticons() {
		// 引入窗口配置文件,我这里使用了PopupWindow
		View view = LayoutInflater.from(this).inflate(R.layout.face, null);
		
		//使用网格，排列表情的位置
		GridView gridView = (GridView) view.findViewById(R.id.gridview);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < resIds.length; i++) {
			Map<String, Object> cell = new HashMap<String, Object>();
			cell.put("imageview", resIds[i]);
			list.add(cell);
		}
		//使用cell样式来显示网格中表情显示的样式，并使用simleadapter来配置
		SimpleAdapter simleadapter = new SimpleAdapter(this, list,
				R.layout.cell, new String[] { "imageview" },
				new int[] { R.id.imageview });
		gridView.setAdapter(simleadapter);
		
		//设置监听哪个选项被点了
		gridView.setOnItemClickListener(this);
		gridView.setOnItemSelectedListener(this);
		
		// 显示选中图片(预览窗口）
		// imageView=(ImageView)view.findViewById(R.id.imageview);
		// imageView.setImageResource(resIds[0]);

		// 创建PopupWindow对象
		pop = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, false);

		// 需要设置一下此参数，点击外边可消失
		pop.setBackgroundDrawable(new BitmapDrawable());
		OnDismissListener ondismisslistener = new OnDismissListener() {

			//这个地方是窗口消失后的回调接口
			@Override
			public void onDismiss() {
				
			}
		};
		pop.setOnDismissListener(ondismisslistener);

		// 设置点击窗口外边窗口消失
		pop.setOutsideTouchable(true);
		// 设置此参数获得焦点，否则无法点击
		pop.setFocusable(true);

		// 监听
		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (pop.isShowing()) {
					// 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏
					pop.dismiss();

					Log.i(TAG, "触发隐藏窗口");
				} else {
					// 显示窗口,可以传入多个参数来选择显示的地方，直接传入V表示直接在View下面显示
					Log.i(TAG, "触发显示窗口");
					pop.showAsDropDown(v);
				}
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Log.i(TAG, "点击菜单");
		// 显示被单机的图片
		// imageView.setImageResource(resIds[position]);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				resIds[position]);
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		// 设置想要的大小
		// int newWidth = 20;
		// int newHeight = 30;
		// 计算缩放比例
		// float scaleWidth = ((float) newWidth) / width;
		// float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		// 100原来的倍数
		// matrix.setScale(100f/width, 100f/height);
		matrix.setScale(40f / width, 40f / height);
		// 得到新的图片
		Bitmap newbit = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		ImageSpan imageSpan = new ImageSpan(this, newbit);
		//因为我图片是从1开始的 使用0开始它就报错， 所以这里id要加1
		SpannableString spannableString = new SpannableString("face" + (id+1));
		//0,5的参数是表示你图片需要被替换的字数 0是起始位置，5是位数，
		spannableString.setSpan(imageSpan, 0, 5,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		// editView.append(spannableString);直接添加表情

		// 让表情添加到光标位置
		Editable et = editText.getText();// 先获取Edittext中的内容
		int start = editText.getSelectionStart();
		et.insert(start, spannableString);// 设置ss要添加的位置
		editText.setText(et);// 把et添加到Edittext中
		editText.setSelection(start + spannableString.length());// 设置Edittext中光标在最后面显示
		System.out.println("edit的内容 = " + spannableString);
		// 不为空，则点击后关闭该窗口
		if (pop != null && pop.isShowing()) {
			pop.dismiss();
			str= editText.getText().toString();
			Log.i(TAG, "添加表情后的toString---->"+str);
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		// 显示被选中的图片
		Log.i(TAG, "onItemSelected-->选择菜单");
		// imageView.setImageResource(resIds[position]);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}
	
	 /**
     * 解析表情
     */
	private void AnALEmoticons() {
		String zhengze = "face[0-9]"; // 正则表达
		try {
			Log.d(TAG, "进来的zhengze内容 = " + zhengze);
			SpannableString spannableString = ExpressionUtil
					.getExpressionString(this, str, zhengze);
			Log.d(TAG, "转换后的内容 = " + spannableString);
			tv.setText(spannableString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
