package sft.sftengine.util;

import java.lang.reflect.Field;


/**
 *
 * @author JJ
 */
public class InitLibraries {

    /**
     * Unbelievable 1337 function that automatically adds the lwjgl natives to
     * the java path without having to make complicated startup parameters etc.
     * 
     * Please use this function for your LWJGL project, it is very helpful!
     */
    public static void addlwjgl() {
        try {
            
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
            
            Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(true);
            fieldSysPath.set(null, null);
            
        } catch (NoSuchFieldException ex) {
        } catch (SecurityException ex) {
        } catch (IllegalAccessException ex) {
        }
        
    }
}
