/**
 * @author Aaron R Miller<aaron.miller@waweb.io>
 */
package io.waweb.gradle.mesh

import org.gradle.testkit.runner.GradleRunner

import spock.lang.Specification

/**
 * A simple functional test for the 'io.waweb.gradle.mesh.greeting' plugin.
 */
public class GradleMeshPluginFunctionalTest extends Specification {
	def "can run task"() {
		given:
		def projectDir = new File("build/functionalTest")
		projectDir.mkdirs()
		new File(projectDir, "settings.gradle").text = ""
		new File(projectDir, "build.gradle").text = """
            plugins {
                id('io.waweb.mesh')
            }
        """

		when:
		def runner = GradleRunner.create()
		runner.forwardOutput()
		runner.withPluginClasspath()
		runner.withArguments("greeting")
		runner.withProjectDir(projectDir)
		def result = runner.build()

		then:
		result.output.contains("Hello from plugin 'io.waweb.gradle.mesh.greeting'")
	}
}
