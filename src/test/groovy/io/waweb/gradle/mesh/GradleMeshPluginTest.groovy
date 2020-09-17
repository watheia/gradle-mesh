/**
 * @author Aaron R Miller<aaron.miller@waweb.io>
 */
package io.waweb.gradle.mesh

import org.gradle.testfixtures.ProjectBuilder

import spock.lang.Specification

/**
 * A simple unit test for the 'io.waweb.mesh' plugin.
 */
public class GradleMeshPluginTest extends Specification {
	def "plugin registers extension"() {
		given:
		def project = ProjectBuilder.builder().build()

		when:
		project.plugins.apply('io.waweb.mesh')

		then:
		project.mesh != null
		project.mesh.host == "localhost"
		project.mesh.port == 8080
		project.mesh.useSsl == false
		project.mesh.projectName == "test"
	}
}
