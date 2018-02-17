package nadav.tasher.toollib;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class Quick {
	public static ImageView quickImageView(Context cont, Drawable image) {
		ImageView iv=new ImageView(cont);
		iv.setImageDrawable(image);
		return iv;
	}
	public static ScrollView quickScrollView(Context cont, View v) {
		ScrollView sv=new ScrollView(cont);
		sv.addView(v);
		return sv;
	}
	public static Button quickButton(Context cont, String text) {
		Button b=new Button(cont);
		b.setText(text);
		return b;
	}
	public static TextView quickTextView(Context cont, String text) {
		TextView tv=new TextView(cont);
		tv.setGravity(Gravity.CENTER);
		tv.setText(text);
		return tv;
	}
	public static TextView quickTextView(Context cont, String text, int size) {
		TextView tv=new TextView(cont);
		tv.setGravity(Gravity.CENTER);
		tv.setText(text);
		tv.setTextSize((float) size);
		return tv;
	}
	public static ListView quickListView(Context cont, ArrayList<String> adp) {
		ListView q=new ListView(cont);
		ArrayAdapter<String> apt=new ArrayAdapter<>(cont, android.R.layout.simple_list_item_1, android.R.id.text1, adp);
		q.setAdapter(apt);
		return q;
	}
	public static ListView quickListView(Context cont, String s, String cutter) {
		ListView q=new ListView(cont);
		ArrayAdapter<String> apt=new ArrayAdapter<>(cont, android.R.layout.simple_list_item_1, android.R.id.text1, Stringer.cutOnEvery(s, cutter));
		q.setAdapter(apt);
		return q;
	}
	public static LinearLayout quickLinearLayoutVertical(Context cont) {
		LinearLayout ll=new LinearLayout(cont);
		ll.setGravity(Gravity.CENTER);
		ll.setOrientation(LinearLayout.VERTICAL);
		return ll;
	}
	public static LinearLayout quickLinearLayoutHorizontal(Context cont) {
		LinearLayout ll=new LinearLayout(cont);
		ll.setGravity(Gravity.CENTER);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		return ll;
	}
}
