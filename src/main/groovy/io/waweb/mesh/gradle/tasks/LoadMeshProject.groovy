/**
 * @author Aaron R Miller<aaron.miller@waweb.io>
 */
package io.waweb.mesh.gradle.tasks

import javax.inject.Inject

import org.gradle.api.Named
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction

import com.gentics.mesh.core.rest.microschema.impl.MicroschemaResponse
import com.gentics.mesh.core.rest.node.NodeListResponse
import com.gentics.mesh.core.rest.node.NodeResponse
import com.gentics.mesh.core.rest.project.ProjectResponse
import com.gentics.mesh.core.rest.schema.MicroschemaListResponse
import com.gentics.mesh.core.rest.schema.SchemaListResponse
import com.gentics.mesh.core.rest.schema.impl.SchemaResponse
import com.gentics.mesh.core.rest.user.NodeReference

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Loads all project nodes and places them in the internal cache.
 * Set autoCreate to false to prevent missing projects from being created
 */
class LoadMeshProject extends DefaultMeshTask {

	static class MeshMicroscema implements Named {

		final MicroschemaResponse payload

		@Override
		public String getName() {
			return payload.name
		}

		MeshMicroscema(MicroschemaResponse payload) {
			this.payload = payload
		}
	}

	static class MeshSchema implements Named {

		final SchemaResponse payload

		@Override
		public String getName() {
			return payload.name
		}

		MeshSchema(SchemaResponse payload) {
			this.payload = payload
		}
	}

	static class MeshNode implements Named {

		final NodeResponse payload

		@Override
		public String getName() {
			return payload.uuid
		}

		MeshNode(NodeResponse payload) {
			this.payload = payload
		}
	}

	@Internal final Property<NodeReference> rootNode = objectFactory.property(NodeReference)

	@Internal final NamedDomainObjectContainer<MeshMicroscema> microschemas = objectFactory.domainObjectContainer(MeshMicroscema)

	@Internal final NamedDomainObjectContainer<MeshSchema> schemas = objectFactory.domainObjectContainer(MeshSchema)

	@Internal final NamedDomainObjectContainer<MeshNode> nodes = objectFactory.domainObjectContainer(MeshNode)

	@Inject
	ObjectFactory getObjectFactory() {
		throw new UnsupportedOperationException()
	}

	@Override
	@TaskAction
	void run() {
		logger.info("Loading mesh project ${projectName.get()}...")

		loadProject()
				.flatMapCompletable({ project ->
					rootNode.set(project.rootNode)
					return loadMicroschemas().andThen(loadSchemas())
							.andThen(loadNodes(project))
				})
				.doOnError({ err -> throw new RuntimeException(err) })
				.blockingAwait()

		logger.info("Mesh Project Loaded.")
	}

	Single<ProjectResponse> loadProject() {
		logger.info("Loading Project: ${projectName.get()}...")
		return client.get().findProjectByName(projectName.get()).toSingle()
	}

	Completable loadMicroschemas() {
		logger.info("Loading Microschemas...")
		return client.get().findMicroschemas(projectName.get())
				.toSingle()
				.flatMapCompletable({ MicroschemaListResponse list ->
					Observable.fromIterable(list.getData()).flatMapCompletable({ MicroschemaResponse payload ->
						microschemas.add(new MeshMicroscema(payload))
						return Completable.complete()
					})
				})
	}

	Completable loadSchemas() {
		logger.info("Loading Schemas...")
		return client.get().findSchemas(projectName.get())
				.toSingle()
				.flatMapCompletable({ SchemaListResponse list ->
					return Observable.fromIterable(list.getData()).flatMapCompletable({ SchemaResponse payload ->
						schemas.add(new MeshSchema(payload))
						return Completable.complete()
					})
				})
	}

	Completable loadNodes(ProjectResponse project) {
		logger.info("Loading Nodes...")
		return client.get().findNodes(projectName.get())
				.toSingle()
				.flatMapCompletable({ NodeListResponse list ->
					return Observable.fromIterable(list.getData()).flatMapCompletable({ NodeResponse payload ->
						nodes.add(new MeshNode(payload))
						return Completable.complete()
					})
				})
	}
}
