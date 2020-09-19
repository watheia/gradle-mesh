/**
 * 
 */
package io.waweb.mesh.gradle.tasks;

import org.gradle.api.tasks.TaskAction;

/**
 * @author Aaron R Miller<aaron.miller@waweb.io>
 *
 */
class MeshTask extends DefaultMeshTask {

	@Override
	@TaskAction
	void run() {
		println "Hello, Mesh Client!"
	}
}
