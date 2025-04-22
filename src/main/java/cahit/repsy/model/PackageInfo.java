package cahit.repsy.model;

import lombok.*;

import java.util.List;

@Data
public class PackageInfo {
    private String name;
    private String version;
    private String author;
    private List<Dep> dependencies;
}