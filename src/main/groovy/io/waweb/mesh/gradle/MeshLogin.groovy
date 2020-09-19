/**
 * @author Aaron R Miller<aaron.miller@waweb.io>
 */
package io.waweb.mesh.gradle

import javax.inject.Inject

import org.gradle.api.DefaultTask
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
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

	@Optional @Input final Property<String> apiKey = objectFactory.property(String)

	@Optional @Input final Property<String> userName = objectFactory.property(String)

	@Optional @Input final Property<String> password = objectFactory.property(String)

	@Inject
	ObjectFactory getObjectFactory() {
		throw new UnsupportedOperationException()
	}

	@TaskAction
	void run() {

		if(!this.apiKey.isPresent() && !this.userName.isPresent()) {
			throw new RuntimeException("Mesh login must have apiKey or userName/password combo")
		}

		final String host = this.host.get()
		final Integer port = this.port.get()
		final Boolean useSsl = this.useSsl.get()
		final MeshRestClientConfig clientConfig = MeshRestClientConfig.newConfig()
				.setHost(host)
				.setPort(port)
				.setSsl(useSsl)
				.build()

		_client = MeshRestClient.create(clientConfig)

		if(this.apiKey.isPresent()) {
			final String apiKey = this.apiKey.get()
			_client.setAPIKey(apiKey)
		} else {
			logger.info("Logging into mesh server on ${host}:${port}...")
			final String userName = this.userName.get()
			final String password = this.password.get()
			_client.setLogin(userName, password)
					.login()
					.doOnError({ it.printStackTrace() })
					.blockingGet()
		}

		logger.info("Login complete.")
	}
}
