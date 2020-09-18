/**
 * @author Aaron R Miller<aaron.miller@waweb.io>
 */
package io.waweb.gradle.mesh

import javax.inject.Inject

import org.gradle.api.DefaultTask
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Internal

import com.gentics.mesh.rest.client.MeshRestClient

/**
 *
 */
class MeshClientTask extends DefaultTask {

	@Internal final Property<MeshRestClient> client = objectFactory.property(MeshRestClient)

	@Inject
	ObjectFactory getObjectFactory() {
		throw new UnsupportedOperationException()
	}

}
