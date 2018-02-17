package nadav.tasher.toollib;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Web {
	public static class FileChecker extends AsyncTask<String, String, String> {
		private long kbs;
		private String addr;
		private boolean available;
		private FileChecker.OnFile of;
		public FileChecker(String url, FileChecker.OnFile onFile) {
			addr=url;
			of=onFile;
		}
		private boolean check() {
			try{
				HttpURLConnection.setFollowRedirects(false);
				HttpURLConnection con=(HttpURLConnection) new URL(addr).openConnection();
				con.setRequestMethod("HEAD");
				return (con.getResponseCode()==HttpURLConnection.HTTP_OK);
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
		}
		@Override
		protected String doInBackground(String... strings) {
			if(check()){
				available=true;
				try{
					HttpURLConnection con=(HttpURLConnection) new URL(addr).openConnection();
					con.connect();
					kbs=con.getContentLength() / 1024;
					con.disconnect();
				}catch(IOException e){
					e.printStackTrace();
				}
			}else{
				available=false;
			}
			return null;
		}
		@Override
		protected void onPostExecute(String s) {
			if(of!=null)
				of.onFinish(kbs, available);
			super.onPostExecute(s);
		}
		public interface OnFile {
			void onFinish(long fileInKB, boolean isAvailable);
		}
	}

	public static class FileDownloader extends AsyncTask<String, String, String> {
		private String furl;
		private File fdpath;
		private boolean available;
		private FileDownloader.OnDownload oe;
		public FileDownloader(String url, File path, FileDownloader.OnDownload onfile) {
			oe=onfile;
			furl=url;
			fdpath=path;
		}
		private boolean check() {
			try{
				HttpURLConnection.setFollowRedirects(false);
				HttpURLConnection con=(HttpURLConnection) new URL(furl).openConnection();
				con.setRequestMethod("HEAD");
				return (con.getResponseCode()==HttpURLConnection.HTTP_OK);
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
		}
		@Override
		protected String doInBackground(String... comment) {
			int perc=0;
			if(check()){
				available=true;
				int count;
				try{
					URL url=new URL(furl);
					URLConnection conection=url.openConnection();
					conection.connect();
					int lenghtOfFile=conection.getContentLength();
					InputStream input=new BufferedInputStream(url.openStream(), 8192);
					OutputStream output=new FileOutputStream(fdpath);
					byte data[]=new byte[1024];
					long total=0;
					while((count=input.read(data))!=-1){
						Log.i("FileDownloader", "File Download: " + furl + " " + total * 100 / lenghtOfFile);
						output.write(data, 0, count);
						total+=count;
						if(perc<(int) (total * 100 / lenghtOfFile)){
							perc++;
							oe.onProgressChanged(fdpath, (int) (total * 100 / lenghtOfFile));
						}
					}
					output.flush();
					output.close();
					input.close();
				}catch(Exception e){
					Log.e("Error: ", e.getMessage());
				}
			}else{
				available=false;
			}
			return null;
		}
		@Override
		protected void onPostExecute(String file_url) {
			if(oe!=null){
				oe.onFinish(fdpath, available);
			}
		}
		public interface OnDownload {
			void onFinish(File output, boolean isAvailable);
			void onProgressChanged(File output, int percent);
		}
	}

	public static class FileReader extends AsyncTask<String, String, String> {
		private InputStream is;
		private FileReader.OnEnd one;
		private String fi;
		public FileReader(String file, FileReader.OnEnd oe) {
			one=oe;
			fi=file;
		}
		@Override
		protected String doInBackground(String... params) {
			try{
				URL url=new URL(fi);
				is=url.openStream();
			}catch(IOException e){
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(String file_url) {
			if(one!=null){
				one.onFileRead(is);
			}
		}
		public interface OnEnd {
			void onFileRead(InputStream stream);
		}
	}

	public static class Pinger extends AsyncTask<String, String, Boolean> {
		private Pinger.OnEnd onEnd;
		private int tmout=2000;
		private String addr;
		public Pinger(int timeout, Pinger.OnEnd e) {
			onEnd=e;
			tmout=timeout;
		}
		@Override
		protected Boolean doInBackground(String... strings) {
			addr=strings[0];
			try{
				HttpURLConnection connection=(HttpURLConnection) new URL(strings[0]).openConnection();
				connection.setConnectTimeout(tmout);
				connection.connect();
				return true;
			}catch(IOException e){
				e.printStackTrace();
				return false;
			}
		}
		@Override
		protected void onPostExecute(Boolean result) {
			if(onEnd!=null){
				onEnd.onPing(addr, result);
			}
		}
		public interface OnEnd {
			void onPing(String url, boolean result);
		}
	}
	public static class PHP {
		public static class Post extends AsyncTask<String, String, String> {
			private String phpurl;
			private ArrayList<PHPParameter> parms;
			private OnPost op;
			private String result;
			public Post(String url, ArrayList<PHPParameter> postvalues, OnPost onpost) {
				this.phpurl=url;
				parms=postvalues;
				op=onpost;
			}
			@Override
			protected String doInBackground(String... comments) {
				String response="";
				String data="";
				BufferedReader reader=null;
				HttpURLConnection conn=null;
				try{
					URL url=new URL(phpurl);
					conn=(HttpURLConnection) url.openConnection();
					conn.setRequestMethod("POST");
					conn.setDoOutput(true);
					OutputStreamWriter wr=new OutputStreamWriter(conn.getOutputStream());
					for(int v=0; v<parms.size(); v++){
						data+="&" + URLEncoder.encode(parms.get(v).getName(), "UTF-8") + "=" + URLEncoder.encode(parms.get(v).getValue(), "UTF-8");
					}
					wr.write(data);
					wr.flush();
					reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
					StringBuilder sb=new StringBuilder();
					boolean first=true;
					String line;
					while((line=reader.readLine())!=null){
						if(!first){
							sb.append("\n");
						}else{
							first=false;
						}
						sb.append(line);
					}
					response=sb.toString();
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					try{
						if(reader!=null){
							reader.close();
						}
						if(conn!=null){
							conn.disconnect();
						}
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}
				result=response;
				return response;
			}
			@Override
			protected void onPostExecute(String s) {
				if(op!=null){
					op.onPost(result);
				}
				super.onPostExecute(s);
			}
			public interface OnPost {
				void onPost(String response);
			}

			public static class PHPParameter {
				private String name;
				private String value;
				public PHPParameter(String n, String v) {
					name=n;
					value=v;
				}
				public String getName() {
					return name;
				}
				public void setName(String newname) {
					name=newname;
				}
				public String getValue() {
					return value;
				}
				public void setValue(String newvalue) {
					value=newvalue;
				}
			}
		}
	}
}
