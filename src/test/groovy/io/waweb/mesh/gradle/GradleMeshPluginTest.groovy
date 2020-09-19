/**
 * @author Aaron R Miller<aaron.miller@waweb.io>
 */
package io.waweb.mesh.gradle

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

import spock.lang.Specification

/**
 * A simple unit test for the 'io.waweb.mesh' plugin.
 */
class GradleMeshPluginTest extends Specification {
	def "plugin registers extension"() {
		given:
		def project = ProjectBuilder.builder().build()

		when:
		project.plugins.apply('io.waweb.mesh')

		then:
		def mesh = project.extensions.findByType(MeshExtension)
		mesh != null
		mesh.host.get() == "localhost"
		mesh.port.get() == 8080
		mesh.useSsl.get() == false
	}

	def "plugin registers login task"() {
		given:
		def project = ProjectBuilder.builder().build()

		when:
		project.plugins.apply('io.waweb.mesh')

		then:
		project.tasks.findByName('meshLogin') != null
	}


	def "MAY configure mesh projects"() {
		given:
		Project project = ProjectBuilder.builder().build()

		when:
		project.plugins.apply('io.waweb.mesh')
		project.with {
			mesh {
				projects {
					foo {}
					bar {}
				}
			}
		}

		then:
		def mesh = project.extensions.findByType(MeshExtension)
		mesh != null
		mesh.projects.getByName('foo') != null
		mesh.projects.getByName('bar') != null
	}
}
