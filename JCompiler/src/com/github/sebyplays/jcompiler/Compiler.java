package com.github.sebyplays.jcompiler;

import com.github.sebyplays.jcompiler.utils.ResourceType;
import com.github.sebyplays.jcompiler.utils.io.ResourceManager;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import javax.smartcardio.ATR;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

public class Compiler {

    @Getter @Setter private Artifact artifact;
    @Getter @Setter private JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(this.getArtifact().getOutputPath() + "/" + this.getArtifact().getArtifactName() + ".jar"), this.getArtifact().getManifest());
    @Getter @Setter private File resourcePath;
    public Compiler(Artifact artifact) throws IOException {
        this.setArtifact(artifact);
        if(this.getArtifact().getArtifactName().equals(null) || this.getArtifact().getArtifactName().equals("")){
            this.getArtifact().setArtifactName("Artifact_" + new SimpleDateFormat("dd_MM_yyyy_HH:mm").format(new Date().toString() + "_"));
        }
        if(this.getArtifact().getResourceType() == ResourceType.INTERNAL){
            new File(System.getProperty("user.dir") + "/temp/").mkdirs();
            this.setResourcePath(new File(System.getProperty("user.dir") + "/temp/"));
            new ResourceManager().saveResource("resources/", this.getResourcePath(), false);
        } else {
            this.setResourcePath(new File(this.getArtifact().getResourcePathExternally()));
        }
        this.setJarOutputStream(new JarOutputStream(new FileOutputStream(this.getArtifact().getOutputPath() + "/" + this.getArtifact().getArtifactName() + ".jar"), this.getArtifact().getManifest()));
    }

    @SneakyThrows
    private void compile(File resourcePath, JarOutputStream jarOutputStream){
        BufferedInputStream in = null;
            if (resourcePath.isDirectory()){
                String name = resourcePath.getPath().replace("\\", "/");
                if (!name.isEmpty()){
                    if (!name.endsWith("/"))
                        name += "/";
                    JarEntry entry = new JarEntry(name);
                    entry.setTime(resourcePath.lastModified());
                    jarOutputStream.putNextEntry(entry);
                    jarOutputStream.closeEntry();
                }
                for (File nestedFile: resourcePath.listFiles())
                    compile(nestedFile, jarOutputStream);
                return;
            }

            JarEntry entry = new JarEntry(resourcePath.getPath().replace("\\", "/"));
            entry.setTime(resourcePath.lastModified());
            jarOutputStream.putNextEntry(entry);
            in = new BufferedInputStream(new FileInputStream(resourcePath));

            byte[] buffer = new byte[1024];
            while (true){
                int count = in.read(buffer);
                if (count == -1)
                    break;
                jarOutputStream.write(buffer, 0, count);
            }
            jarOutputStream.closeEntry();
    }

    public void compile(){
        this.compile(this.getResourcePath(), this.getJarOutputStream());
        if(this.getArtifact().getResourceType() == ResourceType.INTERNAL){
            new File(System.getProperty("user.dir") + "/temp/").delete();
        } else {
            for(File file : new File(System.getProperty("user.dir") + "/in/").listFiles()){
                file.delete();
            }
        }
    }
}
