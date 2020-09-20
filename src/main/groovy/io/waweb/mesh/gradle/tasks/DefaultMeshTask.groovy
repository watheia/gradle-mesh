/**
 * @author Aaron R Miller<aaron.miller@waweb.io>
 */
package io.waweb.mesh.gradle.tasks

import javax.inject.Inject

import org.gradle.api.DefaultTask
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal

import com.gentics.mesh.rest.client.MeshRestClient
/**
 *
 */
abstract class DefaultMeshTask extends DefaultTask {

	@Internal final Property<MeshRestClient> client = objectFactory.property(MeshRestClient)

	@Input final Property<String> projectName = objectFactory.property(String)

	@Inject
	ObjectFactory getObjectFactory() {
		throw new UnsupportedOperationException()
	}

	abstract void run()
}
