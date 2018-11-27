import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Icewater on 20/11/2018.
 */
public class FileReader {
    static List<File> loadFilesInFolder() {


        File dir = new File(System.getProperty("user.dir"));
        FileFilter fileFilter = new WildcardFileFilter("*.pdf");
        File[] files = dir.listFiles(fileFilter);

        return Arrays.asList(files);
    }

    static File returnFileIfContainedIn(List<File> files, String searchString){
        int timesFound = 0;
        File returnFile = null;
        for ( File file : files) {
            if ( file.getName().contains(searchString)){
                timesFound++;
                returnFile = file;
            }
        }
        if ( timesFound > 1) {
            System.out.println("Name: " + searchString+" has been found more tha once. No upload done");
        } else if ( timesFound == 1) {
            return returnFile;
        }
        return null;
    }
}
