package com.github.sebyplays.jcompiler;

import com.github.sebyplays.jcompiler.utils.ResourceType;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class Artifact {

    @Getter @Setter private String outputPath = System.getProperty("user.dir") + "/out/";
    @Getter @Setter private String resourcePath;
    @Getter @Setter private String resourcePathExternally = System.getProperty("user.dir") + "/in/";
    @Getter @Setter private double artefactVersion;
    @Getter @Setter private String artifactName;
    @Getter @Setter private String mainClass;
    @Getter @Setter private ResourceType resourceType;
    @Getter private Manifest manifest = new Manifest();

    public Artifact() {
        //this.setArtifactName("Artifact_" + new SimpleDateFormat("dd_MM_yyyy_HH:mm").format(new Date().toString() + "_"));
    }

    public Artifact(String artifactName, double artifactVersion, String mainClass, ResourceType resourceType, String outputPath) {
        this.setArtifactName(artifactName);
        this.setArtefactVersion(artifactVersion);
        this.setResourceType(resourceType);
        this.setResourcePath(resourcePath);
        this.setOutputPath(outputPath);
        this.setMainClass(mainClass);
        this.manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, this.getArtefactVersion());
        this.manifest.getMainAttributes().put(Attributes.Name.MAIN_CLASS, this.getMainClass());
        new File(this.getOutputPath()).mkdirs();
        new File(this.getResourcePathExternally()).mkdirs();
    }
}