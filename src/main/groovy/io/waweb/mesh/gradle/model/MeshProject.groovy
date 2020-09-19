/**
 * 
 */
package io.waweb.mesh.gradle.model

import javax.inject.Inject

import org.gradle.api.Named
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional

import com.gentics.mesh.core.rest.project.ProjectResponse

/**
 * @author Aaron R Miller<aaron.miller@waweb.io>
 *
 */
class MeshProject implements Named {

	@Optional @Input final Property<String> hostname = objectFactory.property(String)

	@Optional @Input final Property<String> pathPrefix = objectFactory.property(String)

	@Optional @Input final Property<String> schemaRef = objectFactory.property(String)

	@Optional @Input final Property<Boolean> ssl = objectFactory.property(Boolean)

	@Internal final Property<ProjectResponse> node = objectFactory.property(ProjectResponse)

	private final String _name

	@Override
	@Input
	public String getName() {
		return _name
	}

	@Inject
	ObjectFactory getObjectFactory() {
		throw new UnsupportedOperationException()
	}

	MeshProject(final String name) {
		_name = name

		this.schemaRef.convention("folder")
	}
}
