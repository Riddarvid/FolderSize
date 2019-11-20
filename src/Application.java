import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Application {
    private int i = 0;
    private String red = "\033[0;31m";
    private String reset = "\033[0m";
    private long sizeOfGB = 1073741824;

    public static void main(String[] args) {
        new Application().getSizeOfFolder();
    }

    private void getSizeOfFolder() {
        File startNode = new File("D:\\Program Files (x86)\\Steam\\steamapps\\common");
        getSizeOfSubFolders(startNode);
    }

    private void getSizeOfSubFolders(File node) {
        if (node.isDirectory()) {
            String[] subDirs = getSubDirs(node);
            if (subDirs != null) {
                long totalSize = 0;
                long sizeOfLargeGames = 0;
                printSubdirs(subDirs);
                for (String s : subDirs) {
                    File child = new File(node, s);
                    //System.out.println(child.getAbsolutePath());
                    long size = getSizeOfFolder(child);
                    totalSize += size;
                    if (size >= sizeOfGB * 0.5) {
                        sizeOfLargeGames += size;
                        System.out.println(child.getAbsolutePath());
                        System.out.println(red + roundTo2decimals(bytesToGB(size)) + " GB" + reset);
                    } else {
                        //System.out.println(roundTo2decimals(bytesToGB(size)) + " GB");
                    }
                }
                System.out.println();
                System.out.println("Size of " + node.getAbsolutePath() + " is " + roundTo2decimals(bytesToGB(totalSize)) + "GB");
                System.out.println("Size of large folders in " + node.getAbsolutePath() + " is " + roundTo2decimals(bytesToGB(sizeOfLargeGames)) + "GB");
            }
        }
    }

    private void printSubdirs(String[] subDirs) {
        System.out.println("Directories to search:\n");
        for (String s : subDirs) {
            System.out.println("\t" + s);
        }
        System.out.println();
    }

    private String[] getSubDirs(File node) {
        if (node.isDirectory()) {
            String[] children = node.list();
            List<String> subDirs = new ArrayList<>();
            if (children != null) {
                for (String child : children) {
                    if (new File(node, child).isDirectory()) {
                        subDirs.add(child);
                    }
                }
                return subDirs.toArray(String[]::new);
            }
        }
        return null;
    }

    private String roundTo2decimals(double n) {
        return String.format("%.2f", n);
    }

    private long getSizeOfFolder(File node) {
        if (node.isDirectory()) {
            long size = 0;
            String[] children = node.list();
            if (children != null) {
                for (String child : children) {
                    size += getSizeOfFolder(new File(node, child));
                }
            }
            return size;
        }
        return node.length();
    }

    private double bytesToGB(long bytes) {
        return ((double)bytes) / sizeOfGB;
    }
}
