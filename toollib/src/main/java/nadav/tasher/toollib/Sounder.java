package nadav.tasher.toollib;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

public class Sounder {
	public static void playSound(Context context, String asset, boolean looping) {
		MediaPlayer m=new MediaPlayer();
		try{
			AssetFileDescriptor descriptor=context.getAssets().openFd(asset);
			m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
			descriptor.close();
			m.prepare();
			m.setVolume(1f, 1f);
			m.setLooping(looping);
			m.start();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
