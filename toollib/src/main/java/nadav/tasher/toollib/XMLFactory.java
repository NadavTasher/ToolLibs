package nadav.tasher.toollib;

import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class XMLFactory {
	public static final int ASSETS=1;
	public static final int INTERNAL_STORAGE=0;
	public static ArrayList<XMLTag> read(Context context, File file, ArrayList<String> tags, int flag) {
		XmlPullParserFactory pullParserFactory;
		try{
			pullParserFactory=XmlPullParserFactory.newInstance();
			XmlPullParser parser=pullParserFactory.newPullParser();
			FileInputStream fileInputStream=new FileInputStream(file);
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(fileInputStream, null);
			switch(flag){
				case ASSETS:
					parser.setInput(context.getAssets().open(file.toString()), null);
					break;
				case INTERNAL_STORAGE:
					parser.setInput(fileInputStream, null);
					break;
				default:
					break;
			}
			return parse(parser, tags);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	public static ArrayList<XMLTag> read(InputStream input, ArrayList<String> tags) {
		XmlPullParserFactory pullParserFactory;
		try{
			pullParserFactory=XmlPullParserFactory.newInstance();
			XmlPullParser parser=pullParserFactory.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(input, null);
			return parse(parser, tags);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	private static ArrayList<XMLTag> parse(XmlPullParser parser, ArrayList<String> tags) throws Exception {
		ArrayList<XMLTag> products=null;
		XMLTag currentProduct;
		int eventType=parser.getEventType();
		while(eventType!=XmlPullParser.END_DOCUMENT){
			String name;
			switch(eventType){
				case XmlPullParser.START_DOCUMENT:
					products=new ArrayList<>();
					break;
				case XmlPullParser.START_TAG:
					name=parser.getName();
					if(tags!=null){
						for(int i=0; i<tags.size(); i++){
							if(name.equals(tags.get(i))){
								currentProduct=new XMLTag();
								currentProduct.tag=tags.get(i);
								currentProduct.data=parser.nextText();
								products.add(currentProduct);
							}
						}
					}else{
						currentProduct=new XMLTag();
						currentProduct.tag=name;
						currentProduct.data=parser.nextText();
						products.add(currentProduct);
					}
					break;
				case XmlPullParser.END_TAG:
					name=parser.getName();
			}
			eventType=parser.next();
		}
		return products;
	}
	public static class XMLTag {
		public String tag;
		public String data;
	}
}
