/**
 * @author Aaron R Miller<aaron.miller@waweb.io>
 */
package io.waweb.gradle.mesh

import javax.inject.Inject

import org.gradle.api.DefaultTask
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction

import com.gentics.mesh.rest.client.MeshRestClient
import com.gentics.mesh.rest.client.MeshRestClientConfig

/**
 * The MeshLogin task is responsible for providing a fully authenticated MeshRestClient,
 * which can be injected into other tasks. This task does not define any outputs to ensure
 * that it is executed on every Gradle run.
 */
class MeshLogin extends DefaultTask {

	private MeshRestClient _client = null

	@Internal
	MeshRestClient getClient() {
		return _client
	}

	@Input final Property<String> host = objectFactory.property(String)

	@Input final Property<Integer> port = objectFactory.property(Integer)

	@Input final Property<Boolean> useSsl = objectFactory.property(Boolean)

	@Input final Property<String> userName = objectFactory.property(String)

	@Input final Property<String> password = objectFactory.property(String)

	@Input final Property<String> basePath = objectFactory.property(String)

	@Inject
	ObjectFactory getObjectFactory() {
		throw new UnsupportedOperationException();
	}

	@TaskAction
	void run() {
		final String host = this.host.get()
		final Integer port = this.port.get()
		final String basePath = this.basePath.get()

		logger.info("Logging into mesh server on ${host}:${port}${basePath}...")

		final MeshRestClientConfig clientConfig = MeshRestClientConfig.newConfig()
				.setHost(host)
				.setPort(port)
				.setBasePath(basePath)
				.setSsl(useSsl.get())
				.build()

		_client = MeshRestClient.create(clientConfig)
				.setLogin(userName.get(), password.get())

		_client.login()
				.doOnError({ it.printStackTrace() })
				.blockingGet()

		logger.info("Login complete.")
	}
}
