package nadav.tasher.toollib;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FileFactory {
	public static String readFile(File file) {
		try{
			StringBuilder text=new StringBuilder();
			BufferedReader in=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String str;
			while((str=in.readLine())!=null){
				text.append(str);
				text.append('\n');
			}
			in.close();
			return text.toString();
		}catch(IOException e){
			return null;
		}
	}
	public static void writeToFile(File file, String lines) {
		try{
			OutputStreamWriter writer=new OutputStreamWriter(new FileOutputStream(file));
			writer.write(lines);
			writer.flush();
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public static void log(File f, String s) {
		DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss @ ");
		String time=dateFormat.format(new Date());
		String current=FileFactory.readFile(f);
		if(current!=null){
			String all=current + time + s;
			FileFactory.writeToFile(f, all);
		}else{
			String start=time + "Start Log\n";
			String all=start + time + s;
			FileFactory.writeToFile(f, all);
		}
		Log.i("Logger", s);
	}
	public static void logWarning(File f, String s) {
		DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss @ ");
		String time=dateFormat.format(new Date()) + "Warning: ";
		String current=FileFactory.readFile(f);
		if(current!=null){
			String all=current + time + s;
			FileFactory.writeToFile(f, all);
		}else{
			String start=time + "Start Log\n";
			String all=start + time + s;
			FileFactory.writeToFile(f, all);
		}
		Log.i("Logger", s);
	}
	public static void logError(File f, String s) {
		DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss @ ");
		String time=dateFormat.format(new Date()) + "Error!: ";
		String current=FileFactory.readFile(f);
		if(current!=null){
			String all=current + time + s;
			FileFactory.writeToFile(f, all);
		}else{
			String start=time + "Start Log\n";
			String all=start + time + s;
			FileFactory.writeToFile(f, all);
		}
		Log.i("Logger", s);
	}
}
