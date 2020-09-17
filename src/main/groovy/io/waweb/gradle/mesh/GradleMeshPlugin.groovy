/**
 * @author Aaron R Miller<aaron.miller@waweb.io>
 */
package io.waweb.gradle.mesh

import org.gradle.api.Project
import org.gradle.api.Plugin

/**
 * Plugin entry point
 */
public class GradleMeshPlugin implements Plugin<Project> {

    public void apply(Project project) {

        // Register extension model with defaults
        project.extensions.create('mesh', MeshExtension).with {
            host = System.getenv('MESH_HTTP_HOST') ?: "localhost"
            port = System.getenv('MESH_HTTP_PORT') ?: 8080
            useSsl = System.getenv('MESH_HTTP_SSL_ENABLE') ?: false
            projectName = project.name
        }

        project.tasks.register("greeting") {
            doLast {
                println("Hello from plugin 'io.waweb.gradle.mesh.greeting'")
            }
        }
    }
}
