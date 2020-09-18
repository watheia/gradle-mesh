/**
 * @author Aaron R Miller<aaron.miller@waweb.io>
 */
package io.waweb.gradle.mesh

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Provider

/**
 * Plugin entry point
 */
public class GradleMeshPlugin implements Plugin<Project> {

	final static String MESH_GROUP = "Mesh"

	public void apply(Project project) {

		// Register extension model with defaults
		final MeshExtension mesh = project.extensions.create('mesh', MeshExtension).with {
			host = System.getenv('GRADLE_MESH_HTTP_HOST') ?: "localhost"
			port = System.getenv('GRADLE_MESH_HTTP_PORT') ?: 8080
			useSsl = System.getenv('GRADLE_MESH_HTTP_SSL_ENABLE') ?: false
			projectName = project.name
		}

		final MeshLogin meshLogin = project.tasks.register("meshLogin", MeshLogin) {
			group = MESH_GROUP
			description = "Login to mesh rest API"
		}

		final Provider<MeshClient> clientProvider = project.provider {
			return meshLogin.client
		}
	}
}
