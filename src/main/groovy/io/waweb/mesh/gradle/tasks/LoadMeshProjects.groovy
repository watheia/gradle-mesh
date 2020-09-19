/**
 * @author Aaron R Miller<aaron.miller@waweb.io>
 */
package io.waweb.mesh.gradle.tasks

import javax.inject.Inject

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.TaskAction

import com.gentics.mesh.core.rest.project.ProjectCreateRequest
import com.gentics.mesh.core.rest.project.ProjectResponse
import com.gentics.mesh.rest.client.MeshRestClient

import io.reactivex.Single
import io.waweb.mesh.gradle.model.MeshProject

/**
 * Loads all project nodes and places them in the internal cache.
 * Set autoCreate to false to prevent missing projects from being created
 */
class LoadMeshProjects extends DefaultMeshTask {

	public static Single<ProjectResponse> loadOrCreateProject(final MeshRestClient client, final MeshProject project, boolean autoCreate) {
		return client.findProjectByName(project.name).toSingle().onErrorResumeNext({ err ->
			// TODO skip creation if autoCreate is false
			final ProjectCreateRequest request = new ProjectCreateRequest().tap {
				name = project.name
				schemaRef = project.schemaRef.get()
				if(project.hostname.isPresent()) {
					hostname = project.hostname.get()
				}
				if(project.pathPrefix.isPresent()) {
					pathPrefix = project.pathPrefix.get()
				}
				if(project.ssl.isPresent()) {
					ssl = project.ssl.get()
				}
			}

			return client.createProject(request).toSingle()
		})
	}

	@Nested final SetProperty<MeshProject> projects = objectFactory.setProperty(MeshProject)

	@Input final Property<Boolean> autoCreate = objectFactory.property(Boolean)

	@Inject
	ObjectFactory getObjectFactory() {
		throw new UnsupportedOperationException()
	}

	LoadMeshProjects() {
		this.autoCreate.convention(true)
	}

	@Override
	@TaskAction
	void run() {
		final MeshRestClient client = this.client.get()
		final Set<MeshProject> projects = this.projects.get()
		final boolean autoCreate = this.autoCreate.get()

		// TODO load or create async projects

	}
}
