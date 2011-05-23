package sft.jspaceduel.sysinit;

import java.lang.reflect.Field;


/**
 *
 * @author JJ
 */
public class InitLibraries {

    public static void addlwjgl() {
        try {
            /*System.out.println(System.getProperty("os.name"));
            System.out.println(System.getProperty("java.library.path"));
            System.out.println(System.getProperty("user.dir"));
             * 
             */
            
            String osname = System.getProperty("os.name");
            String osfolder = "linux";
            String seperator = ":";
            if(osname.toLowerCase().contains("windows")) {
                osfolder = "windows";
                seperator = ";";
            } else if (osname.toLowerCase().contains("mac") || osname.toLowerCase().contains("osx")) {
                osfolder = "mac";
            }
            
            String oldlibpath = System.getProperty("java.library.path");
            System.setProperty("java.library.path", oldlibpath + seperator+"./lib/lwjgl/native/"+osfolder+seperator);
            
            //System.out.println(System.getProperty("java.library.path"));
            
            Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(true);
            fieldSysPath.set(null, null);
            
        } catch (NoSuchFieldException ex) {
        } catch (SecurityException ex) {
        } catch (IllegalAccessException ex) {
        }
        
    }
}