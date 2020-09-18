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
	def "can apply plugin to project"() {
		given:
		def projectDir = new File("build/functionalTest")
		projectDir.mkdirs()
		new File(projectDir, "settings.gradle").text = ""
		new File(projectDir, "build.gradle").text = """
            plugins {
				id 'base'
                id 'io.waweb.mesh'
            }
        """

		when:
		def runner = GradleRunner.create()
		runner.forwardOutput()
		runner.withPluginClasspath()
		runner.withArguments("build")
		runner.withProjectDir(projectDir)
		def result = runner.build()

		then:
		result.task(':build').outcome == TaskOutcome.SUCCESS || TaskOutcome.UP_TO_DATE
	}
}
