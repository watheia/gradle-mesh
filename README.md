# gradle-mesh

Gradle Plugin for [Genitcs Mesh](https://getmesh.io/) Headless CMS.

## Goals

**Work in Progress**

This is a proof of concept for interacting with a Gentics Mesh server. The initial MVP prototype will explore concepts to:

* Define and merge schemas/microshemas definitions
* Publish and release nodes to server
* Read nodes from the server and wire up results to task inputs/outputs

## Current Features

* [MeshExtension](src/main/groovy/io/waweb/mesh/gradle/MeshExtension.groovy) - basic login configuration
* [MeshLogin](src/main/groovy/io/waweb/mesh/gradle/MeshLogin.groovy) - Initializes client login session
* [DefaultMeshTask](src/main/groovy/io/waweb/mesh/gradle/tasks/DefaultMeshTask.groovy) - Abstract class guaranteed to run after the MeshRestClient is fully authenticated
* [LoadMeshProject](src/main/groovy/io/waweb/mesh/gradle/tasks/LoadMeshProject.groovy) - Loads all microschemas, schemas, and nodes, for the specified `projectName`, and makes them available in a concurrent-safe `NamedDomainObjectContainer`

## Getting Started

Until the plugin is stable and a release published to the Gradle plugin portal, one must add the project directly
to your own project sources, then consume it similar to the following:

*settings.gradle*

```
pluginManagement {
    repositories {
        mavenCentral()
        jcenter()
        maven { url = "https://maven.gentics.com/maven2" }
    }
}

rootProject.name = 'website'

includeBuild "./gradle-mesh"
```

*build.gradle*

```
import io.waweb.mesh.gradle.tasks.*
 
plugins {
	id 'base'
	id 'io.waweb.mesh'
}

mesh {
	host = "localhost"
	port = 8080
	useSsl = false
	userName = "admin"
	password = "admin"
	// Or use a secure token instead...
	// apiKey = "SECURE_API_KEY"
}

meshProject {
	microschemas.all { println "Loaded Microscema: ${it.payload.name}" }
	schemas.all { println "Loaded Schema: ${it.payload.name}" }
	nodes.all { println "Loaded Node: ${it.payload.uuid}" }
	doLast { println "Root Node: ${rootNode.get().uuid}" }
}

build {
	dependsOn meshProject
}
```

The configuration may also be set with the following environment variables:

* `GRADLE_MESH_HTTP_HOST`
* `GRADLE_MESH_HTTP_PORT`
* `GRADLE_MESH_HTTP_SSL_ENABLE`
* `GRADLE_MESH_API_KEY`
* `GRADLE_MESH_USER`
* `GRADLE_MESH_PASS`
* `GRADLE_MESH_PROJECT`

Please refer to the [Gentics Mesh Documentation](https://getmesh.io/docs/api/#users__userUuid__token_post) for more info on how to request an API key.
