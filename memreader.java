import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class memreader {

	public static void main(String[] args) throws IOException {
		File fileSrc = new File("/proc/2663/maps");
		File fileDst = new File("./2663_maps");

		FileInputStream in = null;
		FileOutputStream out = null;

		try {
			in =  new FileInputStream(fileSrc);
			out = new FileOutputStream(fileDst);

			// Transfer bytes from in to out
			byte[] buf = new byte[1024];
			int len;

			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
		} finally {
			if(in != null) {
				in.close();
			}

			if(out != null) {
				out.close();
			}
		}
	}
}
