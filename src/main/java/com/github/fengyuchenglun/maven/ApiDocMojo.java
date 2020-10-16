package com.github.fengyuchenglun.maven;


import com.github.fengyuchenglun.apidoc.core.ApiDoc;
import com.github.fengyuchenglun.apidoc.core.Context;
import com.github.fengyuchenglun.apidoc.core.common.enums.FieldShowWay;
import com.github.fengyuchenglun.apidoc.springmvc.SpringMvcContext;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;


/**
 * generate rest doc with apiDoc
 *
 * @author duanledexianxianxian
 */
@Mojo(name = "apidoc", requiresDependencyResolution = ResolutionScope.COMPILE)
public class ApiDocMojo extends AbstractMojo {

    private static final String PROJECT = "project";

    private static final String COMMA = ",";
    /**
     * The Project.
     */
    MavenProject project;

    /**
     * The Id.
     */
    @Parameter
    String id;
    /**
     * The Name.
     */
    @Parameter
    String name;
    /**
     * The Description.
     */
    @Parameter
    String description;
    /**
     * The Build.
     */
    @Parameter
    String build;
    /**
     * The Source.
     * 传字符串，使用逗号分隔
     */
    @Parameter
    String source;
    /**
     * The Dependency.
     */
    @Parameter
    String dependency;
    /**
     * The Jar.
     */
    @Parameter
    String jar;
    /**
     * The Version.
     */
    @Parameter
    String version;
    /**
     * The Css.
     */
    @Parameter
    String css;

    /**
     * 字段显示方式
     * FLAT-平级
     * TREE=树级
     */
    @Parameter
    FieldShowWay fieldShowWay;

    /**
     * 控制器名称
     */
    @Parameter
    String controller;
    /**
     * rest控制器名称
     */
    @Parameter
    String restController;

    /**
     * markdown模版文件路径
     */
    @Parameter
    String template;
    @Parameter
    String includes;
    @Parameter
    String excludes;
    @Parameter
    String pageClassNames;
    @Parameter
    String scanCode;
    @Parameter
    String scanPackages;


    @Override
    public void execute() {
        if (getPluginContext().containsKey(PROJECT) && getPluginContext().get(PROJECT) instanceof MavenProject) {
            project = (MavenProject) getPluginContext().get(PROJECT);
            build();
        }
    }

    private void build() {
        Context context = new Context();
        if (source != null) {
            for (String dir : source.split(COMMA)) {
                context.addSource(abs(dir));
            }
        } else {
            context.addSource(project.getBasedir().toPath());
        }
        if (dependency != null) {
            for (String dir : dependency.split(COMMA)) {
                context.addDependency(abs(dir));
            }
        } else {
            MavenProject parent = findParent(project);
            context.addDependency(parent.getBasedir().toPath());
        }
        if (jar != null) {
            for (String dir : jar.split(COMMA)) {
                context.addJar(abs(dir));
            }
        }
        context.setId(id != null ? id : project.getName());
        if (build != null) {
            context.setBuildPath(abs(build));
        } else {
            context.setBuildPath(Paths.get(project.getBuild().getDirectory()));
        }
        if (name != null) {
            context.setName(name);
        } else {
            context.setName(project.getName());
        }
        if (description != null) {
            context.setDescription(description);
        } else if (project.getDescription() != null) {
            context.setDescription(project.getDescription());
        }
        if (version != null) {
            context.setVersion(version);
        } else if (project.getVersion() != null) {
            context.setVersion(project.getVersion());
        }
        if (css != null) {
            context.setCss(css);
        }
        // 表格列表展现方式,默认平级
        if (null != fieldShowWay) {
            context.setFileShowWay(fieldShowWay);
        }

        if (null != controller) {
            context.addController(controller);
        }

        if (null != restController) {
            context.addRestController(restController);
        }

        if (null != template) {
            context.setTemplate(template);
        }

        if (includes != null) {
            context.addIncludes(Arrays.asList(includes.split(COMMA)));
        }

        if (includes != null) {
            context.addExcludes(Arrays.asList(excludes.split(COMMA)));
        }

        if (scanPackages != null) {
            context.addScanPackages(Arrays.asList(scanPackages.split(COMMA)));
        }

        if (scanPackages != null) {
            context.addScanPackages(Arrays.asList(scanPackages.split(COMMA)));
        }
        if (pageClassNames != null) {
            context.addPageClassNames(scanPackages.split(COMMA));
        }
        if (scanCode != null) {
            context.setScanCode(Boolean.valueOf(scanCode));
        }
        ApiDoc apiDoc = new ApiDoc(context);
        apiDoc.parse();
        apiDoc.render();
    }

    private MavenProject findParent(MavenProject mp) {
        if (mp.getParentFile() != null && mp.getParentFile().exists()) {
            return findParent(mp.getParent());
        }
        return mp;
    }

    private Path abs(String dir) {
        Path path = Paths.get(dir);
        if (path.isAbsolute()) {
            return path;
        } else {
            return project.getBasedir().toPath().resolve(path);
        }
    }

    /**
     * Gets project.
     *
     * @return the project
     */
    public MavenProject getProject() {
        return project;
    }

    /**
     * Sets project.
     *
     * @param project the project
     */
    public void setProject(MavenProject project) {
        this.project = project;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets build.
     *
     * @return the build
     */
    public String getBuild() {
        return build;
    }

    /**
     * Sets build.
     *
     * @param build the build
     */
    public void setBuild(String build) {
        this.build = build;
    }

    /**
     * Gets source.
     *
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets source.
     *
     * @param source the source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Gets dependency.
     *
     * @return the dependency
     */
    public String getDependency() {
        return dependency;
    }

    /**
     * Sets dependency.
     *
     * @param dependency the dependency
     */
    public void setDependency(String dependency) {
        this.dependency = dependency;
    }

    /**
     * Gets jar.
     *
     * @return the jar
     */
    public String getJar() {
        return jar;
    }

    /**
     * Sets jar.
     *
     * @param jar the jar
     */
    public void setJar(String jar) {
        this.jar = jar;
    }

    /**
     * Gets version.
     *
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets version.
     *
     * @param version the version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Gets css.
     *
     * @return the css
     */
    public String getCss() {
        return css;
    }

    /**
     * Sets css.
     *
     * @param css the css
     */
    public void setCss(String css) {
        this.css = css;
    }
}
