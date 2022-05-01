package org.atomicoke.logic.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/21 15:58
 */
@Configuration
@EnableConfigurationProperties(ProjectProps.class)
public class ProjectConfiguration {

    private static ProjectProps projectProps;

    public ProjectConfiguration(ProjectProps projectProps) {
        ProjectConfiguration.projectProps = projectProps;
    }

    public static ProjectProps getProjectProps() {
        return projectProps;
    }
}