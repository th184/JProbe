package jprobe;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import java.net.URLClassLoader;

import javax.imageio.ImageIO;

import jprobe.services.ErrorHandler;
import jprobe.services.Log;

public class Launcher {
	
	public static void main(String[] args){
		//init the user's jprobe directory
		initUserDirectory();
		//init the logs
		initLogs();
		//init the user subdirectories
		initDir(Constants.USER_PLUGINS_DIR);
		initDir(Constants.FELIX_CACHE_DIR);
		initDir(Constants.PREFERENCES_DIR);
		
		Configuration config = new Configuration(new File(Constants.CONFIG_FILE), args);
		//TESTING
//		BufferedImage image = null;
//        try {
//            URL url = new URL("http://www.mkyong.com/image/mypic.jpg");
//            image = ImageIO.read(url);
//            
//            ImageIO.write(image, "jpg",new File("C:\\Users\\th184\\Documents\\JProbe\\JProbe\\out.jpg"));
//        } catch (IOException e) {
//        	e.printStackTrace();
//        }
//        System.out.println("Done");
//		ClassLoader cl = ClassLoader.getSystemClassLoader();
//        URL[] urls = ((URLClassLoader)cl).getURLs();
//        System.out.println("IN LAUNCHER");
//        for(URL url: urls){
//        	System.out.println(url.getFile());
//        }
        
        
		new JProbe(config);
	}


	private static void initLogs() {
		File log = initFile(Constants.JPROBE_LOG);
		Log.getInstance().init(new TimeStampJournal(log));
		File errorLog = initFile(Constants.JPROBE_ERROR_LOG);
		ErrorHandler.getInstance().init(new TimeStampJournal(errorLog));
	}



	private static void initUserDirectory() {
		File jprobeDir = initDir(Constants.USER_JPROBE_DIR);
		if(!jprobeDir.exists()){
			System.err.println("Error initializing directory "+jprobeDir);
			System.err.println("Trying in working directory...");
			Constants.USER_JPROBE_DIR = "jprobe";
			jprobeDir = initDir(Constants.USER_JPROBE_DIR);
			if(jprobeDir.exists()){
				System.err.println("Success.");
			}else{
				System.err.println("Cannot initialize directory "+jprobeDir);
				System.err.println("Exiting");
				System.exit(-1);
			}
			//update all files and dirs to use new user dir
			Constants.LOG_DIR = Constants.USER_JPROBE_DIR + File.separator + "logs";
			Constants.JPROBE_LOG = Constants.LOG_DIR + File.separator + "jprobe.log";
			Constants.JPROBE_ERROR_LOG = Constants.LOG_DIR + File.separator + "jprobe-error.log";
			Constants.USER_PLUGINS_DIR = Constants.USER_JPROBE_DIR + File.separator + "plugins";
			Constants.FELIX_CACHE_DIR = Constants.USER_JPROBE_DIR + File.separator + "cache";
			Constants.PREFERENCES_DIR = Constants.USER_JPROBE_DIR + File.separator + "preferences";
			Constants.CONFIG_FILE = Constants.PREFERENCES_DIR + File.separator + "jprobe.pref";
		}
	}
	
	
	
	private static File initDir(String path){
		File f= new File(path);
		if(!f.exists()){
			System.err.println("Initializing directory "+f);
			f.mkdirs();
		}
		return f;
	}
	
	private static File initFile(String path){
		File f = new File(path);
		if(!f.exists()){
			System.err.println("Initializing file "+f);
			f.getParentFile().mkdirs();
			try {
				f.createNewFile();
			} catch (IOException e) {
				System.err.println("Error initializing file "+f + ": "+e.getMessage());
			}
		}
		return f;
	}
	
}


