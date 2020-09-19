/**
 * 
 */
package io.waweb.mesh.gradle

import javax.inject.Inject

import org.gradle.api.Named
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

/**
 * @author Aaron R Miller<aaron.miller@waweb.io>
 *
 */
class MeshProjectExtension implements Named {

	final Property<String> hostname = objectFactory.property(String)

	final Property<String> pathPrefix = objectFactory.property(String)

	final Property<String> schema = objectFactory.property(String)

	final Property<Boolean> ssl = objectFactory.property(Boolean)

	final String _name

	@Override
	public String getName() {
		return _name
	}

	@Inject
	ObjectFactory getObjectFactory() {
		throw new UnsupportedOperationException()
	}

	MeshProjectExtension(final String name) {
		_name = name

		this.schema.convention("folder")
		this.ssl.convention(false)
	}
}
