import java.io.*;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;

public class Versions {

    public static void main(String[] args) {
        Versions ver = new Versions();

        LinkedList<File> pomfiles = ver.getPoms();
        Hashtable<String, String> table = new Hashtable<>();

        if(pomfiles.size()>0) {
            for (File pom : pomfiles) {
                table = ver.getDependencies(pom, table);
            }
        }

        ver.writeCSV(table);

        System.out.println("Pom.xml found: " + pomfiles.size());
        System.out.println("Dependencies found: " + table.size());

    }

    private void writeCSV(Hashtable<String, String> table) {
        try (BufferedWriter buff = new BufferedWriter(new FileWriter("dependencies.csv"))){
            buff.write("Dependency,Current Version,Latest Version\n");

            for(Map.Entry<String,String> each: table.entrySet()){
                buff.write(each.getKey()+","+each.getValue()+",\n");
            }
            buff.flush();
        } catch (FileNotFoundException e) {
            System.out.println("Writing to file failed. Check writing rights for this location.");
        }catch(IOException e){
            System.out.println("Writing to file failed. ");
        }
        System.out.println("dependencies.csv created");
    }

    private Hashtable<String, String> getDependencies(File pom, Hashtable<String, String> artifactsAndVersions) {

        try {

            FileReader fileReader = new FileReader(pom);

            BufferedReader reader = new BufferedReader(fileReader);
            String line = "";
            do {
                line = reader.readLine();
            } while (!(line.trim()).equals("<dependencies>") && !line.equals(null));

            String key = "", value = "";
            while(!((line = reader.readLine()).trim()).equals("</dependencies>")){

                if(line.contains("<artifactId>")){
                    key = line.split("artifactId")[1];
                    key = key.replace(">","").replace("</","");
                }
                if(line.contains("<version>")){
                    value = line.split("version")[1];
                    value = value.replace(">","").replace("</","");
                }
                if(!key.isEmpty() & !value.isEmpty()) {
                    if (!artifactsAndVersions.containsKey(key)) {
                        artifactsAndVersions.put(key, value);
                    }
                    key = "";
                    value = "";
                }

            }

        } catch (IOException e) { System.out.println("File error: " + e.getStackTrace());}

        return artifactsAndVersions;
    }

    public LinkedList<File> getPoms() {
        LinkedList<File> directories = new LinkedList<>();
        LinkedList<File> pom = new LinkedList<>();
        directories.add(new File("."));

        while (!directories.isEmpty()) {
            File dir = directories.pop();
            File[] filesList = dir.listFiles();

            for (File file : filesList) {
                if (file.isFile() & file.getName().contains("pom.xml")) {
                    pom.add(file);
                } else {
                    if (file.isDirectory()) {
                        directories.push(file);
                    }
                }
            }
        }
        return pom;
    }
}
