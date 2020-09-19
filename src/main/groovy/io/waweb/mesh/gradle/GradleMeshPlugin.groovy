/**
 * @author Aaron R Miller<aaron.miller@waweb.io>
 */
package io.waweb.mesh.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Provider

import com.gentics.mesh.rest.client.MeshRestClient

import io.waweb.mesh.gradle.tasks.DefaultMeshTask
import io.waweb.mesh.gradle.tasks.LoadMeshProjects

/**
 * Plugin entry point
 */
class GradleMeshPlugin implements Plugin<Project> {

	final static String MESH_GROUP = "Mesh"

	public void apply(Project project) {

		// Register extension model with defaults
		final MeshExtension mesh = project.extensions.create('mesh', MeshExtension).tap {
			host.convention(System.getenv('GRADLE_MESH_HTTP_HOST') ?: "localhost")
			port.convention(System.getenv('GRADLE_MESH_HTTP_PORT') ?: 8080)
			useSsl.convention(System.getenv('GRADLE_MESH_HTTP_SSL_ENABLE') ?: false)
			apiKey.convention(System.getenv('GRADLE_MESH_API_KEY'))
			userName.convention(System.getenv('GRADLE_MESH_USER') ?: "admin")
			password.convention(System.getenv('GRADLE_MESH_PASS') ?: "admin")
			autoCreate.convention(true)
		}

		// Register the login task
		final MeshLogin meshLogin = project.tasks.create("meshLogin", MeshLogin) { MeshLogin task ->
			task.group = MESH_GROUP
			task.description = "Login to mesh rest API"
			task.host.set(mesh.host)
			task.port.set(mesh.port)
			task.useSsl.set(mesh.useSsl)
			task.apiKey.set(mesh.apiKey)
			task.userName.set(mesh.userName)
			task.password.set(mesh.password)
		}

		final Provider<MeshRestClient> clientProvider = project.provider {
			return meshLogin.client
		}

		// Ensure all mesh tasks have an authenticated client
		project.tasks.withType(DefaultMeshTask).all { DefaultMeshTask task ->
			task.client.set(clientProvider)
			task.client.finalizeValueOnRead()
			task.dependsOn(meshLogin)
		}

		// register a default task for mesh projects
		project.tasks.create("meshProjects", LoadMeshProjects) { LoadMeshProjects task ->
			task.group = MESH_GROUP
			task.description = "Load or create all mesh projects"

			task.autoCreate.set(mesh.autoCreate)
			task.projects.set(mesh.projects)

			task.dependsOn(meshLogin)
		}
	}
}
