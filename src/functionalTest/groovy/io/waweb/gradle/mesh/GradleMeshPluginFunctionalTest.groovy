/**
 * @author Aaron R Miller<aaron.miller@waweb.io>
 */
package io.waweb.gradle.mesh

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome

import spock.lang.Specification

/**
 * A simple functional test for the 'io.waweb.gradle.mesh.greeting' plugin.
 */
public class GradleMeshPluginFunctionalTest extends Specification {
	def "can run meshLogin task"() {
		given:
		def projectDir = new File("build/functionalTest")
		projectDir.mkdirs()
		new File(projectDir, "settings.gradle").text = ""
		new File(projectDir, "build.gradle").text = """
            plugins {
                id 'io.waweb.mesh' version '0.0.1-SNAPSHOT'
            }
			mesh {
				host = "demo.getmesh.io"
				port = 80
				useSsl = false
				userName = "admin"
				password = "admin"
				projectName = "demo"
			}
        """

		when:
		def runner = GradleRunner.create()
		runner.forwardOutput()
		runner.withPluginClasspath()
		runner.withArguments("meshLogin")
		runner.withProjectDir(projectDir)
		def result = runner.build()

		then:
		result.task('meshLogin').outcome == TaskOutcome.SUCCESS
	}
}
