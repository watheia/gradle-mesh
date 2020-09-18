/**
 * @author Aaron R Miller<aaron.miller@waweb.io>
 */
package io.waweb.gradle.mesh

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Provider

import com.gentics.mesh.rest.client.MeshRestClient

/**
 * Plugin entry point
 */
public class GradleMeshPlugin implements Plugin<Project> {

	final static String MESH_GROUP = "Mesh"

	final static String BASE_PATH = "/api/v2"

	public void apply(Project project) {

		project.buildscript.repositories.configure {
			mavenLocal()
			jcenter()
			maven { url = "https://maven.gentics.com/maven2" }
		}

		// Register extension model with defaults
		final MeshExtension mesh = project.extensions.create('mesh', MeshExtension).tap {
			host = System.getenv('GRADLE_MESH_HTTP_HOST') ?: "localhost"
			port = System.getenv('GRADLE_MESH_HTTP_PORT') ?: 8080
			useSsl = System.getenv('GRADLE_MESH_HTTP_SSL_ENABLE') ?: false
			userName = System.getenv('GRADLE_MESH_USER') ?: "admin"
			password = System.getenv('GRADLE_MESH_PASSWORD') ?: "admin"
			basePath = BASE_PATH
		}

		// Register the login task
		final MeshLogin meshLogin = project.tasks.create("meshLogin", MeshLogin) { MeshLogin t ->
			t.group = MESH_GROUP
			t.description = "Login to mesh rest API"
			t.host.set(mesh.host)
			t.port.set(mesh.port)
			t.useSsl.set(mesh.useSsl)
			t.userName.set(mesh.userName)
			t.password.set(mesh.password)
			t.basePath.set(mesh.basePath)
		}

		final Provider<MeshRestClient> clientProvider = project.provider {
			return meshLogin.client
		}

		project.tasks.withType(MeshClientTask).all { MeshClientTask task ->
			task.client.set(clientProvider)
			task.client.finalizeValue()
			task.dependsOn(meshLogin)
		}
	}
}
