package nadav.tasher.toollib;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipFactory {
	public static void extract(File file, String extractto) {
		try{
			un(file, new File(extractto));
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	static private void un(File z, File d) throws IOException {
		ZipInputStream zis=new ZipInputStream(new BufferedInputStream(new FileInputStream(z)));
		try{
			ZipEntry ze;
			int count;
			byte[] buffer=new byte[8192];
			while((ze=zis.getNextEntry())!=null){
				File file=new File(d, ze.getName());
				File dir=ze.isDirectory() ? file : file.getParentFile();
				if(!dir.isDirectory()&&!dir.mkdirs())
					throw new FileNotFoundException("Failed to ensure directory: " + dir.getAbsolutePath());
				if(ze.isDirectory())
					continue;
				FileOutputStream fout=new FileOutputStream(file);
				try{
					while((count=zis.read(buffer))!=-1)
						fout.write(buffer, 0, count);
				}finally{
					fout.close();
				}
			}
		}finally{
			zis.close();
		}
	}
}
