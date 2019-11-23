import java.io.File;

/**
 * Created by Icewater on 08/11/2018.
 */
public class FileRenamer {

    private static String FILE_TYPE ="pdf";

    public static void main(String[] args) {
        File directory =null;
        String renameToAddition = "1";
        if (args.length == 1) {
            try {
                renameToAddition = args[0];

            } catch (Exception d) {
                System.out.println("If you specify no directory, the first argument needs to be the filename addition");
            }
            directory = new File(System.getProperty("user.dir"));
        } else if (args.length == 2){
            directory = new File("args[0]");
            renameToAddition = args[1];

        }else {
            System.out.println("Please specify the addition to what you want to rename your files to and optionally the place where the files are");
            System.exit(0);
        }
        renameFiles(directory, renameToAddition);


    }

    private static void renameFiles(File directory, String renameToAddition) {

        if (directory.isDirectory()) { // make sure it's a directory
            for (final File f : directory.listFiles()) {
                if ( f.getName().endsWith("."+ FILE_TYPE)) {
                    try {
                        File newfile = new File(f.getName().replace("."+FILE_TYPE,"")+renameToAddition +"."+FILE_TYPE);

                        if (f.renameTo(newfile)) {
                            System.out.println("Rename succesful");
                        } else {
                            System.out.println("Rename failed");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }
}