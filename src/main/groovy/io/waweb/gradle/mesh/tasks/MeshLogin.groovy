/**
 * @author Aaron R Miller<aaron.miller@waweb.io>
 */
package io.waweb.gradle.mesh.tasks

import javax.inject.Inject

import org.gradle.api.DefaultTask
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction

import io.waweb.gradle.mesh.MeshClient

class MeshLogin extends DefaultTask {

	private MeshClient _client = null

	@Internal
	MeshClient getClient() {
		return _client
	}

	@Inject
	ObjectFactory getObjectFactory() {
		throw new UnsupportedOperationException();
	}

	@TaskAction
	void run() {
		_client = new MeshClient()
	}
}
